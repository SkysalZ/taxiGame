import bagel.Input;

import java.util.Properties;

public class Effect extends Object{
    private final int MAX_FRAMES;
    private int frames;

    public Effect(int x, int y, String type, Properties props){
        super(x, y, type, props);
        MAX_FRAMES = Integer.parseInt(props.getProperty("gameObjects.smoke.ttl"));
        frames = 0;
    }

    public void update(int x, int y, Input input){
        super.setX(x);
        super.setY(y);
        adjustToInputMovement(input);
        if(frames <= MAX_FRAMES){
            move();
            draw();
        }
        frames++;
    }

    public boolean finished(){ return frames >= MAX_FRAMES; }
}
