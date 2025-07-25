package gdd.scene;

import gdd.ActivePowerUp;
import gdd.AudioPlayer;
import gdd.Game;
import static gdd.Global.*;
import gdd.ScoreManager;
import gdd.SpawnDetails;
import gdd.powerup.BIGShot;
import gdd.powerup.BurstShot;
import gdd.powerup.PowerUp;
import gdd.powerup.SpeedUp;
import gdd.powerup.TripleShot;
import gdd.sprite.Alien1;
import gdd.sprite.Alien2;
import gdd.sprite.Enemy;
import gdd.sprite.Explosion;
import gdd.sprite.Player;
import gdd.sprite.Shot;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Scene1 extends JPanel {

    private int frame = 0;
    private List<PowerUp> powerups;
    private List<Enemy> enemies;
    private List<Explosion> explosions;
    private List<Shot> shots;
    private Player player;
    private Shot shot;
    private final List<ActivePowerUp> activePowerUps = new ArrayList<>();
    private static final int SHOT_COOLDOWN_MS = 300;
    private long lastShotTime = 0;
    private boolean isPaused = false;
    private boolean ShowMessage = false;
    private boolean finalwaveSpawned = false;
    private int playerDX = 0;
    private int bgOffsetX = 0; // background horizontal offset



    final int BLOCKHEIGHT = 50;
    final int BLOCKWIDTH = 50;


    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private String message = "Game Over";

    private final Dimension d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    private final Random randomizer = new Random();
    private final Map<Class<? extends PowerUp>, Integer> powerUpEndFrames = new HashMap<>();
    private boolean spawnedAt5 = false;
    private boolean spawnedAt15 = false;
    private boolean spawnedAt20 = false;


    private Timer timer;
    private final Game game;

    private final int[][] MAP = {
    {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
    {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
    {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 1, 0, 0, 1, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
    {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
    {0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0},
    {0, 0, 1, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {1, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 1, 0},
    {0, 0, 0, 0, 0, 1, 0, 0, 0, 0, 0, 0},
    {0, 0, 0, 0, 0, 0, 0, 1, 0, 0, 0, 0}
};

    private final HashMap<Integer, List<SpawnDetails>> spawnMap = new HashMap<>();
    private AudioPlayer backgroundPlayer;
    private AudioPlayer sfxPlayer;

    public Scene1(Game game) {
        this.game = game;
        initBoard();
        loadSpawnDetails();
    }

    private void initAudio() {
        try {
            backgroundPlayer = new AudioPlayer(SND_SCENE1, true);
            backgroundPlayer.play();
        } catch (Exception e) {
            System.err.println("Error initializing audio player: " + e.getMessage());
        }
    }

    private void loseAudio() {
        try {
            backgroundPlayer = new AudioPlayer(SND_LOSE, true);
            backgroundPlayer.play();
        } catch (Exception e) {
            System.err.println("Error initializing audio player: " + e.getMessage());
        }
    }

    private void explAudio() {
        try {
            sfxPlayer = new AudioPlayer(SND_EXPL, false);
            sfxPlayer.play();
        } catch (Exception e) {
            System.err.println("Error initializing audio player: " + e.getMessage());
        }
    }

    private void shotAudio() {
        try {
            sfxPlayer = new AudioPlayer(SND_SHOT, false);
            sfxPlayer.play();
        } catch (Exception e) {
            System.err.println("Error initializing audio player: " + e.getMessage());
        }
    }

    private void bombAudio() {
        try {
            sfxPlayer = new AudioPlayer(SND_LZR[randomizer.nextInt(SND_LZR.length)], false);
            sfxPlayer.play();
        } catch (Exception e) {
            System.err.println("Error initializing audio player: " + e.getMessage());
        }
    }

    private void powerupAudio() {
        try {
            sfxPlayer = new AudioPlayer(SND_POWERUP, false);
            sfxPlayer.play();
        } catch (Exception e) {
            System.err.println("Error initializing audio player: " + e.getMessage());
        }
    }

    private void loadSpawnDetails() {
        addSpawn(200, new SpawnDetails("Alien1", randomizer.nextInt(100, 300), 0));
        addSpawn(300, new SpawnDetails("Alien1", randomizer.nextInt(200, 400), 0));

        int randspawn1 = randomizer.nextInt(200, 600);
        addSpawn(400, new SpawnDetails("Alien1", randspawn1, 0));
        addSpawn(400, new SpawnDetails("Alien1", randspawn1 + 50, 0));

        int randspawn2 = randomizer.nextInt(0, 200);
        addSpawn(500, new SpawnDetails("Alien1", randspawn2, 0));
        addSpawn(500, new SpawnDetails("Alien1", randspawn2 + 50, 0));
        addSpawn(500, new SpawnDetails("Alien1", randspawn2 + 100, 0));
        addSpawn(500, new SpawnDetails("Alien1", randspawn2 + 150, 0));
    }

    private void addSpawn(int frame, SpawnDetails details) {
        spawnMap.computeIfAbsent(frame, k -> new ArrayList<>()).add(details);
    }



    private void initBoard() {

    }

    public void start() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        requestFocusInWindow();
        setBackground(Color.black);

        timer = new Timer(1000 / 30, new GameCycle());
        timer.start();

        gameInit();
        initAudio();
    }

    public void stop() {
        timer.stop();
        try {
            if (backgroundPlayer != null) {
                backgroundPlayer.stop();
            }
            if (sfxPlayer != null) {
                sfxPlayer.stop();
            }
        } catch (Exception e) {
            System.err.println("Error closing audio player.");
        }
    }

    private void gameInit() {

        enemies = new ArrayList<>();
        powerups = new ArrayList<>();
        explosions = new ArrayList<>();
        shots = new ArrayList<>();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 6; j++) {
                var enemy = new Enemy(ALIEN_INIT_X + (ALIEN_WIDTH + ALIEN_GAP) * j,
                ALIEN_INIT_Y + (ALIEN_HEIGHT + ALIEN_GAP) * i);
                enemies.add(enemy);
            }
        }
        player = new Player();
        shot = new Shot();
    }

    private void drawMap(Graphics g) {
        int scrollSpeedY = 2;
        int scrollOffsetY = (frame * scrollSpeedY) % BLOCKHEIGHT;
        int baseRow = (frame * scrollSpeedY) / BLOCKHEIGHT;
        int rowsNeeded = (BOARD_HEIGHT / BLOCKHEIGHT) + 3;

        for (int screenRow = 0; screenRow < rowsNeeded; screenRow++) {
            int mapRow = (baseRow + screenRow) % MAP.length;
            int y = (screenRow * BLOCKHEIGHT) - scrollOffsetY;

            if (y > BOARD_HEIGHT || y < -BLOCKHEIGHT) continue;

            for (int col = 0; col < MAP[mapRow].length; col++) {
                if (MAP[mapRow][col] == 1) {
                    int x = (col * BLOCKWIDTH) + bgOffsetX;

                    int wrappedX = ((x % BOARD_WIDTH) + BOARD_WIDTH) % BOARD_WIDTH;

                    drawStarCluster(g, wrappedX, y, BLOCKWIDTH, BLOCKHEIGHT);
                }
            }
        }
    }




    private void drawStarCluster(Graphics g, int x, int y, int width, int height) {
        // Set star color to white
        g.setColor(Color.WHITE);

        // Draw multiple stars in a cluster pattern
        // Main star (larger)
        int centerX = x + width / 2;
        int centerY = y + height / 2;
        g.fillOval(centerX - 2, centerY - 2, 4, 4);

        // Smaller surrounding stars
        g.fillOval(centerX - 15, centerY - 10, 2, 2);
        g.fillOval(centerX + 12, centerY - 8, 2, 2);
        g.fillOval(centerX - 8, centerY + 12, 2, 2);
        g.fillOval(centerX + 10, centerY + 15, 2, 2);

        // Tiny stars for more detail
        g.fillOval(centerX - 20, centerY + 5, 1, 1);
        g.fillOval(centerX + 18, centerY - 15, 1, 1);
        g.fillOval(centerX - 5, centerY - 18, 1, 1);
        g.fillOval(centerX + 8, centerY + 20, 1, 1);
    }

    private void drawAliens(Graphics g) {

        for (Enemy enemy : enemies) {

            if (enemy.isVisible()) {
                
                g.drawImage(enemy.getImage(), enemy.getX(), enemy.getY(), this);
            }

            if (enemy.isDying()) {

                enemy.die();
            }
        }
    }

    private void drawPowerUps(Graphics g) {

        for (PowerUp p : powerups) {

            if (p.isVisible()) {

                g.drawImage(p.getImage(), p.getX(), p.getY(), this);
            }

            if (p.isDying()) {

                p.die();
            }
        }
    }

    private void drawPlayer(Graphics g) {

        if (player.isVisible()) {
            g.drawImage(player.getCurrentImage(), player.getX(), player.getY(), this);
        }

        if (player.isDying()) {
            explosions.add(new Explosion(player.getX(), player.getY()));
            new javax.swing.Timer(100, e -> {
                player.die();
                inGame = false;
                ((javax.swing.Timer) e.getSource()).stop();
            }).start();
        }
    }

    private void drawShot(Graphics g) {
        for (Shot shot : shots) {
            if (shot.isVisible()) {
                g.drawImage(shot.getImage(), shot.getX(), shot.getY(), this);
            }
        }
    }


    private void drawBombing(Graphics g) {

        for (Enemy e : enemies) {
             Enemy.Bomb b = e.getBomb();
             if (!b.isDestroyed()) {
                 g.drawImage(b.getImage(), b.getX(), b.getY(), this);
             }
         }
    }

    private void drawExplosions(Graphics g) {

        List<Explosion> toRemove = new ArrayList<>();

        for (Explosion explosion : explosions) {

            if (explosion.isVisible()) {
                g.drawImage(explosion.getImage(), explosion.getX(), explosion.getY(), this);
                explosion.visibleCountDown();
                if (!explosion.isVisible()) {
                    toRemove.add(explosion);
                }
            }
        }

        explosions.removeAll(toRemove);
    }

    private void drawUI(Graphics g) {
        g.setColor(Color.green);
        g.setFont(g.getFont().deriveFont(20f));
        g.drawString("Score: " + ScoreManager.getInstance().getScore(), 30, 640);

        g.setColor(Color.green);
        g.setFont(g.getFont().deriveFont(20f));
        g.drawString("Power-Ups", 420, 640);
        
        int[] xSlots = {540, 581, 622};

        var uiIcon = new ImageIcon(IMG_UI);
        var uiImage = uiIcon.getImage();
        g.drawImage(uiImage, 540, 615, this);
        g.drawImage(uiImage, 581, 615, this);
        g.drawImage(uiImage, 622, 615, this);
        
        for (int i = 0; i < activePowerUps.size(); i++) {
            ActivePowerUp ap = activePowerUps.get(i);
            int x = xSlots[i];
            g.drawImage(ap.image, x, 615, this);
            //timer bar
            int barWidth = 30;
            int barHeight = 4;
            int fillWidth = (int)(barWidth * ap.getProgress());
            g.setColor(Color.GREEN);
            if (ap.getProgress() < 0.25f) g.setColor(Color.RED);
            g.fillRect(x, 615 + 32, fillWidth, barHeight);
        }
    }


    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
    }

    private void doDrawing(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);

        g.setColor(Color.white);
        g.drawString("FRAME: " + frame, 10, 10);

        g.setColor(Color.green);

        if (inGame) {

            drawMap(g);
            drawPowerUps(g);
            drawAliens(g);
            drawPlayer(g);
            drawExplosions(g);
            drawShot(g);
            drawBombing(g);
            drawUI(g);

            if(ShowMessage){
                Incoming(g);
            }

            if (isPaused) {
                drawPauseMenu(g);
            }
        } else {
            if (timer.isRunning()) {
                timer.stop();
            }
            backgroundPlayer.pause();
            gameOver(g);
            loseAudio();
            new javax.swing.Timer(3000, e -> {
                System.exit(0);
            }).start();

        }

        Toolkit.getDefaultToolkit().sync();
    }

    private void drawPauseMenu(Graphics g){
        g.setColor(new Color(0, 0, 0, 150));
        g.fillRect(0, 0, getWidth(), getHeight());

        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 48));
        String text = "PAUSED";
        FontMetrics fm = g.getFontMetrics();
        int x = (getWidth() - fm.stringWidth(text)) / 2;
        int y = getHeight() / 3;
        g.drawString(text, x, y);

        g.setFont(new Font("Arial", Font.PLAIN, 24));
        String resume = "Press ESC to Resume";
        int rx = (getWidth() - g.getFontMetrics().stringWidth(resume)) / 2;
        int ry = y + 50;
        g.drawString(resume, rx, ry);

        String quit = "Press Q to Quit";
        int qx = (getWidth() - g.getFontMetrics().stringWidth(quit)) / 2;
        int qy = ry + 30;
        g.drawString(quit, qx, qy);
    }

    private void gameOver(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);
        g.setColor(Color.white);
        g.drawRect(50, BOARD_WIDTH / 2 - 30, BOARD_WIDTH - 100, 50);

        var small = new Font("Helvetica", Font.BOLD, 14);
        var fontMetrics = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
                BOARD_WIDTH / 2 - 15);
        g.drawString("Your Score: " + ScoreManager.getInstance().getScore(), (BOARD_WIDTH - fontMetrics.stringWidth("Your Score: " + ScoreManager.getInstance().getScore())) / 2,
                BOARD_WIDTH / 2 + 15);
    }



    private void Incoming(Graphics g) {
    g.setColor(new Color(20, 20, 30));
    g.fillRoundRect(100, BOARD_HEIGHT / 2 - 20, BOARD_WIDTH - 200, 40, 15, 15);

    g.setColor(new Color(0, 200, 255));
    g.drawRoundRect(100, BOARD_HEIGHT / 2 - 20, BOARD_WIDTH - 200, 40, 15, 15);

    Font messageFont = new Font("Arial", Font.BOLD, 16);
    FontMetrics fm = this.getFontMetrics(messageFont);
    String msg = "Big wave approaching!";

    g.setFont(messageFont);
    g.setColor(new Color(180, 255, 255));
    g.drawString(msg, (BOARD_WIDTH - fm.stringWidth(msg)) / 2, BOARD_HEIGHT / 2 + 6);
}

    private void update() {
        ScoreManager.getInstance().update();
        // Check enemy spawn
        List<SpawnDetails> sds = spawnMap.get(frame);
        if (sds != null) {
            for (SpawnDetails sd : sds) {
                // Create a new enemy based on the spawn details
                switch (sd.type) {
                    case "Enemy" -> {
                        Enemy enemy = new Enemy(sd.x, sd.y, true);
                        enemies.add(enemy);
                    }
                    case "Alien1" -> {
                        Enemy enemy = new Alien1(sd.x, sd.y, false);
                        enemies.add(enemy);
                    }
                    case "Alien2" -> {
                        Enemy enemy = new Alien2(sd.x, sd.y, false);
                        enemies.add(enemy);
                    }
                    case "PowerUp-SpeedUp" -> {
                        PowerUp speedUp = new SpeedUp(sd.x, sd.y);
                        powerups.add(speedUp);

                    }
                    case "PowerUp-TripleShot" -> {
                        PowerUp TripleShot = new TripleShot(sd.x, sd.y);
                        powerups.add(TripleShot);

                    }
                    case "PowerUp-BIG" -> {
                        PowerUp BIGShot = new BIGShot(sd.x, sd.y);
                        powerups.add(BIGShot);

                    }
                    case "PowerUp-Burst" -> {
                        PowerUp BurstShot = new BurstShot(sd.x, sd.y);
                        powerups.add(BurstShot);

                    }
                    default -> System.out.println("Unknown enemy type: " + sd.type);
                }
            }

            // Add more cases for different enemy types if needed
            // Enemy enemy2 = new Alien2(sd.x, sd.y);
            // enemies.add(enemy2);
        }

        if (deaths == 5 && !spawnedAt5) {
            int powerUpX = randomizer.nextInt(100, 400);
            int spawnFrame = frame + 30;

            addSpawn(spawnFrame, new SpawnDetails("PowerUp-SpeedUp", powerUpX, 0));
            spawnedAt5 = true;
        }

        if (deaths == 15 && !spawnedAt15) {
            int powerUpX = randomizer.nextInt(100, 400);
            int spawnFrame = frame + 30;

            addSpawn(spawnFrame, new SpawnDetails("PowerUp-TripleShot", powerUpX, 0));
            spawnedAt15 = true;
        }

        if (deaths == 20 && !spawnedAt20) {
            int powerUpX = randomizer.nextInt(100, 400);
            int spawnFrame = frame + 30;

            addSpawn(spawnFrame, new SpawnDetails("PowerUp-SpeedUp", powerUpX, 0));
            spawnedAt20 = true;
        }


        if (deaths == 62) {
            inGame = false;
            timer.stop();
            ScoreManager.getInstance().addLevelCompletion();
            game.loadScene2();
        }

        //Message Showing
        if (deaths == 32 && !ShowMessage && !finalwaveSpawned) {
            ShowMessage = true;
            finalwaveSpawned = true;
            int waveX = randomizer.nextInt(100, 300);
            int baseSpawnFrame = frame + 90; // Start delay (~3 second if 30 FPS)
            addSpawn(frame + 10, new SpawnDetails("PowerUp-TripleShot", waveX, 0));

            new javax.swing.Timer(1500, e -> {
                ShowMessage = false;
                repaint();
            }).start();

            for (int j = 0; j < 6; j++) {
                int spawnAtFrame = baseSpawnFrame + j * 160; // Space 120 frames apart

                for (int i = 0; i < 5; i++) {
                    addSpawn(spawnAtFrame, new SpawnDetails("Enemy", waveX + (ALIEN_WIDTH + ALIEN_GAP) * i, 30));
                }
            }
        }


        //Player
        player.act(0);
        playerDX = player.getDx();

        //BG-Scrolling based on input
        bgOffsetX -= playerDX / 3;

        //Power-ups
        for (PowerUp powerup : powerups) {
            if (powerup.isVisible()) {
                powerup.act(0);
            }

            if (powerup.collidesWith(player)) {
                powerupAudio();
                powerup.upgrade(player);
                ScoreManager.getInstance().addPowerUp();

                Class<? extends PowerUp> type = powerup.getClass();
                int duration = getDurationForPowerUp(type);
                Image icon = getIconForPowerUp(type);

                boolean found = false;
                for (ActivePowerUp ap : activePowerUps) {
                    if (ap.type == type) {
                        ap.reset(duration);
                        found = true;
                        break;
                    }
                }

                if (!found) {
                    activePowerUps.add(new ActivePowerUp(icon, duration, type));
                }

                powerup.die();
            }
        }

        //Remove active PowerUps
        Iterator<ActivePowerUp> iterator = activePowerUps.iterator();
        while (iterator.hasNext()) {
            ActivePowerUp ap = iterator.next();
            if (ap.update()) {
                iterator.remove();
                downgradePlayer(ap.type);
            }
        }


        


        //Enemies
        for (Enemy enemy : enemies) {
            if (enemy.isVisible()) {
                enemy.act(direction);
            }
        }

        //Shot
        List<Shot> shotsToRemove = new ArrayList<>();
        for (Shot shot : shots) {

            if (shot.isVisible()) {
                shot.act(0);
                int shotX = shot.getX();
                int shotY = shot.getY();

                for (Enemy enemy : enemies) {
                    // Collision detection: shot and enemy
                    int enemyX = enemy.getX();
                    int enemyY = enemy.getY();
                    if (player.getShot() != 3) {
                        if (enemy.isVisible() && shot.isVisible()
                            && shotX >= (enemyX)
                            && shotX <= (enemyX + ALIEN_WIDTH)
                            && shotY >= (enemyY)
                            && shotY <= (enemyY + ALIEN_HEIGHT)) {

                        explAudio();
                        enemy.setDying(true);
                        ScoreManager.getInstance().addEnemyKill();
                        explosions.add(new Explosion(enemyX, enemyY));
                        deaths++;
                        shot.die();
                        shotsToRemove.add(shot);

                        
                    }
                    }else{
                        if (enemy.isVisible() && shot.isVisible()
                            && shotX >= (enemyX-5)
                            && shotX <= (enemyX + ALIEN_WIDTH+5)
                            && shotY >= (enemyY-5)
                            && shotY <= (enemyY + ALIEN_HEIGHT+5)) {

                        explAudio();
                        enemy.setDying(true);
                        ScoreManager.getInstance().addEnemyKill();
                        explosions.add(new Explosion(enemyX, enemyY));
                        deaths++;           
                    }
                    }
                }

                int y = shot.getY();

                if (y < 0) {
                    shot.die();
                    shotsToRemove.add(shot);
                }
            }
        }
        shots.removeAll(shotsToRemove);


        for (Enemy enemy : enemies) {
            int x = enemy.getX();

            if (x >= BOARD_WIDTH - BORDER_RIGHT && direction != -1) {
                direction = -1;
                for (Enemy e2 : enemies) {
                    if (e2.isMainWave()) {
                        e2.setY(e2.getY() + GO_DOWN);
                    }
                }
            }

            if (x <= BORDER_LEFT && direction != 1) {
                direction = 1;
                for (Enemy e : enemies) {
                    if (e.isMainWave()) {
                        e.setY(e.getY() + GO_DOWN);
                    }
                }
            }
        }

         for (Enemy enemy : enemies) {
             if (enemy.isVisible()) {
                 int y = enemy.getY();
                 if (y > GROUND - ALIEN_HEIGHT) {
                     inGame = false;
                     message = "Invasion!";
                 }
                 enemy.act(direction);
             }
         }
         //bombs - collision detection
         //Bomb is with enemy, so it loops over enemies
        
        for (Enemy enemy : enemies) {
            if (frame > 25){
                int chance = randomizer.nextInt(100);
                Enemy.Bomb bomb = enemy.getBomb();

                if (chance == CHANCE && enemy.isVisible() && bomb.isDestroyed()) {

                    bomb.setDestroyed(false);
                    bomb.setX(enemy.getX());
                    bomb.setY(enemy.getY());
                    bombAudio();
                }

                int bombX = bomb.getX();
                int bombY = bomb.getY();
                int playerX = player.getX();
                int playerY = player.getY();

                if (player.isVisible() && !bomb.isDestroyed()
                        && bombX >= (playerX)
                        && bombX <= (playerX + PLAYER_WIDTH)
                        && bombY >= (playerY)
                        && bombY <= (playerY + PLAYER_HEIGHT)) {
                    
                    explAudio();
                    player.setDying(mortal);
                    bomb.setDestroyed(true);
                }

                if (!bomb.isDestroyed()) {
                    bomb.setY(bomb.getY() + 3);
                    if (bomb.getY() >= GROUND - BOMB_HEIGHT) {
                        bomb.setDestroyed(true);
                    }
                }
            }
        }
    }

    private int getDurationForPowerUp(Class<? extends PowerUp> type) {
        if (type == SpeedUp.class) return 450;
        if (type == TripleShot.class) return 200;
        if (type == BIGShot.class) return 150;
        if (type == BurstShot.class) return 250;
        return 200; 
    }

    private Image getIconForPowerUp(Class<? extends PowerUp> type) {
        if (type == SpeedUp.class) return new ImageIcon(IMG_POWERUP_SPEEDUP).getImage();
        if (type == TripleShot.class) return new ImageIcon(IMG_POWERUP_TRIPLE).getImage();
        if (type == BIGShot.class) return new ImageIcon(IMG_POWERUP_BIG).getImage();
        if (type == BurstShot.class) return new ImageIcon(IMG_POWERUP_BURST).getImage();
        return null; 
    }

    private void downgradePlayer(Class<?> type) {
        if (type == SpeedUp.class) new SpeedUp().downgrade(player);
        else if (type == TripleShot.class) new TripleShot().downgrade(player);
        else if (type == BIGShot.class) new BIGShot().downgrade(player);
        else if (type == BurstShot.class) new BurstShot().downgrade(player);
    }

    private void doGameCycle() {
        if(isPaused){

        }else{
            frame++;
            update();
            repaint();
        }
        
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {
            player.keyReleased(e);
        }

        @Override
        public void keyPressed(KeyEvent e) {
            //System.out.println("Scene1.keyPressed: " + e.getKeyCode());

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE && inGame && !isPaused) {
                long now = System.currentTimeMillis();
                if (shots.size() < 9) {
                    int shotMode = player.getShot();
                    switch (shotMode) {
                        case 1 -> {
                            if(now - lastShotTime > SHOT_COOLDOWN_MS){
                                shots.add(new Shot(x, y, 3));
                                shotAudio();
                                lastShotTime = now;
                            }
                        }
                        case 2 -> {
                            if(now - lastShotTime > SHOT_COOLDOWN_MS + 100){
                                double angle = Math.toRadians(10);
                                shots.add(new Shot(x, y, 3));
                                shots.add(new Shot(x, y, -angle, 3));
                                shots.add(new Shot(x, y, angle, 3));
                                shotAudio();
                                lastShotTime = now;
                            }
                        }
                        case 3 -> {
                            if(now - lastShotTime > SHOT_COOLDOWN_MS + 300){
                                shots.add(new Shot(x, y, 20));
                                shotAudio();
                                lastShotTime = now;
                            }
                        }
                        case 4 -> {
                            if (now - lastShotTime > SHOT_COOLDOWN_MS + 200) {
                                final int[] burstCount = {0}; // shot counter

                                Timer[] burstTimer = new Timer[1];
                                burstTimer[0] = new Timer(50, null);

                                burstTimer[0].addActionListener(v -> {
                                    shots.add(new Shot(x, y, 3));
                                    shotAudio();
                                    burstCount[0]++;

                                    if (burstCount[0] >= 4) {
                                        burstTimer[0].stop();
                                    }
                                });

                                burstTimer[0].start();
                                lastShotTime = now;
                            }
                        }

                    }
                }
            }
            if (key == KeyEvent.VK_ESCAPE && inGame) {
                    isPaused = !isPaused;  
                    repaint();
                }
            if (isPaused) {
                if (key == KeyEvent.VK_Q) {
                    System.exit(0);
                }
            }
        }
    }
}