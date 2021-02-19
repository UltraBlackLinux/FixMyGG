package me.ultrablacklinux.fixgg.mixin;

import me.ultrablacklinux.fixgg.FixGG;
import me.ultrablacklinux.fixgg.config.Config;
import me.ultrablacklinux.fixgg.util.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import org.apache.logging.log4j.Logger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

@Mixin(ClientPlayNetworkHandler.class)
public class NetworkMixin {
    @Shadow
    private MinecraftClient client;
    @Unique
    private final Logger FixMyGG_LOGGER = FixGG.getLogger();
    String itemSeperator = Config.get().misc.itemSeparator;
    boolean autoGG = Config.get().autogg.autoGG;
    private static final String[] ggTriggers = new String[]
            {"^ +1st Killer - ?\\[?\\w*\\+*\\]? \\w+ - \\d+(?: Kills?)?$",
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
    private static final String antikarma = "^\\+(?<karma>\\d)+ Karma!$";

    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    public void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        //antiKarma
        if (client != null && Config.get().misc.antiKarma && packet.getMessage().getString().matches(antikarma)) {
            ci.cancel();
        }

        //autogg
        for (String pattern : ggTriggers) {
            if (packet.getMessage().getString().matches(pattern) && Config.get().autogg.autoGG) { //if the message matches the pattern & autogg is turned on, it will send the server a message
                ArrayList<String> tmp = new ArrayList<>(Arrays.asList(Config.get().autogg.strings.split(itemSeperator)));
                int number;
                if (Config.get().autogg.stringsNumber <= -1) {
                    Collections.shuffle(tmp);
                    number = 0;
                }
                else number = Config.get().autogg.stringsNumber;
                FixMyGG_LOGGER.info(number);
                try { //1t = 1/3s
                    ci.cancel();
                    Config.get().fixMyGG.skipCheck = true;
                    client.player.sendChatMessage(tmp.get(0));
                    Config.get().fixMyGG.skipCheck = false;
                } catch (ArrayIndexOutOfBoundsException e) {
                    client.player.sendMessage(Text.of("§1[AutoGG] §cWrong index detected!"), false);
                }

            }
        }
    }
}
