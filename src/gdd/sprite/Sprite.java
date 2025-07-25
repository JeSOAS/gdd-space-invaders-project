package gdd.sprite;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import javax.swing.ImageIcon;

abstract public class Sprite {

    protected boolean visible;
    protected Image image;
    protected boolean dying;
    protected int visibleFrames = 10;

    protected int x;
    protected int y;
    protected int dx;

    // Animation fields
    protected ArrayList<Image> frames = new ArrayList<>();
    protected int frameIndex = 0;
    protected int frameDelay = 5;            
    protected int frameDelayCounter = 0;
    protected int currentFrame = 0;
    protected int animationCounter = 0;

    public Sprite() {
        visible = true;
    }

    abstract public void act(int direction);

    public boolean collidesWith(Sprite other) {
        if (other == null || !this.isVisible() || !other.isVisible()) {
            return false;
        }
        return this.getX() < other.getX() + other.getCurrentImage().getWidth(null)
                && this.getX() + this.getCurrentImage().getWidth(null) > other.getX()
                && this.getY() < other.getY() + other.getCurrentImage().getHeight(null)
                && this.getY() + this.getCurrentImage().getHeight(null) > other.getY();
    }

    public void die() {
        visible = false;
    }

    public boolean isVisible() {
        return visible;
    }

    public void visibleCountDown() {
        if (visibleFrames > 0) {
            visibleFrames--;
        } else {
            visible = false;
        }
    }

    protected void setVisible(boolean visible) {
        this.visible = visible;
    }

    public final void setImage(Image image) {
        this.image = image;
    }

    public Image getImage() {
        if (!frames.isEmpty()) {
            return frames.get(currentFrame);
        }
        return image;
    }

    // Use this in your game rendering logic
    public Image getCurrentImage() {
        return frames.isEmpty() ? image : frames.get(currentFrame);
    }


    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getY() {
        return y;
    }

    public int getX() {
        return x;
    }

    public void setDying(boolean dying) {
        this.dying = dying;
    }

    public boolean isDying() {
        return this.dying;
    }

    // ==== Animation Handling ====

    public void updateAnimation() {
        if (frames.size() <= 1) return;
        animationCounter++;
            if (animationCounter >= frameDelay) {
                animationCounter = 0;
                currentFrame = (currentFrame + 1) % frames.size();
                System.out.println("Loaded image: " + currentFrame);
                image = frames.get(currentFrame);
            }
    }

    public void setFrameDelay(int delay) {
        this.frameDelay = Math.max(1, delay);
    }



    public void loadFrames(Image spriteSheet, List<Rectangle> frameRects, int scale) {
        frames.clear();
        for (Rectangle r : frameRects) {
            BufferedImage buf = toBufferedImage(spriteSheet);
            BufferedImage frame = buf.getSubimage(r.x, r.y, r.width, r.height);
            Image scaled = frame.getScaledInstance(r.width * scale, r.height * scale, Image.SCALE_SMOOTH);
            frames.add(scaled);
        }
        currentFrame = 0;
        animationCounter = 0;
    }


    // Helper to convert Image to BufferedImage
    private BufferedImage toBufferedImage(Image img) {
        if (img instanceof BufferedImage) {
            return (BufferedImage) img;
        }

        // Use ImageIcon to get dimensions reliably
        ImageIcon icon = new ImageIcon(img);
        int width = icon.getIconWidth();
        int height = icon.getIconHeight();

        BufferedImage bimage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = bimage.createGraphics();
        g2d.drawImage(img, 0, 0, null);
        g2d.dispose();

        return bimage;
    }

}
