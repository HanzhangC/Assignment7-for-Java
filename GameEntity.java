import java.util.Properties;

public class GameEntity {
    private double x;  // X position.
    private double y;  // Y position.
    private final Properties gameProps;  // Game properties.

    // Loading the game.
    public GameEntity(Properties gameProps, double x, double y) {
        this.gameProps = gameProps;
        this.x = x;
        this.y = y;
    }

    // Get game properties.
    public Properties getGameProps() { return this.gameProps; }
    // Get X position.
    public double getX() { return this.x; }
    // Get Y position.
    public double getY() { return this.y; }
    // Set X position.
    public void setX(double x) { this.x = x; }
    // Set Y position.
    public void setY(double y) { this.y = y; }
}