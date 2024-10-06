import bagel.Image;
import bagel.Input;
import bagel.Keys;

import javax.lang.model.element.ModuleElement;
import java.util.ArrayList;
import java.util.Properties;
public class EnemyCar extends Car implements Generatable<EnemyCar>{
    private final static int DIVISIBILITY = 400;
    private final static int SPAWN_Y_1 = -50;
    private final static int SPAWN_Y_2 = 768;
    private final float FIREBALL_RADIUS;

    public EnemyCar(int x, int y, Properties props) {

        super(x, y, GameObjectType.ENEMY_CAR.name(), props);
        FIREBALL_RADIUS = Float.parseFloat(super.getProps().getProperty("gameObjects.fireball.radius"));
    }

    @Override
    public EnemyCar toGenerate(Properties props){
        int laneNumber = MiscUtils.getRandomInt(1, 3);
        int newX;
        if(laneNumber == 1){
            newX = Integer.parseInt(super.getProps().getProperty("roadLaneCenter1"));
        }else if(laneNumber == 2){
            newX = Integer.parseInt(super.getProps().getProperty("roadLaneCenter2"));
        }else if(laneNumber == 3){
            newX = Integer.parseInt(super.getProps().getProperty("roadLaneCenter3"));
        }else{
            newX = 0;
        }
        int newY = MiscUtils.selectAValue(SPAWN_Y_1, SPAWN_Y_2);
        EnemyCar newEnemyCar = new EnemyCar(newX, newY, props);
        newEnemyCar.assignSpeed();
        return newEnemyCar;
    }

    @Override
    public boolean checkGeneratability(){
        return MiscUtils.canSpawn(DIVISIBILITY);
    }

    public FireBall makeFireBall(Properties props) {
        if(MiscUtils.canSpawn(FireBall.getDivisibility()))
            return new FireBall(super.getX(), (int) (super.getY() - super.getRadius() - FIREBALL_RADIUS), props);
        else
            return null;
    }

    /**
     * check Collision with other objects
     */
    public void updateCollision(ArrayList<EnemyCar> enemyCars, ArrayList<OtherCar> otherCars,
                                ArrayList<FireBall> fireBalls, Passenger[] passengers, Taxi taxi, Driver driver) {
        for(EnemyCar enemyCar : enemyCars){
            checkCollision(this, enemyCar);
        }
        for(OtherCar otherCar : otherCars){
            checkCollision(this, otherCar);
        }
        for(FireBall fireBall : fireBalls){
            checkCollision(this, fireBall);
        }
        for(Passenger passenger : passengers){
            if(!taxi.getIsActive() || !passenger.getIsGetInTaxi())
                checkCollision(this, passenger);
        }
        if(!taxi.getIsActive()) {
            checkCollision(this, driver);
        }
        checkCollision(this, taxi);
    }




}
