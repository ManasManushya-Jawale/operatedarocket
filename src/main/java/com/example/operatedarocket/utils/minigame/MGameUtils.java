package com.example.operatedarocket.utils.minigame;

import java.awt.Point;

import org.jline.terminal.Terminal;

public class MGameUtils {
    public enum Direction {
        UP('A'), DOWN('B'), LEFT('D'), RIGHT('C');

        private char code;

        Direction(char code) {
            this.code = code;
        }

        public char getDirection() {
            return this.code;
        }
    }

    public static void clearScreen() {
        System.out.print("\033[2J\033[0;0H");
        System.out.flush();
    }

    public static void move(int n, Direction direction) {
        System.out.printf("\033[%d%c", n, direction.getDirection());
    }

    public static void move(int x, int y) {
        System.out.printf("\033[%d;%dH", x, y);
    }

    public static void delay(long mls) {
        try {
            Thread.sleep(mls);
        } catch (Exception e) { }
    }

    public static void printAt(Terminal terminal, Point pos, String str) {
        terminal.writer().printf("\033[%d;%dH%s", pos.y, pos.x, str);
    }

}
