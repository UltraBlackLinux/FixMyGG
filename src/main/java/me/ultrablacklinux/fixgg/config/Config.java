package me.ultrablacklinux.fixgg.config;


import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.ConfigEntry;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import me.shedaniel.autoconfig.serializer.PartitioningSerializer;
import me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson.Comment;
@SuppressWarnings("unused")
@me.shedaniel.autoconfig.annotation.Config(name = "fixmygg")
@me.shedaniel.autoconfig.annotation.Config.Gui.Background("minecraft:textures/block/oak_planks.png")
public class Config extends PartitioningSerializer.GlobalData {
    @ConfigEntry.Category("TypoPrevention")
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public FixMyGG fixMyGG = new FixMyGG();

    @ConfigEntry.Category("AutoGG")
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public AutoGG autogg = new AutoGG();

    @ConfigEntry.Category("ChatUtils")
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public ChatUtils chatUtils = new ChatUtils();

    @ConfigEntry.Category("Misc")
    @ConfigEntry.Gui.TransitiveObject
    @ConfigEntry.Gui.CollapsibleObject(startExpanded = true)
    public Misc misc = new Misc();


    public static void init() {
        AutoConfig.register(Config.class, PartitioningSerializer.wrap(GsonConfigSerializer::new));
    }

    public static Config get() {
        return AutoConfig.getConfigHolder(Config.class).getConfig();
    }

    @me.shedaniel.autoconfig.annotation.Config(name = "fixgg")
    public static class FixMyGG implements ConfigData {

        @ConfigEntry.Gui.Excluded
        public boolean skipCheck;

        public boolean enabled = true;

        @Comment("Words to check for - They mustn't contain themselves")
        public String words = "gg;gf;gp";

        @Comment("Max amount of characters allowed in front of the gg")
        public int index = 3;

        @Comment("Max amount of characters allowed in the word")
        public int length = 6;

        public boolean message = true;
    }

    @me.shedaniel.autoconfig.annotation.Config(name = "autogg")
    public static class AutoGG implements ConfigData {

        @Comment("Note: Custom regex patterns can be added via the config")
        public boolean autoGG = true;

        @Comment("AutoGG delay in ms")
        @ConfigEntry.BoundedDiscrete(min = 0, max = 2000)
        public int delayTime = 350;

        @Comment("List of messages, that can be sent")
        public String strings = "Good Game;Good Fight;gg;gf";

        @Comment("The index of the Strings above, or just \"-1\" for a random entry")
        public int stringsNumber = -1;


        @Comment("Message to be sent after the gg")
        public String finalMsg = "";

        @ConfigEntry.Gui.Excluded
        public String[] autoGGRegexPatterns = {"^ +1st Killer - ?\\[?\\w*\\+*\\]? \\w+ - \\d+(?: Kills?)?$",
                "^ *1st (?:Place ?)?(?:-|:)? ?\\[?\\w*\\+*\\]? \\w+(?: : \\d+| - \\d+(?: Points?)?| - \\d+(?: x .)?| \\(\\w+ .{1,6}\\) - \\d+ Kills?|: \\d+:\\d+| - \\d+ (?:Zombie )?(?:Kills?|Blocks? Destroyed)| - \\[LINK\\])?$",
                "^ +Winn(?:er #1 \\(\\d+ Kills\\): \\w+ \\(\\w+\\)|er(?::| - )(?:Hiders|Seekers|Defenders|Attackers|PLAYERS?|MURDERERS?|Red|Blue|RED|BLU|\\w+)(?: Team)?|ers?: ?\\[?\\w*\\+*\\]? \\w+(?:, ?\\[?\\w*\\+*\\]? \\w+)?|ing Team ?[\\:-] (?:Animals|Hunters|Red|Green|Blue|Yellow|RED|BLU|Survivors|Vampires))$",
                "^ +Alpha Infected: \\w+ \\(\\d+ infections?\\)$",
                "^ +Murderer: \\w+ \\(\\d+ Kills?\\)$",
                "^ +You survived \\d+ rounds!$",
                "^ +(?:UHC|SkyWars|The Bridge|Sumo|Classic|OP|MegaWalls|Bow|NoDebuff|Blitz|Combo|Bow Spleef) (?:Duel|Doubles|Teams|Deathmatch|2v2v2v2|3v3v3v3)? ?- \\d+:\\d+$",
                "^ +They captured all wools!$",
                "^ +Game over!$",
                "^ +[\\d\\.]+k?/[\\d\\.]+k? \\w+$",
                "^ +(?:Criminal|Cop)s won the game!$",
                "^ +\\[?\\w*\\+*\\]? \\w+ - \\d+ Final Kills$",
                "^ +Zombies - \\d*:?\\d+:\\d+ \\(Round \\d+\\)$",
                "^ +. YOUR STATISTICS .$"};
    }
    @me.shedaniel.autoconfig.annotation.Config(name = "chatUtils")
    public static class ChatUtils implements ConfigData {
        public boolean enabled = true;
        public String varied = "!;!";
        public String wide = "-;-";
    }

    @me.shedaniel.autoconfig.annotation.Config(name = "misc")
    public static class Misc implements ConfigData {

        public boolean antiKarma = false;

        public String itemSeparator = ";";

    }

}



