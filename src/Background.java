import bagel.Image;
import bagel.Input;
import bagel.Keys;

import java.util.ArrayList;
import java.util.Properties;

/**
 * A class representing the background of the game play.
 */
public class Background {
    private final Properties PROPS;
    private final int WINDOW_HEIGHT;
    private Image image;
    private final int SPEED_Y;

    private int x;
    private int y;
    private int moveY;

    public Background(int x, int y, Properties props, boolean isSunny) {
        this.x = x;
        this.y = y;
        this.moveY = 0;

        this.SPEED_Y = Integer.parseInt(props.getProperty("gameObjects.taxi.speedY"));
        this.WINDOW_HEIGHT = Integer.parseInt(props.getProperty("window.height"));
        this.PROPS = props;
        if(isSunny) {
            image = new Image(props.getProperty("backgroundImage.sunny"));
        }else{
            image = new Image(props.getProperty("backgroundImage.raining"));
        }
    }

    /**
     * Move the background in y direction according to the keyboard input. And render the background image.
     * @param input The current mouse/keyboard input.
     */
    public void update(Input input, Background background, ArrayList<Weather> weathers, int frameNumber) {
        int weatherIndex = 0;
        if (Weather.weatherChanged(weathers, frameNumber)){
            if(weathers.get(Weather.getWeatherIndex()).isSunny()){
                image = new Image(PROPS.getProperty("backgroundImage.sunny"));
            }else{
                image = new Image(PROPS.getProperty("backgroundImage.raining"));
            }
        }

        if(input != null) {
            adjustToInputMovement(input);
            move();
        }
        draw();


        if (y >= WINDOW_HEIGHT * 1.5) {
            y = background.getY() - WINDOW_HEIGHT;
        }
    }
    public Image getImage() {
        return image;
    }
    public int getY() {
        return y;
    }

    /**
     * Move the GameObject object in the y-direction based on the speedY attribute.
     */
    public void move() {
        this.y += SPEED_Y * moveY;
    }

    /**
     * Draw the GameObject object into the screen.
     */
    public void draw() {
        image.draw(x, y);
    }

    /**
     * Adjust the movement direction in y-axis of the GameObject based on the keyboard input.
     * @param input The current mouse/keyboard input.
     */
    public void adjustToInputMovement(Input input) {
        if(input != null) {
            if (input.isDown(Keys.UP)) {
                moveY = 1;
            }else {
                moveY = 0;
            }
        }else  {
            moveY = 0;
        }
    }
    //make sure two backgrounds are the same
    public void updateWeather(Image newImage){
        this.image = newImage;
    }

    public int getMoveY() {
        return moveY;
    }
}
