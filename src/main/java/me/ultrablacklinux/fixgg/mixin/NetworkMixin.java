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

@Mixin(ClientPlayNetworkHandler.class)
public class NetworkMixin {
    @Shadow
    private MinecraftClient client;
    @Unique
    private final Logger FixMyGG_LOGGER = FixGG.getLogger();
    String itemSeperator = Config.get().misc.itemSeparator;
    boolean autoGG = Config.get().autogg.autoGG;
    String finalMsg = Config.get().autogg.finalMsg;
    int delayTime = Config.get().autogg.delayTime;
    private static final String[] ggTriggers = Config.get().autogg.autoGGRegexPatterns;
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
                try {
                    ci.cancel();
                    Config.get().fixMyGG.skipCheck = true;
                    //thread for delay
                    Thread thread = new Thread(() -> {
                        try {
                            Thread.sleep(delayTime);
                            client.player.sendChatMessage(tmp.get(0));
                            if (!finalMsg.replace(" ", "").equals("")) {
                                client.player.sendChatMessage(finalMsg);
                            }
                        } catch (InterruptedException e) {
                            //ignore
                        }
                    });
                    thread.start();
                    Config.get().fixMyGG.skipCheck = false;
                } catch (ArrayIndexOutOfBoundsException e) {
                    client.player.sendMessage(Text.of("§1[AutoGG] §cWrong index detected!"), false);
                }

            }
        }
    }
}
