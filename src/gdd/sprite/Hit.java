package gdd.sprite;

import static gdd.Global.*;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.ImageIcon;

public class Hit extends Sprite {

    private final List<Rectangle> IDLE = List.of(
        new Rectangle(301, 36, 16, 8)
    );

    private int lifetime = 4;

    public Hit(int x, int y, int scale) {
        initHit(x, y, scale);
    }

    private void initHit(int x, int y, int scale) {
        Image sheet = new ImageIcon(IMG_SPRITES).getImage();
        loadFrames(sheet, IDLE, scale);
        this.x = x;
        this.y = y;
    }

    @Override
    public void act(int direction) {
        updateAnimation();
        lifetime--;
    }

    public boolean isAlive() {
        return lifetime > 0;
    }
}
