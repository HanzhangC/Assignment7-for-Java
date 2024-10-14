import bagel.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Properties;

/**
 * Skeleton Code for SWEN20003 Project 1, Semester 1, 2024
 *
 * Please enter your name below
 * @HanzhangChen
 */
public class ShadowMario extends AbstractGame {
    private final Image BACKGROUND_IMAGE;
    private final String TITLE_MESSAGE;
    private final int TITLE_X;
    private final int TITLE_Y;
    private final Font TITLE_FONT;
    private final String START_MESSAGE;
    private final int START_Y;
    private final String WIN_MESSAGE;
    private final String LOSE_MESSAGE;
    private final int MESSAGE_Y;
    private final Font MESSAGE_FONT;
    private final String SCORE_MESSAGE;
    private final int SCORE_X;
    private final int SCORE_Y;
    private final Font SCORE_FONT;
    private final String HEALTH_MESSAGE;
    private final int HEALTH_X;
    private final int HEALTH_Y;
    private final Font HEALTH_FONT;
    private double playerInitX;
    private double playerInitY;
    private Player player;
    private Platform platform;
    private Coin[] coins;
    private Enemy[] enemies;
    private EndFlag endFlag;
    private boolean isStart = true;
    private boolean gameOn = false;
    private boolean gameWin = false;
    private boolean gameLose = false;

    /**
     * The constructor
     */
    public ShadowMario(Properties game_props, Properties message_props) {
        super(Integer.parseInt(game_props.getProperty("windowWidth")),
                Integer.parseInt(game_props.getProperty("windowHeight")),
                message_props.getProperty("title"));

        BACKGROUND_IMAGE = new Image(game_props.getProperty("backgroundImage"));
        TITLE_MESSAGE = message_props.getProperty("title");
        TITLE_X = Integer.parseInt(game_props.getProperty("title.x"));
        TITLE_Y = Integer.parseInt(game_props.getProperty("title.y"));
        int titleFontSize = Integer.parseInt(game_props.getProperty("title.fontSize"));
        TITLE_FONT = new Font(game_props.getProperty("font"), titleFontSize);
        START_MESSAGE = message_props.getProperty("instruction");
        START_Y = Integer.parseInt(game_props.getProperty("instruction.y"));
        WIN_MESSAGE = message_props.getProperty("gameWon");
        LOSE_MESSAGE = message_props.getProperty("gameOver");
        MESSAGE_Y = Integer.parseInt(game_props.getProperty("message.y"));
        int messageFontSize = Integer.parseInt(game_props.getProperty("instruction.fontSize"));
        MESSAGE_FONT = new Font(game_props.getProperty("font"), messageFontSize);
        SCORE_MESSAGE = message_props.getProperty("score");
        SCORE_X = Integer.parseInt(game_props.getProperty("score.x"));
        SCORE_Y = Integer.parseInt(game_props.getProperty("score.y"));
        int scoreFontSize = Integer.parseInt(game_props.getProperty("score.fontSize"));
        SCORE_FONT = new Font(game_props.getProperty("font"), scoreFontSize);
        HEALTH_MESSAGE = message_props.getProperty("health");
        HEALTH_X = Integer.parseInt(game_props.getProperty("health.x"));
        HEALTH_Y = Integer.parseInt(game_props.getProperty("health.y"));
        int healthFontSize = Integer.parseInt(game_props.getProperty("health.fontSize"));
        HEALTH_FONT = new Font(game_props.getProperty("font"), healthFontSize);

        // Initialize game.
        coins = new Coin[Integer.parseInt(game_props.getProperty("gameObjects.coin.coinCount"))];
        enemies = new Enemy[Integer.parseInt(game_props.getProperty("gameObjects.enemy.enemyCount"))];
        readCSV(game_props);
    }

    /**
     * The entry point for the program.
     */
    public static void main(String[] args) {
        Properties game_props = IOUtils.readPropertiesFile("res/app.properties");
        Properties message_props = IOUtils.readPropertiesFile("res/message_en.properties");

        ShadowMario game = new ShadowMario(game_props, message_props);
        game.run();
    }

    /**
     * Performs a state update.
     * Allows the game to exit when the escape key is pressed.
     */
    @Override
    protected void update(Input input) {
        // Close window.
        if (input.wasPressed(Keys.ESCAPE)){
            Window.close();
        }

        // Draw background.
        BACKGROUND_IMAGE.draw(Window.getWidth()/2.0, Window.getHeight()/2.0);
        if(!gameOn){
            drawStartScreen(input);
        } else if(gameWin) {
            drawEndScreen(input);
        } else if(gameLose){
            player.dropOut();
            drawEndScreen(input);
            if (input.wasPressed(Keys.SPACE)) {
                resetGame();
                gameOn = false;
            }
        }

        if(gameOn && !gameWin && !gameLose){
            gameplay(input);
        }
    }

    // Logic when game on.
    private void gameplay(Input input) {
        SCORE_FONT.drawString(SCORE_MESSAGE + player.getScore(), SCORE_X, SCORE_Y);
        HEALTH_FONT.drawString(HEALTH_MESSAGE + player.renderHealth(), HEALTH_X, HEALTH_Y);
        playerControl(input);
        checkCollisions();
    }

