package org.mooner.wordbot;

import org.mooner.wordbot.cmd.BotEventListener;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import org.mooner.wordbot.game.GameManager;

import javax.security.auth.login.LoginException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static final String version = "v1.0";

    public static JDA jda;
    public static BotEventListener commandListener;

    public static HashMap<String, List<String>>
            letters5 = new HashMap<>(),
            means5 = new HashMap<>(),
            letters6 = new HashMap<>(),
            means6 = new HashMap<>();

    public static void main(String[] args) {
        UpdateManager.update("5", letters5, means5);
        UpdateManager.update("6", letters6, means6);

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
                    letters5 = new HashMap<>();
                    letters6 = new HashMap<>();
                    means5 = new HashMap<>();
                    means6 = new HashMap<>();
                    UpdateManager.update("5", letters5, means5);
                    UpdateManager.update("6", letters6, means6);
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
