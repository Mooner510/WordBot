package org.mooner.wordbot.data;

import org.mooner.wordbot.UpdateManager;
import org.mooner.wordbot.game.GameType;

import java.util.HashMap;
import java.util.List;

public class GameResource {
    private final HashMap<String, List<String>> letters;
    private final HashMap<String, List<String>> means;

    private final GameType type;

    public GameResource(GameType type) {
        this.letters = new HashMap<>();
        this.means = new HashMap<>();
        this.type = type;

        UpdateManager.update(type, letters, means);
    }

    public HashMap<String, List<String>> getLetters() {
        return letters;
    }

    public HashMap<String, List<String>> getMeans() {
        return means;
    }

    public boolean isType(GameType gameType) {
        return this.type == gameType;
    }
}
