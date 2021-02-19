package me.ultrablacklinux.fixgg;

import me.ultrablacklinux.fixgg.config.Config;
import net.fabricmc.api.ModInitializer;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class FixGG implements ModInitializer {
    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public void onInitialize() {
        Config.init();
        LOGGER.info("[FixMyGG] Started");
    }

    public static Logger getLogger() {
        return LOGGER;
    }
}
