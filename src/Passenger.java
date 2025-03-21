import bagel.Font;
import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

public class Passenger extends Character{

    private final TravelPlan TRAVEL_PLAN;
    private final boolean UMBRELLA;
    private final static int NO_UMBRELLA_PRIORITY = 1;


    private final int PRIORITY_OFFSET;
    private final int EXPECTED_FEE_OFFSET;
    private final static int PASSENGER_EJECTION_OFFSET = 50;
    private Trip trip;

    private boolean reachedFlag;

    public Passenger(int x, int y, int priority, int endX, int distanceY, boolean umbrella, Properties props) {
        super(x, y, GameObjectType.PASSENGER.name(), props);
        this.TRAVEL_PLAN = new TravelPlan(endX, distanceY, priority, props);

        this.PRIORITY_OFFSET = 30;
        this.EXPECTED_FEE_OFFSET = 100;
        this.UMBRELLA = umbrella;
    }


    public TravelPlan getTravelPlan() {
        return TRAVEL_PLAN;
    }
    public boolean getUmbrella() { return UMBRELLA; }
    public Trip getTrip(){ return this.trip;}

    /**
     * Update the passenger status, move according to the input, active taxi and trip status.
     * Initiate the trip if the passenger is in the taxi.
     * See move method below to understand the movement of the passenger better.
     * @param input The current mouse/keyboard input.
     * @param taxi The active taxi in the game play.
     */
    public void update(Input input, Taxi taxi, Driver driver, boolean isSunny) {


        updateCollision(input);
        // if the passenger is not in the taxi or the trip is completed, update the passenger status based on keyboard
        // input. This means the passenger is go down when taxi moves up.
        if (!super.getIsGetInTaxi() || (trip != null && trip.isComplete())) {
            adjustToInputMovement(input);
            move();
            draw();
        }

        // if the passenger is not in the taxi and there's no trip initiated, draw the priority number on the passenger.
        if (!super.getIsGetInTaxi() && trip == null) {
            drawPriority(isSunny);
        }

        if (adjacentToObject(taxi) && !super.getIsGetInTaxi() && trip == null && taxi.getIsActive()) {
            // if the passenger has not started the trip yet,
            // Taxi must be stopped in passenger's vicinity and not having another trip.
            setIsGetInTaxi(taxi);
            move(taxi);
        }else if(super.getIsGetInTaxi() && !taxi.getIsActive()) {
            ejectionMovement(driver);
            draw();
        }else if(super.getIsGetInTaxi()) {
            // if the passenger is in the taxi, initiate the trip and move the passenger along with the taxi.
            if(trip == null) {
                //Create new trip
                getTravelPlan().setStartY(super.getY());
                trip = new Trip(this, taxi, super.getPROPS());
                taxi.setTrip(trip);
                if(!isSunny && !UMBRELLA)
                    TRAVEL_PLAN.setPriority(NO_UMBRELLA_PRIORITY);
            }

            move(taxi);
            draw();

        } else if(!super.getIsGetInTaxi() && trip != null && trip.isComplete()) {
            move(taxi);
            draw();
        }
        if(super.getHealth() <= 0 && super.getBlood() == null){
            super.setBlood(super.getX(), super.getY(), super.getProps());
        }
    }

    /**
     * Draw the priority number on the passenger.
     */
    private void drawPriority(boolean isSunny) {
        Font font = new Font(super.getPROPS().getProperty("font"),
                Integer.parseInt(super.getPROPS().getProperty("gameObjects.passenger.fontSize")));
        boolean isUrgent = !isSunny && !UMBRELLA;
        font.drawString(String.valueOf(TRAVEL_PLAN.getPriority(isUrgent)), super.getX() - PRIORITY_OFFSET, super.getY());
        font.drawString(String.valueOf(TRAVEL_PLAN.getExpectedFee(isUrgent)), super.getX() - EXPECTED_FEE_OFFSET, super.getY());
    }



    /**
     * Move in relevant to the taxi and passenger's status.
     * @param taxi active taxi
     */

    @Override
    protected void move(Taxi taxi) {
        if (super.getIsGetInTaxi()) {
            // if the passenger is in the taxi, move the passenger along with the taxi.
            moveWithTaxi(taxi);
        } else if(!super.getIsGetInTaxi() && trip != null && trip.isComplete() && super.getBlood() == null) {
            //walk towards end flag if the trip is completed and not in the taxi.
            if(!hasReachedFlag(taxi.getIsActive()) && getCollisionTime() <= getPostCollisionTime()) {
                TripEndFlag tef = trip.getTripEndFlag();
                walkXDirectionObj(tef.getX());
                walkYDirectionObj(tef.getY());
                walk();
            }
        } else if(super.getBlood() == null){
            // Walk towards the taxi if other conditions are not met.
            // (That is when taxi is stopped with not having a trip and adjacent to the passenger and the passenger
            // hasn't initiated the trip yet.)
            walkXDirectionObj(taxi.getX());
            walkYDirectionObj(taxi.getY());
            walk();
        }
    }



    /**
     * Determine the walk direction in x-axis of the passenger based on the x direction of the object.
     */
    private void walkXDirectionObj(int otherX) {
        if (otherX > super.getX()) {
            super.setWalkDirectionX(1);
        } else if (otherX < super.getX()) {
            super.setWalkDirectionX(-1);
        } else {
            super.setWalkDirectionX(0);
        }
    }

    /**
     * Determine the walk direction in y-axis of the passenger based on the x direction of the object.
     */
    private void walkYDirectionObj(int otherY) {
        if (otherY > super.getY()) {
            super.setWalkDirectionY(1);
        } else if (otherY < super.getY()) {
            super.setWalkDirectionY(-1);
        } else {
            super.setWalkDirectionY(0);
        }
    }

    /**
     * Check if the passenger has reached the end flag of the trip.
     * @return a boolean value indicating if the passenger has reached the end flag.
     */
    public boolean hasReachedFlag(boolean isActive) {
        if(trip != null) {
            TripEndFlag tef = trip.getTripEndFlag();
            if(tef.getX() == super.getX() && tef.getY() == super.getY() && isActive) {
                reachedFlag = true;
            }
            return reachedFlag;
        }
        return false;
    }

    /**
     * Check if the passenger has reached the end flag of the trip.
     * @return a boolean value indicating if the passenger has reached the end flag.
     */
    private void ejectionMovement(Driver driver) {
        super.setX(driver.getX() - PASSENGER_EJECTION_OFFSET);
        super.setY(driver.getY());
    }

    /**
     * Check if the taxi is adjacent to the passenger. This is evaluated based on multiple crietria.
     * @param taxi The active taxi in the game play.
     * @return a boolean value indicating if the taxi is adjacent to the passenger.
     */
    private boolean adjacentToObject(Taxi taxi) {
        // Check if Taxi is stopped and health > 0
        // Check if Taxi is in the passenger's detect radius
        boolean taxiStopped = !taxi.getIsMovingX() && !taxi.getIsMovingY();
        // Check if Taxi is in the passenger's detect radius
        float currDistance = (float) Math.sqrt(Math.pow(taxi.getX() - super.getX(), 2) +
                Math.pow(taxi.getY() - super.getY(), 2));
        // Check if Taxi is not having another trip
        boolean isHavingAnotherTrip = taxi.getTrip() != null && taxi.getTrip().getPassenger() != this;

        return currDistance <= super.getTaxiDetectRadius() && taxiStopped && !isHavingAnotherTrip;
    }


    public int noUmbrellaPriority() { return NO_UMBRELLA_PRIORITY; }

}
