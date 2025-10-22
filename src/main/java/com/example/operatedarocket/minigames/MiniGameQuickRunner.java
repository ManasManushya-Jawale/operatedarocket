package com.example.operatedarocket.minigames;

import java.io.IOException;
import java.util.Scanner;

import org.jline.nativ.JLineLibrary;
import org.jline.terminal.TerminalBuilder;
import org.springframework.context.ApplicationContext;

import com.example.operatedarocket.utils.minigame.loader.MiniGamesLoader;

/**
 * A Java Class that runs a minigame assigned to the given id
 */
public class MiniGameQuickRunner {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.print("Enter the minigame id: ");
        String id = sc.nextLine();
        System.out.println("Tuning " + id);
        System.out.println("Press any key to start");
        
        try {
            new MiniGamesLoader(TerminalBuilder.terminal()).runMiniGame(id);
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
