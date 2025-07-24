package gdd.sprite;

import static gdd.Global.*;
import javax.swing.ImageIcon;

public class Shot extends Sprite {

    private static final int H_SPACE = 20;
    private static final int V_SPACE = 1;

    private double dx = 0;      // Horizontal speed
    private double dy = -20;    // Vertical speed (upwards)
    private boolean angled = false;

    public Shot() {

    }

    // Constructor for straight 
    public Shot(int x, int y) {
        initShot(x, y);
    }

    // Constructor for angled 
    public Shot(int x, int y, double angle) {
        initShot(x, y);
        angled = true;

        int speed = 20;
        dx = speed * Math.sin(angle);
        dy = -speed * Math.cos(angle);  
    }

    private void initShot(int x, int y) {
        var ii = new ImageIcon(IMG_SHOT);
        var image = ii.getImage();

        var fullyLoadedImage = new javax.swing.ImageIcon(image).getImage();

        var scaledImage = fullyLoadedImage.getScaledInstance(
            ii.getIconWidth() * SCALE_FACTOR,
            ii.getIconHeight() * SCALE_FACTOR,
            java.awt.Image.SCALE_SMOOTH
        );
        setImage(scaledImage);

        setX(x + H_SPACE);
        setY(y - V_SPACE);
    }

    @Override
    public void act(int direction) {
        if (angled) {
            this.x += dx;
            this.y += dy;
        } else {
            this.y -= 20;
        }
    }
}
