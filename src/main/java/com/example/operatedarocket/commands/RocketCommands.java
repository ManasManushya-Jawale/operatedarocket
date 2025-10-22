package com.example.operatedarocket.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.component.flow.ComponentFlow;
import org.springframework.shell.component.flow.SelectItem;
import org.springframework.shell.standard.ShellComponent;

import java.util.ArrayList;
import java.util.List;

@ShellComponent
public class RocketCommands {
    @Autowired
    private static ComponentFlow.Builder componentFlowBuilder;

    public static String navOptions(String[] bodies) {
        List<SelectItem> bodiesSelect = new ArrayList<>();
        for (String body : bodies) {
            bodiesSelect.add(SelectItem.of(body, body));
        }
        ComponentFlow flow = componentFlowBuilder
                .withSingleItemSelector("multi1")
                .selectItems(bodiesSelect)
                .name("Navigate to:")
                .and()
                .build();

        ComponentFlow.ComponentFlowResult result = flow.run();
        return result.getContext().get("multi1");
    }
}
