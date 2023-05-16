package org.mooner.wordbot.game;

public enum GameType {
    LETTER_2023_2("2023년 2과 단어"),
    MEANS_2023_2("2023년 2과 단어 뜻", true),
    LETTER_2023_3("2023년 3과 단어"),
    MEANS_2023_3("2023년 3과 단어 뜻", true),
    LETTER_2022_5("2022년 5과 단어"),
    MEANS_2022_5("2022년 5과 단어 뜻", true),
    LETTER_2022_6("2022년 6과 단어"),
    MEANS_2022_6("2022년 6과 단어 뜻", true),
    LETTER_2022_7("2022년 7과 단어"),
    MEANS_2022_7("2022년 7과 단어 뜻", true),
    LETTER_2022_8("2022년 8과 단어"),
    MEANS_2022_8("2022년 8과 단어 뜻", true)
    ;

    private final String tag;
    private final boolean meanType;

    GameType(String s, boolean meanType) {
        this.tag = s;
        this.meanType = meanType;
    }

    GameType(String s) {
        this.tag = s;
        this.meanType = false;
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

    public boolean isMeanType() {
        return meanType;
    }
}
