import java.util.Properties;

public class Blood extends Effect{
    public Blood(int x, int y, Properties props){
        super(x, y, GameObjectType.BLOOD.name(), props);
    }
}
