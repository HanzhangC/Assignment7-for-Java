import bagel.Image;
import java.util.Properties;

public class Coin extends GameEntity {
    private boolean isCollide = false;

    private final Image COIN_IMAGE = new Image(this.getGameProps().getProperty("gameObjects.coin.image"));
    private final double COIN_RADIUS = Double.parseDouble(this.getGameProps().getProperty("gameObjects.coin.radius"));
    private final int COIN_VALUE = Integer.parseInt(this.getGameProps().getProperty("gameObjects.coin.value"));
    private final double COIN_SPEED = Double.parseDouble(this.getGameProps().getProperty("gameObjects.coin.speed"));

    // New variables to save initial positions.
    private final double initX;
    private final double initY;

    public Coin(Properties gameProps, double x, double y) {
        super(gameProps, x, y);
        this.initX = x;  // Save initial x position.
        this.initY = y;  // Save initial y position.
    }

    public boolean getIsCollide() { return this.isCollide; }
    public Image getImage() { return this.COIN_IMAGE; }
    public double getCoinRadius() { return this.COIN_RADIUS; }
    public int getCoinValue() { return this.COIN_VALUE; }
    public void setIsCollide(boolean isCollide) { this.isCollide = isCollide; }

    public void moveLeft() { this.setX(this.getX() - COIN_SPEED); } // Move coins left.
    public void moveRight() { this.setX(this.getX() + COIN_SPEED); } // Move coins right.
    public void moveUp() { this.setY(this.getY() - COIN_SPEED * 2); } // Move coins up.

    public boolean checkCollision(Player p) {
        double range = p.getPlayerRadius() + this.getCoinRadius();
        double distance = Math.sqrt(Math.pow(p.getX() - this.getX(), 2) + Math.pow(p.getY() - this.getY(), 2));
        return distance <= range; // Check whether there is collision with player.
    }

    // Reset the coins.
    public void reset() {
        this.setX(initX);
        this.setY(initY);
        this.isCollide = false;
    }
}