package gdd.sprite;

import static gdd.Global.*;
import javax.swing.ImageIcon;

public class Shot extends Sprite {

    private static final int H_SPACE = 20;
    private static final int V_SPACE = 1;

    private int local_scale = 3;

    private double dx = 0;      // Horizontal speed
    private double dy = -20;    // Vertical speed (upwards)
    private boolean angled = false;

    public Shot() {
        this.local_scale = SCALE_FACTOR;
    }

    public Shot(int x, int y, int local_scale) {
        this.local_scale = local_scale;
        initShot(x, y, local_scale);
    }

    public Shot(int x, int y, double angle, int local_scale) {
        this.local_scale = local_scale;
        angled = true;
        initShot(x, y, local_scale);

        int speed = 20;
        dx = speed * Math.sin(angle);
        dy = -speed * Math.cos(angle);  
    }


    private void initShot(int x, int y, int local_scale) {
        var ii = new ImageIcon(IMG_SHOT);
        var image = ii.getImage();

        var fullyLoadedImage = new javax.swing.ImageIcon(image).getImage();

        var scaledImage = fullyLoadedImage.getScaledInstance(
            ii.getIconWidth() * local_scale,
            ii.getIconHeight() * local_scale,
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
