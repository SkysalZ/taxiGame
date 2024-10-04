import java.util.Properties;

public class Fire extends Effect{
    public Fire(int x, int y, Properties props){
        super(x, y, GameObjectType.FIRE.name(), props);
    }
}
