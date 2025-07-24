package gdd.sprite;

import static gdd.Global.*;
import javax.swing.ImageIcon;

public class Alien1 extends Enemy {

    private Bomb bomb;

    public Alien1(int x, int y) {
        super(x, y);
    }

    @Override
    public void act(int direction) {
        this.y ++;
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
