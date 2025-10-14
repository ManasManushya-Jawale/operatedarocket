package com.example.operatedarocket.utils.Saving;

import static com.example.operatedarocket.OperateDaRocketApp.util;

import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

@ShellComponent
public class GameInfoCommands {
    @ShellMethod
    public void showGameInfo() {
        GameState gameState = util.getGameState();
        System.out.println("Player Name: " + gameState.name);
        System.out.println("Chapters Unlocked: " + gameState.chaptersUnlocked);
        System.out.println("Current Chapter: " + gameState.currentChapter);
        System.out.println("Date: " + gameState.date.toString());
    }
}
