import bagel.Image;
import java.util.Properties;
public class Player extends GameEntity {
    private double speed = 0;
    private int jumpNum = 20;
    private int score = 0;
    private double health = 1;
    private int SPEED_INCREASE = 1;
    private boolean faceLeft = false;

    // New variables to save initial positions and health.
    private final double initX;
    private final double initY;
    private final double initHealth;

    private final Image PLAYER_RIGHT = new Image(this.getGameProps().getProperty("gameObjects.player.imageRight"));
    private final Image PLAYER_LEFT = new Image(this.getGameProps().getProperty("gameObjects.player.imageLeft"));
    private final double PLAYER_RADIUS = Double.parseDouble(this.getGameProps().getProperty("gameObjects.player.radius"));
    private final double PLAYER_HEALTH = Double.parseDouble(this.getGameProps().getProperty("gameObjects.player.health"));

    public Player(Properties gameProps, double x, double y) {
        super(gameProps, x, y);
        this.initX = x;
        this.initY = y;
        this.initHealth = PLAYER_HEALTH;
    }

    public int renderHealth() { return (int)((this.health / PLAYER_HEALTH) * 100); }
    public double getSpeed() { return this.speed; }
    public int getScore() { return this.score; }
    public double getHealth() { return this.health; }
    public boolean getFaceLeft() { return this.faceLeft; }
    public Image getPlayerRight() { return this.PLAYER_RIGHT; }
    public Image getPlayerLeft() { return this.PLAYER_LEFT; }
    public double getPlayerRadius() { return this.PLAYER_RADIUS; }
    public void setSpeed(double speed) { this.speed = speed; }
    public void setScore(int score) { this.score = score; }
    public void setHealth(double health) { this.health = health; }
    public void setFaceLeft(boolean faceLeft) { this.faceLeft = faceLeft; }

    public void moveUp() { this.setY(this.getY() - jumpNum); }
    public void moveDown() { this.setY(this.getY() + speed); }
    public void increaseSpeed() { this.setSpeed(this.getSpeed() + SPEED_INCREASE); }
    public void dropOut() { this.setY(this.getY() + SPEED_INCREASE * 2); }

    // Reset player.
    public void reset() {
        this.setX(initX);
        this.setY(initY);
        this.health = initHealth;
        this.speed = 0;
        this.score = 0;
        this.faceLeft = false;
    }
}