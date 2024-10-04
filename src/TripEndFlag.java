import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

/**
 * A class representing the trip end flag in the game play.
 * Objects of this class will only move up and down based on the keyboard input. No other functionalities needed.
 */
public class TripEndFlag extends  Object {

    private final float RADIUS;

    public TripEndFlag(int x, int y, Properties props) {
        super(x, y, GameObjectType.TRIP_END_FLAG.name(), props);
        this.RADIUS = Float.parseFloat(props.getProperty("gameObjects.tripEndFlag.radius"));
    }

    public float getRadius() {
        return RADIUS;
    }
}