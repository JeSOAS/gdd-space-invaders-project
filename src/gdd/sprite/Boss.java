package gdd.sprite;

import gdd.AudioPlayer;
import static gdd.Global.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;
import javax.swing.Timer;

public class Boss extends Sprite {

    private AudioPlayer sfxPlayer;
    private final List<Rectangle>[] STAGES = new List[5];
    private final List<Projectile> projectiles = new ArrayList<>();
    private int stage = 1;
    private int frameCount = 0;
    private int shakeOffset = 0;

    private int moveTimer = 0;
    private int direction = 1;
    private int lastStage = 1;
    public boolean isDead = false;
    private int HP = 100;
    public int stage2Hp = 25;
    public int stage3Hp = 15;
    public int stage4Hp = 5;


    public Boss(int x, int y, int HP) {
        initBoss(x, y, HP);
    }

    private void initBoss(int x, int y, int HP) {
        this.HP = HP;
        this.x = x;
        this.y = y;
        Image sheet = new ImageIcon(IMG_SPRITES).getImage();

        STAGES[0] = List.of(new Rectangle(0, 0, 64, 64));
        STAGES[1] = List.of(new Rectangle( 256, 112, 64, 64));
        STAGES[2] = List.of(new Rectangle(320, 112, 64, 64));
        STAGES[3] = List.of(new Rectangle(384, 112, 64, 64));
        STAGES[4] = List.of(new Rectangle(64, 0, 64, 64)); 

        loadFrames(sheet, STAGES[stage - 1], 2);
    }

    public void takeDamage(int dmg){
        this.HP -= dmg;
    }

    public int getHp(){
        return this.HP;
    }

    public List<Projectile> getFire() {
        return projectiles;
    }

    public int getStage(){
        return this.stage;
    }

    @Override
    public void act(int direction) {
        frameCount++;
        if (stage < 5) updateStageLogic();
        else {
            applyShakeEffect();
            Timer waitForDeath = new Timer(100, null);
            waitForDeath.addActionListener(e ->{
                isDead = true;
            });
            
        }
        
        updateAnimation();
        updateProjectiles();
    }

    public void updateStageLogic() {
        switch (stage) {
            case 1 -> {
                driftToCenter();
            }
            case 2 -> {
                patternZigzag();
            }
            case 3 -> {
                patternPauseAndDash();
            }
            case 4 -> {
                patternRandomStops();
            }
        }

        if (frameCount % 60 == 0) fire();
    }

    public void checkAndUpdateStage() {
        int hp = getHp();

        if (hp <= 0 && lastStage < 5) {
            stage = 5;
        } else if (hp <= stage4Hp && lastStage < 4) {
            stage = 4;
        } else if (hp <= stage3Hp && lastStage < 3) {
            stage = 3;
        } else if (hp <= stage2Hp && lastStage < 2) {
            stage = 2;
        }

        if (stage > lastStage) {
            lastStage = stage;
            loadFrames(new ImageIcon(IMG_SPRITES).getImage(), STAGES[stage - 1], 2);
        }
    }

    private void driftToCenter() {
        if (y < 100) y += 6;
    }

    private void patternZigzag() {
        x += direction * 4;

        if (x < 0) {
            x = 0;
            direction *= -1;
        } else if (x > BOARD_WIDTH - 128 - BORDER_LEFT) {
            x = BOARD_WIDTH - 128 - BORDER_LEFT;
            direction *= -1;
        }
    }


    private void patternPauseAndDash() {
        moveTimer++;
        if (moveTimer % 360 == 0) {
            direction *= -1;
        }
        if (moveTimer % 90 < 30) {
            x += direction * 9;
            if (x < 0) {
                x = 0;
                direction *= -1;
            } else if (x > 716 - 128 - BORDER_LEFT) {
                x = 716 - 128 - BORDER_LEFT;
                direction *= -1;
            }
        }
    }

