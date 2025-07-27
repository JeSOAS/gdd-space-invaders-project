package gdd.sprite;

import static gdd.Global.*;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.ImageIcon;

public class Enemy extends Sprite {

    private Bomb bomb;

    private boolean isMainWave;

    private final List<Rectangle> IDLE = List.of(
        new Rectangle(161, 45, 34, 39),
        new Rectangle(195, 45, 34, 39),
        new Rectangle(161, 84, 34, 39),
        new Rectangle(195, 84, 34, 39)
    );

    public Enemy(int x, int y) {
        initEnemy(x, y);
        this.isMainWave = true;
    }


    public Enemy(int x, int y, boolean isMainWave) {
        initEnemy(x, y);
        this.isMainWave = isMainWave;
    }

    public boolean isMainWave() {
        return isMainWave;
    }

    public void setMainWave(boolean mainWave) {
        this.isMainWave = mainWave;
    }

    private void initEnemy(int x, int y) {
        Image sheet = new ImageIcon(IMG_SPRITES).getImage();
        loadFrames(sheet, IDLE, 1);
        this.x = x;
        this.y = y;
        bomb = new Bomb(x, y);
    }

    @Override
    public void act(int direction) {
        updateAnimation();
        this.x += direction;
    }

    public Bomb getBomb() {
        return bomb;
    }

    public class Bomb extends Sprite {

        private boolean destroyed;

        private final List<Rectangle> BOMB = List.of(
            new Rectangle(0, 85, 8, 8)
        );

        public Bomb(int x, int y) {

            initBomb(x, y);
        }

        private void initBomb(int x, int y) {

            setDestroyed(true);

            this.x = x;
            this.y = y;

            Image sheet = new ImageIcon(IMG_SPRITES).getImage();
            loadFrames(sheet, BOMB, 2);
        }

        public void setDestroyed(boolean destroyed) {

            this.destroyed = destroyed;
        }

        public boolean isDestroyed() {

            return destroyed;
        }
        
        @Override
        public void act(int direction) {
            updateAnimation();
        }
    }

}
