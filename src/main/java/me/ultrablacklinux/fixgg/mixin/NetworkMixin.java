package me.ultrablacklinux.fixgg.mixin;

import me.ultrablacklinux.fixgg.config.Config;
import me.ultrablacklinux.fixgg.util.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.packet.s2c.play.GameMessageS2CPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@Mixin(ClientPlayNetworkHandler.class)
public class NetworkMixin {
    @Shadow private MinecraftClient client;
    String itemSeperator = Config.get().misc.itemSeparator;
    String finalMsg = Config.get().autogg.finalMsg;
    int delayTime = Config.get().autogg.delayTime;
    private static final String[] ggTriggers = Utils.regexProcessing(Config.get().autogg.autoGGRegexPatterns); //FREEZES CLIENT

    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    public void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        //antiKarma
        if (client != null && Config.get().misc.antiKarma && packet.getMessage().getString().matches("^\\+(?<karma>\\d)+ Karma!$")) {
            ci.cancel();
        }

        //autogg
        for (String pattern : ggTriggers) {
            if (packet.getMessage().getString().matches(pattern) && Config.get().autogg.autoGG) {
                ArrayList<String> tmp = new ArrayList<>(Arrays.asList(Config.get().autogg.strings.split(itemSeperator)));
                int number;

                if (Config.get().autogg.stringsNumber <= -1) {
                    Collections.shuffle(tmp);
                    number = 0;
                }
                else number = Config.get().autogg.stringsNumber;

                try {
                    ci.cancel();
                    //prevent the code from checking itself
                    Config.get().fixMyGG.skipCheck = true;

                    Thread thread = new Thread(() -> {
                        try {

                            Thread.sleep(delayTime);
                            client.player.sendChatMessage(tmp.get(number));
                            if (!finalMsg.trim().equals("")) {
                                client.player.sendChatMessage(finalMsg);
                            }
                        } catch (InterruptedException ignore) {}
                    });

                    thread.start();
                    //turn of the prevention
                    Config.get().fixMyGG.skipCheck = false;
                } catch (ArrayIndexOutOfBoundsException e) { client.player.sendMessage(Text.of("§1[AutoGG] §cWrong index detected!"), false); }
            }
        }
    }
}

