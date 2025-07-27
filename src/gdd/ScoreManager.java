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

    public void addBossKill() {
        score += 3000;
    }

    public void update() {
        frameCounter++;
        if (frameCounter >= 3) {
            score += 1;
            frameCounter = 0;
        }
    }
}
