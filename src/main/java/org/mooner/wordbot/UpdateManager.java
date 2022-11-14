package org.mooner.wordbot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class UpdateManager {
    public static void update(char c, HashMap<String, List<String>> letter, HashMap<String, List<String>> means) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/data/"+c+".yml"));
            String s, eng;
            String[] mean;
            while ((s = reader.readLine()) != null) {
                if (s.startsWith("#")) continue;
                String[] s1 = s.split("-");
                eng = s1[0];
                mean = s1[1].split(",");

                letter.put(eng, new ArrayList<>(Arrays.asList(mean)));
                means.put(String.join(", ", mean), List.of(eng));
            }
            reader.close();
        } catch (IOException ignore) {}
    }
}
