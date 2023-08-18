package com.example.demo.common.game;

public class SnakeGame {
    public static void main(String[] args) {
        MyGame.init("贪吃蛇", Config.SCREEN_WIDTH, Config.SCREEN_HEIGHT, new SnakeGameEngine());
    }
}
