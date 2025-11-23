package operatedarocket.util.PlayerData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class PlayerDataSaverAndReader {

    public static final String path = System.getProperty("user.home") +
            "/.operateDaRocket/player.json";

    public static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

    public static void save(PlayerData player) {
        try {
            File file = new File(path);
            File parent = file.getParentFile();

            if (parent != null && !parent.exists()) {
                boolean created = parent.mkdirs();
                System.out.println("Created folder: " + created);
            }

            try (FileWriter writer = new FileWriter(file)) {
                gson.toJson(player, writer);
                System.out.println("Saved to " + path);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static PlayerData load() {
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("Save file not found, creating new player...");
            PlayerData p = new PlayerData();
            save(p);
            return p;
        }

        try (FileReader reader = new FileReader(file)) {
            return gson.fromJson(reader, PlayerData.class);

        } catch (Exception e) {
            e.printStackTrace();
            // If corrupted JSON, return a fresh player instead of crashing
            return new PlayerData();
        }
    }
}
