package gdd.sprite;

import static gdd.Global.*;
import java.awt.Image;
import java.awt.Rectangle;
import java.util.List;
import javax.swing.ImageIcon;

public class Alien2 extends Enemy {

    public Alien2(int x, int y, int dx) {
        super(x, y, true);
        initAlien2(dx);
    }

    public Alien2(int x, int y, boolean isMainWave, int dx) {
        super(x, y, isMainWave);
        initAlien2(dx);
    }

    private void initAlien2(int dx) {
        Image sheet = new ImageIcon(IMG_SPRITES).getImage();
        List<Rectangle> ALIEN2_IDLE = List.of(
            new Rectangle(14, 112, 18, 17),
            new Rectangle(38, 112, 18, 17),
            new Rectangle(62, 112, 18, 17),
            new Rectangle(86, 112, 18, 17),
            new Rectangle(110, 112, 18, 17)
        );
        loadFrames(sheet, ALIEN2_IDLE, 2);
        this.dx = dx;
    }

    @Override
    public void act(int ignored) {
        updateAnimation();
        setX(getX() + dx);

        if (getX() <= BORDER_LEFT || getX() >= BOARD_WIDTH - ALIEN_WIDTH) {
            dx = -dx;
        }

        setY(getY() + 1);
    }
}
