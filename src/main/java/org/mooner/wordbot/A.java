package org.mooner.wordbot;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class A {
    public static void main(String[] args) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader("src/main/resources/asdf.txt"));
            String s;
            while ((s = reader.readLine()) != null) {
                if (s.isBlank()) continue;
                System.out.println("<p>"+s+"</p>");
            }
            reader.close();
        } catch (IOException ignore) {}
    }
}
