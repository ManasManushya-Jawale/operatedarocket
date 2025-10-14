package com.example.operatedarocket.commands;

import static com.example.operatedarocket.OperateDaRocketApp.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.component.view.TerminalUI;
import org.springframework.shell.component.view.TerminalUIBuilder;
import org.springframework.shell.component.view.control.BoxView;
import org.springframework.shell.component.view.screen.Screen.Writer;
import org.springframework.shell.geom.HorizontalAlign;
import org.springframework.shell.geom.Rectangle;
import org.springframework.shell.geom.VerticalAlign;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.example.operatedarocket.utils.Emails.Email;
import com.example.operatedarocket.utils.Emails.EmailReader;

@ShellComponent
public class DefaultCommands {
    @Autowired
    TerminalUIBuilder builder;

    @ShellMethod("Open inbox panel")
    public void inbox() {
        if (builder == null) {
            System.err.println("TerminalUIBuilder is not initialized.");
            return;
        }

        try {
            TerminalUI ui = builder.build();

            // Safely build inbox text
            StringBuilder inboxTextBuilder = new StringBuilder();
            List<Email> emails = EmailReader.getEmails();
            ArrayList<String> messages = new ArrayList<>();

            if (emails != null && !emails.isEmpty()) {
                for (Email email : emails) {
                    if (email != null) {
                        messages.add("Email from " + email.id + " - " + email.title);
                    }
                }
            } else {
                inboxTextBuilder.append("No emails found.\n");
            }

            // Main content panel
            BoxView main = new BoxView();
            main.setRect(2, 2, 50, 50);
            main.setShowBorder(true);
            main.setDrawFunction((screen, rect) -> {
                Rectangle safeRect = new Rectangle(
                        rect.x() + 2,
                        rect.y() + 1,
                        Math.max(0, rect.width() - 4),
                        Math.max(0, rect.height() - 2)); 

                Writer writer = screen.writerBuilder().build();
                for (String string : messages) {
                    writer.text(string + "\n", safeRect, HorizontalAlign.LEFT, VerticalAlign.TOP);
                }
                return rect;
            });

            // Launch UI
            ui.setRoot(main, false);
            ui.run(); // blocks until exit

        } catch (Exception e) {
            System.err.println("Error launching inbox panel: " + e.getMessage());
        }
    }

    @ShellMethod("Time travel to a specific date")
    public void timeTravel(@ShellOption() String dateStr) {
        if (dateStr == null || dateStr.length() < 2) {
            System.err.println("Invalid input: date string too short.");
            return;
        }

        StringBuilder hexStr = new StringBuilder();
        try {
            for (int i = 0; i < dateStr.length(); i += 2) {
                if (i + 2 <= dateStr.length()) {
                    String str = dateStr.substring(i, i + 2);
                    hexStr.append((char) Integer.parseInt(str, 16));
                }
            }

            String decoded = hexStr.toString();
            System.out.println("Decoded date string: " + decoded);

            SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy");
            Date date = format.parse(decoded);
            util.setDate(date);

        } catch (NumberFormatException | ParseException e) {
            System.err.println("Failed to parse date: " + e.getMessage());
        }
    }
}
