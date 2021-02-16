package me.ultrablacklinux.fixgg;

import me.ultrablacklinux.fixgg.config.FixGGConfig;
import net.fabricmc.api.ModInitializer;

public class FixGG implements ModInitializer {
    @Override
    public void onInitialize() {
        FixGGConfig.init();
    }
}
