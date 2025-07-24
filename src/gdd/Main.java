package gdd;

import java.awt.EventQueue;

public class Main {
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            ScoreManager.getInstance().reset();
            var game = new Game();
            game.setVisible(true);
        });
    }
}