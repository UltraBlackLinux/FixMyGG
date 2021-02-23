package me.ultrablacklinux.fixgg.mixin;


import me.ultrablacklinux.fixgg.config.Config;
import me.ultrablacklinux.fixgg.util.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
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
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onChatMessage(String fmsg, CallbackInfo info) {
        String itemSeperator = Config.get().misc.itemSeparator;
        boolean chatUtilsEnabled = Config.get().chatUtils.enabled;
        String[] variedChat = Config.get().chatUtils.varied.split(itemSeperator);
        String[] wideChat = Config.get().chatUtils.wide.split(itemSeperator);

        ArrayList<String> filterStrings = new ArrayList<>();
        for (String s : variedChat) filterStrings.add(s.replace(itemSeperator, ""));
        for (String s : wideChat) filterStrings.add(s.replace(itemSeperator, ""));

        //fixMyGG
        String[] words = Config.get().fixMyGG.words.split(itemSeperator);
        for (int location = 0; location < words.length; location++) words[location] = words[location].replace(itemSeperator, "");
        boolean fmggenabled = Config.get().fixMyGG.enabled;
        boolean showMessage = Config.get().fixMyGG.message;
        int maxIndex = Config.get().fixMyGG.index;
        int maxLength = Config.get().fixMyGG.length;
        String[] msg = fmsg.split(" ");
        boolean changed = false;
        if (fmggenabled) {
            try {
                if (Config.get().fixMyGG.words.replace(itemSeperator, "").equals("")) {
                    throw new Exception();
                }
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
            } catch (Exception e) {
                client.player.sendMessage(Text.of("§1[FixMyGG] §cWrong input detected!"), false);
            }
        }
        if (changed && !Config.get().fixMyGG.skipCheck) {
            if (showMessage) client.player.sendMessage(Text.of("Fixed a typo!"), true);
            info.cancel();
            client.player.sendChatMessage(String.join(" ", msg));
            changed = false;
        }

        //chatUtils
        if (chatUtilsEnabled) {
            int type = -1;
            try {
                if (fmsg.startsWith(wideChat[0]) && fmsg.endsWith(wideChat[1])) {
                    type = 1;
                    info.cancel();
                    client.player.sendChatMessage(Utils.formatChat(filterStrings, type, fmsg));
                } else if (fmsg.startsWith(variedChat[0]) && fmsg.endsWith(variedChat[1])) {
                    type = 0;
                    info.cancel();
                    client.player.sendChatMessage(Utils.formatChat(filterStrings, type, fmsg));

                }
            } catch (ArrayIndexOutOfBoundsException e) {
            client.player.sendMessage(Text.of("§1[ChatUtils] §cWrong input detected!"), false);
            }
        }
    }
}