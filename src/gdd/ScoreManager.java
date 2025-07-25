package gdd;

public class ScoreManager {

    private static final ScoreManager instance = new ScoreManager();

    private int score;
    private int frameCounter;

    @SuppressWarnings("OverridableMethodCallInConstructor")
    private ScoreManager() {
        reset();
    }

    public static ScoreManager getInstance() {
        return instance;
    }

    public void reset() {
        score = 0;
        frameCounter = 0;
    }

    public int getScore() {
        return score;
    }

    public void addEnemyKill() {
        score += 50;
    }

    public void addPowerUp() {
        score += 100;
    }

    public void addLevelCompletion() {
        score += 500;
    }

    // Call this once per frame in your game loop
    public void update() {
        frameCounter++;
        if (frameCounter >= 3) { // Assuming 60 FPS = 1 second
            score += 1;
            frameCounter = 0;
        }
    }
}
