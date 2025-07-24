package gdd;

import java.awt.Image;

public class ActivePowerUp {
    public Image image;        // power-up icon
    public int duration = 300; // total duration (frames)
    public int remaining;      // countdown timer

    public ActivePowerUp(Image image) {
        this.image = image;
        this.remaining = duration;
    }

    public void tick() {
        if (remaining > 0) remaining--;
    }

    public boolean isExpired() {
        return remaining <= 0;
    }

    public float getProgress() {
        return (float) remaining / duration;
    }
}
