package gdd;

public class Global {
    private Global() {
        // Prevent instantiation
    }

    public static final int player_direction = 0;

    public static boolean mortal = true;

    public static final int SCALE_FACTOR = 3; // Scaling factor for sprites

    public static final int FPS = 30; 

    public static final int BOARD_WIDTH = 716; // Doubled from 358
    public static final int BOARD_HEIGHT = 700; // Doubled from 350
    public static final int BORDER_RIGHT = 60; // Doubled from 30
    public static final int BORDER_LEFT = 10; // Doubled from 5

    public static final int GROUND = 610; // Corrected
    public static final int BOMB_HEIGHT = 10; // Doubled from 5

    public static final int ALIEN_HEIGHT = 39; // Corrected
    public static final int ALIEN_WIDTH = 34; // Corrected
    public static final int ALIEN_INIT_X = 300; // Doubled from 150
    public static final int ALIEN_INIT_Y = 10; // Doubled from 5
    public static final int ALIEN_GAP = 15; // Gap between aliens

    public static final int GO_DOWN = 45; // Corrected
    public static final int CHANCE = 1;
    public static final int DELAY = 17;
    public static final int PLAYER_WIDTH = 32; // Corrected
    public static final int PLAYER_HEIGHT = 64; // Corrected

    // Images
    public static final String IMG_TITLE = "/images/title.png";
    public static final String IMG_KEYS = "/images/keys.png";
    public static final String IMG_SPACE = "/images/space.png";
    public static final String IMG_SPRITES = "images/sprites.png";

    public static final String IMG_ENEMY = "images/alien.png";
    public static final String IMG_ALIEN1 = "images/alien1.png";
    public static final String IMG_PLAYER = "images/player.png";
    public static final String IMG_SHOT = "images/shot.png";
    public static final String IMG_BOMB = "images/bomb.png";

    public static final String IMG_EXPLOSION = "images/explosion.png";
    public static final String IMG_EXPLOSION2 = "images/explosion2.png";
    public static final String IMG_POWERUP_SPEEDUP = "images/powerup-s.png";
    public static final String IMG_POWERUP_TRIPLE = "images/triple.png";
    public static final String IMG_UI = "images/powerUi.png";
    public static final String IMG_POWERUP_BIG = "images/Big.png";
    public static final String IMG_POWERUP_BURST = "images/burst.png";
    public static final String IMG_POWERUP_SHIELD = "images/shield.png";

    // Music
    public static final String SND_TITLE = "audio/title.wav";
    public static final String SND_SCENE1 = "audio/scene1.wav";
    public static final String SND_WIN = "audio/win.wav";
    public static final String SND_LOSE = "audio/defeat.wav";
    public static final String SND_FIN = "audio/fin.wav";
    public static final String SND_SCENE2 = "audio/scene2.wav";
    public static final String SND_BOSS = "audio/boss.wav";

    // Audio
    public static final String SND_HIT = "audio/hit.wav";
    public static final String SND_HIT2 = "audio/hit2.wav";
    public static final String SND_SHOT = "audio/shot.wav";
    public static final String SND_BOSS_SHOT = "audio/bossShot.wav";
    public static final String SND_EXPL = "audio/explosion.wav";
    public static final String SND_POWERUP = "audio/powerUp.wav";
    public static final String SND_SECRET = "audio/secret.wav";
    public static final String[] SND_LZR = {
        "audio/lazer1.wav",
        "audio/lazer2.wav"
    };

    //A lot of stuff here is corrected and it doesn't really make sence reading :p
}
