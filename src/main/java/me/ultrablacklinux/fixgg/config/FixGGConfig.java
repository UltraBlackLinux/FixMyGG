package me.ultrablacklinux.fixgg.config;


import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;

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
    public static class General implements ConfigData {
        @Comment("Words to check for typos. They mustn't contain themselves.")
        public String words = "gg;gf;gp";
        @Comment("Max amount of characters to be in front of the words.")
        public int index = 3;
        @Comment("Max amount of characters in the message part.")
        public int length = 6;
        @Comment("Toggle the notification")
        public boolean message = true;
    }
}



