package org.mooner.wordbot.cmd;

import net.dv8tion.jda.api.events.interaction.component.ButtonInteractionEvent;

public interface BotButton {
    void onClick(ButtonInteractionEvent event, String[] arguments);
}
