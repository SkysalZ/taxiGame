import bagel.Input;

import java.util.Properties;

public class Driver extends Character{
    private final int HEALTH_X;
    private final int HEALTH_Y;

    public Driver(int x, int y, Properties props) {
        super(x, y, GameObjectType.DRIVER.name(), props);
        HEALTH_X = Integer.parseInt(props.getProperty("gamePlay.driverHealth.x"));
        HEALTH_Y = Integer.parseInt(props.getProperty("gamePlay.driverHealth.y"));
    }
    /**
     * Update the driver status, move according to the input, active taxi and ejection status.
     * @param input The current mouse/keyboard input.
     * @param taxi The active taxi in the game play.
     */
    @Override
    public void updateWithTaxi(Input input, Taxi taxi) {
        if(!super.getIsGetInTaxi()) {
            if(input != null) {
                adjustToInputMovement(input);
            }

            move();
            draw();
        }
        if(adjacentToObject(taxi) && !super.getIsGetInTaxi()) {
            // if the passenger has not started the trip yet,
            // Taxi must be stopped in passenger's vicinity and not having another trip.
            setIsGetInTaxi(taxi);
            move(taxi);
        }
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
}
