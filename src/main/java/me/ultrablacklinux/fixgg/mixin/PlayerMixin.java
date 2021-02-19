package me.ultrablacklinux.fixgg.mixin;

import com.google.common.util.concurrent.AbstractScheduledService;
import me.ultrablacklinux.fixgg.config.Config;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public abstract class PlayerMixin {
    @Shadow
    private MinecraftClient client;
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onChatMessage(String fmsg, CallbackInfo info) {
        String itemSeperator = Config.get().misc.itemSeparator;
        String[] words = Config.get().fixMyGG.words.split(itemSeperator);
        for (int location = 0; location < words.length; location++) words[location] = words[location].replace(itemSeperator, "");
        boolean enabled = Config.get().fixMyGG.enabled;
        boolean showMessage = Config.get().fixMyGG.message;
        int maxIndex = Config.get().fixMyGG.index;
        int maxLength = Config.get().fixMyGG.length;
        String[] msg = fmsg.split(" ");
        boolean changed = false;



        if (enabled) {
            for (String checkWord : words) {
                for (int currentLocation = 0; currentLocation < msg.length; currentLocation++) {
                    if (msg[currentLocation].toLowerCase().contains(checkWord) && !msg[currentLocation].toLowerCase().matches(checkWord)) {
                        if (msg[currentLocation].indexOf(checkWord) <= maxIndex && msg[currentLocation].length() <= maxLength) {
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
        }
        if (changed && !Config.get().fixMyGG.skipCheck) {
            if (showMessage) client.player.sendMessage(Text.of("Fixed a typo!"), true);
            info.cancel();
            client.player.sendChatMessage(String.join(" ", msg));
            changed = false;
        }
    }
}