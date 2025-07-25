package gdd.scene;

import gdd.AudioPlayer;
import gdd.Game;
import static gdd.Global.*;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.Timer;

public class TitleScene extends JPanel {

    private int frame = 0;
    private Image image;
    private Image image2;
    private Image image3;
    private AudioPlayer backgroundPlayer;
    private AudioPlayer sfxPlayer;
    private final Dimension d = new Dimension(BOARD_WIDTH, BOARD_HEIGHT);
    private Timer timer;
    private final Game game;
    private final String konami = "38384040373937396665";
    private String code = "";
    private boolean check = true;
    

    public TitleScene(Game game) {
        this.game = game;
        initBoard();
    }

    private void initBoard() {
        
    }

    public void start() {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.black);

        timer = new Timer(1000 / 30, new GameCycle());
        timer.start();

        initTitle();
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

            if (sfxPlayer != null) {
                sfxPlayer.stop();
            }
        } catch (Exception e) {
            System.err.println("Error closing audio player.");
        }
    }

    private void initTitle() {

        var url = getClass().getResource(IMG_TITLE);
        var url2 = getClass().getResource(IMG_KEYS);
        var url3 = getClass().getResource(IMG_SPACE);
        
            if (url == null) {
                System.err.println("Could not load image: " + IMG_TITLE);
            } else {
                var ii = new ImageIcon(url);
                var ii2 = new ImageIcon(url2);
                var ii3 = new ImageIcon(url3);
                image = ii.getImage();
                image2 = ii2.getImage();
                image3 = ii3.getImage();
            }
    }

    private void initAudio() {
        try {
            backgroundPlayer = new AudioPlayer(SND_TITLE, true);

            backgroundPlayer.play();
        } catch (Exception e) {
            System.err.println("Error with playing sound.");
        }

    }

    private void secretAudio() {
        try {
            sfxPlayer = new AudioPlayer(SND_SECRET, false);
            sfxPlayer.play();
        } catch (Exception e) {
            System.err.println("Error initializing audio player: " + e.getMessage());
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        doDrawing(g);
        
    }

    private void doDrawing(Graphics g) {

        g.setColor(Color.black);
        g.fillRect(0, 0, d.width, d.height);

        g.drawImage(image, 0, -80, d.width, d.height, this);
        g.drawImage(image2, BOARD_WIDTH / 4 + 252, 500, 128, 119, this);
        g.drawImage(image3, BOARD_WIDTH / 4 - 30, 550, 252, 52, this);
        

        if (frame % 60 < 30) {
            g.setColor(Color.red);
        } else {
            g.setColor(Color.white);
        }

        g.setFont(g.getFont().deriveFont(32f));
        String text = "Press SPACE to Start";
        int stringWidth = g.getFontMetrics().stringWidth(text);
        int x = (d.width - stringWidth) / 2;
        g.drawString(text, x, 480);

        g.setColor(Color.gray);
        g.setFont(g.getFont().deriveFont(12f));
        g.drawString("Game by Aleksandr Romanov(6530338)", 10, 650);

        Toolkit.getDefaultToolkit().sync();
    }

    private void update() {
        frame++; 
    }

    private void doGameCycle() {
        update();
        repaint();
    }

    private class GameCycle implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            doGameCycle();
        }
    }

    private class TAdapter extends KeyAdapter {

        @Override
        public void keyReleased(KeyEvent e) {

        }

        @Override
        public void keyPressed(KeyEvent e) {
            //System.out.println("Title.keyPressed: " + e.getKeyCode());
            code += e.getKeyCode();
            if(check){
                if(code.length()>=19){
                    if (code.substring(0,20).equals(konami)) {
                        mortal = false;
                        check = false;
                        secretAudio();
                    }
                if(code.length()>20){
                    code = code.substring(code.length()-20, code.length());
                }
                }
            }
            int key = e.getKeyCode();
            if (key == KeyEvent.VK_SPACE) {
                game.loadScene0();
            }

        }
    }
}
