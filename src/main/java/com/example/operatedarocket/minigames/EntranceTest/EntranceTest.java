package com.example.operatedarocket.minigames.EntranceTest;

import java.awt.Point;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import org.jline.terminal.Terminal;
import org.jline.utils.NonBlockingReader;

import com.example.operatedarocket.utils.Emails.NotificationService;
import com.example.operatedarocket.utils.annotations.*;
import com.example.operatedarocket.utils.minigame.MGameUtils;

import static com.example.operatedarocket.OperateDaRocketApp.util;

@GameAnnot(id = "456e7472616e636554657374")
public class EntranceTest implements Game {

    boolean running = true;
    long lastFrame = System.nanoTime();
    long frameDuration = 33_000_000; // ~30 FPS
    long now = System.nanoTime();

    int prob = 10;

    Point rocket = new Point(10, 20);
    ArrayList<Point> asteroids = new ArrayList<>();

    Random random = new Random();

    int score = 0;

    @Override
    public void run(Terminal terminal) {
        System.out.println("Hi");

        terminal.enterRawMode();
        terminal.writer().print("\033[?25l"); // Hide cursor
        terminal.flush();

        NonBlockingReader reader = terminal.reader(); // Create once

        while (running) {
            now = System.nanoTime();

            if (now - lastFrame >= frameDuration) {
                draw(terminal);
                terminal.flush();
                lastFrame = now;
            }

            input(reader);
            update(terminal);
        }

        terminal.writer().print("\033[?25h"); // Show cursor again
        terminal.flush();
    }

    public void update(Terminal terminal) {
        for (int x = 0; x < 20; x++) {
            boolean spawn = random.nextInt(prob + 1) == prob;
            if (spawn) {
                asteroids.add(new Point(random.nextInt(20), 1));
            }
        }

        for (Point point : asteroids) {
            if (point.equals(rocket)) {
                running = false;
                terminal.puts(org.jline.utils.InfoCmp.Capability.clear_screen);
                MGameUtils.move(0, 0);
                System.out.println("""
                        You         played          really         did        well
                        Results   will   be   showen   in   a   matter   of   time
                        Pls       do       not       close       the  command line
                        """);
                MGameUtils.delay(2500);
                System.out.println("""
                        Congratulation🎉   You    Scored   1st    In    the   test
                        You are the only one among 100,000 astronouts participated
                        """);

                NotificationService.notify("Entrance Test Passed",
                        "Astronout " + util.getName() + " has successfully passed the entrance test with a score of "
                                + score + ".");

                MGameUtils.delay(1000);
                NotificationService.notifyEmail("fafc66cd-e36b-4551-9456-42005e513ccf");
            } else {
                point.y += 1;
            }
        }

        score++;
    }

    public void draw(Terminal terminal) {
        terminal.puts(org.jline.utils.InfoCmp.Capability.clear_screen);
        MGameUtils.printAt(terminal, new Point(0, 0), "Frames " + now);
        MGameUtils.printAt(terminal, rocket, "@");

        for (Point point : asteroids) {
            MGameUtils.printAt(terminal, point, "*");
        }
        lastFrame = now;
    }

    public void input(NonBlockingReader reader) {

        int key;
        try {
            key = reader.read();
            switch (key) {
                case 'w':
                    rocket.y = Math.max(1, rocket.y - 1);
                    break;
                case 's':
                    rocket.y = Math.min(20, rocket.y + 1);
                    break;
                case 'a':
                    rocket.x = Math.max(1, rocket.x - 1);
                    break;
                case 'd':
                    rocket.x = Math.min(40, rocket.x + 1);
                    break;
                default:
                    break;
            }
            if (key == 'q') {
                running = false;
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // non-blocking key read
    }
}