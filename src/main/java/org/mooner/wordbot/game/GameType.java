package org.mooner.wordbot.game;

import org.jetbrains.annotations.Nullable;

public enum GameType {
//    TEST_2025_2_1("2025/2-1.yml", "2학년 ???"),
    TEST_Dummy("2025/other.yml", "영단어 연습")
    ;

    private final String key;
    private final String tag;

    GameType(String key, String tag) {
        this.key = key;
        this.tag = tag;
    }

    public String getKey() {
        return key;
    }

    public String getTag() {
        return tag;
    }

    @Nullable
    public static GameType fromKey(String key) {
        for (GameType t : GameType.values()) {
            if (t.key.equals(key)) {
                return t;
            }
        }
        return null;
    }
}
