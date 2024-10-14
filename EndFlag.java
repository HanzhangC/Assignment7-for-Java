import bagel.Image;
import java.util.Properties;

public class EndFlag extends GameEntity {
    private final Image END_FLAG_IMAGE = new Image(this.getGameProps().getProperty("gameObjects.endFlag.image"));
    private final double END_FLAG_SPEED = Double.parseDouble(this.getGameProps().getProperty("gameObjects.endFlag.speed"));
    private final double END_FLAG_RADIUS = Double.parseDouble(this.getGameProps().getProperty("gameObjects.endFlag.radius"));

    // New variables to save initial positions.
    private final double initX;

    public EndFlag(Properties gameProps, double x, double y) {
        super(gameProps, x, y);
        this.initX = x;  // Save initial x position.
    }

    public Image getEndFlagImage() { return this.END_FLAG_IMAGE; }
    public double getEndFlagRadius() { return this.END_FLAG_RADIUS; }

    // Move endflag left.
    public void moveLeft() { this.setX(this.getX() - END_FLAG_SPEED); }
    // Move endflag right.
    public void moveRight() { this.setX(this.getX() + END_FLAG_SPEED); }

    // Check whether there is collision with player.
    public boolean checkCollision(Player p) {
        double range = p.getPlayerRadius() + this.getEndFlagRadius();
        double distance = Math.sqrt(Math.pow(p.getX() - this.getX(), 2) + Math.pow(p.getY() - this.getY(), 2));
        return distance <= range;
    }

    // Reset endflag.
    public void reset() {
        this.setX(initX);
    }
}