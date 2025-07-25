package gdd.scene;

import gdd.AudioPlayer;
import gdd.Game;
import static gdd.Global.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Scene2 extends JPanel implements ActionListener {

    private AudioPlayer backgroundPlayer;
    private Timer timer;
    private int elapsedTime = 0; 
    private final int waitTime = 9000; 

    private String message = "Level 1 Complete!";

    private Game game; // reference to main game to switch scenes

    public Scene2(Game game) {
        this.game = game;
        initBoard();
    }

    private void initBoard() {
        
    }

    public void start() {
        setFocusable(true);
        setBackground(Color.black);

        timer = new Timer(1000 / 30, this);
        timer.start();

        initAudio();
    }

    public void stop() {
        try {
            if (timer != null) {
                timer.stop();
            }
            if (backgroundPlayer != null) {
                backgroundPlayer.stop();
            }
        } catch (Exception e) {
            System.err.println("Error closing audio player.");
        }
    }

    private void initAudio() {
        try {
            backgroundPlayer = new AudioPlayer(SND_WIN, false);
            backgroundPlayer.play();
        } catch (Exception e) {
            System.err.println("Error initializing audio player: " + e.getMessage());
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        doDrawing(g);
    }


    private void doDrawing(Graphics g) {
        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);

        g.setColor(new Color(0, 32, 48));
        g.fillRect(50, BOARD_HEIGHT / 2 - 30, BOARD_WIDTH - 100, 50);

        g.setColor(Color.white);
        g.drawRect(50, BOARD_HEIGHT / 2 - 30, BOARD_WIDTH - 100, 50);

        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics fm = this.getFontMetrics(small);

        g.setColor(Color.white);
        g.setFont(small);
        g.drawString(message, (BOARD_WIDTH - fm.stringWidth(message)) / 2,
                BOARD_HEIGHT / 2);
    }


    @Override
public void actionPerformed(ActionEvent e) {
    elapsedTime += 100;

    if (elapsedTime >= 6000 && message.equals("Level 1 Complete!")) {
        message = "Level 2";
    }

    if (elapsedTime >= waitTime) {
        timer.stop();
        backgroundPlayer.pause();
        game.loadScene3();
    }

    repaint();
}
}
