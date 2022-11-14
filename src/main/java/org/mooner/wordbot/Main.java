package org.mooner.wordbot;

import org.mooner.wordbot.cmd.BotEventListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.mooner.wordbot.data.GameResource;
import org.mooner.wordbot.game.GameManager;
import org.mooner.wordbot.game.GameType;

import javax.security.auth.login.LoginException;
import java.io.File;
import java.util.*;

public class Main {
//    public static final String version = "v1.0";

    public static JDA jda;
    public static BotEventListener commandListener;

    public static HashSet<GameResource> resources;

    public static HashMap<String, List<String>> getResource(GameType type) {
        if(type.getTag().contains("뜻")) return resources.stream().filter(r -> r.isType(type)).findFirst().orElseThrow().getMeans();
        else return resources.stream().filter(r -> r.isType(type)).findFirst().orElseThrow().getLetters();
    }

    public static void main(String[] args) {
        String[] file = Objects.requireNonNull(new File("src/main/resources/data/").list());
        resources = new HashSet<>();

        for (String s : file) {
            char c = s.charAt(0);
            resources.add(new GameResource(GameType.valueOf("LETTER_"+c), GameType.valueOf("MEANS_"+c)));
        }

        try {
            jda = JDABuilder.create(args[0], Arrays.asList(GatewayIntent.values()))
                    .addEventListeners(commandListener = new BotEventListener())
                    .build();
        } catch (LoginException e) {
            throw new RuntimeException(e);
        }
        commandListener.register();
        commandListener.updateCommand();

        jda.getPresence().setPresence(Activity.playing("/start"), true);

        Scanner scanner = new Scanner(System.in);
        tag: while(true) {
            switch (scanner.nextLine()) {
                case "update" -> {
                    file = Objects.requireNonNull(new File("src/main/resources/data/").list());
                    resources = new HashSet<>();

                    for (String s : file) {
                        char c = s.charAt(0);
                        resources.add(new GameResource(GameType.valueOf("LETTER_"+c), GameType.valueOf("MEANS_"+c)));
                    }
                    System.out.println("Update Successful");
                }
                case "", "s", "S", "ㄴ", "stop", "ㄴ새ㅔ", "STOP" -> {
                    GameManager.stopAll();
                    jda.cancelRequests();
                    jda.shutdown();
                    break tag;
                }
            }
        }
    }
}
