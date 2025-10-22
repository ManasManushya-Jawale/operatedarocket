package com.example.operatedarocket.utils;

import org.jline.utils.AttributedString;
import org.jline.utils.AttributedStyle;
import org.springframework.context.annotation.Bean;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class PromptServer {
    @Bean
    public PromptProvider myPromptProvider() {
        return () -> new AttributedString("lotusos$ ",
                AttributedStyle.DEFAULT.foreground(AttributedStyle.MAGENTA));
    }

}
