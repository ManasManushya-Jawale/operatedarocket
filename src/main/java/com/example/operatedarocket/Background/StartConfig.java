package com.example.operatedarocket.Background;

import javax.swing.JDialog;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.plaf.nimbus.NimbusLookAndFeel;

import org.jline.jansi.Ansi;
import org.springframework.stereotype.Component;

import com.example.operatedarocket.utils.Emails.NotificationService;
import com.example.operatedarocket.utils.minigame.MGameUtils;

import jakarta.annotation.PostConstruct;

@Component
public class StartConfig {
    @PostConstruct
    public void init() {
        System.out.println("StartConfig initialized");
        System.out.println("Java Class Path:\t" + System.getProperty("java.class.path"));
        System.out.println("OS Name:\t\t" + System.getProperty("os.name"));
        System.out.println("OS Arch:\t\t" + System.getProperty("os.arch"));
        System.out.println("OS Version:\t\t" + System.getProperty("os.version"));
        System.out.println("Username:\t\t" + System.getProperty("user.name"));
        System.out.println("Userhome:\t\t" + System.getProperty("user.home"));
        System.out.println("UserDir:\t\t" + System.getProperty("user.dir"));

        System.out.println("\n" + Ansi
                .ansi()
                .fgRed()
                .a("[WARNING]")
                .fgDefault()
                .a(" - the following game is in its alpha version so there \'are\' or \'may\' errors in this game.")
                .reset());

        System.out.println(Ansi
                .ansi()
                .fgBrightGreen()
                 .a("If your brain cells die playing this game, its ")
                .fgRed()
                .a("OK\n")
                .fgDefault()
                .a("I almost lost all of them making this game."));

        try {
            UIManager.setLookAndFeel(NimbusLookAndFeel.class.getName());
        } catch (Exception e) {
            System.out.println("Nimubus L&F not found, using default L&F");
        }

        System.out.println(
            Ansi
            .ansi()
            .fgGreen()
            .a("Type ")
            .fgBrightCyan()
            .a("\"exit\" ")
            .fgGreen()
            .a("to quit the game\n")
            .a("Type ")
            .fgBrightCyan()
            .a("\"start\" ")
            .fgGreen()
            .a("to start the game\n")
            .fgGreen()
            .a("Type ")
            .fgBrightCyan()
            .a("\"help\" ")
            .fgGreen()
            .a("to get further help about the game\n")
            .reset()
        );
    }
}
