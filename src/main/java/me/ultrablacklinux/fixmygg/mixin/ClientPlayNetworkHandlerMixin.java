package me.ultrablacklinux.fixmygg.mixin;

import me.ultrablacklinux.fixmygg.FixMyGG;
import me.ultrablacklinux.fixmygg.config.Config;
import me.ultrablacklinux.fixmygg.util.Utils;
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
public class ClientPlayNetworkHandlerMixin {

    @Shadow private MinecraftClient client;
    String itemSeparator = Config.get().misc.itemSeparator;
    String finalMsg = Config.get().autogg.finalMsg;
    int delayTime = Config.get().autogg.delayTime;

    @Inject(method = "onGameMessage", at = @At("HEAD"), cancellable = true)
    public void onGameMessage(GameMessageS2CPacket packet, CallbackInfo ci) {
        //antiKarma
        if (client != null && Config.get().misc.antiKarma && packet.getMessage().getString().matches("^\\+(?<karma>\\d)+ Karma!$")) {
            ci.cancel();
        }

        //autogg

        for (String pattern : Utils.processRegex(Config.get().autogg.autoGGRegexPatterns)) {
            if (packet.getMessage().getString().matches(pattern) && Config.get().autogg.autoGG) {
                ArrayList<String> tmp = new ArrayList<>(Arrays.asList(Config.get().autogg.strings.split(itemSeparator)));
                int number;

                if (Config.get().autogg.stringsNumber <= -1) {
                    Collections.shuffle(tmp);
                    number = 0;
                }
                else number = Config.get().autogg.stringsNumber;

                try {
                    ci.cancel();
                    //prevent the code from checking itself
                    FixMyGG.skipCheck = true;

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
                    FixMyGG.skipCheck = false;
                } catch (ArrayIndexOutOfBoundsException e) { client.player.sendMessage(Text.of("§1[AutoGG] §cWrong index detected!"), false); }
            }
        }
    }
}

