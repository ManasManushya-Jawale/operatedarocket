package com.example.operatedarocket.utils.minigame.assets;

import com.example.operatedarocket.utils.minigame.MGameUtils;

public class Sprite {
    public int x, y;
    public char icon;

    public Sprite(int x, int y, char icon) {
        this.x = x;
        this.y = y;
        this.icon = icon;
    }

    public String draw() {
        return "\033[" + y + ";" + x + "H" + icon;
    }
}
