public interface Collidable {
    float getRadius();
    public int getDamage();
    int getY();
    int getX();
    boolean getInvincible();
    void hasCollided(int diffY, int damage);
    int getTimeout();

}
