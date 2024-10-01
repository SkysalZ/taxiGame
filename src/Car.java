import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

public abstract class Car {
    private final Image IMAGE;
    private final int SPEED_X;
    private final float RADIUS;
    private final int DAMAGE;

    private int timeOut;
    private static final int MAX_TIME_OUT = 200;
    private int health;
    private boolean isCollided;
    private boolean isBreaking;
    private boolean isBroken;

    private int x;
    private int y;

    private final Properties PROPS;

    public Car(int x, int y, String type, Properties props) {
        this.x = x;
        this.y = y;

        this.SPEED_X = Integer.parseInt(props.getProperty("gameObjects.taxi.speedX"));
        this.RADIUS = Float.parseFloat(props.getProperty("gameObjects.taxi.radius"));
        this.health = (int) Float.parseFloat(props.getProperty("gameObjects.taxi.health")) * 100;
        this.PROPS = props;

        //check what type of car it is to correctly assign the image
        if(type.equals(GameObjectType.TAXI.name())){
            this.IMAGE = new Image(props.getProperty("gameObjects.taxi.image"));
            this.DAMAGE = (int) Float.parseFloat(props.getProperty("gameObjects.taxi.damage")) * 100;
        }else if(type.equals(GameObjectType.ENEMY_CAR.name())){
            this.IMAGE = new Image(props.getProperty("gameObjects.enemyCar.image"));
            this.DAMAGE = (int) Float.parseFloat(props.getProperty("gameObjects.enemyCar.damage")) * 100;
        }else if(type.equals(GameObjectType.OTHER_CAR.name())){
            this.IMAGE = new Image(props.getProperty("gameObjects.otherCar.image"));
            this.DAMAGE = (int) Float.parseFloat(props.getProperty("gameObjects.otherCar.damage")) * 100;
        }else{
            this.IMAGE = null;
            this.DAMAGE = 0;
        }
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public float getRadius() {
        return RADIUS;
    }
    public Properties getProps() {
        return this.PROPS;
    }

    public int getHealth() {
        return this.health;
    }



    /**
     * Adjust the movement of the car based on the keyboard input.
     * If the taxi has a driver, and taxi has health>0 the taxi can only move left and right (fixed in y direction).
     * If the taxi does not have a driver, the taxi can move in all directions.
     * @param input The current mouse/keyboard input.
     */
    public void adjustToInputMovement(Input input) {
        if(input.isDown(Keys.LEFT)) {
            x -= SPEED_X;
        }  else if(input.isDown(Keys.RIGHT)) {
            x += SPEED_X;
        } else if(input.wasReleased(Keys.LEFT) || input.wasReleased(Keys.RIGHT)) {
        }
    }



    /**
     * Draw the GameObject object into the screen.
     */
    public void draw() {
        IMAGE.draw(x, y);
    }


    /**
     * checks if the car is eligible to be generated
     */
    public abstract boolean checkGeneratability();

    /**
     * check if a car of specific type is needed to generate
     */
    public static <T extends Car & Generatable<T>> T checkAndGenerate(T thisCar, Properties props){
        if(thisCar.checkGeneratability()){
            return thisCar.toGenerate(props);
        }else{
            return null;
        }
    }
}
