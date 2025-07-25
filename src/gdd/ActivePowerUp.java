package gdd;

import java.awt.Image;

public class ActivePowerUp {
    public Image image;
    public int remaining;
    public final int fullDuration;
    public final Class<?> type;

    public ActivePowerUp(Image image, int duration, Class<?> type) {
        this.image = image;
        this.remaining = duration;
        this.fullDuration = duration;
        this.type = type;
    }

    public void tick() {
        if (remaining > 0) remaining--;
    }

    public boolean isExpired() {
        return remaining <= 0;
    }

    public float getProgress() {
        return (float) remaining / fullDuration;
    }
}

