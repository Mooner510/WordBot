package org.mooner.wordbot.data;

import org.mooner.wordbot.UpdateManager;
import org.mooner.wordbot.game.GameType;

import java.util.HashMap;
import java.util.List;

public class GameResource {
    private final HashMap<String, List<String>> letters;
    private final HashMap<String, List<String>> means;

    private final GameType letterType;
    private final GameType meanType;

    public GameResource(GameType letterType, GameType meanType) {
        this.letters = new HashMap<>();
        this.means = new HashMap<>();
        this.letterType = letterType;
        this.meanType = meanType;

        String s = letterType.toString();
        UpdateManager.update(Integer.parseInt(s.split("_")[1]), s.charAt(s.length() - 1), letters, means);
    }

    public HashMap<String, List<String>> getLetters() {
        return letters;
    }

    public HashMap<String, List<String>> getMeans() {
        return means;
    }

    public boolean isType(GameType gameType) {
        return letterType == gameType || meanType == gameType;
    }
}
