package gdd.sprite;

import static gdd.Global.*;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.util.List;
import javax.swing.ImageIcon;

public class Player extends Sprite {

    private static final int START_X = 270;
    private static final int START_Y = 540;
    private int width;
    private int currentSpeed = 6;
    private int shot_type = 1;
    private int dx = 0;
    private boolean isDead = false;

    private final List<Rectangle> IDLE = List.of(
        new Rectangle(152, 8, 16, 32),
        new Rectangle(176, 8, 16, 32)
    );
    private final List<Rectangle> LEFT = List.of(
        new Rectangle(200, 8, 16, 32)
    );
    private final List<Rectangle> RIGHT = List.of(
        new Rectangle(224, 8, 16, 32)
    );
    private final List<Rectangle> DIE = List.of(
        new Rectangle(249, 56, 16, 24),
        new Rectangle(273, 56, 16, 24),
        new Rectangle(297, 56, 16, 24)
    );
    private final List<Rectangle> IDLESHIELD = List.of(
        new Rectangle(153, 136, 16, 32),
        new Rectangle(177, 136, 16, 32)
    );
    private final List<Rectangle> LEFTSHIELD = List.of(
        new Rectangle(201, 136, 16, 32)
    );
    private final List<Rectangle> RIGHTSHIELD = List.of(
        new Rectangle(225, 136, 16, 32)
    );

    public Player() {
        initPlayer();
    }

    private void initPlayer() {
        if (!isDead){
            Image sheet = new ImageIcon(IMG_SPRITES).getImage();
            loadFrames(sheet, IDLE, 2);
        }
        setX(START_X);
        setY(START_Y);
    }

    public int getSpeed() {
        return currentSpeed;
    }

    public int setSpeed(int speed) {
        if (speed < 3) {
            speed = 3; // Ensure speed is at least 3
        }
        this.currentSpeed = speed;
        return currentSpeed;
    }

    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT && !isDead) {
            dx = -currentSpeed;
            Image sheet = new ImageIcon(IMG_SPRITES).getImage();
            loadFrames(sheet, LEFT, 2);
        }

        if (key == KeyEvent.VK_RIGHT && !isDead) {
            dx = currentSpeed;
            Image sheet = new ImageIcon(IMG_SPRITES).getImage();
            loadFrames(sheet, RIGHT, 2);
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
            if (!isDead){
                Image sheet = new ImageIcon(IMG_SPRITES).getImage();
                loadFrames(sheet, IDLE, 2);
            }
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
            if (!isDead){
                Image sheet = new ImageIcon(IMG_SPRITES).getImage();
                loadFrames(sheet, IDLE, 2);
            }
        }
    }

    public int getDx(){
        return dx;
    }

    public void setShot(int number){
        this.shot_type = number;
    }

    public int getShot(){
        return shot_type;
    }

    public boolean getDeath(){
        return isDead;
    }

    @Override
    public void setDying(boolean value) {
        if (value && !this.dying) {
            isDead = true;
            Image sheet = new ImageIcon(IMG_SPRITES).getImage();
            loadFrames(sheet, DIE, 2);
        }
        this.dying = value;
    }

    @Override
    public void act(int direction) {
        updateAnimation();
        if(!isDead){
            x += dx;

            if (x <= 3) {
                x = 3;
            }

            if (x >= BOARD_WIDTH - 2 * width) {
                x = BOARD_WIDTH - 2 * width;
            }
        }
    }
}
