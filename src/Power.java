import bagel.Image;
import bagel.Input;

import java.util.Properties;

public class Power extends Object{
    private final int MAX_FRAMES;
    private final float RADIUS;


    private int framesActive;
    private boolean isCollided;

    public Power(int x, int y, String type, Properties props) {
        super(x, y, type, props);
        if(type.equals(GameObjectType.COIN.name())){
            this.MAX_FRAMES = Integer.parseInt(props.getProperty("gameObjects.coin.maxFrames"));
            this.RADIUS = Float.parseFloat(props.getProperty("gameObjects.coin.radius"));
        }else{
            this.MAX_FRAMES = Integer.parseInt(props.getProperty("gameObjects.invinciblePower.maxFrames"));
            this.RADIUS = Float.parseFloat(props.getProperty("gameObjects.invinciblePower.radius"));
        }
        framesActive = 0;
    }

    /**
     * Mark the status of the object as collided when it's collided with another object.
     * This will initiate the collision timeout.
     */
    public void setIsCollided() {
        this.isCollided = true;
    }

    public boolean getIsActive() {
        return isCollided && framesActive <= MAX_FRAMES && framesActive > 0;
    }

    public int getFramesActive() {
        return framesActive;
    }

    public int getMaxFrames(){
        return MAX_FRAMES;
    }
    public boolean getIsCollided() {
        return isCollided;
    }
    public void setFramesActive(){
        framesActive++;
    }

    /**
     * Check if the power has collided with any PowerCollectable objects, and power will be collected by PowerCollectable
     * object that is collided with.
     */
    public void collide(Taxi taxi, Driver driver) {
        if(hasCollidedWith(taxi, driver)) {
            setIsCollided();
            if(this instanceof Coin)
                taxi.collectPower(this);
            else{
                driver.collectPower(this);
                if(!taxi.getIsBroken()){
                    taxi.collectPower(this);
                }
            }
        }
    }

    /**
     * Check if the object is collided with another object based on the radius of the two objects.
     * @param taxi The taxi object to be checked.
     * @return True if the two objects are collided, false otherwise.
     */
    public boolean hasCollidedWith(Taxi taxi, Driver driver) {
        // if the distance between the two objects is less than the sum of their radius, they are collided
        float collisionDistance;
        if(taxi.getIsActive()) {
            collisionDistance = RADIUS + taxi.getRadius();
            float currDistance = (float) Math.sqrt(Math.pow(super.getX() - taxi.getX(), 2) + Math.pow(super.getY() - taxi.getY(), 2));
            return currDistance <= collisionDistance;
        }else{
            collisionDistance = RADIUS + driver.getRadius();
            float currDistance = (float) Math.sqrt(Math.pow(super.getX() - driver.getX(), 2) + Math.pow(super.getY() - driver.getY(), 2));
            return currDistance <= collisionDistance;
        }
    }

    /**
     * Check if the power is collided with another object based on the radius of the two objects.
     * @return True if the two objects are collided, false otherwise.
     */
    @Override
    public void update(Input input) {
        if(isCollided) {
            framesActive++;
        }else {
            adjustToInputMovement(input);

            move();
            draw();
        }
    }

    //This 'removes' all power upon taxi destruction
    public void removePower(){
        this.framesActive = this.MAX_FRAMES + 1;
    }
}
