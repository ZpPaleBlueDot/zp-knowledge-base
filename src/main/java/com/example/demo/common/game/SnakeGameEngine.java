package com.example.demo.common.game;

import java.awt.*;
import java.awt.event.KeyEvent;

public class SnakeGameEngine extends GameEngine {
    Grid grid = new Grid();
    Snake snake = new Snake();
    Apple apple = new Apple();

    @Override
    public void updateLogic() {
        boolean flag = snake.headHaveApple(apple);
        int keyCode = getCurrentPressedKeyCode();
        if (keyCode == KeyEvent.VK_RIGHT) {
            snake.moveRight(flag);
        }
        if (keyCode == KeyEvent.VK_LEFT) {
            snake.moveLeft(flag);
        }
        if (keyCode == KeyEvent.VK_UP) {
            snake.moveUp(flag);
        }
        if (keyCode == KeyEvent.VK_DOWN) {
            snake.moveDown(flag);
        }

        switch(snake.headDirection) {
            case Direction.RIGHT:
                snake.moveRight(flag);
                break;
            case Direction.LEFT:
                snake.moveLeft(flag);
                break;
            case Direction.UP:
                snake.moveUp(flag);
                break;
            case Direction.DOWN:
                snake.moveDown(flag);
                break;
        }
    }

    @Override
    public void renderUI(Graphics2D g2d) {
        grid.draw(g2d);
        snake.draw(g2d);
        apple.draw(g2d);
    }
}

