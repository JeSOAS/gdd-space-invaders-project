package gdd.powerup;

import static gdd.Global.*;
import gdd.sprite.Player;
import javax.swing.ImageIcon;

public class SpeedUp extends PowerUp {

    public SpeedUp(int x, int y) {
        super(x, y);
        // Set image
        ImageIcon ii = new ImageIcon(IMG_POWERUP_SPEEDUP);
        var scaledImage = ii.getImage().getScaledInstance(ii.getIconWidth(), ii.getIconHeight(), java.awt.Image.SCALE_SMOOTH);
        setImage(scaledImage);
    }

    @Override
    public void act(int direction) {
        this.y += 3; // Move down by 3 pixels each frame
    }

    
    @Override
    public void upgrade(Player player) {
        // Upgrade the player with speed boost
        int boostAmount = 4;
        player.setSpeed(player.getSpeed() + boostAmount); // Increase speed
        this.die(); // Remove the power-up from screen

        // Create a timer to revert the speed boost after 10 seconds (600 frames @ 60 FPS)
        new javax.swing.Timer(10000, e -> {
            player.setSpeed(player.getSpeed() - boostAmount); // Revert speed
            ((javax.swing.Timer) e.getSource()).stop(); // Stop the timer
        }).start();
    }



}
