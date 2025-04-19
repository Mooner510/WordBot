package org.mooner.wordbot.cmd;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.ChannelType;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageType;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.buttons.Button;
import org.jetbrains.annotations.NotNull;
import org.mooner.wordbot.cmd.buttons.RiceButton;
import org.mooner.wordbot.cmd.commands.FastCommand;
import org.mooner.wordbot.cmd.commands.LogViewCommand;
import org.mooner.wordbot.cmd.commands.StartCommand;
import org.mooner.wordbot.cmd.selects.RiceSelects;
import org.mooner.wordbot.game.Game;
import org.mooner.wordbot.game.GameManager;
import org.mooner.wordbot.game.ResultPackage;
import org.mooner.wordbot.utils.InteractionIdParser;

import java.awt.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringJoiner;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.mooner.wordbot.Main.jda;

public class BotEventListener extends ListenerAdapter {
    public static HashMap<String, BotCommand> commands;
    public static HashMap<String, BotButton> buttons;
    public static HashMap<String, BotSelectMenu> selects;

    public BotEventListener() {
        commands = new HashMap<>();
        buttons = new HashMap<>();
        selects = new HashMap<>();
    }

    public void register() {
        commands.put("start", new StartCommand());
        commands.put("log", new LogViewCommand());
        commands.put("fast", new FastCommand());

        buttons.put("rice", new RiceButton());

        selects.put("rice", new RiceSelects());
        updateCommand();
    }

    public void updateCommand() {
        HashSet<SlashCommandData> data = new HashSet<>();
        commands.forEach((s, i) -> data.add(i.getCommand()));
        jda.updateCommands().addCommands(data).queue();
    }

    public static String createId(long userId, String command, Object... arguments) {
        StringJoiner joiner = new StringJoiner("/");
        for (Object o : arguments) joiner.add(String.valueOf(o));
        return userId + ":" + command + "%" + joiner;
    }

    public static InteractionIdParser parseId(String id) {
        String[] v1 = id.split(":");
        String[] v2 = v1[1].split("%");
        String[] v3 = v2[1].split("/");
        return new InteractionIdParser(Long.parseLong(v1[0]), v2[0], v3);
    }

    public HashMap<Long, Long> commandDelay = new HashMap<>();

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        long time = commandDelay.getOrDefault(event.getUser().getIdLong(), 0L);
        long l = System.currentTimeMillis();
        if (time + 2000 >= l) {
            event.deferReply(true).setContent("잠시 멈춰요! 천천히좀 해줘요;;").queue();
            commandDelay.put(event.getUser().getIdLong(), l);
            return;
        } else {
            commandDelay.put(event.getUser().getIdLong(), l);
        }
        String id;
        if (commands.containsKey(id = event.getName())) {
            commands.get(id).onCommand(event);
        }
    }

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if (event.getAuthor().isBot() || event.getAuthor().isSystem()) return;
        Message message = event.getMessage();
        if (message.getChannelType() == ChannelType.TEXT || message.getChannelType() == ChannelType.PRIVATE) {
            if (message.getType() == MessageType.DEFAULT) {
                Game game = GameManager.getGame(event.getAuthor().getIdLong());
                if (game != null) {
                    if(message.getChannel().getIdLong() != game.getChannel()) return;
                    if(message.getContentRaw().equals("포기")) {
                        UUID key = null;
                        message.delete().queue();
                        EmbedBuilder builder = new EmbedBuilder();
                        builder.setColor(Color.cyan);
                        builder.setTitle((game.isFast() ? "[스피드런] " : "") + "문제 포기");
                        builder.appendDescription("문제를 푸는 도중 포기했습니다.")
                                .appendDescription("\n진행한 문항 수: **`" + game.current() + "/" + game.size() + "`**")
                                .appendDescription("\n최종 점수: **" + game.getScore() + "**")
                                .appendDescription("\n\nMax Combo: **" + game.getMaxCombo() + "**")
                                .appendDescription("\nPerfect: **" + game.getPerfect() + "**");
                        if(game.current() >= game.size() / 5) {
                            builder.appendDescription("\n\n아래의 Key를 복사하여 나중에 해당 문제 결과를 확인할 수 있습니다.");
                            builder.setFooter("Key: " + (key = GameManager.stopGame(game, true)));
                        } else {
                            builder.appendDescription("\n\n20% 이상 문제를 풀어야 문제 결과가 저장됩니다.");
                            GameManager.stopGame(game, false);
                        }
                        game.getLastMessage().editMessage(key != null ? key.toString() : "").setEmbeds(builder.build()).queue();
                        return;
                    }
                    message.delete().queue();
                    ResultPackage compare = game.compare(message.getContentRaw());
                    game.getLastMessage().editMessageEmbeds(compare.getEmbed())
                            .queue(m -> m.editMessageEmbeds(StartCommand.getEmbed(game))
                                    .queueAfter(game.isFast() ? 800 : 3000, TimeUnit.MILLISECONDS, game::setLastMessage)
                            );
                }
            }
        }
    }

    //    @Override
//    public void onCommandAutoCompleteInteraction(@NotNull CommandAutoCompleteInteractionEvent event) {
//        String id;
//        if(commands.containsKey(id = event.getName())) {
//            commands.get(id).onComplete(event);
//        }
//    }
}
