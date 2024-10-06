import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

/**
 * Class representing coins in the game. Coins can be collected by either the player or the taxi.
 * It will set one level higher priority for the passengers that are waiting to get-in or already in the taxi.
 */
public class Coin extends Power{

    public Coin(int x, int y, Properties props) {
        super(x, y, GameObjectType.COIN.name(), props);
    }

    /**
     * Apply the effect of the coin on the priority of the passenger.
     * @param priority The current priority of the passenger.
     * @return The new priority of the passenger.
     */
    public Integer applyEffect(Integer priority) {
        if (super.getFramesActive() <= super.getMaxFrames() && priority > 1) {
            priority -= 1;
        }

        return priority;
    }





}
