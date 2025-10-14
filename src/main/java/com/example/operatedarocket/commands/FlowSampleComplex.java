package com.example.operatedarocket.commands;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.shell.component.flow.ComponentFlow;
import org.springframework.shell.component.flow.SelectItem;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;

import java.util.Arrays;
import java.util.List;

@ShellComponent
public class FlowSampleComplex {

    @Autowired
    private ComponentFlow.Builder componentFlowBuilder;

    @ShellMethod(key = "flow", value = "runs a sample complex flow")
    public void runFlow() {
        List<SelectItem> single1SelectItems = Arrays.asList(SelectItem.of("key1", "value1"),
                SelectItem.of("key2", "value2"));
        List<SelectItem> multi1SelectItems = Arrays.asList(SelectItem.of("key1", "value1"),
                SelectItem.of("key2", "value2"), SelectItem.of("key3", "value3"));
        componentFlowBuilder.clone().reset()
                .withStringInput("field1")
                .name("Field1")
                .defaultValue("defaultField1Value")
                .and()
                .withStringInput("field2")
                .name("Field2")
                .and()
                .withConfirmationInput("confirmation1")
                .name("Confirmation1")
                .and()
                .withPathInput("path1")
                .name("Path1")
                .and()
                .withSingleItemSelector("single1")
                .name("Single1")
                .selectItems(single1SelectItems)
                .and()
                .withMultiItemSelector("multi1")
                .name("Multi1")
                .selectItems(multi1SelectItems)
                .and()
                .build()
                .run();
    }

}