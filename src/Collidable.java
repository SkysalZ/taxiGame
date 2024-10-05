public interface Collidable {
    float getRadius();
    int getDamage();
    int getY();
    int getX();
    boolean getInvincible();
    void hasCollided(int diffY, int damage);
    void hasCollided(int damage);
    int getTimeout();

}
