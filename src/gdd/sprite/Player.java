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
    private int currentSpeed = 3;
    private int shot_type = 1;
    private int dx = 0;

    private final List<Rectangle> IDLE = List.of(
        new Rectangle(152, 8, 16, 32),
        new Rectangle(176, 8, 16, 32)
    );



    //private final Rectangle bounds = new Rectangle(175,135,17,32);

    public Player() {
        initPlayer();
    }

    private void initPlayer() {
        Image sheet = new ImageIcon(IMG_SPRITES).getImage();
        loadFrames(sheet, IDLE, 2);

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

        if (key == KeyEvent.VK_LEFT) {
            dx = -currentSpeed;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = currentSpeed;
        }
    }

    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();

        if (key == KeyEvent.VK_LEFT) {
            dx = 0;
        }

        if (key == KeyEvent.VK_RIGHT) {
            dx = 0;
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

    @Override
    public void act(int direction) {
        updateAnimation();
        x += dx;

        if (x <= 3) {
            x = 3;
        }

        if (x >= BOARD_WIDTH - 2 * width) {
            x = BOARD_WIDTH - 2 * width;
        }
    }
}
