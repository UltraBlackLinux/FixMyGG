package me.ultrablacklinux.fixgg.config;

import me.sargunvohra.mcmods.autoconfig1u.AutoConfig;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;
import me.sargunvohra.mcmods.autoconfig1u.annotation.ConfigEntry;
import me.sargunvohra.mcmods.autoconfig1u.serializer.GsonConfigSerializer;
import me.sargunvohra.mcmods.autoconfig1u.serializer.PartitioningSerializer;
import me.sargunvohra.mcmods.autoconfig1u.shadowed.blue.endless.jankson.Comment;

@SuppressWarnings("unused")
@Config(name = "fixmygg")
@Config.Gui.Background("minecraft:textures/block/oak_planks.png")
public class FixGGConfig extends PartitioningSerializer.GlobalData {
    @ConfigEntry.Category("general")
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public General general = new General();

    public static void init() { AutoConfig.register(FixGGConfig.class, PartitioningSerializer.wrap(GsonConfigSerializer::new)); }

    public static FixGGConfig get() {
        return AutoConfig.getConfigHolder(FixGGConfig.class).getConfig();
    }

    @Config(name = "general")
    public
    class General implements ConfigData {
        @Comment("Words to check for typos. They mustn't contain themselves.")
        public String words = "gg gf gp";
        @Comment("Max amount of characters to be in front of the words.")
        public int index = 3;
        @Comment("Toggle the notification")
        public boolean message = true;
    }
}



