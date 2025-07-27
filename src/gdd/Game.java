package gdd;

import gdd.scene.Scene0;
import gdd.scene.Scene1;
import gdd.scene.Scene2;
import gdd.scene.Scene3;
import gdd.scene.TitleScene;
import javax.swing.JFrame;

public final class Game extends JFrame  {

    TitleScene titleScene;
    Scene0 scene0;
    Scene1 scene1;
    Scene2 scene2;
    Scene3 scene3;

    public Game() {
        titleScene = new TitleScene(this);
        scene0 = new Scene0(this);
        scene1 = new Scene1(this);
        scene2 = new Scene2(this);
        scene3 = new Scene3(this);
        initUI();
        loadTitle();
    }

    private void initUI() {

        setTitle("Space Invaders");
        setSize(Global.BOARD_WIDTH, Global.BOARD_HEIGHT);

        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);
        setLocationRelativeTo(null);

    }

    public void loadTitle() {
        getContentPane().removeAll();
        add(titleScene);
        titleScene.start();
        revalidate();
        repaint();
    }

    public void loadScene0() {
        getContentPane().removeAll();
        add(scene0);
        titleScene.stop();
        scene0.start();
        revalidate();
        repaint();
    }

    public void loadScene1() {
        getContentPane().removeAll();
        add(scene1);
        scene0.stop();
        scene1.start();
        revalidate();
        repaint();
    }

    public void loadScene2() {
        getContentPane().removeAll();
        add(scene2);
        scene1.stop();
        scene2.start();
        revalidate();
        repaint();
    }

    public void loadScene3() {
        getContentPane().removeAll();
        add(scene3);
        titleScene.stop();
        scene3.start();
        revalidate();
        repaint();
    }

}