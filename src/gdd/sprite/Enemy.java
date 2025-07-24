package gdd.sprite;

import static gdd.Global.*;
import java.awt.Image;
import javax.swing.ImageIcon;

public class Enemy extends Sprite {

    private Bomb bomb;

    private boolean isMainWave;

    private static final Image ALIEN_IMG;

    static {
        ImageIcon ii = new ImageIcon(IMG_ENEMY);
        Image raw = ii.getImage();

        Image fullyLoadedImage = new ImageIcon(raw).getImage();
        Image scaledImage = fullyLoadedImage.getScaledInstance(
                ii.getIconWidth() * SCALE_FACTOR,
                ii.getIconHeight() * SCALE_FACTOR,
                Image.SCALE_SMOOTH
        );

        ALIEN_IMG = scaledImage;
    }

    public Enemy(int x, int y) {
        this(x, y, true); // Default to part of main wave
        setImage(ALIEN_IMG);
    }


    public Enemy(int x, int y, boolean isMainWave) {
        initEnemy(x, y);
        this.isMainWave = isMainWave;
        setImage(ALIEN_IMG);
    }

    public boolean isMainWave() {
        return isMainWave;
    }

    public void setMainWave(boolean mainWave) {
        this.isMainWave = mainWave;
    }

    private void initEnemy(int x, int y) {
        this.x = x;
        this.y = y;

        bomb = new Bomb(x, y);
    }

    @Override
    public void act(int direction) {
        this.x += direction;
    }

    public Bomb getBomb() {

        return bomb;
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
            this.y -= direction;
        }
    }

}
