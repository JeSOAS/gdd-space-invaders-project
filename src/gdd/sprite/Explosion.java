package gdd.sprite;

import static gdd.Global.*;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.ImageIcon;

public class Explosion extends Sprite {

    private final List<Rectangle> IDLE = List.of(
        new Rectangle(10, 137, 32, 32),
        new Rectangle(42, 137, 32, 32),
        new Rectangle(74, 137, 32, 32),
        new Rectangle(106, 137, 32, 32)
    );

    public Explosion(int x, int y, int scale) {
        initExplosion(x, y, scale);
    }

    private void initExplosion(int x, int y, int scale) {
        
        Image sheet = new ImageIcon(IMG_SPRITES).getImage();
        loadFrames(sheet, IDLE, scale);
        this.x = x;
        this.y = y;

    }
    
    @Override
    public void act(int direction) {
        updateAnimation();
    }


}
