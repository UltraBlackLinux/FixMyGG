package me.ultrablacklinux.fixgg.util;


import me.ultrablacklinux.fixgg.config.Config;
import java.util.ArrayList;

public class Utils  {
    private static String asciiChars = Config.get().chatUtils.asciiChars;
    private static String unicodeChars = Config.get().chatUtils.unicodeChars;

    public static String formatChat(ArrayList<String> filterStrings, int type, String message) {
        for (String s : filterStrings) {
            message = message.replace(s, "");
        }
        String[] charMsg = message.split("");

        for (int c = 0; c < message.length(); c++) {
            if (type == 1) {
                charMsg[c] += " ";
            } else if (type == 0) {
                if (c % 2 == 0) {
                    charMsg[c] = charMsg[c].toLowerCase();
                } else {
                    charMsg[c] = charMsg[c].toUpperCase();
                }
            } else if (type == 2) {
                try {
                    charMsg[c] = String.valueOf(unicodeChars.charAt(asciiChars.indexOf(charMsg[c])));
                } catch (Exception e) {
                }
            }
        }
        return String.join("", charMsg);
    }
}

