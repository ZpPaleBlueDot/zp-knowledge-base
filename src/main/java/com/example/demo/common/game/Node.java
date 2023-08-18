package com.example.demo.common.game;

import java.awt.*;

public class Node {
    int x;
    int y;

    public Node(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void draw(Graphics2D g2d) {
        g2d.fillRect(x * Config.GRID_SiZE, y * Config.GRID_SiZE, Config.GRID_SiZE,
                Config.GRID_SiZE);
    }
}

