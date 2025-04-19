package org.mooner.wordbot.cmd.commands;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.CommandAutoCompleteInteractionEvent;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.mooner.wordbot.cmd.BotCommand;
import org.mooner.wordbot.game.Game;
import org.mooner.wordbot.game.GameManager;
import org.mooner.wordbot.game.GameType;

import java.awt.*;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class StartCommand implements BotCommand {
    @Override
    public SlashCommandData getCommand() {
        OptionData data = new OptionData(OptionType.STRING, "선택", "무엇을 할꺼냐!", true);
        for (GameType value : GameType.values()) data.addChoice(value.getTag(), value.toString());
        return Commands.slash("start", "테스트 시작").addOptions(data);
    }

    public static MessageEmbed getEmbed(Game game) {
        EmbedBuilder builder = new EmbedBuilder();
        String next = game.next();
        if(next == null) {
            builder.setColor(Color.cyan);
            builder.setTitle((game.isFast() ? "[스피드런] " : "") + "모든 문제 종료");
            builder.appendDescription("더 이상 풀 문제가 없습니다.")
                    .appendDescription("\n최종 점수: **" + game.getScore() + "**")
                    .appendDescription("\n\nMax Combo: **" + game.getMaxCombo() + "**")
                    .appendDescription("\nPerfect: **" + game.getPerfect() + "**/"+game.size() + " [" + (game.getPerfect() == 0 ? 0 : game.getPerfect() * 100 / game.size()) + "%]")
                    .appendDescription("\n\n아래의 Key를 복사하여 나중에 해당 문제 결과를 확인할 수 있습니다.");
            builder.setFooter("Key: " + GameManager.stopGame(game, true));
            if(game.getPerfect() == game.size()) {
                game.getLastMessage().getChannel().sendMessage(":tada: **풀콤보다!! FULL COMBO!!!** :tada:")
                        .queueAfter(2, TimeUnit.SECONDS);
            }
        } else {
            Random random = new Random();
            builder.setColor(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256)));
            builder.setTitle((game.isFast() ? "[스피드런] " : "") + next);
            builder.appendDescription("위 단어의 뜻이나 단어를 채팅에 입력하세요.")
                    .appendDescription("\n포기하시려면 `포기`라고 입력하세요.")
                    .appendDescription("\n현재 점수: **" + game.getScore() + "**");
            builder.setFooter(game.current() + "/" + game.size());
        }
        return builder.build();
    }

    @Override
    public void onCommand(SlashCommandInteractionEvent event) {
        ReplyCallbackAction reply = event.deferReply(false);
        if(!GameManager.isAllowed(event.getChannel().getIdLong(), event.getUser().getIdLong())) {
            reply.setEphemeral(true).setContent("누군가 먼저 해당 채널에서 시작했습니다! 끝나기 전까지 기다리시거나 다른 채널로 가 주세요.")
                    .queue(m -> m.deleteOriginal().queueAfter(5, TimeUnit.SECONDS));
            return;
        }
        Game pGame = GameManager.getGame(event.getUser().getIdLong());
        if(pGame != null) {
            reply.setContent("이전 문제를 취소하고 다시 시작합니다.");
            GameManager.stopGame(pGame, false);
        }
        String s = Objects.requireNonNull(event.getOption("선택")).getAsString();
        GameType type;
        boolean mean = s.endsWith("?");
        if(mean) type = GameType.valueOf(s.substring(0, s.length() - 1));
        else type = GameType.valueOf(s);
        Game game = GameManager.startGame(event.getUser().getIdLong(), event.getChannel().getIdLong(), type, false, mean);
        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle(type.getTag());
        builder.appendDescription("단어 수: " + game.size());
        reply.addEmbeds(builder.build()).queue();
        event.getChannel().sendMessageEmbeds(getEmbed(game)).queueAfter(1, TimeUnit.SECONDS, game::setLastMessage);
    }

    @Override
    public void onComplete(CommandAutoCompleteInteractionEvent event) {
//        List<String> list = l.stream().filter(s -> s.startsWith(event.getFocusedOption().getValue())).toList();
//        ArrayList<Command.Choice> choices = new ArrayList<>();
//        for (String s : list) {
//            if(choices.size() >= 24) break;
//            choices.add(new Command.Choice(s, s));
//        }
//        event.replyChoices(choices).queue();
    }
}
