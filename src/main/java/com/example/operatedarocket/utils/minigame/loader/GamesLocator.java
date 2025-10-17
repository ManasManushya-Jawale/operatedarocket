package com.example.operatedarocket.utils.minigame.loader;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.reflections.Reflections;

import com.example.operatedarocket.utils.annotations.Game;
import com.example.operatedarocket.utils.annotations.GameAnnot;

public class GamesLocator {
    public static List<Class<? extends Game>> getGameInstances() {
        Reflections reflections = new Reflections("com.example.operatedarocket");
        Set<Class<? extends Game>> allGameClasses = reflections.getSubTypesOf(Game.class);

        List<Class<? extends Game>> result = new ArrayList<>();
        for (Class<? extends Game> clazz : allGameClasses) {
            if (clazz.isAnnotationPresent(GameAnnot.class)) {
                System.out.println("Appending: " + clazz.getTypeName());
                result.add(clazz);
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        return result;
    }
}
