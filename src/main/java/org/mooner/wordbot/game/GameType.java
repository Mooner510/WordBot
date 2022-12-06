package org.mooner.wordbot.game;

public enum GameType {
    LETTER_5("5과 단어"),
    MEANS_5("5과 단어 뜻"),
    LETTER_6("6과 단어"),
    MEANS_6("6과 단어 뜻"),
    LETTER_7("7과 단어"),
    MEANS_7("7과 단어 뜻"),
    LETTER_8("8과 단어"),
    MEANS_8("8과 단어 뜻")
    ;

    private final String tag;

    GameType(String s) {
        this.tag = s;
    }

    public static GameType fromString(String key) {
        for (GameType value : values()) {
            if(value.getTag().equals(key)) return value;
        }
        return null;
    }

    public String getTag() {
        return tag;
    }
}
