package org.mooner.wordbot.cmd.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.mooner.wordbot.Main;
import org.mooner.wordbot.cmd.BotCommand;
import org.mooner.wordbot.game.GameType;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static org.mooner.wordbot.game.GameManager.parsePlayTime;

public class LogViewCommand implements BotCommand {
    @Override
    public SlashCommandData getCommand() {
        return Commands.slash("log", "이전 로그 확인")
                .addOption(OptionType.STRING, "key", "저장된 Key 입력", true);
    }

    public static MessageEmbed getEmbed(String uuid) {
        EmbedBuilder builder = new EmbedBuilder();
        try {
            UUID u = UUID.fromString(uuid);
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/log/"+u+".log"));
            builder.setColor(Color.MAGENTA);

            User user = Main.jda.getUserById(reader.readLine());
            boolean fast = reader.readLine().equals("true");
            GameType type = GameType.valueOf(reader.readLine());
            String score = reader.readLine();
            String maxCombo = reader.readLine();
            int perfect = Integer.parseInt(reader.readLine());
            long time = Long.parseLong(reader.readLine());
            int size = Integer.parseInt(reader.readLine());

            HashMap<String, List<String>> answer = Main.getResource(type);
            builder.setTitle("과거 기록: " + (fast ? "[스피드런] " : "") + type.getTag());
            builder.setAuthor(user.getName(), null, user.getAvatarUrl());
            builder.addField("최종 점수", score, true);
            builder.addField("Max Combo", maxCombo, true);
            builder.addField("Perfect", perfect + "/" + size + " [" + (perfect * 100 / size) + "%]", true);
            String playTime = reader.readLine();
            builder.addField("PlayTime", playTime.equals("?") ? "알 수 없음" : parsePlayTime(time), true);
            builder.appendDescription("```diff");
            String s;
            while ((s = reader.readLine()) != null) {
                if(s.isBlank()) continue;
                String[] split = s.split("=");
                if(split[1].equals("true")) {
                    builder.appendDescription("\n+" + split[0] + ":" + split[2]);
                } else {
                    String string = answer.get(split[0]).toString();
                    builder.appendDescription("\n-" + split[0] + ":" + split[2] + "➜" + string.substring(1, string.length() - 1));
                }
            }
            builder.appendDescription("```");
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
            EmbedBuilder b = new EmbedBuilder();
            b.setTitle("찾을 수 없는 기록입니다.");
            b.appendDescription("해당 Key로 저장된 로그를 찾을 수 없었습니다.");
            return b.build();
        }
        return builder.build();
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        String key = Objects.requireNonNull(event.getOption("key")).getAsString();
        event.deferReply().addEmbeds(getEmbed(key)).queue();
    }
}
