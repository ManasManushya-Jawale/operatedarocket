package com.example.operatedarocket.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.component.StringInput;
import org.springframework.shell.component.flow.ComponentFlow;
import org.springframework.shell.component.flow.SelectItem;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import org.springframework.shell.standard.ShellOption;

import com.example.operatedarocket.utils.Chapters;

import static com.example.operatedarocket.OperateDaRocketApp.util;
import static com.example.operatedarocket.utils.UtilFuncs.*;
import static org.jline.jansi.AnsiConsole.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@ShellComponent
public class StartingCommand {
    
    @Autowired
    public ComponentFlow.Builder builder;


    @ShellMethod(key="start", value = "start by selecting a chapter")
    public void start()
    {
        List<SelectItem> chapterItems = new ArrayList<>();
        for (Chapters chap : Chapters.values()) {
            chapterItems.add(SelectItem.of(chap.getLabel(), chap.getLabel()));
        }
        ComponentFlow flow = builder.clone().reset()
                .withSingleItemSelector("chapter")
                .name("Select Chapter")
                .selectItems(chapterItems)
                .and()
                .build();

        String chap = ((String)flow.run().getContext().get("chapter"));

        for (Chapters chapters : Chapters.values()) {
            if (chapters.getLabel() == chap) {
                System.out.println(chapters.getDesc());
                util.setCurrentChapter(chapters);
                break;
            }
        }
    }

}
