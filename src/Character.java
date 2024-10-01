import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.Properties;

public abstract class Character {
    private final int TAXI_DETECT_RADIUS;
    private final Properties PROPS;

    private final int WALK_SPEED_X;
    private final int WALK_SPEED_Y;
    private int walkDirectionX;
    private int walkDirectionY;
    private boolean isGetInTaxi;
    private boolean isEjected;

    private final Image IMAGE;
    private final int SPEED_Y;

    private int x;
    private int y;
    private int moveY;

    private boolean reachedTaxi;
    private int timeOut;
    private static final int MAX_TIME_OUT = 200;
    private int health;
    private boolean isDead;

    public Character(int x, int y, String type, Properties props) {
        this.WALK_SPEED_X = Integer.parseInt(props.getProperty("gameObjects.passenger.walkSpeedX"));
        this.WALK_SPEED_Y = Integer.parseInt(props.getProperty("gameObjects.passenger.walkSpeedY"));
        this.PROPS = props;

        this.TAXI_DETECT_RADIUS = Integer.parseInt(props.getProperty("gameObjects.passenger.taxiDetectRadius"));

        this.x = x;
        this.y = y;
        this.moveY = 0;
        this.SPEED_Y = Integer.parseInt(props.getProperty("gameObjects.taxi.speedY"));
        if(type.equals(GameObjectType.PASSENGER.name())){
            IMAGE = new Image(props.getProperty("gameObjects.passenger.image"));
        }else if(type.equals(GameObjectType.DRIVER.name())){
            IMAGE = new Image(props.getProperty("gameObjects.driver.image"));
            this.isGetInTaxi = true;
        }else{
            this.isGetInTaxi = false;
            IMAGE = null;
        }
        this.isEjected = false;


    }
    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

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
    public void setMoveY(int newMoveY){
        moveY = newMoveY;
    }
    public void setWalkDirectionX(int newWalkDirectionX){
        walkDirectionX = newWalkDirectionX;
    }
    public void setWalkDirectionY(int newWalkDirectionY){
        walkDirectionY = newWalkDirectionY;
    }

    public abstract void updateWithTaxi(Input input, Taxi taxi);
    protected void adjustToInputMovement(Input input) {
        if (input.wasPressed(Keys.UP)) {
            moveY = 1;
        }  else if(input.wasReleased(Keys.UP)) {
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
     * Check if the taxi is adjacent to the passenger. This is evaluated based on multiple crietria.
     * @param taxi The active taxi in the game play.
     * @return a boolean value indicating if the taxi is adjacent to the passenger.
     */
    protected boolean adjacentToObject(Taxi taxi) {
        // Check if Taxi is stopped and health > 0
        // Check if Taxi is in the passenger's detect radius
        boolean taxiStopped = !taxi.getIsMovingX() && !taxi.isMovingY();
        // Check if Taxi is in the passenger's detect radius
        float currDistance = (float) Math.sqrt(Math.pow(taxi.getX() - x, 2) + Math.pow(taxi.getY() - y, 2));
        // Check if Taxi is not having another trip
        boolean isHavingAnotherTrip = taxi.getTrip() != null && taxi.getTrip().getPassenger() != this;

        return currDistance <= TAXI_DETECT_RADIUS && taxiStopped && !isHavingAnotherTrip;
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
        } else if((float) Math.sqrt(Math.pow(taxi.getX() - x, 2) + Math.pow(taxi.getY() - y, 2)) <= 1) {
            isGetInTaxi = true;
        }
    }
}
