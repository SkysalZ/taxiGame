import bagel.Input;

import java.util.ArrayList;
import java.util.Properties;

public class FireBall extends Object{
    private final float RADIUS;
    private final int SPEED_Y;
    private boolean isCollided;
    private final int DAMAGE;
    private final static int DIVISIBILITY = 300;

    public FireBall(int x, int y, Properties props) {
        super(x, y, GameObjectType.FIRE_BALL.name(), props);

        this.RADIUS = Float.parseFloat(props.getProperty("gameObjects.fireball.radius"));
        this.DAMAGE = (int) (Float.parseFloat(props.getProperty("gameObjects.fireball.damage")) * 100);
        this.SPEED_Y = Integer.parseInt(props.getProperty("gameObjects.fireball.shootSpeedY"));
    }

    public static int getDivisibility() {
        return DIVISIBILITY;
    }

    public float getRadius() {
        return RADIUS;
    }

    public int getDamage() { return DAMAGE; }
    public boolean getIsCollided() { return isCollided; }

    /**
     * implements collision behaviour
     */
    public void hasCollided(){
        this.isCollided = true;
    }

    @Override
    public void update(Input input) {
        if(!isCollided) {
            adjustToInputMovement(input);
            setY(getY() - SPEED_Y);
            move();
            draw();
        }
    }

    /**
     * check Collision with other objects
     */
    public void updateCollision(Taxi taxi) {
        checkCollision(this, taxi);
    }

    private <T extends Collidable>void checkCollision(FireBall fireBall, T t) {
        float currDistance = (float) Math.sqrt(Math.pow(fireBall.getX() - t.getX(), 2) +
                Math.pow(fireBall.getY() - t.getY(), 2));
        if (currDistance < t.getRadius() + fireBall.getRadius() && t.getTimeout() == -1 && !fireBall.getIsCollided()) {
            fireBall.hasCollided();
            t.hasCollided(0, DAMAGE);
        }
    }




}
