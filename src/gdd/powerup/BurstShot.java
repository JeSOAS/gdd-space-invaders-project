package gdd.powerup;

import static gdd.Global.*;
import gdd.sprite.Player;
import javax.swing.ImageIcon;

public class BurstShot extends PowerUp {

    public BurstShot(){
        super();
    }

    public BurstShot(int x, int y) {
        super(x, y);
        ImageIcon ii = new ImageIcon(IMG_POWERUP_BURST);
        var scaledImage = ii.getImage().getScaledInstance(ii.getIconWidth(), ii.getIconHeight(), java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);
    }

    @Override
    public void act(int direction) {
        this.y += 4;
    }

    
    @Override
    public void upgrade(Player player) {
        if (player.getShot() != 4){
            player.setShot(4); 
        }
        this.die(); //Remove the power-up after use
    }

    @Override
    public void downgrade(Player player) {
        player.setShot(1);
    }

}
