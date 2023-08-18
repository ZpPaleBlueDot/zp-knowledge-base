package com.example.demo.common.game;

import java.awt.*;
import java.util.Random;

public class Apple {
    Random random = new Random();
    Node apple;

    public Apple() {
        apple = new Node(random.nextInt(Config.COLOUM), random.nextInt(Config.ROW));
    }

    public int getX() {
        return apple.x;
    }

    public int getY() {
        return apple.y;
    }

    public void draw(Graphics2D g2d) {
        g2d.setColor(Color.RED);
        apple.draw(g2d);
        g2d.setColor(Color.BLACK);
    }

    public void generateNext() {
        apple = new Node(random.nextInt(Config.COLOUM), random.nextInt(Config.ROW));
    }
}

