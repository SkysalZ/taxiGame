import bagel.Font;
import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

/**
 * The class representing the taxis in the game play
 */
public class Taxi extends Car implements Generatable<Taxi>{
    private final static int MIN_Y = 200;
    private final static int MAX_Y = 400;

    private final Trip[] TRIPS;
    private int tripCount;
    private boolean isMovingY;
    private boolean isMovingX;

    private Coin coinPower;
    private Star starPower;
    private Trip trip;
    private boolean isActive;
    private boolean isBroken;

    public Taxi(int x, int y, String type, int maxTripCount, Properties props) {
        super(x, y, GameObjectType.TAXI.name(), props);
        TRIPS = new Trip[maxTripCount];
        super.setSpeedY(0);
    }
    public Taxi(int x, int y, Properties props) {
        super(x, y, GameObjectType.BROKEN_TAXI.name(), props);
        TRIPS = new Trip[0];
    }



    /**
     * If it's a new trip, it will added to the list of trips.
     * @param trip trip object
     */
    public void setTrip(Trip trip) {
        this.trip = trip;
        if(trip != null) {
            this.TRIPS[tripCount] = trip;
            tripCount++;
        }
    }
    public boolean getIsMovingY() {return isMovingY && isActive;}
    public boolean getIsMovingX() {return isMovingX && isActive;}
    public Coin getCoinPower() {return coinPower;}
    public boolean checkStarPower() {
        if(starPower == null)
            return false;
        return starPower.applyEffect();
    }

    public Trip getTrip() {
        return this.trip;
    }

    public void setMovingXY(Input input){
        if(input.isDown(Keys.LEFT)) {
            isMovingX = true;
        }  else if(input.isDown(Keys.RIGHT)) {
            isMovingX =  true;
        } else if(input.wasReleased(Keys.LEFT) || input.wasReleased(Keys.RIGHT)) {
            isMovingX = false;
        }
        if(input.isDown(Keys.UP)) {
            isMovingY = true;
        }else {
            isMovingY = false;
        }
    }
    public boolean getIsActive() {return isActive;}
    public void setIsActive() { this.isActive = true; }

    /**
     * Get the last trip from the list of trips.
     * @return Trip object
     */
    public Trip getLastTrip() {
        if(tripCount == 0) {
            return null;
        }
        return TRIPS[tripCount - 1];
    }


    /**
     * Update the GameObject object's movement states based on the input.
     * Render the game object into the screen.
     * @param input The current mouse/keyboard input.
     */
    public void update(Input input, boolean isSunny) {
        // if the taxi has coin power, apply the effect of the coin on the priority of the passenger
        // (See the logic in TravelPlan class)
        if (trip != null && coinPower != null) {
            TravelPlan tp = trip.getPassenger().getTravelPlan();
            int newPriority = tp.getPriority();
            if(!tp.getCoinPowerApplied()) {
                newPriority = coinPower.applyEffect(tp.getPriority());
            }
            if(newPriority < tp.getPriority()) {
                tp.setCoinPowerApplied();
            }
            tp.setPriority(newPriority);
        }
        super.updateCollision(input);
        if(input != null && isActive) {
            setMovingXY(input);
            adjustToInputMovement(input);
        }else if(input != null){
            super.adjustToInputMovement(input);
            move();
        }
        setTimeOutY(-1);

        super.checkInvincibility();
        if(starPower != null) {
            super.setInvincible(getInvincible());
        }

        if(trip != null && trip.hasReachedEnd(isActive)) {
            getTrip().end();
        }

        draw();

        // the flag of the current trip renders to the screen
        if(tripCount > 0) {
            Trip lastTrip = TRIPS[tripCount - 1];
            if(!lastTrip.getPassenger().hasReachedFlag(this.isActive)) {
                lastTrip.getTripEndFlag().update(input);
            }
        }


    }

    public void updateBrokenTaxi(Input input) {
        super.adjustToInputMovement(input);
        super.move();
        draw();
    }


    /**
     * Adjust the movement of the taxi based on the keyboard input.
     * If the taxi has a driver, and taxi has health>0 the taxi can only move left and right (fixed in y direction).
     * If the taxi does not have a driver, the taxi can move in all directions.
     * @param input The current mouse/keyboard input.
     */
    @Override
    public void adjustToInputMovement(Input input) {
        if(input != null) {
            if (input.isDown(Keys.LEFT)) {
                super.setX( super.getX() -Integer.parseInt(getProps().getProperty("gameObjects.taxi.speedX")));
                isMovingX = true;
            } else if (input.isDown(Keys.RIGHT)) {
                super.setX(super.getX() + Integer.parseInt(getProps().getProperty("gameObjects.taxi.speedX")));
                isMovingX = true;
            } else if (input.wasReleased(Keys.LEFT) || input.wasReleased(Keys.RIGHT)) {
                isMovingX = false;
            }
        }
    }

    public <T extends Power> void collectPower(T t) {
        if(t instanceof Coin)
            coinPower = (Coin) t;
        else
            starPower = (Star) t;
    }

    /**
     * Calculate total earnings. (See how fee is calculated for each trip in Trip class.)
     * @return int, total earnings
     */
    public float calculateTotalEarnings() {
        float totalEarnings = 0;
        for(Trip trip : TRIPS) {
            if (trip != null) {
                totalEarnings += trip.getFee();
            }
        }
        return totalEarnings;
    }

    @Override
    public boolean checkGeneratability(){
        return super.getHealth() <= 0;
    }

    @Override
    public Taxi toGenerate(Properties props){
        return new Taxi(-1,-1,null, -1, props);
    }


    /**
     * Calculate total earnings. (See how fee is calculated for each trip in Trip class.)
     * @return int, total earnings
     */
    public void newTaxi(Driver driver){
        driver.ejection(this);
        this.resetHealth();
        super.setX(MiscUtils.selectAValue(Integer.parseInt(super.getProps().getProperty("roadLaneCenter1")),
                Integer.parseInt(super.getProps().getProperty("roadLaneCenter3"))));
        super.setY(MiscUtils.getRandomInt(MIN_Y, MAX_Y));
        int tempX = MiscUtils.selectAValue(Integer.parseInt(super.getProps().getProperty("roadLaneCenter1")),
                Integer.parseInt(super.getProps().getProperty("roadLaneCenter3")));
        int tempY = MiscUtils.getRandomInt(MIN_Y, MAX_Y);
        this.isActive = false;
        super.setX(tempX);
        super.setY(tempY);
        super.setTimeOut(-1);
        this.coinPower = null;
        this.starPower = null;
    }

    public void activate(){
        isActive = true;
    }

    @Override
    public void hasCollided(int diffY, int damage){
        if(this.checkStarPower())
            damage = 0;
        super.hasCollided(diffY, damage);
    }



}
