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
import gdd.powerup.Shield;
import gdd.powerup.SpeedUp;
import gdd.powerup.TripleShot;
import gdd.sprite.Alien1;
import gdd.sprite.Alien2;
import gdd.sprite.Boss;
import gdd.sprite.Boss.Projectile;
import gdd.sprite.Enemy;
import gdd.sprite.Explosion;
import gdd.sprite.Hit;
import gdd.sprite.Player;
import gdd.sprite.Shot;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class Scene3 extends JPanel {

    private int frame = 0;
    private List<PowerUp> powerups;
    private List<Enemy> enemies;
    private List<Explosion> explosions;
    private List<Shot> shots;
    private List<Hit> hits;
    private Player player;
    private Set<Enemy> hitEnemiesThisFrame = new HashSet<>();
    private final List<ActivePowerUp> activePowerUps = new ArrayList<>();
    private static final int SHOT_COOLDOWN_MS = 300;
    private long lastShotTime = 0;
    private boolean isPaused = false;
    private boolean ShowMessage = false;
    private int frameCursor = 0;
    private int playerDX = 0;
    private int bgOffsetX = 0; // background horizontal offset
    private final String REG = "REG";
    private final String RIGHT = "RIGHT";
    private final String LEFT = "LEFT";
    private final String BURST = "BURST";
    private final String BIG = "BIG";
    private Boss boss;
    private boolean canHit = false;
    private boolean lose = true;



    final int BLOCKHEIGHT = 50;
    final int BLOCKWIDTH = 50;


    private int direction = -1;
    private int deaths = 0;

    private boolean inGame = true;
    private String message = "";

    private final Dimension d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    private final Random randomizer = new Random();
    private boolean wave1 = false;
    private boolean wave2 = false;
    private boolean wave3 = false;
    private boolean wave4 = false;
    private boolean wave5 = false;
    private boolean wave6 = false;
    private boolean wave7 = false;
    private boolean wave8 = false;
    private boolean wave9 = false;
    private boolean wave10 = false;
    private boolean waveFinal = false;
    private boolean BossWave = false;



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
    }

    private void initAudio() {
        try {
            backgroundPlayer = new AudioPlayer(SND_SCENE2, true);
            backgroundPlayer.play();
        } catch (Exception e) {
            System.err.println("Error initializing audio player: " + e.getMessage());
        }
    }

    private void initBossAudio() {
        try {
            backgroundPlayer = new AudioPlayer(SND_BOSS, true);
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

    private void winAudio() {
        try {
            backgroundPlayer = new AudioPlayer(SND_FIN, true);
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

    private void hit1Audio() {
        try {
            sfxPlayer = new AudioPlayer(SND_HIT, false);
            sfxPlayer.play();
        } catch (Exception e) {
            System.err.println("Error initializing audio player: " + e.getMessage());
        }
    }

    private void hit2Audio() {
        try {
            sfxPlayer = new AudioPlayer(SND_HIT2, false);
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
        hits = new ArrayList<>();

        player = new Player();
        Shot shot = new Shot();
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
                
                g.drawImage(enemy.getCurrentImage(), enemy.getX(), enemy.getY(), this);
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
            new javax.swing.Timer(600, e -> {
                player.die();
                inGame = false;
                ((javax.swing.Timer) e.getSource()).stop();
            }).start();
        }
    }

    private void drawShot(Graphics g) {
        for (Shot shot : shots) {
            if (shot.isVisible()) {
                g.drawImage(shot.getCurrentImage(), shot.getX(), shot.getY(), this);
            }
        }
    }


    private void drawBombing(Graphics g) {

        for (Enemy e : enemies) {
             Enemy.Bomb b = e.getBomb();
             if (!b.isDestroyed()) {
                 g.drawImage(b.getCurrentImage(), b.getX(), b.getY(), this);
             }
         }
    }

    private void drawExplosions(Graphics g) {

        List<Explosion> toRemove = new ArrayList<>();

        for (Explosion explosion : explosions) {

            if (explosion.isVisible()) {
                explosion.updateAnimation();
                g.drawImage(explosion.getCurrentImage(), explosion.getX(), explosion.getY(), this);
                explosion.visibleCountDown();
                if (!explosion.isVisible()) {
                    toRemove.add(explosion);
                }
            }
        }

        explosions.removeAll(toRemove);
    }

    private void drawHits(Graphics g) {

        List<Hit> toRemove = new ArrayList<>();

        for (Hit hit : hits) {

            hit.act(0);
            if (hit.isAlive()) {
                g.drawImage(hit.getCurrentImage(), hit.getX(), hit.getY(), this);
            } else {
                toRemove.add(hit);
            }
        }

        hits.removeAll(toRemove);
    }

    private void drawUI(Graphics g) {
        g.setColor(Color.DARK_GRAY);
        g.fillRect(0, GROUND, getWidth(), getHeight() - GROUND);

        g.setColor(Color.green);
        g.setFont(g.getFont().deriveFont(20f));
        g.drawString("Score: " + ScoreManager.getInstance().getScore(), 30, 640);

        g.setColor(Color.blue);
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
            // Timer bar
            int barWidth = 30;
            int barHeight = 4;
            int fillWidth = (int)(barWidth * ap.getProgress());
            g.setColor(Color.GREEN);
            if (ap.getProgress() < 0.25f) g.setColor(Color.RED);
            g.fillRect(x, 615 + 32, fillWidth, barHeight);
        }
    }

    private void drawBoss(Graphics g) {
        if (boss != null && boss.isVisible()) {
            g.drawImage(boss.getCurrentImage(), boss.getX(), boss.getY(), this);
        }

        if (boss != null && boss.isDying()) {
            boss.die();
        }
    }

    private void drawBossAttacks(Graphics g) {
        if (boss != null) {
            List<Projectile> bossProjectiles = boss.getFire();
            if (bossProjectiles != null) {
                for (Projectile p : bossProjectiles) {
                    if (p.isVisible()) {
                        g.drawImage(p.getCurrentImage(), p.getX(), p.getY(), this);
                    }
                }
            }
        }
    }

    private void checkBossHit() {
        if (boss == null || !boss.isVisible()) return;

        for (Projectile proj : boss.getFire()) {

            int projX = proj.getX();
            int projY = proj.getY();
            int playerX = player.getX();
            int playerY = player.getY();

            //Collision check
            if (player.isVisible() && proj.isVisible()
                    && projX >= playerX
                    && projX <= (playerX + PLAYER_WIDTH)
                    && projY >= playerY
                    && projY <= (playerY + PLAYER_HEIGHT)) {

                proj.die();
                explAudio();
                player.setDying(mortal);
            }

            //Movement and offscreen check
            if (proj.isVisible()) {
                proj.setY(proj.getY() + 3);
                if (proj.getY() >= GROUND - proj.getHeight()) {
                    proj.die();
                }
            }
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

        g.setColor(Color.green);

        if (inGame) {

            drawMap(g);
            drawPowerUps(g);
            drawAliens(g);
            drawPlayer(g);
            drawBossAttacks(g);
            drawBoss(g);
            drawShot(g);
            drawExplosions(g);
            drawHits(g);
            drawBombing(g);
            drawUI(g);

            if(ShowMessage){
                Incoming(g, message);
            }

            if (isPaused) {
                drawPauseMenu(g);
            }
        } else {
            backgroundPlayer.pause();
            if (timer.isRunning()) {
                timer.stop();
            }
            if(lose){
                gameOver(g);
                loseAudio();
                new javax.swing.Timer(6000, e -> {
                    System.exit(0);
                }).start();
            }else {
                gameBeaten(g);
                winAudio();
            }

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
        String resume = "Press SPACE to Resume";
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
        message = "Game Over";
        g.drawString(message, (BOARD_WIDTH - fontMetrics.stringWidth(message)) / 2,
                BOARD_WIDTH / 2 - 15);
        g.drawString("Your Score: " + ScoreManager.getInstance().getScore(), (BOARD_WIDTH - fontMetrics.stringWidth("Your Score: " + ScoreManager.getInstance().getScore())) / 2,
                BOARD_WIDTH / 2 + 15);
    }

    private void gameBeaten(Graphics g) {
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

        g.setFont(g.getFont().deriveFont(32f));
        String text = "Press SPACE to Exit";
        int stringWidth = g.getFontMetrics().stringWidth(text);
        int x = (d.width - stringWidth) / 2;
        g.drawString(text, x, 480);
                
    }



    private void Incoming(Graphics g, String msg) {
        g.setColor(new Color(20, 20, 30));
        g.fillRoundRect(100, BOARD_HEIGHT / 2 - 20, BOARD_WIDTH - 200, 40, 15, 15);

        g.setColor(new Color(0, 200, 255));
        g.drawRoundRect(100, BOARD_HEIGHT / 2 - 20, BOARD_WIDTH - 200, 40, 15, 15);

        Font messageFont = new Font("Arial", Font.BOLD, 16);
        FontMetrics fm = this.getFontMetrics(messageFont);

        g.setFont(messageFont);
        g.setColor(new Color(180, 255, 255));
        g.drawString(msg, (BOARD_WIDTH - fm.stringWidth(msg)) / 2, BOARD_HEIGHT / 2 + 6);
    }

    private void update() {
        ScoreManager.getInstance().update();
        //Check enemy spawn
        List<SpawnDetails> sds = spawnMap.get(frame);
        if (sds != null) {
            for (SpawnDetails sd : sds) {
                //Create a new enemy based on the spawn details
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
                        Enemy enemy = new Alien2(sd.x, sd.y, false, sd.getDx());
                        enemies.add(enemy);
                    }
                    case "PowerUp-Speed" -> {
                        PowerUp speedUp = new SpeedUp(sd.x, sd.y);
                        powerups.add(speedUp);

                    }
                    case "PowerUp-Triple" -> {
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

        //Boss
        if (boss != null && boss.isVisible()) {
            boss.act(0); 
            checkBossHit();
            boss.checkAndUpdateStage();
        }
        

        //Enemies
        for (Enemy enemy : enemies) {
            if (enemy.isVisible()) {
                enemy.act(direction);
            }
        }

        //Enemy spawns

        if (deaths >= 0 && !wave1) {
            frameCursor = frame;
            wave1 = true;

            int baseX = 100;
            int delay = 10;

            for (int i = 1; i < 7; i++) {
                SpawnDetails sd = new SpawnDetails("Alien2", baseX + i * 40, 0, -1);
                addSpawn(frameCursor + i * delay, sd);
            }

            for (int i = 1; i <= 7; i++) {
                SpawnDetails sd = new SpawnDetails("Enemy", 300 + i * 50, 60, 0);
                addSpawn(frameCursor + i * delay, sd);
            }
            frameCursor += 120;
            addSpawn(frameCursor, new SpawnDetails("PowerUp-Speed", 320, 0, 0));
        }

        if (deaths >= 13 && !wave2) {
            frameCursor = frame;
            wave2 = true;

            int baseXLeft = 50;
            int baseXRight = 500;
            int delay = 8;

            for (int i = 1; i <= 6; i++) {
                SpawnDetails leftAlien = new SpawnDetails("Alien2", baseXLeft + i * 40, 0, -1);
                addSpawn(frameCursor + i * delay, leftAlien);

                SpawnDetails rightAlien = new SpawnDetails("Alien2", baseXRight - i * 40, 30, 1);
                addSpawn(frameCursor + i * delay, rightAlien);
            }

            frameCursor += 140;
            addSpawn(frameCursor + 10, new SpawnDetails("PowerUp-Burst", 320, 0, 0));
        }

        if (deaths >= 25 && !wave3) {
            frameCursor = frame;
            wave3 = true;

            int delay = 5;
            int baseX = 200;
            int baseY = 0;

            for (int row = 0; row < 3; row++) {
                int actualCol = 0;
                for (int col = 0; col < 12; col++) {
                    if ((row + col) % 2 == 0) {
                        SpawnDetails sd = new SpawnDetails("Enemy", baseX + actualCol * 45, baseY + row * 40, 0);
                        addSpawn(frameCursor + (row + col + 1) * delay, sd);
                        actualCol++;
                    }
                }
            }  


            frameCursor += 160;
            
        }

        if (deaths >= 43 && !wave4) {
            frameCursor = frame;
            wave4 = true;

            int delay = 6;
            int baseX = 120;

            for (int i = 1; i <= 10; i++) {
                SpawnDetails sd = new SpawnDetails("Alien1", baseX + i * 35, 10,0);
                addSpawn(frameCursor + i * delay, sd);
            }

            frameCursor += 120;
            addSpawn(frameCursor - 30, new SpawnDetails("PowerUp-Triple", 320, 0, 0));
        }

        if (deaths >= 53 && !wave5) {
            frameCursor = frame;
            wave5 = true;

            int delay = 7;

            for (int i = 1; i <= 8; i++) {
                SpawnDetails left = new SpawnDetails("Alien2", 50 + i * 30, i * 15, 1);
                addSpawn(frameCursor + i * delay, left);

                SpawnDetails right = new SpawnDetails("Alien2", 600 - i * 30, i * 15, -1);
                addSpawn(frameCursor + i * delay, right);
            }

            addSpawn(frameCursor + 50, new SpawnDetails("PowerUp-Speed", 320, 0, 0));

            frameCursor += 160;
        }

        if (deaths >= 69 && !wave6) { //nice
            frameCursor = frame;
            wave6 = true;

            int delay = 8;
            int baseX = 100;
            int cols = 12;  

            for (int row = 0; row < 5; row++) {
                int actualCol = 0;
                for (int col = 0; col < cols; col++) {
                    if ((row + col) % 2 == 0) {
                        SpawnDetails sd = new SpawnDetails("Alien1", baseX + actualCol * 45, row * 40, 0);
                        int delayIndex = row * cols + col + 1; 
                        addSpawn(frameCursor + delayIndex * delay, sd);
                        actualCol++;
                    }
                }
            }

            frameCursor += 200;
        }


        if (deaths >= 99 && !wave7) {
            frameCursor = frame;
            wave7 = true;

            int delay = 7;

            for (int i = 1; i <= 5; i++) {
                SpawnDetails a1 = new SpawnDetails("Alien1", 80 + i * 40, 20, 0);
                addSpawn(frameCursor + i * delay, a1);

                SpawnDetails a2 = new SpawnDetails("Alien2", 80 + i * 40, 60, 1);
                addSpawn(frameCursor + i * delay, a2);
            }

            frameCursor += 140;
            addSpawn(frameCursor, new SpawnDetails("PowerUp-BIG", 310, 90, 0));
        }

        if (deaths >= 109 && !wave8) {
            frameCursor = frame;
            wave8 = true;

            int delay = 3;
            int steps = 10;
            int startXLeft = 100;
            int startXRight = 650;
            int stepY = 30;

            for (int i = 1; i <= steps; i++) {
                SpawnDetails leftDiag = new SpawnDetails("Enemy", startXLeft + i * (ALIEN_WIDTH + 10), i * stepY, 0);
                addSpawn(frameCursor + i * delay, leftDiag);

                SpawnDetails rightDiag = new SpawnDetails("Enemy", startXRight - i * (ALIEN_WIDTH + 20), i * stepY, 0);
                addSpawn(frameCursor + i * delay, rightDiag);
            }

            frameCursor += 220;
            

        }

        if (deaths >= 129 && !wave9) {
            frameCursor = frame;
            wave9 = true;

            int delay = 5;

            //cool spiral
            int centerX = 300;
            int centerY = 50;
            int radius = 70;
            for (int i = 1; i <= 12; i++) {
                double angle = (2 * Math.PI / 12) * i;
                int x = centerX + (int)(radius * Math.cos(angle));
                int y = centerY + (int)(radius * Math.sin(angle));
                SpawnDetails sd = new SpawnDetails("Alien2", x, y,-1);
                addSpawn(frameCursor + i * delay, sd);
            }

            frameCursor += 30;
        }

        if (deaths >= 141 && !wave10) {
            frameCursor = frame;
            wave10 = true;

            addSpawn(frameCursor+10, new SpawnDetails("PowerUp-Triple", 320, 90, 0));

            int delay = 6;
            int baseXLeft = 50;
            int baseXRight = 600;
            int cols = 12;  

            for (int row = 0; row < 4; row++) {
                int actualColLeft = 0;
                int actualColRight = 0;
                for (int col = 0; col < cols; col++) {
                    if ((row + col) % 2 == 0) {
                        SpawnDetails leftAlien = new SpawnDetails("Alien2", baseXLeft + actualColLeft * 45, row * 45, 1);
                        SpawnDetails rightAlien = new SpawnDetails("Alien2", baseXRight - actualColRight * 45, row * 45, -1);
                        int delayIndex = row * cols + col + 1;

                        addSpawn(frameCursor + delayIndex * delay, leftAlien);
                        addSpawn(frameCursor + delayIndex * delay, rightAlien);

                        actualColLeft++;
                        actualColRight++;
                    }
                }
            }
            addSpawn(frameCursor + 90, new SpawnDetails("PowerUp-Triple", 320, 90, 0));

            frameCursor += 240;
        }


        if (deaths >= 189 && !waveFinal) {
            frameCursor = frame;
            waveFinal = true;

            int delay = 3;
            int startX = 40;

            for (int i = 1; i <= 40; i++) {
                SpawnDetails sd = new SpawnDetails("Enemy", startX + (i % 10) * 35, (i / 10) * 45, 0);
                addSpawn(frameCursor + i * delay, sd);
            }

            addSpawn(frameCursor + 30, new SpawnDetails("PowerUp-BIG", 320, 0, 0));

        }

        if(deaths >= 229 && !BossWave){
            frameCursor = frame;
            BossWave = true;
            ShowMessage = true;
            message = "Boss Approaching!";
            final int blinkDuration = 2000;
            final int blinkInterval = 500; 
            Timer blinkTimer = new Timer(blinkInterval, null);

            long startTime = System.currentTimeMillis();
            blinkTimer.addActionListener(e -> {
                ShowMessage = !ShowMessage;
                repaint();

                if (System.currentTimeMillis() - startTime >= blinkDuration) {
                    ((Timer) e.getSource()).stop();
                    ShowMessage = false;
                    repaint();
                }
            });
            blinkTimer.start();

            try {
                    if (backgroundPlayer != null) {
                        backgroundPlayer.stop();
                    }
                    if (sfxPlayer != null) {
                        sfxPlayer.stop();
                    }
                } catch (Exception v) {
                    System.err.println("Error closing audio player.");
                }
                initBossAudio();

            Timer bossTimer = new Timer(1000, null);
            bossTimer.addActionListener(e -> {
                System.out.print("Boss spawned\n");
                boss = new Boss(BOARD_WIDTH / 2 - 80, -64, 30);
            });
            bossTimer.setRepeats(false);
            bossTimer.start();

            Timer canHitTimer = new Timer(3000, null);
            canHitTimer.addActionListener(e -> {
                canHit = true;
            });
            canHitTimer.setRepeats(false);
            canHitTimer.start();
        }

        //End game
        if(deaths >= 259){
            lose = false;
            message = "Congratulations, you save Earth!";
            inGame = false;
            timer.stop();
            ScoreManager.getInstance().addLevelCompletion();
            //game.loadScene4();
        }

        //Shot
        List<Shot> shotsToRemove = new ArrayList<>();
        for (Shot shot : shots) {

            if (shot.isVisible()) {
                shot.act(0);
                int shotX = shot.getX();
                int shotY = shot.getY();

                //Enemy collision with shot
                for (Enemy enemy : enemies) {

                    if (hitEnemiesThisFrame.contains(enemy)) continue;

                    Rectangle shotRect = new Rectangle(shot.getX(), shot.getY(), shot.getWidth(), shot.getHeight());
                    Rectangle enemyRect = new Rectangle(enemy.getX(), enemy.getY(), ALIEN_WIDTH, ALIEN_HEIGHT);

                    if (enemy.isVisible() && shot.isVisible() && shotRect.intersects(enemyRect) && player.getShot() != 3) {
                        explAudio();
                        enemy.setDying(true);
                        ScoreManager.getInstance().addEnemyKill();
                        explosions.add(new Explosion(enemy.getX(), enemy.getY(), 1));
                        deaths++;
                        shot.die();
                        shotsToRemove.add(shot);
                        hitEnemiesThisFrame.add(enemy);
                        break;
                    }else if (enemy.isVisible() && shot.isVisible() && shotRect.intersects(enemyRect)){
                        explAudio();
                        enemy.setDying(true);
                        ScoreManager.getInstance().addEnemyKill();
                        explosions.add(new Explosion(enemy.getX(), enemy.getY(),1));
                        deaths++;
                        hitEnemiesThisFrame.add(enemy);
                        break;
                    }

                }

                //Boss collision with shot
                
                if (boss != null && boss.isVisible() && shot.isVisible()) {
                    Rectangle shotRect = new Rectangle(shotX, shotY, shot.getWidth(), shot.getHeight());
                    Rectangle bossRectMiss1 = new Rectangle(boss.getX(), boss.getY(), 52, 116);
                    Rectangle bossRectMiss2 = new Rectangle(boss.getX()+76, boss.getY(), 52, 116);
                    
                    //stage1
                    if(boss.getHp() > boss.stage2Hp+1){
                        if(boss.getStage() == 1){
                            Rectangle bossRectHit = new Rectangle(boss.getX()+52, boss.getY() + 96, 24, 12);
                            if (shotRect.intersects(bossRectHit)) {
                                hits.add(new Hit(shot.getX()-4, shot.getY()+24, 1));
                                if (canHit) {
                                    hit2Audio();
                                    boss.takeDamage(1); 
                                }
                                shot.die();
                                shotsToRemove.add(shot);
                            }
                        }
                    }else if(boss.getHp() == boss.stage2Hp+1 && boss.getStage() == 1){
                        Rectangle bossRectHit = new Rectangle(boss.getX()+52, boss.getY() + 96, 24, 12);
                            if (shotRect.intersects(bossRectHit)) {
                                boss.takeDamage(1); 
                                hit2Audio();
                                explosions.add(new Explosion(shot.getX(), shot.getY(), 1));
                                explAudio();
                                shot.die();
                                shotsToRemove.add(shot);
                            }
                    }

                    //stage2
                    if(boss.getHp() > boss.stage3Hp+1){
                        if(boss.getStage() == 2){
                            Rectangle bossRectHit = new Rectangle(boss.getX()+52, boss.getY() + 80, 24, 12);
                            if (shotRect.intersects(bossRectHit)) {
                                boss.takeDamage(1); 
                                hit2Audio();
                                hits.add(new Hit(shot.getX()-4, shot.getY()+8, 1));
                                shot.die();
                                shotsToRemove.add(shot);
                            }
                        }
                    }else if(boss.getHp() == boss.stage3Hp+1 && boss.getStage() == 2){
                        Rectangle bossRectHit = new Rectangle(boss.getX()+52, boss.getY() + 80, 24, 12);
                            if (shotRect.intersects(bossRectHit)) {
                                boss.takeDamage(1); 
                                hit2Audio();
                                explosions.add(new Explosion(shot.getX(), shot.getY(), 1));
                                explAudio();
                                shot.die();
                                shotsToRemove.add(shot);
                            }
                    }

                    //stage3
                    if(boss.getHp() > boss.stage4Hp+1){
                        if(boss.getStage() == 3){
                            Rectangle bossRectHit = new Rectangle(boss.getX()+52, boss.getY() + 64, 24, 12);
                            if (shotRect.intersects(bossRectHit)) {
                                boss.takeDamage(1); 
                                hit2Audio();
                                hits.add(new Hit(shot.getX()-4, shot.getY()+8, 1));
                                shot.die();
                                shotsToRemove.add(shot);
                            }
                        }
                    }else if(boss.getHp() == boss.stage4Hp+1 && boss.getStage() == 3){
                        Rectangle bossRectHit = new Rectangle(boss.getX()+52, boss.getY() + 64, 24, 12);
                            if (shotRect.intersects(bossRectHit)) {
                                boss.takeDamage(1); 
                                hit2Audio();
                                explosions.add(new Explosion(shot.getX(), shot.getY(), 1));
                                explAudio();
                                shot.die();
                                shotsToRemove.add(shot);
                            }
                    }

                    //stage4
                    if(boss.getHp() > 1){
                        if(boss.getStage() == 4){
                            Rectangle bossRectHit = new Rectangle(boss.getX()+52, boss.getY() + 32, 24, 12);
                            if (shotRect.intersects(bossRectHit)) {
                                boss.takeDamage(1); 
                                hit2Audio();
                                hits.add(new Hit(shot.getX()-4, shot.getY()+32, 1));
                                shot.die();
                                shotsToRemove.add(shot);
                            }
                        }
                    }
                    else if(boss.getHp() == 1 && boss.getStage() == 4){
                        Rectangle bossRectHit = new Rectangle(boss.getX()+52, boss.getY() + 32, 24, 12);
                            if (shotRect.intersects(bossRectHit)) {
                                boss.takeDamage(1); 
                                hit2Audio();
                                explosions.add(new Explosion(shot.getX(), shot.getY(), 1));
                                explAudio();
                                shot.die();
                                shotsToRemove.add(shot);
                                if (boss.getHp() <= 0 && !boss.isDying()) {
                                    ScoreManager.getInstance().addBossKill();
                                }
                            }
                    }
                    
                    //stage 4
                    if(boss.getStage() == 5 && !boss.isDead){
                        final int[] explosionCount = {0};
                        int totalExplosions = 14;
                        int delayBetween = 400; 

                        Timer explosionTimer = new Timer(delayBetween, null);
                        explosionTimer.addActionListener(e -> {
                            if (explosionCount[0] < totalExplosions) {
                                int scale = randomizer.nextInt(1, 3);
                                int explosionSize = 64 * scale;

                                int areaSize = 160;
                                int offsetX = randomizer.nextInt(areaSize - explosionSize + 1);
                                int offsetY = randomizer.nextInt(areaSize - explosionSize + 1);

                                int spawnX = boss.getX() + 32 - (areaSize - 128) / 2 + offsetX;
                                int spawnY = boss.getY() + 32 - (areaSize - 128) / 2 + offsetY;

                                explosions.add(new Explosion(spawnX, spawnY, scale));
                                explAudio();
                                explosionCount[0]++;
                            } else {
                                deaths+=30;
                                ((Timer) e.getSource()).stop();
                            }
                        });
                        explosionTimer.start();

                        Timer bossDeath = new Timer(6000, null);
                        bossDeath.addActionListener(e -> {
                            boss.setDying(true);
                        });
                        bossDeath.setRepeats(false);
                        bossDeath.start();
                    }

                    if (shotRect.intersects(bossRectMiss1) || shotRect.intersects(bossRectMiss2)){
                        hits.add(new Hit(shot.getX()-4, shot.getY()+8, 1));
                        hit1Audio();
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
                 if (y > GROUND - ALIEN_HEIGHT/3) {
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
                    
                    bomb.setDestroyed(true);
                    
                    /*for (ActivePowerUp ap : activePowerUps) {
                        if (ap.type == Shield.class) {
                            activePowerUps.remove(ap);
                            downgradePlayer(ap.type);
                            break;
                        }else{
                        } */
                    explAudio();
                    player.setDying(mortal);
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
        if (type == Shield.class) return new ImageIcon(IMG_POWERUP_SHIELD).getImage();
        return null; 
    }

    private void downgradePlayer(Class<?> type) {
        if (type == SpeedUp.class) new SpeedUp().downgrade(player);
        else if (type == TripleShot.class) new TripleShot().downgrade(player);
        else if (type == BIGShot.class) new BIGShot().downgrade(player);
        else if (type == BurstShot.class) new BurstShot().downgrade(player);
        else if (type == Shield.class) new Shield().downgrade(player);
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

            player.keyPressed(e);

            int x = player.getX();
            int y = player.getY();

            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE && inGame && !isPaused && !player.getDeath()) {
                long now = System.currentTimeMillis();
                if (shots.size() < 9) {
                    int shotMode = player.getShot();
                    switch (shotMode) {
                        case 1 -> {
                            if(now - lastShotTime > SHOT_COOLDOWN_MS){
                                shots.add(new Shot(x, y, 1, REG));
                                shotAudio();
                                lastShotTime = now;
                            }
                        }
                        case 2 -> {
                            if(now - lastShotTime > SHOT_COOLDOWN_MS + 100){
                                double angle = 10;
                                shots.add(new Shot(x, y, 1, REG));
                                shots.add(new Shot(x, y, -angle, 1, LEFT));
                                shots.add(new Shot(x, y, angle, 1, RIGHT));
                                shotAudio();
                                lastShotTime = now;
                            }
                        }
                        case 3 -> {
                            if(now - lastShotTime > SHOT_COOLDOWN_MS + 300){
                                shots.add(new Shot(x, y, 3, BIG));
                                shotAudio();
                                lastShotTime = now;
                            }
                        }
                        case 4 -> {
                            if (now - lastShotTime > SHOT_COOLDOWN_MS + 200) {

                                final int[] burstCount = {0}; 

                                Timer[] burstTimer = new Timer[1];
                                burstTimer[0] = new Timer(50, null);

                                burstTimer[0].addActionListener(v -> {
                                    shots.add(new Shot(player.getX(), player.getY(), 2, BURST));
                                    shotAudio();
                                    burstCount[0]++;

                                    if (burstCount[0] >= 3) {
                                        burstTimer[0].stop();
                                    }
                                });

                                burstTimer[0].start();
                                lastShotTime = now;
                            }
                        }

                    }
                }
            }else if (key == KeyEvent.VK_SPACE && !inGame && !isPaused && !player.getDeath()){
                System.exit(0);
            }
            
            if (key == KeyEvent.VK_ESCAPE && inGame && !player.getDeath() && !isPaused) {
                    isPaused = !isPaused;  
                    repaint();
                }
            if (isPaused) {
                if (key == KeyEvent.VK_Q) {
                    System.exit(0);
                }
                if (key == KeyEvent.VK_SPACE) {
                    isPaused = !isPaused;  
                    repaint();
                }
            }
        }
    }
}