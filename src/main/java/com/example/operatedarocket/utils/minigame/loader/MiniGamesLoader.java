package com.example.operatedarocket.utils.minigame.loader;

import java.util.List;

import org.jline.terminal.Terminal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.example.operatedarocket.utils.annotations.Game;
import com.example.operatedarocket.utils.annotations.GameAnnot;

@ShellComponent
public class MiniGamesLoader {

    private final Terminal terminal;

    @Autowired
    public MiniGamesLoader(Terminal terminal) {
        this.terminal = terminal;
    }

    @ShellMethod(key = "mgame", value = "runs a mini game")
    public void runMiniGame(@ShellOption String id) {
        List<Class<? extends Game>> games = GamesLocator.getGameInstances();
        for (Class<? extends Game> game : games) {
            GameAnnot annot = game.getAnnotation(GameAnnot.class);
            if (isEqual(annot.id(), id)) {
                try {
                    Game instance = game.getDeclaredConstructor().newInstance();
                    instance.run(terminal);
                } catch (Exception e) {
                    System.out.println("Unable to run");
                    e.printStackTrace(); // or use a logger
                }
            }
        }
    }

    private boolean isEqual(String a, String b) {
        if (a.length() != b.length())
            return false;
        boolean res = true;
        for (int i = 0; i < Math.min(a.length(), b.length()); i++) {
            res &= a.charAt(i) == b.charAt(i);
        }
        return res;
    }
}