    private void patternRandomStops() {
        moveTimer++;
        if (moveTimer % 120 < 30) {
            x += direction * 4;
        } else {
            x -= direction * 4;
        }
        if (x < 0) {
            x = 0;
            direction *= -1;
        } else if (x > BOARD_WIDTH - 128 - BORDER_LEFT) {
            x = BOARD_WIDTH - 128 - BORDER_LEFT;
            direction *= -1;
        }
    }


    private void applyShakeEffect() {
        shakeOffset = (int) (Math.random() * 5 - 2);
    }

    private void updateProjectiles() {
        for (Projectile p : projectiles) {
            p.move();
        }
        projectiles.removeIf(p -> !p.isVisible());
    }

    public void fire() {
        switch (stage) {
            case 1 -> {
                int delayBetweenShots = 200; 
                final int[] shotsFired = {0};

                Timer burstFire = new Timer(delayBetweenShots, null);
                burstFire.addActionListener(e -> {
                    if (shotsFired[0] < 3) {
                        projectiles.add(new Projectile(x + 50, y + 50, -1, 3));
                        projectiles.add(new Projectile(x + 60, y + 50, 0, 3));
                        projectiles.add(new Projectile(x + 70, y + 50, 1, 3));
                        bombAudio();
                        shotsFired[0]++;
                    } else {
                        ((Timer) e.getSource()).stop(); 
                    }
                });

                burstFire.start();
            }

            case 2 -> {
                bombAudio();
                for (int dx = -2; dx <= 2; dx++) {
                    projectiles.add(new Projectile(x + 60, y + 50, dx, 2));
                }
            }
            case 3 -> {
                final int[][] directions = {
                    {-2, -1, 0, 1, 2},
                    {-2, -1, 1, 2}
                };

                final int[] currentRow = {0};

                Timer fireTimer = new Timer(250, null);
                fireTimer.addActionListener(e -> {
                    int[] row = directions[currentRow[0]];
                    bombAudio();
                    for (int dx : row) {
                        projectiles.add(new Projectile(x + 60, y + 50, dx, 2));
                    }

                    currentRow[0]++;
                    if (currentRow[0] >= directions.length) {
                        ((Timer) e.getSource()).stop();
                    }
                });

                fireTimer.start();
            }


            case 4 -> {
                if (frameCount % 120 == 0) {
                    bombAudio();
                    for (int i = 0; i < 360; i += 45) {
                        double rad = Math.toRadians(i);
                        projectiles.add(new Projectile(x + 32, y + 32,
                                (int) (Math.cos(rad) * 2), (int) (Math.sin(rad) * 2)));
                    }
                }
            }
        }
    }

    public List<Projectile> getProjectiles() {
        return projectiles;
    }

    public void nextStage() {
        if (stage < 5) {
            stage++;
            loadFrames(new ImageIcon(IMG_SPRITES).getImage(), STAGES[stage - 1], 1);
        }
    }

    @Override
    public Image getCurrentImage() {
        return super.getCurrentImage();
    }

    @Override
    public int getX() {
        return x + (stage == 5 ? shakeOffset : 0);
    }

    public class Projectile extends Sprite {
        private int dx, dy;
        private List<Rectangle> IDLE = List.of(new Rectangle(5, 73, 8, 8));

        @Override
        public void act(int direction) {
            updateAnimation();
            this.y -= direction;
        }

        public Projectile(int x, int y, int dx, int dy) {
            Image sheet = new ImageIcon(IMG_SPRITES).getImage();
            loadFrames(sheet, IDLE, 1);
            this.x = x;
            this.y = y;
            this.dx = dx;
            this.dy = dy;
        }

        public void move() {
            x += dx;
            y += dy;
            if (y > BOARD_HEIGHT || x < 0 || x > BOARD_WIDTH) {
                setVisible(false);
            }

        }
    }
    //Sounds
    private void bombAudio() {
        try {
            sfxPlayer = new AudioPlayer(SND_BOSS_SHOT, false);
            sfxPlayer.play();
        } catch (Exception e) {
            System.err.println("Error initializing audio player: " + e.getMessage());
        }
    }
}
