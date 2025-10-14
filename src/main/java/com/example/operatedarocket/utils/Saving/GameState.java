package com.example.operatedarocket.utils.Saving;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import com.example.operatedarocket.utils.Chapters;

public class GameState implements Serializable {
    public String name;
    public int chaptersUnlocked = 1;
    public Chapters currentChapter;
    public Date date;

    public GameState(String name, int chaptersUnlocked, Chapters currentChapter, Date date) {
        this.name = name;
        this.chaptersUnlocked = chaptersUnlocked;
        this.currentChapter = currentChapter;
        this.date = date;
    }

    private static final String SAVE_FOLDER = "saves";
    private static final String SAVE_FILE_NAME = "savegame.dat";

    private static File getSaveFile() {
        File saveDir = new File(SAVE_FOLDER);
        if (!saveDir.exists()) {
            saveDir.mkdirs(); // Create saves/ folder if it doesn't exist
            File file = new File(saveDir, "savegame.dat");
            try {
                SaveGame(new GameState(
                        "XXXmanushyaXXX",
                        1,
                        Chapters.THE_BEGINNING,
                        new SimpleDateFormat("dd-MM-yyyy")
                                .parse("25-06-1150")));
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return new File(saveDir, SAVE_FILE_NAME);
    }

    public static void SaveGame(GameState gameState) {
        File saveFile = getSaveFile();
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(saveFile))) {
            out.writeObject(gameState);
            System.out.println("Game saved to: " + saveFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Failed to save game:");
            e.printStackTrace();
        }
    }

    public static GameState loadGame() {
        File saveFile = getSaveFile();
        if (!saveFile.exists()) {
            System.err.println("No save file found at: " + saveFile.getAbsolutePath());
            return null;
        }

        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(saveFile))) {
            return (GameState) in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }
}
