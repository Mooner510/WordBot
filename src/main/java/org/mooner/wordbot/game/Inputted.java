package org.mooner.wordbot.game;

import net.dv8tion.jda.api.entities.User;
import org.mooner.wordbot.Main;

public class Inputted {
    private final long user;
    private final String text;

    public Inputted(long user, String text) {
        this.user = user;
        this.text = text;
    }

    @Override
    public String toString() {
        User user = Main.jda.getUserById(this.user);
        return user.getName() + ", " + text;
    }
}
