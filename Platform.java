import bagel.Image;
import java.util.Properties;

public class Platform extends GameEntity {
    private final Image PLATFORM_IMAGE = new Image(this.getGameProps().getProperty("gameObjects.platform.image"));
    private final double PLATFORM_SPEED = Double.parseDouble(this.getGameProps().getProperty("gameObjects.platform.speed"));

    // New variables to save initial positions.
    private final double initX;
    private final int MAX_X = 3000;

    public Platform(Properties gameProps, double x, double y) {
        super(gameProps, x, y);
        this.initX = x;  // Save initial x position.
    }

    public Image getPlatformImage() {
        return this.PLATFORM_IMAGE;
    }

    public void moveLeft() {
        this.setX(this.getX() - PLATFORM_SPEED); // Move platform to the left.
    }

    public void moveRight() {
        if (this.getX() < MAX_X) {  // Move platform to the right.
            this.setX(this.getX() + PLATFORM_SPEED);
        }
    }

    // Reset the platform.
    public void reset() {
        this.setX(initX);
    }
}