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

public class Scene3 extends JPanel {

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


    private int pwr_time1 = 450;
    private int pwr_time2 = 200;
    private int pwr_time3 = 150;
    private int pwr_time4 = 300;
    private int pwr_end1;
    private int pwr_end2;
    private int pwr_end3;
    private int pwr_end4;

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

    public Scene3(Game game) {
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
    
    addSpawn(25, new SpawnDetails("PowerUp-TripleShot", randomizer.nextInt(100, 300), 0));
    addSpawn(50, new SpawnDetails("Alien2", randomizer.nextInt(100, 300), 0));
    addSpawn(150, new SpawnDetails("Alien2", randomizer.nextInt(200, 400), 0));
    addSpawn(200, new SpawnDetails("Alien2", randomizer.nextInt(150, 350), 0));
    addSpawn(300, new SpawnDetails("Alien2", randomizer.nextInt(100, 300), 0));
    addSpawn(400, new SpawnDetails("Alien2", randomizer.nextInt(200, 400), 0));
    addSpawn(500, new SpawnDetails("Alien2", randomizer.nextInt(150, 350), 0));

    int randspawn1 = randomizer.nextInt(200, 600);
    addSpawn(250, new SpawnDetails("Alien1", randspawn1, 0));
    addSpawn(250, new SpawnDetails("Alien1", randspawn1 + 50, 0));

    int randspawn2 = randomizer.nextInt(0, 200);
    addSpawn(400, new SpawnDetails("Alien1", randspawn2, 0));
    addSpawn(400, new SpawnDetails("Alien1", randspawn2 + 50, 0));
    addSpawn(400, new SpawnDetails("Alien1", randspawn2 + 100, 0));
    addSpawn(400, new SpawnDetails("Alien1", randspawn2 + 150, 0));

    int randspawn3 = randomizer.nextInt(100, 300);
    for (int i = 0; i < 6; i++) {
        addSpawn(450, new SpawnDetails("Alien1", randspawn3 + i * 40, 0));
    }

    int randspawn4 = randomizer.nextInt(50, 250);
    addSpawn(650, new SpawnDetails("Alien1", randspawn4, 0));
    addSpawn(650, new SpawnDetails("Alien1", randspawn4 + 60, 0));
    addSpawn(650, new SpawnDetails("Alien1", randspawn4 + 120, 0));

    int randspawn5 = randomizer.nextInt(0, 300);
    addSpawn(900, new SpawnDetails("PowerUp-BIG", randomizer.nextInt(0, 300), 0));
    for (int i = 0; i < 2; i++) {
        addSpawn(1000, new SpawnDetails("Alien1", randspawn5 + i * 40, 0));
    }
    for (int i = 0; i < 2; i++) {
        addSpawn(1020, new SpawnDetails("Alien1", randspawn5 + i * 40, 0));
    }
    for (int i = 0; i < 2; i++) {
        addSpawn(1040, new SpawnDetails("Alien1", randspawn5 + i * 40, 0));
    }
    for (int i = 0; i < 2; i++) {
        addSpawn(1060, new SpawnDetails("Alien1", randspawn5 + i * 40, 0));
    }
    for (int i = 0; i < 2; i++) {
        addSpawn(1080, new SpawnDetails("Alien1", randspawn5 + i * 40, 0));
    }

    int randspawn6 = randomizer.nextInt(300, 600);
    addSpawn(1200, new SpawnDetails("PowerUp-BIG", randomizer.nextInt(300, 600), 0));
    for (int i = 0; i < 2; i++) {
        addSpawn(1300, new SpawnDetails("Alien1", randspawn6 + i * 40, 0));
    }
    for (int i = 0; i < 2; i++) {
        addSpawn(1320, new SpawnDetails("Alien1", randspawn6 + i * 40, 0));
    }
    for (int i = 0; i < 2; i++) {
        addSpawn(1340, new SpawnDetails("Alien1", randspawn6 + i * 40, 0));
    }
    for (int i = 0; i < 2; i++) {
        addSpawn(1360, new SpawnDetails("Alien1", randspawn6 + i * 40, 0));
    }
    for (int i = 0; i < 2; i++) {
        addSpawn(1380, new SpawnDetails("Alien1", randspawn6 + i * 40, 0));
    }
    
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
            }
        }
        player = new Player();
        shot = new Shot();
    }

    private void drawMap(Graphics g) {
    // Calculate smooth scrolling offset (1 pixel per frame)
    int scrollOffset = (frame) % BLOCKHEIGHT;

    // Calculate which rows to draw based on screen position
    int baseRow = (frame) / BLOCKHEIGHT;
    int rowsNeeded = (BOARD_HEIGHT / BLOCKHEIGHT) + 2; // +2 for smooth scrolling

    // Loop through rows that should be visible on screen
    for (int screenRow = 0; screenRow < rowsNeeded; screenRow++) {
        // Calculate which MAP row to use (with wrapping)
        int mapRow = (baseRow + screenRow) % MAP.length;

        // Calculate Y position for this row
        int y = (screenRow * BLOCKHEIGHT) - scrollOffset;

        // Skip if row is completely off-screen
        if (y > BOARD_HEIGHT || y < -BLOCKHEIGHT) {
            continue;
        }

        // Draw each column in this row
        for (int col = 0; col < MAP[mapRow].length; col++) {
            if (MAP[mapRow][col] == 1) {
                // Calculate X position
                int x = col * BLOCKWIDTH;

                // Draw a cluster of stars
                drawStarCluster(g, x, y, BLOCKWIDTH, BLOCKHEIGHT);
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
            g.drawImage(player.getImage(), player.getX(), player.getY(), this);
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

    private void update() {
        ScoreManager.getInstance().update();
        // Check enemy spawn
        List<SpawnDetails> sds = spawnMap.get(frame);
        if (sds != null) {
            for (SpawnDetails sd : sds) {
                // Create a new enemy based on the spawn details
                switch (sd.type) {
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
                    default -> System.out.println("Unknown enemy type: " + sd.type);
                }
            }

            // Add more cases for different enemy types if needed
            // Enemy enemy2 = new Alien2(sd.x, sd.y);
            // enemies.add(enemy2);
        }

        if (deaths == 2 && !spawnedAt5) {
            int powerUpX = randomizer.nextInt(100, 400);
            int spawnFrame = frame + 30;

            addSpawn(spawnFrame, new SpawnDetails("PowerUp-SpeedUp", powerUpX, 0));
            spawnedAt5 = true;
        }

        if (deaths == 5 && !spawnedAt15) {
            int powerUpX = randomizer.nextInt(100, 400);
            int spawnFrame = frame + 30;

            addSpawn(spawnFrame, new SpawnDetails("PowerUp-TripleShot", powerUpX, 0));
            spawnedAt15 = true;
        }


        if (deaths == 42) {
            inGame = false;
            timer.stop();
            ScoreManager.getInstance().addLevelCompletion();
            game.loadScene2();
        }

        //Player
        player.act(0);

        //Power-ups
        for (PowerUp powerup : powerups) {
            if(powerup.isVisible()){
                    powerup.act(0);
                }
            if (powerup.collidesWith(player)){
                powerupAudio();
                powerup.upgrade(player);
                ScoreManager.getInstance().addPowerUp();
                if (powerup instanceof SpeedUp) {
                        Class<? extends PowerUp> type = SpeedUp.class;
                        if (!powerUpEndFrames.containsKey(type) || frame >= powerUpEndFrames.get(type)) {
                            powerUpEndFrames.put(type, frame + pwr_time1);
                            pwr_end1 = frame + pwr_time1;

                            if (!hasPowerUpIcon(type)) {
                                Image powerUpIcon = new ImageIcon(IMG_POWERUP_SPEEDUP).getImage();
                                activePowerUps.add(new ActivePowerUp(powerUpIcon, pwr_time1, type));
                            }
                        }
                    } else if (powerup instanceof TripleShot) {
                        Class<? extends PowerUp> type = TripleShot.class;
                        if (!powerUpEndFrames.containsKey(type) || frame >= powerUpEndFrames.get(type)) {
                            powerUpEndFrames.put(type, frame + pwr_time2);
                            pwr_end2 = frame + pwr_time2;

                            if (!hasPowerUpIcon(type)) {
                                Image powerUpIcon = new ImageIcon(IMG_POWERUP_TRIPLE).getImage();
                                activePowerUps.add(new ActivePowerUp(powerUpIcon, pwr_time2, type));
                            }
                        }
                    } else if (powerup instanceof BIGShot) {
                        Class<? extends PowerUp> type = BIGShot.class;
                        if (!powerUpEndFrames.containsKey(type) || frame >= powerUpEndFrames.get(type)) {
                            powerUpEndFrames.put(type, frame + pwr_time3);
                            pwr_end3 = frame + pwr_time3;

                            if (!hasPowerUpIcon(type)) {
                                Image powerUpIcon = new ImageIcon(IMG_POWERUP_BIG).getImage();
                                activePowerUps.add(new ActivePowerUp(powerUpIcon, pwr_time3, type));
                            }
                        }
                    }else if (powerup instanceof BurstShot) {
                        Class<? extends PowerUp> type = BurstShot.class;
                        if (!powerUpEndFrames.containsKey(type) || frame >= powerUpEndFrames.get(type)) {
                            powerUpEndFrames.put(type, frame + pwr_time4);
                            pwr_end3 = frame + pwr_time4;

                            if (!hasPowerUpIcon(type)) {
                                Image powerUpIcon = new ImageIcon(IMG_POWERUP_BURST).getImage();
                                activePowerUps.add(new ActivePowerUp(powerUpIcon, pwr_time4, type));
                            }
                        }
                    }
                    powerup.die();
            }
        }

        //Remove active PowerUps
        Iterator<ActivePowerUp> iterator = activePowerUps.iterator();
        while (iterator.hasNext()) {
            ActivePowerUp ap = iterator.next();
            ap.tick();
            if (ap.isExpired()) {
                iterator.remove();

                //downgrade
                if (ap.type == SpeedUp.class) {
                    new SpeedUp().downgrade(player);
                } else if (ap.type == TripleShot.class) {
                    new TripleShot().downgrade(player);
                } else if (ap.type == BIGShot.class) {
                    new BIGShot().downgrade(player);
                } else if (ap.type == BurstShot.class) {
                    new BurstShot().downgrade(player);
                }
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
                        if (player.getShot() != 3){
                            shot.die();
                            shotsToRemove.add(shot);
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
            if (frame > 50){
                int chance = randomizer.nextInt(15);
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

    private boolean hasPowerUpIcon(Class<?> type) {
        for (ActivePowerUp ap : activePowerUps) {
            if (ap.type == type) return true;
        }
        return false;
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
                            if(now - lastShotTime > SHOT_COOLDOWN_MS + 200){
                                shots.add(new Shot(x, y, 15));
                                shotAudio();
                                lastShotTime = now;
                            }
                        }
                        case 4 -> {
                            if (now - lastShotTime > SHOT_COOLDOWN_MS + 200) {
                                final int[] burstCount = {0}; // shot counter

                                Timer[] burstTimer = new Timer[1]; // array to hold Timer reference
                                burstTimer[0] = new Timer(50, null);

                                burstTimer[0].addActionListener(v -> {
                                    shots.add(new Shot(x, y, 3));
                                    shotAudio();
                                    burstCount[0]++;

                                    if (burstCount[0] >= 4) {
                                        burstTimer[0].stop(); // stop safely
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
