package me.ultrablacklinux.fixmygg;

import me.ultrablacklinux.fixmygg.command.FixMyGGCommand;
import me.ultrablacklinux.fixmygg.config.Config;
import net.fabricmc.api.ModInitializer;

public class FixMyGG implements ModInitializer {
    public static boolean skipCheck = false;

    @Override
    public void onInitialize() {
        Config.init();
        FixMyGGCommand.registerCommands();
    }
}
