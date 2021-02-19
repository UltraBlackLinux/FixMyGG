package me.ultrablacklinux.fixgg.util;

import net.minecraft.util.Tickable;

public class Utils extends Thread implements Tickable {
    public static long TICK;
    @Override
    public void tick() {
        TICK++;
    }
}
