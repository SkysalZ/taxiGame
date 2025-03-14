import bagel.Image;
import bagel.Input;
import bagel.Keys;

import javax.lang.model.element.ModuleElement;
import java.util.ArrayList;
import java.util.Properties;
public class OtherCar extends Car implements Generatable<OtherCar>{
    private final static int DIVISIBILITY = 200;
    private final static int SPAWN_Y_1 = -50;
    private final static int SPAWN_Y_2 = 768;

    public OtherCar(int x, int y, Properties props) {
        super(x, y, GameObjectType.OTHER_CAR.name(), props);
    }

    @Override
    public OtherCar toGenerate(Properties props){
        int laneNumber = MiscUtils.getRandomInt(1, 4);
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
        OtherCar newOtherCar = new OtherCar(newX, newY, props);
        newOtherCar.assignSpeed();
        return newOtherCar;
    }

    @Override
    public boolean checkGeneratability(){
        return MiscUtils.canSpawn(DIVISIBILITY);
    }

    /**
     * check Collision with other objects
     */
    public void updateCollision(ArrayList<OtherCar> otherCars, ArrayList<FireBall> fireBalls,
                                Passenger[] passengers, Taxi taxi, Driver driver) {
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
        if(!taxi.getIsActive())
            checkCollision(this, driver);
        checkCollision(this, taxi);
    }

}
