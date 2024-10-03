import bagel.Font;
import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

public abstract class Car implements Collidable{
    private final Image IMAGE;
    private final int SPEED_X;
    private final float RADIUS;
    private final int DAMAGE;
    private final int SCROLL_SPEED;
    private final static int POST_COLLISION_TIME = 10;

    private int timeOut;
    private int speedY;
    private int moveY;
    private static final int MAX_TIME_OUT = 200;
    private int health;
    private boolean isCollided;
    private boolean isBreaking;
    private boolean isBroken;
    private boolean invincible;
    private int timeOutY;
    private int collisionTime;

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
        this.SCROLL_SPEED = Integer.parseInt(props.getProperty("gameObjects.taxi.speedY"));

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
    @Override
    public int getTimeout(){
        return timeOut;
    }
    public int getHealth() {
        return this.health;
    }
    protected void setX(int x) { this.x = x; }
    protected void setY(int y) { this.y = y; }



    public int getDamage(){ return DAMAGE; }
    public void setTimeOutY(int timeOutY) { this.timeOutY = timeOutY; }
    public void setTimeOut(int timeOut) { this.timeOut = timeOut; }
    public void setInvincible(boolean invincible) {
        this.invincible = invincible;
    }
    public boolean getInvincible() {
        return invincible;
    }

    /**
     * Adjust the movement of the car based on the keyboard input.
     * If the taxi has a driver, and taxi has health>0 the taxi can only move left and right (fixed in y direction).
     * If the taxi does not have a driver, the taxi can move in all directions.
     * @param input The current mouse/keyboard input.
     */
    public void adjustToInputMovement(Input input) {
        if (input != null){
            if (input.isDown(Keys.UP)) {
                moveY = 1;
            }else {
                moveY = 0;
            }
        }else  {
            moveY = 0;
        }
    }



    protected void move(){y -= speedY - moveY * SCROLL_SPEED;}
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
    public static <T extends Car & Generatable> T checkAndGenerate(T thisCar, Properties props){
        if(thisCar.checkGeneratability()){
            return (T) thisCar.toGenerate(props);
        }else{
            return null;
        }
    }

    /**
     * assign speed for other cars and enemy cars
     */
    protected void assignSpeed(){
        this.speedY = MiscUtils.getRandomInt(
                Integer.parseInt(PROPS.getProperty("gameObjects.otherCar.minSpeedY")),
                Integer.parseInt(PROPS.getProperty("gameObjects.otherCar.maxSpeedY"))
        );
    }

    /**
     * implements movement of cars
     */
    protected void update(Input input){

        this.move();
        this.draw();

        this.adjustToInputMovement(input);
        updateCollision(input);
    }


    /**
     * implements collision behaviour for collision with objects other than fireball
     */
    @Override
    public void hasCollided(int diffY, int damage){
        this.isCollided = true;
        collisionTime = POST_COLLISION_TIME;
        this.speedY = 0;
        if(diffY > 0)
            this.timeOutY = 1;
        else
            this.timeOutY = -1;
        if(damage != 0)
            this.timeOut = MAX_TIME_OUT;
    }

    /**
     * checks if a collision is present with non-people
     */
    protected <T extends Collidable, D extends Collidable> void checkCollision(T t, D d) {
        float currDistance = (float) Math.sqrt(Math.pow(t.getX() - d.getX(), 2) +
                Math.pow(t.getY() - d.getY(), 2));
        if (currDistance < t.getRadius() + d.getRadius() && (int) (currDistance) != 0) {
            int damageD = 0;
            int damageT = 0;
            if(!d.getInvincible() && !t.getInvincible()){
                damageD = d.getDamage();
                damageT = t.getDamage();
            }
            if(t.getTimeout() == -1 && d.getTimeout() == -1) {
                t.hasCollided(t.getY() - d.getY(), damageD);
                d.hasCollided(d.getY() - t.getY(), damageT);
            }
        }
    }



    /**
     * updates Collision behaviour
     */
    protected void updateCollision(Input input){
        if(this.collisionTime > 0){
            postCollisionMovement();
        }else{
            timeOutY = 0;
        }
        if(this.timeOut > 0){
            this.invincible = true;
            this.timeOut --;
        }else if(timeOut == 0){
            if(!(this instanceof Taxi))
                this.assignSpeed();
            invincible = false;
            this.timeOut --;
        }

        Font trial = new Font("res/FSO8BITR.TTF", 20);
        trial.drawString(String.format("time: %d, HP: %d, ctime: %d", timeOut,health, collisionTime), x-30, y);

        collisionTime --;
    }





    /**
     * implements post collision behaviour
     */
    protected void postCollisionMovement(){
        y += timeOutY;
    }

    /**
     * implements timeout behaviour
     */
    protected void checkInvincibility(){
        setInvincible(timeOut > 0);
    }
}
