import bagel.Image;
import bagel.Input;
import bagel.Keys;

import javax.lang.model.element.ModuleElement;
import java.util.Properties;
public class EnemyCar extends Car implements Generatable<EnemyCar>{
    private final static int DIVISIBILITY = 400;
    private final static int SPAWN_Y_1 = -50;
    private final static int SPAWN_Y_2 = 768;

    public EnemyCar(int x, int y, Properties props) {
        super(x, y, GameObjectType.ENEMY_CAR.name(), props);
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
        int newY = MiscUtils.getRandomInt(SPAWN_Y_1, SPAWN_Y_2);
        return new EnemyCar(newX, newY, props);
    }

    @Override
    public boolean checkGeneratability(){
        return MiscUtils.canSpawn(DIVISIBILITY);
    }

}
