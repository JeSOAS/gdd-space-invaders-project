package gdd.powerup;

import static gdd.Global.*;
import gdd.sprite.Player;
import javax.swing.ImageIcon;

public class TripleShot extends PowerUp {

    public TripleShot(int x, int y) {
        super(x, y);
        // Set image
        ImageIcon ii = new ImageIcon(IMG_POWERUP_TRIPLE);
        var scaledImage = ii.getImage().getScaledInstance(ii.getIconWidth(), ii.getIconHeight(), java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);
    }

    @Override
    public void act(int direction) {
        this.y += 3; // Move down by 3 pixels each frame
    }

    
    @Override
    public void upgrade(Player player) {
        // Upgrade the player with shot boost
        if (player.getShot() != 2){
            player.setShot(2); // Set shot to triple
        }
        this.die(); // Remove the power-up after use
    }


}
