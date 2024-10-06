import java.util.Properties;

public class Star extends Power{
    public Star(int x, int y, Properties props) {
        super(x, y, GameObjectType.STAR.name(), props);
    }

    /**
     * Apply the effect of the Star on the object.
     * @return The new priority of the passenger.
     */
    public boolean applyEffect() {
        return  super.getFramesActive() < super.getMaxFrames();
    }




}