    // Player control logic.
    private void playerControl(Input input) {
        if(isStart){
            player.setFaceLeft(false);
            isStart = false;
        }
        if(input.isDown(Keys.RIGHT) || input.wasPressed(Keys.RIGHT)){
            player.setFaceLeft(false);
            moveGameObjectsLeft();
        } else if(input.isDown(Keys.LEFT) || input.wasPressed(Keys.LEFT)){
            player.setFaceLeft(true);
            moveGameObjectsRight();
        }
        if(input.isDown(Keys.UP)){
            player.moveUp();
        }
        playerGravityControl();
    }

    // Move game object.
    private void moveGameObjectsLeft() {
        for(Coin coin: coins){
            coin.moveLeft();
        }
        for(Enemy enemy: enemies){
            enemy.moveLeft();
        }
        platform.moveLeft();
        endFlag.moveLeft();
    }

    private void moveGameObjectsRight() {
        for(Coin coin: coins){
            coin.moveRight();
        }
        for(Enemy enemy: enemies){
            enemy.moveRight();
        }
        platform.moveRight();
        endFlag.moveRight();
    }

    // Control player gravity.
    private void playerGravityControl() {
        if(player.getY() < playerInitY){
            player.increaseSpeed();
            player.moveDown();
        } else{
            player.setY(playerInitY);
            player.setSpeed(0);
        }
        drawPlayer();
    }

    // Draw player.
    private void drawPlayer() {
        if(player.getFaceLeft()){
            player.getPlayerLeft().draw(player.getX(), player.getY());
        } else{
            player.getPlayerRight().draw(player.getX(), player.getY());
        }
    }

    // Check whether collisions.
    private void checkCollisions() {
        for(Coin coin: coins){
            if(coin.getIsCollide()){
                coin.moveUp();
            } else{
                if(coin.checkCollision(player)){
                    player.setScore(player.getScore() + coin.getCoinValue());
                    coin.setIsCollide(true);
                }
            }
            coin.getImage().draw(coin.getX(), coin.getY());
        }
        for(Enemy enemy: enemies){
            if(enemy.checkCollision(player)){
                if(enemy.getStatus()){
                    player.setHealth(player.getHealth() - enemy.getDamageSize());
                    enemy.setStatus(false);
                }
            }
            enemy.getEnemyImage().draw(enemy.getX(), enemy.getY());
        }
        platform.getPlatformImage().draw(platform.getX(), platform.getY());
        endFlag.getEndFlagImage().draw(endFlag.getX(), endFlag.getY());
        if(endFlag.checkCollision(player)){
            gameWin = true;
        }
        if (player.getHealth() <= 0 && !gameLose) {
            gameLose = true;  // Set game as lose.
        }
    }

    // Game reset.
    private void resetGame() {
        gameOn = true;
        gameWin = false;
        gameLose = false;
        isStart = true;

        player.reset(); // Reset player state
        for (Enemy enemy : enemies) {
            enemy.reset();
        }
        for (Coin coin : coins) {
            coin.reset();
        }
        platform.reset();
        endFlag.reset();
    }

    // Read CSV.
    private void readCSV(Properties gameProps){
        try(BufferedReader reader = new BufferedReader(new FileReader(gameProps.getProperty("levelFile")))){
            String line;
            int coin_count = 0;
            int enemy_count = 0;
            while((line = reader.readLine()) != null){
                String[] section = line.split(",");
                switch (section[0]) {
                    case "PLATFORM":
                        platform = new Platform(gameProps, Double.parseDouble(section[1]), Double.parseDouble(section[2]));
                        break;
                    case "PLAYER":
                        player = new Player(gameProps, Double.parseDouble(section[1]), Double.parseDouble(section[2]));
                        playerInitY = Double.parseDouble(section[2]);
                        playerInitX = Double.parseDouble(section[1]);
                        break;
                    case "COIN":
                        coins[coin_count++] = new Coin(gameProps, Double.parseDouble(section[1]), Double.parseDouble(section[2]));
                        break;
                    case "ENEMY":
                        enemies[enemy_count++] = new Enemy(gameProps, Double.parseDouble(section[1]), Double.parseDouble(section[2]));
                        break;
                    case "END_FLAG":
                        endFlag = new EndFlag(gameProps, Double.parseDouble(section[1]), Double.parseDouble(section[2]));
                        break;
                }
            }
        } catch(IOException e){
            e.printStackTrace();
            System.exit(-1);
        }
    }

    // Draw the start screen.
    private void drawStartScreen(Input input){
        TITLE_FONT.drawString(TITLE_MESSAGE, TITLE_X, TITLE_Y);
        MESSAGE_FONT.drawString(START_MESSAGE, (Window.getWidth() - MESSAGE_FONT.getWidth(START_MESSAGE)) / 2.0, START_Y);
        if(input.wasPressed(Keys.SPACE)){
            gameOn = true;
        }
    }

    // Draw the end screen.
    private void drawEndScreen(Input input){
        if(gameWin){
            MESSAGE_FONT.drawString(WIN_MESSAGE, (Window.getWidth() - MESSAGE_FONT.getWidth(WIN_MESSAGE)) / 2.0, MESSAGE_Y);
        }
        if(gameLose){
            MESSAGE_FONT.drawString(LOSE_MESSAGE, (Window.getWidth() - MESSAGE_FONT.getWidth(LOSE_MESSAGE)) / 2.0, MESSAGE_Y);
        }
    }
}