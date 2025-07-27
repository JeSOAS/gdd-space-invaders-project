package gdd.sprite;

import static gdd.Global.*;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.ImageIcon;


public class Shot extends Sprite {

    private int local_scale = 1;

    private double dx = 0;      // Horizontal speed
    private double dy = -20;    // Vertical speed (upwards)
    private boolean angled = false;

    private final List<Rectangle> REG = List.of(
        new Rectangle(344, 32, 8, 16)
    );

    private final List<Rectangle> BURST = List.of(
        new Rectangle(329, 56, 8, 16),
        new Rectangle(337, 56, 8, 16),
        new Rectangle(345, 56, 8, 16)
    );

    private final List<Rectangle> BIG = List.of(
        new Rectangle(361, 55, 16, 32),
        new Rectangle(377, 55, 16, 32),
        new Rectangle(393, 55, 16, 32)
    );

    private final List<Rectangle> RIGHT = List.of(
        new Rectangle(361, 32, 8, 16)
    );

    private final List<Rectangle> LEFT = List.of(
        new Rectangle(352 , 32, 8, 16)
    );



    public Shot() {
    }

    public Shot(int x, int y, int local_scale, String type) {
        if ("REG".equals(type)){
            initShot(x, y, local_scale, REG);
        }else if ("BURST".equals(type)){
            initShot(x, y, local_scale, BURST);
        }else if ("BIG".equals(type)){
            initShot(x, y, local_scale, BIG);
        }
    }

    public Shot(int x, int y, double angle, int local_scale, String type) {
        angled = true;
        if ("RIGHT".equals(type)){
            initShot(x, y, local_scale, RIGHT);
        }else if ("LEFT".equals(type)){
            initShot(x, y, local_scale, LEFT);
        }
        int speed = 20;
        double radians = Math.toRadians(angle);
        dx = speed * Math.sin(radians);
        dy = -speed * Math.cos(radians);

    }


    private void initShot(int x, int y, int local_scale, List<Rectangle> rect) {
        this.local_scale = local_scale;
        Image sheet = new ImageIcon(IMG_SPRITES).getImage();
        loadFrames(sheet, rect, local_scale);

        setX(x + (PLAYER_WIDTH / 2) - (getWidth() / 2));
        setY(y - getHeight()/2);
    }

    @Override
    public void act(int direction) {
        updateAnimation();
        if (angled) {
            this.x += dx;
            this.y += dy;
        } else {
            this.y -= 20;
        }
    }
}
