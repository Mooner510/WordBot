package org.mooner.wordbot.game;

import net.dv8tion.jda.api.entities.Message;
import org.mooner.wordbot.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

public class BattleGame {
    private final List<String> questions;
    private final List<String> inputs;
    private final List<Boolean> corrects;

    private final GameType type;

    private final long id, channel;

    private String leastQuestion;

    private int index;
    private HashMap<Long, Integer> score;
    private HashMap<Long, Integer> perfect;
    private HashMap<Long, Integer> combo;
    private HashMap<Long, Integer> maxCombo;
    private final int size;

    private Message lastMessage;

    public BattleGame(long id, long channel, GameType type, List<String> questions) {
        this.id = id;
        this.channel = channel;
        this.type = type;
        this.questions = questions;
        corrects = new ArrayList<>();
        inputs = new ArrayList<>();
        index = -1;
        this.size = questions.size();
    }

    public long getChannel() {
        return channel;
    }

    public long getId() {
        return id;
    }

    public void setLastMessage(Message lastMessage) {
        this.lastMessage = lastMessage;
    }

    public Message getLastMessage() {
        return lastMessage;
    }

    public ResultPackage compare(long id, String input) {
        List<String> answer = type == GameType.LETTER_5 ? Main.letters5.get(leastQuestion) : type == GameType.LETTER_6 ? Main.letters6.get(leastQuestion) : type == GameType.MEANS_5 ? Main.means5.get(leastQuestion) : Main.means6.get(leastQuestion);
        inputs.add(input);
        StringJoiner joiner = new StringJoiner(", ");
        String rep = type != GameType.MEANS_5 && type != GameType.MEANS_6 ? input.replace(" ", "") : input;
        int cur = 0;
        for (String s1 : answer) {
            joiner.add(s1);
            if(type != GameType.MEANS_5 && type != GameType.MEANS_6) {
                cur = Math.max(cur, (int) Math.round(StringManager.findSimilarity(rep, s1.replace(" ", "")) * 100));
            } else {
                cur = Math.max(cur, (int) Math.round(StringManager.findSimilarity(input, s1) * 100));
            }
        }
//        score += cur + combo * 5;
//        if(cur == 100) {
//            perfect++;
//            corrects.add(true);
//            combo++;
//        } else {
//            corrects.add(false);
//            combo = 0;
//        }
//        maxCombo = Math.max(maxCombo, combo);
//        return new ResultPackage(leastQuestion, input, score, cur, current(), size(), perfect, combo, joiner.toString());
        return null;
    }

    public String next() {
        try {
            return leastQuestion = questions.get(++index);
        } catch (Exception ignore) {
            return null;
        }
    }

    public int size() {
        return size;
    }

    public int current() {
        return index + 1;
    }

//    public int getScore() {
//        return score;
//    }
//
//    public int getPerfect() {
//        return perfect;
//    }
//
//    public int getMaxCombo() {
//        return maxCombo;
//    }

    public UUID saveGame() {
        try {
            UUID uuid = UUID.randomUUID();
            File file = new File("src/main/resources/log/" + uuid + ".log");
            file.createNewFile();
            try (FileWriter fileWriter = new FileWriter(file)) {
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.println(type);
                printWriter.println(score);
                printWriter.println(maxCombo);
                printWriter.println(perfect);
                printWriter.println(size());
                int s = questions.size();
                for (int i = 0; i < s; i++) {
                    if(inputs.size() > i)
                        printWriter.println(questions.get(i) + "=" + corrects.get(i) + "=" + inputs.get(i));
                    else printWriter.println(questions.get(i) + "=" + false + "=");
                }
            }
            return uuid;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
