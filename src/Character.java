import bagel.Font;
import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

public abstract class Character implements Collidable{
    private final int TAXI_DETECT_RADIUS;
    private final Properties PROPS;
    private final static int POST_COLLISION_TIME = 10;
    private final static int DRIVER_EJECTION_OFFSET = 50;

    private final int WALK_SPEED_X;
    private final int WALK_SPEED_Y;
    private int walkDirectionX;
    private int walkDirectionY;
    private boolean isGetInTaxi;
    private boolean isEjected;
    private boolean invincible;
    private int collisionTime;

    private final Image IMAGE;
    private Blood BLOOD;
    private final int SPEED_Y;

    private int x;
    private int y;
    private int moveY;
    private int timeOutY;

    private boolean reachedTaxi;
    private int timeOut;
    private static final int MAX_TIME_OUT = 200;
    private int health;
    private boolean isDead;
    private final float RADIUS;

    public Character(int x, int y, String type, Properties props) {
        this.WALK_SPEED_X = Integer.parseInt(props.getProperty("gameObjects.passenger.walkSpeedX"));
        this.WALK_SPEED_Y = Integer.parseInt(props.getProperty("gameObjects.passenger.walkSpeedY"));
        this.PROPS = props;

        this.TAXI_DETECT_RADIUS = Integer.parseInt(props.getProperty("gameObjects.passenger.taxiDetectRadius"));

        this.x = x;
        this.y = y;
        this.moveY = 0;
        this.SPEED_Y = Integer.parseInt(props.getProperty("gameObjects.taxi.speedY"));
        this.health = (int) (Float.parseFloat(props.getProperty("gameObjects.driver.health")) * 100);
        if(type.equals(GameObjectType.PASSENGER.name())){
            IMAGE = new Image(props.getProperty("gameObjects.passenger.image"));
            RADIUS = Float.parseFloat(props.getProperty("gameObjects.passenger.radius"));
        }else if(type.equals(GameObjectType.DRIVER.name())){
            IMAGE = new Image(props.getProperty("gameObjects.driver.image"));
            this.isGetInTaxi = true;
            RADIUS = Float.parseFloat(props.getProperty("gameObjects.driver.radius"));
        }else{
            this.isGetInTaxi = false;
            IMAGE = null;
            RADIUS = 0;
        }
        this.isEjected = false;


    }
    public Properties getProps() { return PROPS; }
    public int getX() {
        return x;
    }
    public int getY() {
        return y;
    }
    public boolean getInvincible() { return invincible; }
    public void setX(int x) { this.x = x; }
    public void setY(int y) {
        this.y = y;
    }
    public boolean getIsGetInTaxi() {
        return isGetInTaxi;
    }
    public int getWalkDirectionX() {
        return walkDirectionX;
    }
    public int getWalkDirectionY() {
        return walkDirectionY;
    }
    public int getWALK_SPEED_X(){
        return WALK_SPEED_X;
    }
    public int getWALK_SPEED_Y(){
        return WALK_SPEED_Y;
    }
    public int getTAXI_DETECT_RADIUS(){
        return TAXI_DETECT_RADIUS;
    }
    public Properties getPROPS(){
        return PROPS;
    }
    public int getSPEED_Y(){
        return SPEED_Y;
    }
    public int getMoveY(){
        return moveY;
    }
    public int getHealth(){
        return health;
    }
    public int getTimeOut(){
        return timeOut;
    }
    public int getMaxTimeOut(){
        return MAX_TIME_OUT;
    }
    public int getPostCollisionTime(){
        return POST_COLLISION_TIME;
    }
    public int getCollisionTime(){ return collisionTime; }
    public int getTaxiDetectRadius(){ return TAXI_DETECT_RADIUS; }
    @Override
    public int getTimeout(){
        return timeOut;
    }
    public void setMoveY(int newMoveY){
        moveY = newMoveY;
    }
    public void setWalkDirectionX(int newWalkDirectionX){
        walkDirectionX = newWalkDirectionX;
    }
    public void setWalkDirectionY(int newWalkDirectionY){
        walkDirectionY = newWalkDirectionY;
    }
    public boolean getIsDead(){ return BLOOD != null && BLOOD.finished(); }
    public Blood getBlood() { return BLOOD; }
    public void setBlood(int x, int y, Properties props){
        this.BLOOD = new Blood(x, y, props);
    }

    protected void adjustToInputMovement(Input input) {
        if(input != null) {
            if (input.isDown(Keys.UP)) {
                moveY = 1;
            }else {
                moveY = 0;
            }
        }else{
            moveY = 0;
        }
    }
    protected abstract void move(Taxi taxi);
    protected void move(){
        y += SPEED_Y * moveY;
    }
    protected void draw() {
        IMAGE.draw(x, y);
    }

    /**
     * Walk the people object based on the walk direction and speed.
     */
    protected void walk() {
        x += + WALK_SPEED_X * walkDirectionX;
        y += + WALK_SPEED_Y * walkDirectionY;
    }

    /**
     * Move the people object along with taxi when the people object is in the taxi.
     * @param taxi Active taxi in the game play
     */
    protected void moveWithTaxi(Taxi taxi) {
        x = taxi.getX();
        y = taxi.getY();
    }


    /**
     * Set the get in taxi status of the people object.
     * This is used to set an indication to check whether the people object is in the taxi or not.
     * @param taxi The taxi object to be checked. If it is null, the people object is not in a taxi at the moment in
     *             the game play.
     */
    public void setIsGetInTaxi(Taxi taxi) {
        if(taxi == null) {
            isGetInTaxi = false;
        } else if(taxi.getX() == x && taxi.getY() == y && this  instanceof  Passenger) {
            isGetInTaxi = true;
        }else if(this instanceof  Driver) {
            isGetInTaxi = true;
        }
    }

    /**
     * implements collision behaviour for collision with objects other than fireball
     */
    @Override
    public void hasCollided(int diffY, int damage){
        if(damage != 0)
            this.timeOut = MAX_TIME_OUT;
        collisionTime = POST_COLLISION_TIME;
        if(diffY > 0)
            this.timeOutY = 1;
        else
            this.timeOutY = -1;
        this.health -= damage;
    }

    /**
     * implements post collision behaviour
     */
    protected void postCollisionMovement(){
        y += timeOutY;
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
            invincible = false;
            this.timeOut --;
        }


        if(this.BLOOD != null)
            this.BLOOD.update(this.x, this.y, input);
        collisionTime --;
    }

    /**
     * implements collision behaviour for collision with fireball
     */
    @Override
    public void hasCollided(int damage){
        this.timeOut = MAX_TIME_OUT;
        this.timeOutY = 0;
        this.health -= damage;
    }

    @Override
    public int getDamage(){
        return 0;
    }

    @Override
    public float getRadius(){
        return this.RADIUS;
    }

    public void ejection(Taxi taxi){
        if(this.getIsGetInTaxi()) {
            x = taxi.getX() - DRIVER_EJECTION_OFFSET;

            y = taxi.getY();
        }
        setIsGetInTaxi(null);
    }


}
