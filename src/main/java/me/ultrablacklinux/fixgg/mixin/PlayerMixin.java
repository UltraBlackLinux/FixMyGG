package me.ultrablacklinux.fixgg.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.network.packet.c2s.play.ChatMessageC2SPacket;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(ClientPlayerEntity.class)
public abstract class PlayerMixin {
    @Shadow @Final
    protected MinecraftClient client;

    @Shadow @Final
    public ClientPlayNetworkHandler networkHandler;

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onChatMessage(String fmsg, CallbackInfo info) {
        //important values
        String[] words = new String[] {"gf", "gg", "gp"};
        int maxIndex = 3;
        //important values

        String[] msg = fmsg.split(" ");
        Boolean changed = false;

        for (String checkWord : words) { //check, if word exists
            for (int currentLocation = 0; currentLocation < msg.length; currentLocation++) {
                if (msg[currentLocation].toLowerCase().contains(checkWord) && !msg[currentLocation].toLowerCase().matches(checkWord)) {
                    if (msg[currentLocation].length() <= maxIndex) {
                        changed = true;
                        if (msg[currentLocation].contains(checkWord.toUpperCase())) {
                            msg[currentLocation] = checkWord.toUpperCase();
                        } else if (msg[currentLocation].contains(checkWord.toLowerCase())) {
                            msg[currentLocation] = checkWord.toLowerCase();
                        }
                    }
                }
            }
        }
        if (changed) {
            client.player.sendMessage(Text.of("Fixed a typo!"), true);
            this.networkHandler.sendPacket(new ChatMessageC2SPacket(String.join(" ", msg)));
            info.cancel();
            changed = false;
        }

    }
}