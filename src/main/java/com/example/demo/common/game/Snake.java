package com.example.demo.common.game;

import java.awt.*;
import java.util.LinkedList;

public class Snake {
    LinkedList<Node> snake;
    int headDirection = Direction.RIGHT;

    public Snake() {
        snake = new LinkedList<Node>();
        snake.add(new Node(6, 4));
        snake.addLast(new Node(5, 4));
        snake.addLast(new Node(4, 4));
        snake.addLast(new Node(3, 4));
    }

    public void moveRight(boolean flag) {
        if (headDirection == Direction.LEFT) {
            return;
        }
        headDirection = Direction.RIGHT;

        Node node = new Node(snake.getFirst().x + 1, snake.getFirst().y);
        snake.addFirst(node);
        if (flag == true) {

        } else {
            snake.removeLast();
        }
    }

    public void moveLeft(boolean flag) {
        if (headDirection == Direction.RIGHT) {
            return;
        }
        headDirection = Direction.LEFT;

        Node node = new Node(snake.getFirst().x - 1, snake.getFirst().y);
        snake.addFirst(node);
        if (flag == true) {

        } else {
            snake.removeLast();
        }
    }

    public void moveUp(boolean flag) {
        if (headDirection == Direction.DOWN) {
            return;
        }
        headDirection = Direction.UP;

        Node node = new Node(snake.getFirst().x, snake.getFirst().y - 1);
        snake.addFirst(node);
        if (flag == true) {

        } else {
            snake.removeLast();
        }
    }

    public void moveDown(boolean flag) {
        if (headDirection == Direction.UP) {
            return;
        }
        headDirection = Direction.DOWN;

        Node node = new Node(snake.getFirst().x, snake.getFirst().y + 1);
        snake.addFirst(node);
        if (flag == true) {

        } else {
            snake.removeLast();
        }
    }

    public boolean headHaveApple(Apple apple) {
        if ((snake.getFirst().x == apple.getX()) && (snake.getFirst().y == apple.getY())) {
            apple.generateNext();
            return true;
        } else {
            return false;
        }
    }


    public void draw(Graphics2D g2d) {
//        for (Node node : snake) {
//            node.draw(g2d);
//        }
        g2d.drawString("蛇的长度:" + snake.size(), 50, 50);
        for (int i = 0; i < snake.size(); i ++ ) {
            if (i == 0) {
                g2d.setColor(Color.GREEN);

            } else {
                g2d.setColor(Color.BLACK);
            }

            snake.get(i).draw(g2d);
        }
    }
}

