package me.ultrablacklinux.fixmygg;

import me.ultrablacklinux.fixmygg.command.FixMyGGCommand;
import me.ultrablacklinux.fixmygg.config.Config;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FixMyGG implements ModInitializer {
    private static final Logger LOGGER = LogManager.getLogger();
    public static boolean skipCheck = false;

    @Override
    public void onInitialize() {
        Config.init();
        FixMyGGCommand.registerCommands();
        LOGGER.info("[FixMyGG] Started");
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
