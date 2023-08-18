package com.example.demo.common.game;

import java.awt.*;

public class Grid {
    int x1;
    int y1;
    int x2;
    int y2;

    public void draw(Graphics2D g2d) {
        if (Config.DEBUG_FLAG == true) {
            for (int i = 0; i < Config.ROW; i ++ ) {
                g2d.drawLine(0, Config.GRID_SiZE * i, Config.SCREEN_WIDTH,
                        Config.GRID_SiZE * i);
            }
            for (int i = 0; i < Config.COLOUM; i ++ ) {
                g2d.drawLine(Config.GRID_SiZE * i, 0, Config.GRID_SiZE * i,
                        Config.SCREEN_HEIGHT);
            }
        }
    }
}

