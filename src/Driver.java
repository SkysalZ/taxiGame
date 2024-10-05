import bagel.Font;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

public class Driver extends Character{
    private final int HEALTH_X;
    private final int HEALTH_Y;
    private final int TAXI_GET_IN_RADIUS;

    public Driver(int x, int y, Properties props) {
        super(x, y, GameObjectType.DRIVER.name(), props);
        HEALTH_X = Integer.parseInt(props.getProperty("gamePlay.driverHealth.x"));
        HEALTH_Y = Integer.parseInt(props.getProperty("gamePlay.driverHealth.y"));
        TAXI_GET_IN_RADIUS = Integer.parseInt(props.getProperty("gameObjects.driver.taxiGetInRadius"));
    }
    /**
     * Update the driver status, move according to the input, active taxi and ejection status.
     * @param input The current mouse/keyboard input.
     * @param taxi The active taxi in the game play.
     */
    public void updateWithTaxi(Input input, Taxi taxi) {
        updateCollision(input);
        if(!taxi.getIsActive()) {
            Font trial = new Font("res/FSO8BITR.TTF", 20);
            trial.drawString(String.format("time: %d, HP: %d, ctime: %d", super.getTimeOut(), super.getHealth(), super.getCollisionTime()), super.getX() - 30, super.getY());
        }
        if(!super.getIsGetInTaxi()) {
            adjustToInputMovement(input);
            draw();
        }else {
            move(taxi);
        }
        if(!super.getIsGetInTaxi() && nextToTaxi(taxi)) {
            super.setX(taxi.getX());
            super.setY(taxi.getY());
            setIsGetInTaxi(taxi);
            taxi.activate();
        }
        if(super.getHealth() <= 0 && super.getBlood() == null){
            super.setBlood(super.getX(), super.getY(), super.getProps());
        }
    }

    /**
     * Check if the taxi is adjacent to the driver. This is evaluated based on multiple crietria.
     * @param taxi The active taxi in the game play.
     * @return a boolean value indicating if the taxi is adjacent to the passenger.
     */
    protected boolean nextToTaxi(Taxi taxi) {
        // Check if Taxi is stopped and health > 0
        // Check if Taxi is in the passenger's detect radius
        boolean taxiStopped = !taxi.getIsMovingX() && !taxi.getIsMovingY();
        // Check if Taxi is in the passenger's detect radius
        float currDistance = (float) Math.sqrt(Math.pow(taxi.getX() - super.getX(), 2) +
                Math.pow(taxi.getY() - super.getY(), 2));
        // Check if Taxi is not having another trip
        return currDistance <= TAXI_GET_IN_RADIUS && taxiStopped ;
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
        }
    }

    public boolean checkPostCollision(){
        return super.getMaxTimeOut() - super.getTimeOut() <= super.getPostCollisionTime();
    }

    public void adjustToInputMovement(Input input) {
        if(input != null) {
            if (input.isDown(Keys.LEFT)) {
                super.setX( super.getX() -Integer.parseInt(getProps().getProperty("gameObjects.driver.walkSpeedX")));
            } else if (input.isDown(Keys.RIGHT)) {
                super.setX(super.getX() + Integer.parseInt(getProps().getProperty("gameObjects.driver.walkSpeedX")));
            }
            if (input.isDown(Keys.UP)) {
                super.setY( super.getY() -Integer.parseInt(getProps().getProperty("gameObjects.driver.walkSpeedY")));
            } else if (input.isDown(Keys.DOWN)) {
                super.setY(super.getY() + Integer.parseInt(getProps().getProperty("gameObjects.driver.walkSpeedY")));
            }
        }
    }

    @Override
    public void hasCollided(int diffY, int damage){
        super.hasCollided(diffY, damage);
    }
}
