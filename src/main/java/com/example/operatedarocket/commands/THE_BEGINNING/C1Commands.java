package com.example.operatedarocket.commands.THE_BEGINNING;

import static com.example.operatedarocket.OperateDaRocketApp.util;

import java.util.concurrent.CountDownLatch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.component.flow.ComponentFlow;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.example.operatedarocket.utils.Chapters;
import com.example.operatedarocket.utils.Emails.NotificationService;
import com.example.operatedarocket.utils.window.WebImageFrame;

@ShellComponent
public class C1Commands {

    @Autowired
    private ComponentFlow.Builder builder;

    @ShellMethod(key = "intro", value = "starts the game")
    public void inst() {
        try {
            if (util.getCurrentChapter() != Chapters.THE_BEGINNING) {
                System.out.println("Chapter: " + util.getCurrentChapter().getLabel());
                System.out.println("Command not available in this chapter.");
                return;
            }
        } catch (Exception e) {
            System.out.println("Please initialize the chapter before intro");
        }
        System.out.println("""
                Date - 25 June 2050
                 _   _     _     ____      _
                | \\ | |   / \\   / ___|    / \\
                |  \\| |  / _ \\  \\___ \\   / _ \\
                | |\\  | / ___ \\  ___) | / ___ \\
                |_| \\_|/_/   \\_\\|____/ /_/   \\_\\


                National Aeronautics and
                  Space Administration

                Hi                       Astronout!
                We have planning for a new mission,
                this  mission  will  try  its  best
                to     go     intestaller    travel
                to    start    your    rejistration
                type          command           rej
                \s""");
    }

    @ShellMethod(key = "rej", value = "register yourself to the space program")
    public void rej() {
        if (util.getCurrentChapter() != Chapters.THE_BEGINNING) {
            return;
        }

        ComponentFlow flow = builder.clone().reset()
                .withStringInput("name")
                .name("Enter Your Name: ")
                .defaultValue(System.getProperty("user.name"))
                .and()
                .withConfirmationInput("proceed")
                .name("Do you proceed? ")
                .and()
                .build();

        ComponentFlow.ComponentFlowResult cfr = flow.run();

        String name = cfr.getContext().get("name");
        boolean proceed = cfr.getContext().get("proceed");
        System.out.println(name + " " + proceed);

        if (proceed) {
            System.out.printf("Welcome\033[%dC%s\n", (68 - name.length() - 7), name);
            System.out.println("""
                    Your            rejistration             was              successful
                    The selections   have   started   and   to    test    your    skills
                    We       have         arranged        a        computer         game
                    So   that   we   will   get   real   astronouts   and   not  donkeys
                    Type   'mgame 456e7472616e636554657374'   to    proceed    for  test
                    This    command    might    sound    complex    but  has  a  meaning
                    """);
            util.setName(name);
        } else {
            System.out.println("""
                    Ok    Astronout,    It    was
                    your's    choice    at    all
                    """);

            System.exit(0);
        }
    }

    @ShellMethod(key = "choice 4c6976654f72446965", value = "***********")
    public void choiceOfLife() {
        if (util.getCurrentChapter() != Chapters.THE_BEGINNING)
            return;
        System.out.println("8g");
        ComponentFlow flow = builder.clone().reset()
                .withConfirmationInput("confirmation")
                .name("Do you really confirm to resign from intestaller travel project?")
                .and()
                .build();

        Boolean confirm = flow.run().getContext().get("confirmation");
        while (confirm) {
            confirm = flow.run().getContext().get("confirmation");
        }
        if (!confirm) {
            System.out.println("""
                    Thank you for not conforming  resignation
                    Don't believe in spam or suspicies emails
                    We  will  get  the space ships ready upto
                    24  July  2155  Be  patient and be humble
                    Type     'time-travel 32342d372d32313535'
                    """);

            NotificationService.notify("Resignation Attempt",
                    "Astronout " + util.getName()
                            + " attempted to resign from the intestaller travel project but reconsidered.");

            NotificationService.notifyEmail("d75d1256-84dc-44f5-b2c3-e2e14d133b93");
        }
    }

    @ShellMethod("Gets an image from the internet authorized to you")
    public void webImage(@ShellOption() String url) {
        if (util.getCurrentChapter() != Chapters.THE_BEGINNING)
            return;

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < url.length(); i += 2) {
            String str = url.substring(i, i + 2);
            sb.append((char) Integer.parseInt(str, 16));
        }
        System.out.println(sb.toString());

        CountDownLatch latch = new CountDownLatch(1);
        WebImageFrame frame = new WebImageFrame("Image Viewer", sb.toString());
        new Thread(() -> {
            try {
                frame.addWindowListener(new java.awt.event.WindowAdapter() {
                    @Override
                    public void windowClosing(java.awt.event.WindowEvent windowEvent) {
                        latch.countDown();
                    }
                });
                latch.await(); // Wait until the frame is closed
            } catch (Exception e) {
                System.out.println("Error displaying image: " + e.getMessage());
            } finally {
                latch.countDown();
            }
        }).start();
    }

}
