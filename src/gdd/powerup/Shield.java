package gdd.powerup;

import static gdd.Global.*;
import gdd.sprite.Player;
import javax.swing.ImageIcon;

public class Shield extends PowerUp {

    public int boostAmount = 4;

    public Shield(){
        super();
    }

    public Shield(int x, int y) {
        super(x, y);
        ImageIcon ii = new ImageIcon(IMG_POWERUP_SHIELD);
        var scaledImage = ii.getImage().getScaledInstance(ii.getIconWidth(), ii.getIconHeight(), java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);
    }

    @Override
    public void act(int direction) {
        this.y += 4;
    }

    
    @Override
    public void upgrade(Player player) {
        mortal = false;
    }

    @Override
    public void downgrade(Player player) {
        mortal = true;
    }

}
