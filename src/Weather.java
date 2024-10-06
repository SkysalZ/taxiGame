import java.util.ArrayList;

public class Weather implements Comparable<Weather>{
    private final String WEATHER;
    private final static String SUNNY_WEATHER = "SUNNY";
    private final int START_FRAME;
    private final int END_FRAME;
    private static int weatherIndex = 0;

    public Weather(String weather, String startFrame, String endFrame) {
        WEATHER = weather;
        START_FRAME = Integer.parseInt(startFrame);
        END_FRAME = Integer.parseInt(endFrame);
    }

    //check weather
    public boolean isSunny() {
        return WEATHER.equals(SUNNY_WEATHER);
    }

    //check if weather changed
    public static boolean weatherChanged(ArrayList<Weather> weathers, int currentFrame){
        boolean changed = weathers.get(weatherIndex).getEndFrame() < currentFrame;
        if(changed)
            weatherIndex++;
        return changed;
    }

    //getters and setters\
    public int getEndFrame() {
        return END_FRAME;
    }
    public static int getWeatherIndex() {
        return weatherIndex;
    }

    @Override
    public int compareTo(Weather weather) {
        return Integer.compare(END_FRAME, weather.getEndFrame());
    }

}
