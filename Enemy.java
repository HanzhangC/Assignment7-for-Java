import bagel.Image;
import java.util.Properties;

public class Enemy extends GameEntity {
    private final Image ENEMY_IMAGE = new Image(this.getGameProps().getProperty("gameObjects.enemy.image"));
    private final double ENEMY_RADIUS = Double.parseDouble(this.getGameProps().getProperty("gameObjects.enemy.radius"));
    private final double DAMAGE_SIZE = Double.parseDouble(this.getGameProps().getProperty("gameObjects.enemy.damageSize"));
    private final double ENEMY_SPEED = Double.parseDouble(this.getGameProps().getProperty("gameObjects.enemy.speed"));
    private boolean isActive = true;

    // New variables to save initial positions.
    private final double initX;
    private final double initY;

    public Enemy(Properties gameProps, double x, double y) {
        super(gameProps, x, y);
        this.initX = x;  // Save initial x position.
        this.initY = y;  // Save initial y position.
    }

    public Image getEnemyImage() { return this.ENEMY_IMAGE; }
    public double getEnemyRadius() { return this.ENEMY_RADIUS; }
    public double getDamageSize() { return this.DAMAGE_SIZE; }
    public boolean getStatus() { return this.isActive; }
    public void setStatus(boolean isActive) { this.isActive = isActive; }

    // Move enemies left.
    public void moveLeft() { this.setX(this.getX() - ENEMY_SPEED); }
    // Move enemies right.
    public void moveRight() { this.setX(this.getX() + ENEMY_SPEED); }

    // Check whether there is collision with player.
    public boolean checkCollision(Player p) {
        double range = p.getPlayerRadius() + this.getEnemyRadius();
        double distance = Math.sqrt(Math.pow(p.getX() - this.getX(), 2) + Math.pow(p.getY() - this.getY(), 2));
        return distance <= range;
    }

    // Reset enemies.
    public void reset() {
        this.setX(initX);
        this.setY(initY);
        this.isActive = true;
    }
}