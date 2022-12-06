package org.mooner.wordbot.game;

public class StringManager {
    private static int getLevenshteinDistance(String X, String Y) {
        int m = X.length();
        int n = Y.length();

        int[][] T = new int[m + 1][n + 1];
        for (int i = 1; i <= m; i++) T[i][0] = i;
        for (int j = 1; j <= n; j++) T[0][j] = j;

        int cost;
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                cost = X.charAt(i - 1) == Y.charAt(j - 1) ? 0 : 1;
                T[i][j] = Integer.min(Integer.min(T[i - 1][j] + 1, T[i][j - 1] + 1), T[i - 1][j - 1] + cost);
            }
        }

        return T[m][n];
    }

    public static double findSimilarity(String x, String y) {
        if(x == null && y == null) return 1;
        if (x == null || y == null) return 0;
        x = x.toLowerCase(); y = y.toLowerCase();

        double maxLength = Double.max(x.length(), y.length());
        if (maxLength > 0) return (maxLength - getLevenshteinDistance(x, y)) / maxLength;
        return 1;
    }

    private static double starting(String string, String finder) {
        int length = Integer.min(string.length(), finder.length());
        int counter = 0;
        for (int i = 0; i < length; i++) {
            if (string.charAt(i) == finder.charAt(i)) counter++;
            else break;
        }
        return counter * 100d / length;
    }

    public static double find(String string, String finder) {
        if(string == null && finder == null) return 1;
        if (string == null || finder == null) return 0;
        string = string.toLowerCase(); finder = finder.toLowerCase();

        double increment = starting(string, finder);

        double maxLength = Double.max(string.length(), finder.length());
        if (maxLength > 0) return (maxLength - getLevenshteinDistance(string, finder)) / maxLength + increment;
        return 1;
    }
}
