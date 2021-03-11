package me.ultrablacklinux.fixgg.util;


import com.mojang.authlib.GameProfile;
import me.ultrablacklinux.fixgg.config.Config;
import net.minecraft.client.MinecraftClient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class Utils  {
    public static String playername;
    private static MinecraftClient client = MinecraftClient.getInstance();
    private static String asciiChars = Config.get().chatUtils.asciiChars;
    private static String unicodeChars = Config.get().chatUtils.unicodeChars;

    static String itemSeperator = Config.get().misc.itemSeparator;
    static String optItemSeperator = Config.get().misc.optItemSeparator;

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
                    //ignore
                }
            }
        }
        return String.join("", charMsg);
    }

    public static ArrayList<String> stringALJoiner(ArrayList<ArrayList<String>> input) {
        ArrayList<String> finished = new ArrayList<>();
        for (ArrayList<String> s : input) {
            for (String s2 : s) {
                finished.add(s2.replace(itemSeperator, ""));
            }
        }
        return finished;
    }

    public static ArrayList<String> simpleStringToAL(String input) {
        ArrayList<String> finished = new ArrayList<>();
        for (String s : input.split(itemSeperator)) {
            finished.add(s.replace(itemSeperator, ""));
        }
        return finished;
    }

    public static HashMap<String, String> advancedStringToHM(String input) {
        HashMap<String, String> finished = new HashMap<>();
        try {
            for (String s : input.split(itemSeperator)) {
                String[] split = s.split(optItemSeperator);
                finished.put(split[0].replace(itemSeperator, ""), split[1].replace(optItemSeperator, ""));
            }
        } catch (Exception e) {finished = null;}
        return finished;
    }

    public static String[] regexProcessing(String[] input) {
        ArrayList<String> triggers = simpleStringToAL(Config.get().misc.regexPlaceholder);
        HashMap<String, String> placeholders = new HashMap<>();
        //placeholders with value
        placeholders.put("PLAYER", playername);

        for (int s = 0; s < input.length; s++) {
            for (Map.Entry<String, String> entry : placeholders.entrySet()) {
                input[s] = input[s].replace(triggers.get(0) + entry.getKey() + triggers.get(1), entry.getValue());
            }
        }
        return input;
    }
}