package org.mooner.wordbot.cmd.selects;

import net.dv8tion.jda.api.events.interaction.component.SelectMenuInteractionEvent;
import org.mooner.wordbot.cmd.BotSelectMenu;

public class RiceSelects implements BotSelectMenu {
    @Override
    public void onSelect(SelectMenuInteractionEvent event) {
        if ((event.getInteraction().getSelectedOptions().size() <= 0)) return;
    }
}
