import java.util.Properties;

public class Smoke extends Effect{
    public Smoke(int x, int y, Properties props){
        super(x, y, GameObjectType.SMOKE.name(), props);
    }
}
