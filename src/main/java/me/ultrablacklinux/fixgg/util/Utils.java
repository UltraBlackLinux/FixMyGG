package me.ultrablacklinux.fixgg.util;

import me.ultrablacklinux.fixgg.config.Config;
import net.minecraft.util.Tickable;

import java.util.ArrayList;

public class Utils extends Thread implements Tickable {
    public static long TICK;

    @Override
    public void tick() {
        TICK++;
    }


    public static String formatChat(ArrayList<String> filterStrings, int type, String message) {
        for (String s : filterStrings) {
            message = message.replace(s.replace(Config.get().misc.itemSeparator, ""), "");
        }

        String[] charMsg = message.split("");
        for (int c = 0; c < message.length(); c++) {
            if (type == 1) {
                charMsg[c] += " ";
            }
            else if (type == 0) {
                if (c % 2 == 0) {
                    charMsg[c] = charMsg[c].toLowerCase();
                }
                else {
                    charMsg[c] = charMsg[c].toUpperCase();
                }
            }
        }
        return String.join("", charMsg);
    }
}
