package com.example.demo.common.game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MyGame extends JPanel {
    static GameEngine gameEngine;
    static int keyCode;

    /**
     * 初始化方法，展现游戏画面
     * @param title  窗体标题
     * @param width  窗体宽度
     * @param height 窗体高度
     * @param engine 引擎
     */
    public static void init(String title, int width, int height, GameEngine engine) {
        gameEngine = engine;
        JFrame jFrame = new JFrame(title);
        MyGame myGame = new MyGame();
        myGame.setPreferredSize(new Dimension(width, height));
        jFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jFrame.add(myGame);
        jFrame.pack();

        //匿名内部类创建键盘监听器
        jFrame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                keyCode = e.getKeyCode();
            }

            @Override
            public void keyReleased(KeyEvent e) {
                keyCode = -1;
            }
        });

        jFrame.setVisible(true);
        while (true){
            engine.updateLogic();
            myGame.repaint(); //自动重绘
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                RenderingHints.VALUE_ANTIALIAS_ON);
        gameEngine.renderUI(g2d);
    }

    /**
     * 返回所按的按键码
     * @return int类型的按键码
     */
    public static int getCurrentKeyCode(){
        return keyCode;
    }

    /**
     * 游戏结束,结束JVM进程
     * @param message 退出时所显示的文字,例：游戏失败.
     */
    public static void GameOver(String message){
        JOptionPane.showMessageDialog(null,message);
        System.exit(0);
    }
}
