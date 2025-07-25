package gdd.powerup;

import static gdd.Global.*;
import gdd.sprite.Player;
import javax.swing.ImageIcon;

public class SpeedUp extends PowerUp {

    public int boostAmount = 4;

    public SpeedUp(){
        super();
    }

    public SpeedUp(int x, int y) {
        super(x, y);
        // Set image
        ImageIcon ii = new ImageIcon(IMG_POWERUP_SPEEDUP);
        var scaledImage = ii.getImage().getScaledInstance(ii.getIconWidth(), ii.getIconHeight(), java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);
    }

    @Override
    public void act(int direction) {
        this.y += 4; // Move down by 3 pixels each frame
    }

    
    @Override
    public void upgrade(Player player) {
        // Upgrade the player with speed boost
        player.setSpeed(player.getSpeed() + boostAmount); // Increase speed
        this.die(); // Remove the power-up from screen
    }

    @Override
    public void downgrade(Player player) {
        player.setSpeed(player.getSpeed() - boostAmount);
    }

}
