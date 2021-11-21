package com.epam.training.ticketservice.core.configuration;

import static org.jline.utils.AttributedStyle.GREEN;
import static org.jline.utils.AttributedStyle.DEFAULT;

import org.jline.utils.AttributedString;
import org.springframework.shell.jline.PromptProvider;
import org.springframework.stereotype.Component;

@Component
public class PromptConfiguration implements PromptProvider {

    @Override
    public AttributedString getPrompt() {
        return new AttributedString("Ticket service> ", DEFAULT.foreground(GREEN));
    }
}
