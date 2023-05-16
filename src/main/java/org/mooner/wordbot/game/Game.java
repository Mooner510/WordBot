package org.mooner.wordbot.game;

import net.dv8tion.jda.api.entities.Message;
import org.mooner.wordbot.Main;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.StringJoiner;
import java.util.UUID;

public class Game {
    private final List<String> questions;
    private final List<String> inputs;
    private final List<Boolean> corrects;

    private final GameType type;
    private final boolean isFast;

    private final long startTime, id, channel;

    private String leastQuestion;

    private int index;
    private int score;
    private int perfect;
    private int combo;
    private int maxCombo;
    private final int size;

    private Message lastMessage;

    public Game(long id, long channel, boolean fast, GameType type, List<String> questions) {
        this.id = id;
        this.channel = channel;
        this.type = type;
        this.isFast = fast;
        this.questions = questions;
        this.startTime = System.currentTimeMillis();
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

    public ResultPackage compare(String input) {
        List<String> answer = Main.getResource(type).get(leastQuestion);
        inputs.add(input);
        StringJoiner joiner = new StringJoiner(", ");
        String rep = !type.isMeanType() ? input.replace(" ", "") : input;
        int cur = 0;
        for (String s1 : answer) {
            joiner.add(s1);
            if(!type.isMeanType()) {
                cur = Math.max(cur, (int) Math.round(StringManager.findSimilarity(rep, s1.replace(" ", "")) * 100));
            } else {
                cur = Math.max(cur, (int) Math.round(StringManager.findSimilarity(input, s1) * 100));
            }
        }
        score += cur + combo * 5;
        if(cur == 100) {
            perfect++;
            corrects.add(true);
            combo++;
        } else {
            corrects.add(false);
            combo = 0;
        }
        maxCombo = Math.max(maxCombo, combo);
        return new ResultPackage(leastQuestion, input, score, cur, current(), size(), perfect, combo, joiner.toString());
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

    public int getScore() {
        return score;
    }

    public int getPerfect() {
        return perfect;
    }

    public int getMaxCombo() {
        return maxCombo;
    }

    public long getStartTime() {
        return startTime;
    }

    public boolean isFast() {
        return isFast;
    }

    public UUID saveGame(long end) {
        try {
            UUID uuid = UUID.randomUUID();
            File file = new File("src/main/resources/log/" + uuid + ".log");
            file.createNewFile();
            try (FileWriter fileWriter = new FileWriter(file)) {
                PrintWriter printWriter = new PrintWriter(fileWriter);
                printWriter.println(id);
                printWriter.println(isFast);
                printWriter.println(type);
                printWriter.println(score);
                printWriter.println(maxCombo);
                printWriter.println(perfect);
                printWriter.println(end - startTime);
                printWriter.println(size());
                int s = questions.size();
                for (int i = 0; i < s; i++) {
                    if(inputs.size() > i)
                        printWriter.println(questions.get(i) + "=" + corrects.get(i) + "=" + inputs.get(i));
                    else printWriter.println(questions.get(i) + "=.= ");
                }
            }
            return uuid;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
