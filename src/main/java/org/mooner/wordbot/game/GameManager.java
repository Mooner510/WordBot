package org.mooner.wordbot.game;

import net.dv8tion.jda.api.EmbedBuilder;
import org.mooner.wordbot.Main;

import java.awt.*;
import java.util.*;
import java.util.List;

public class GameManager {
    public static Map<Long, Game> gameMap = Collections.synchronizedMap(new HashMap<>());

    public static boolean isAllowed(long channel, long user) {
        return gameMap.values().stream().noneMatch(p -> p.getChannel() == channel && p.getId() != user);
    }

    public static Game getGame(long id) {
        return gameMap.get(id);
    }

    public static Game startGame(long id, long channel, GameType type, boolean fast) {
        List<String> cloneList = new ArrayList<>(Main.getResource(type).keySet());
        Collections.shuffle(cloneList);
        Game game = new Game(id, channel, fast, type, cloneList);
        gameMap.put(id, game);
        return game;
    }

    public static UUID stopGame(Game game, boolean save) {
        gameMap.remove(game.getId());
        long end = System.currentTimeMillis();
        if(save) return game.saveGame(end);
        return null;
    }

    private static final long HOUR = 3600000L;
    private static final long MINUTE = 60000L;
    private static final long SECOND = 1000L;

    public static String parsePlayTime(long time) {
        StringBuilder builder = new StringBuilder();
        if(time > HOUR) {
            builder.append(time / HOUR).append('h');
            time %= HOUR;
            if(time > MINUTE) {
                builder.append(' ').append(time / MINUTE).append('m');
                time %= MINUTE;
            } else builder.append(' ').append(0).append('m');
            if(time > SECOND) {
                builder.append(' ').append(time / SECOND).append('s');
                time %= SECOND;
            } else builder.append(' ').append(0).append('s');
        } else if(time > MINUTE) {
            builder.append(time / MINUTE).append('m');
            time %= MINUTE;
            if(time > SECOND) {
                builder.append(' ').append(time / SECOND).append('s');
                time %= SECOND;
            } else builder.append(' ').append(0).append('s');
        } else if(time > SECOND) {
            builder.append(time / SECOND).append('s');
            time %= SECOND;
        } else builder.append(0);
        builder.append('.').append(time);
        return builder.toString();
    }

    public static void stopAll() {
        long end = System.currentTimeMillis();
        gameMap.forEach((key, value) -> {
            EmbedBuilder builder = new EmbedBuilder();
            builder.setColor(Color.cyan);
            builder.setTitle("어머! 점검중!");
            switch (new Random().nextInt(5)) {
                case 0 -> builder.appendDescription("누군가 봇을 죽였어!!!!!!!! ((꾸엑");
                case 1 -> builder.appendDescription("흐으르을읅ㄺㄷㄱㄺㄱㄱㄱ 나 죽는다라아알ㄺㄺㄺㄱㄱㄱㄺ");
                case 2 -> builder.appendDescription("우으으우엉우어ㅜ웅ㅇ으으응어ㅓ엉ㅇㅇ어ㅓ 빨ㄹ려들어간나다닫ㅇㄱ");
                case 3 -> builder.appendDescription("으으걱국ㄱ갹가ㅜㄱㄱ ㄴㄴ나.. 나의 죽음을 모두에게 알려라....");
                case 4 -> builder.appendDescription("닌 더 못한당께 ㅋㅋ 난 죽을거랑깨 ((자살");
            }
            builder.appendDescription("\n최종 점수 였던것: **" + value.getScore() + "**")
                    .appendDescription("\n\nAlmost max Combo: **" + value.getMaxCombo() + "**")
                    .appendDescription("\nNot enough perfect: **" + value.getPerfect() + "**/"+value.size() + " [" + (value.getPerfect() == 0 ? 0 : value.getPerfect() * 100 / value.size()) + "%]")
                    .appendDescription("\nUnknown PlayTime: " + parsePlayTime(end - value.getStartTime()))
                    .appendDescription("\n\n아래의 Key를 복사하..라고 하고 싶은데 봇이 꺼짐");
            value.getLastMessage().editMessageEmbeds(builder.build()).queue();
        });
        gameMap = new HashMap<>();
    }
}
