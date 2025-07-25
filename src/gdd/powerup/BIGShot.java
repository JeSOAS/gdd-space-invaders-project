package gdd.powerup;

import static gdd.Global.*;
import gdd.sprite.Player;
import javax.swing.ImageIcon;

public class BIGShot extends PowerUp {

    public BIGShot(){
        super();
    }

    public BIGShot(int x, int y) {
        super(x, y);
        // Set image
        ImageIcon ii = new ImageIcon(IMG_POWERUP_BIG);
        var scaledImage = ii.getImage().getScaledInstance(ii.getIconWidth(), ii.getIconHeight(), java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);
    }

    @Override
    public void act(int direction) {
        this.y += 4; // Move down by 3 pixels each frame
    }

    
    @Override
    public void upgrade(Player player) {
        // Upgrade the [[little sponge]] with [[BIG]] shot
        if (player.getShot() != 3){
            player.setShot(3); // Set shot to [[BIG]]
        }
        this.die(); // Remove the power-up after use
    }

    @Override
    public void downgrade(Player player) {
        player.setShot(1);
    }

}
