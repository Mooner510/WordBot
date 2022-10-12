package org.mooner.wordbot.game;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public class ResultPackage {
    public final String question, input, answer;
    public final int score, additiveScore, current, size, correct, combo;

    public ResultPackage(String question, String input, int score, int additiveScore, int current, int size, int correct, int combo, String answer) {
        this.question = question;
        this.input = input;
        this.score = score;
        this.additiveScore = additiveScore;
        this.current = current;
        this.size = size;
        this.combo = combo;
        this.correct = correct;
        this.answer = answer;
    }

    public MessageEmbed getEmbed() {
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(question);
        builder.appendDescription("입력한 답: **" + input + "**")
                .appendDescription("\n정답: **`" + answer + "`**")
                .appendDescription("\n")
                .appendDescription("\nCombo / Perfect: **"+combo+"** / **" + correct + "**");
        if(combo > 1)
            builder.appendDescription("\n점수: **" + score + "** [ +" + additiveScore + " ] (+" + ((combo - 1) * 5) + ")");
        else builder.appendDescription("\n점수: **" + score + "** [ +" + additiveScore + " ]");
        builder.appendDescription("\n\n2초 후에 다음 문제로 넘어갑니다");
        builder.setFooter(current + "/" + size);
        if(additiveScore == 100) builder.setColor(Color.GREEN);
        else if(additiveScore >= 75) builder.setColor(Color.YELLOW);
        else if(additiveScore >= 50) builder.setColor(Color.ORANGE);
        else if(additiveScore >= 25) builder.setColor(Color.RED);
        else builder.setColor(Color.DARK_GRAY);
        return builder.build();
    }
}
