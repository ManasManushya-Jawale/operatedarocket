package com.example.operatedarocket.Background;

import org.jline.jansi.Ansi;
import org.springframework.stereotype.Component;

import com.example.operatedarocket.utils.Emails.NotificationService;

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
        System.out.println(
                Ansi.ansi()
                        .fgBlack()
                        .bg(Ansi.Color.WHITE)
                        .bold()
                        .a("Welcome " + System.getProperty("user.name") + " to OperateDaRocket!")
                        .a("\nType 'help' to see the list of commands available.")
                        .a("\nType 'start' to select your chapter and start the game.")
                        .reset());

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
    }
}
