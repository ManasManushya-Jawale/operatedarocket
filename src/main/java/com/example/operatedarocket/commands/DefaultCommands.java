package com.example.operatedarocket.commands;

import static com.example.operatedarocket.OperateDaRocketApp.util;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.component.flow.ComponentFlow;
import org.springframework.shell.component.flow.SelectItem;
import org.springframework.shell.component.flow.ComponentFlow.ComponentFlowResult;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.example.operatedarocket.utils.Emails.Email;
import com.example.operatedarocket.utils.Emails.EmailReader;
import com.example.operatedarocket.utils.minigame.MGameUtils;

@ShellComponent
public class DefaultCommands {
    @Autowired
    ComponentFlow.Builder builder;

    @ShellMethod("Open inbox panel")
    public void inbox() {
        if (builder == null) {
            System.err.println("TerminalUIBuilder is not initialized.");
            return;
        }
        List<Email> emails = EmailReader.getEmails();
        List<SelectItem> emailItems = new ArrayList<>();
        for (int i = 0; i < emails.size(); i++) {
            Email email = emails.get(i);
            emailItems.add(SelectItem.of(email.id + " " + email.title, Integer.toString(email.hashCode())));
        }
        ComponentFlow flow = builder.clone().reset()
        .withSingleItemSelector("email")
        .selectItems(emailItems)
        .name("Select the wanted inbox")
        .and()
        .build();

        ComponentFlowResult cfr = flow.run();
        int hash = Integer.valueOf(cfr.getContext().get("email"));
        Email result;

        for (Email email : emails) {
            if (email.hashCode() == hash) {
                String body = email.body;
                result = email;
                body = body.replaceAll("\\[(\\d{1,3})\\]", "\u001B[$1m");

                String[] logs = {"Finding hashcode...", "Looking for color codes...", "Looking for *&#@%!..."};
                System.out.println("\u001B[32m");
                for (String log : logs) {
                    for (int i = 0; i < log.length(); i++) {
                        System.out.print(log.charAt(i));
                        MGameUtils.delay(10);
                    }
                    System.out.println();
                }
                for (int i = 0; i < body.length(); i++) {
                    System.out.print(body.charAt(i));
                    int delay = 20;
                    switch (body.charAt(i)) {
                        case '.' -> delay = 100;
                        case ',' -> delay = 50;
                        case '\n' -> delay = 200;
                    }
                    MGameUtils.delay(delay);
                }
            }
        }
        System.out.println();
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
