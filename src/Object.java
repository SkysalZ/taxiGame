import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

public abstract class Object {
    private final Image IMAGE;
    private final int SPEED_Y;

    private int x;
    private int y;
    private int moveY;
    private final Properties PROPS;

    public Object(int x, int y, String type, Properties props) {
        this.x = x;
        this.y = y;
        this.moveY = 0;
        this.SPEED_Y = Integer.parseInt(props.getProperty("gameObjects.taxi.speedY"));
        this.PROPS = props;

        if(type.equals(GameObjectType.COIN.name())){
            this.IMAGE = new Image(props.getProperty("gameObjects.coin.image"));
        }else if(type.equals(GameObjectType.TRIP_END_FLAG.name())){
            this.IMAGE = new Image(props.getProperty("gameObjects.tripEndFlag.image"));
        }else if(type.equals(GameObjectType.STAR.name())){
            this.IMAGE = new Image(props.getProperty("gameObjects.invinciblePower.image"));
        }else if(type.equals(GameObjectType.SMOKE.name())){
            this.IMAGE = new Image(props.getProperty("gameObjects.smoke.image"));
        }else if(type.equals(GameObjectType.FIRE.name())){
            this.IMAGE = new Image(props.getProperty("gameObjects.fire.image"));
        }else if(type.equals(GameObjectType.BLOOD.name())){
            this.IMAGE = new Image(props.getProperty("gameObjects.blood.image"));
        }else if(type.equals(GameObjectType.FIRE_BALL.name())){
            this.IMAGE = new Image(props.getProperty("gameObjects.fireball.image"));
        }else{
            this.IMAGE = null;
        }
    }

    public int getX() { return x; }
    public int getY() { return y; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) { this.y = y; }
    public Properties getProps() { return PROPS; }

    /**
     * Adjust the movement direction in y-axis of the GameObject based on the keyboard input.
     * @param input The current mouse/keyboard input.
     */
    public void adjustToInputMovement(Input input) {
        if(input != null) {
            if (input.isDown(Keys.UP)) {
                moveY = 1;
            }else {
                moveY = 0;
            }
        }else  {
            moveY = 0;
        }
    }

    /**
     * Move the GameObject object in the y-direction based on the speedY attribute.
     */
    public void move() {
        this.y += SPEED_Y * moveY;
    }

    /**
     * Draw the GameObject object into the screen.
     */
    public void draw() {
        IMAGE.draw(x, y);
    }

    public void update(Input input) {
        adjustToInputMovement(input);

        move();
        draw();
    }

}
