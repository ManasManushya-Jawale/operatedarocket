package com.example.operatedarocket.utils;

import java.util.Date;
import com.example.operatedarocket.utils.Emails.EmailReader;
import com.example.operatedarocket.utils.Saving.GameState;

public class UtilFuncs {
    private GameState gameState;

    public EmailReader eReader;

    public UtilFuncs() {
        gameState = GameState.loadGame();
        eReader = new EmailReader();
        eReader.initEmails();
    }

    public String getName() {
        return gameState.name;
    }

    public void setName(String name) {
        gameState.name = name;
        GameState.SaveGame(gameState);
    }

    public int getChaptersUnlocked() {
        return gameState.chaptersUnlocked;
    }

    public void setChaptersUnlocked(int chaptersUnlocked) {
        gameState.chaptersUnlocked = chaptersUnlocked;
        GameState.SaveGame(gameState);
    }

    public Chapters getCurrentChapter() {
        return gameState.currentChapter;
    }

    public void setCurrentChapter(Chapters currentChapter) {
        gameState.currentChapter = currentChapter;
        GameState.SaveGame(gameState);
    }

    public Date getDate() {
        return gameState.date;
    }

    public void setDate(Date date) {
        gameState.date = date;
        GameState.SaveGame(gameState);
    }

    public void updateGameState(String name,
            int chaptersUnlocked,
            Chapters currentChapter,
            Date date) {
        gameState = new GameState(name, chaptersUnlocked, currentChapter, date);
        GameState.SaveGame(gameState);
    }

    public static org.springframework.shell.component.view.control.BoxView BoxView(String title) {
        org.springframework.shell.component.view.control.BoxView bView = new org.springframework.shell.component.view.control.BoxView();
        bView.setTitle(title);
        return bView;
    }

    public GameState getGameState() {
        return gameState;
    }

}
