package gdd.sprite;

import static gdd.Global.*;
import java.awt.Image;
import java.util.Random;
import javax.swing.ImageIcon;

public class Alien2 extends Enemy {

    private Bomb bomb;

    private static final Image ALIEN_IMG;
    private final Random randomizer = new Random();

    static {
        ImageIcon ii = new ImageIcon(IMG_ALIEN1);
        Image raw = ii.getImage();

        Image fullyLoadedImage = new ImageIcon(raw).getImage();
        Image scaledImage = fullyLoadedImage.getScaledInstance(
                ii.getIconWidth() * SCALE_FACTOR,
                ii.getIconHeight() * SCALE_FACTOR,
                Image.SCALE_SMOOTH
        );

        ALIEN_IMG = scaledImage;
    }

    public Alien2(int x, int y) {
        super(x, y); // Defaults to isMainWave = true
        this.image = ALIEN_IMG;
        int[] options = {-1, 0, 1};
        this.dx = options[randomizer.nextInt(options.length)];
    }

    public Alien2(int x, int y, boolean isMainWave) {
        super(x, y, isMainWave);
        this.image = ALIEN_IMG;
        int[] options = {-1, 0, 1 };
        this.dx = options[randomizer.nextInt(options.length)];
    }

    @Override
    public void act(int ignored) {

        setX(getX() + dx);

        if (getX() <= BORDER_LEFT
         || getX() >= BOARD_WIDTH - ALIEN_WIDTH) {
            dx = -dx;
        }

        setY(getY() + 1);
    }

    public class Bomb extends Sprite {

        private boolean destroyed;

        public Bomb(int x, int y) {

            initBomb(x, y);
        }

        private void initBomb(int x, int y) {

            setDestroyed(true);

            this.x = x;
            this.y = y;

            var ii = new ImageIcon(IMG_BOMB);
            setImage(ii.getImage());
        }

        public void setDestroyed(boolean destroyed) {

            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {

            return destroyed;
        }

        @Override
        public void act(int direction) {
            this.y ++;
        }
    }
}
