package org.mooner.wordbot.cmd.commands;

import net.dv8tion.jda.api.EmbedBuilder;
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

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import static org.mooner.wordbot.cmd.commands.StartCommand.getEmbed;

public class FastCommand implements BotCommand {
    @Override
    public SlashCommandData getCommand() {
        OptionData data = new OptionData(OptionType.STRING, "선택", "무엇을 할꺼냐!", true);
        for (GameType value : GameType.values()) {
            data.addChoice(value.getTag() + " 뜻", value + "?");
            data.addChoice(value.getTag() + " 단어", value.toString());
        }
        return Commands.slash("fast", "스피드런 시작 (넘어가는 시간 1초)").addOptions(data);
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
        GameType type = GameType.valueOf(s);
        boolean mean = Objects.requireNonNull(event.getOption("게임 모드")).getAsInt() == 1;
        Game game = GameManager.startGame(event.getUser().getIdLong(), event.getChannel().getIdLong(), type, true, mean);
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
