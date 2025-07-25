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

    public void reset(int duration) {
        this.remaining = duration;
    }

    public float getProgress() {
        return (float) remaining / fullDuration;
    }

    public boolean update() {
        if (remaining > 0) remaining--;
        return remaining <= 0;
    }
}


