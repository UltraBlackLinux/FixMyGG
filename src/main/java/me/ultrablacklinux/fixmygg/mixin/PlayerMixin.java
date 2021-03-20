package me.ultrablacklinux.fixmygg.mixin;


import me.ultrablacklinux.fixmygg.FixMyGG;
import me.ultrablacklinux.fixmygg.config.Config;
import me.ultrablacklinux.fixmygg.util.Utils;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.*;

@Mixin(ClientPlayerEntity.class)
public abstract class PlayerMixin {
    @Shadow
    @Final
    protected MinecraftClient client;

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onChatMessage(String fmsg, CallbackInfo info) {

        String itemSeperator = Config.get().misc.itemSeparator;

        ArrayList<String> trigger = Utils.simpleStringToAL(Config.get().emojis.trigger);
        boolean chatUtilsEnabled = Config.get().chatUtils.enabled;
        boolean emojisEnabled = Config.get().emojis.enabled;

        //chatformatting
        ArrayList<String> variedChat = Utils.simpleStringToAL(Config.get().chatUtils.varied);
        ArrayList<String> wideChat = Utils.simpleStringToAL(Config.get().chatUtils.varied);
        ArrayList<String> fancyChat = Utils.simpleStringToAL(Config.get().chatUtils.fancy);

        //value: name; key: emoji
        HashMap<String, String> emojis;

        //Strings to remove from the message
        ArrayList<ArrayList<String>> tmpfilter = new ArrayList<>();
        tmpfilter.add(variedChat);
        tmpfilter.add(wideChat);
        tmpfilter.add(fancyChat);
        ArrayList<String> filterStrings = Utils.stringALJoiner(tmpfilter);

        //fixMyGG
        String[] words = Config.get().fixMyGG.words.split(itemSeperator);
        for (int location = 0; location < words.length; location++)
            words[location] = words[location].replace(itemSeperator, "");
        boolean fmggenabled = Config.get().fixMyGG.enabled;
        boolean showMessage = Config.get().fixMyGG.message;
        int maxIndex = Config.get().fixMyGG.index;
        int maxLength = Config.get().fixMyGG.length;
        String[] msg = fmsg.split(" ");
        boolean changed = false;
        if (fmggenabled) {
            try {
                if (Config.get().fixMyGG.words.replace(itemSeperator, "").trim().equals("")) {
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
                info.cancel();
            }
        }
        if (changed && !FixMyGG.skipCheck) {
            if (showMessage) client.player.sendMessage(Text.of("Fixed a typo!"), true);
            info.cancel();
            client.player.sendChatMessage(String.join(" ", msg));
        }

        //chatUtils
        if (chatUtilsEnabled && fmsg.length() >= 3) {
            try {
                if (fmsg.startsWith(wideChat.get(0)) && fmsg.endsWith(wideChat.get(1))) {
                    info.cancel();
                    client.player.sendChatMessage(Utils.formatChat(filterStrings, 1, fmsg));
                } else if (fmsg.startsWith(variedChat.get(0)) && fmsg.endsWith(variedChat.get(1))) {
                    info.cancel();
                    client.player.sendChatMessage(Utils.formatChat(filterStrings, 0, fmsg));

                } else if (fmsg.startsWith(fancyChat.get(0)) && fmsg.endsWith(fancyChat.get(1))) {
                    info.cancel();
                    client.player.sendChatMessage(Utils.formatChat(filterStrings, 2, fmsg));
                }
            } catch (ArrayIndexOutOfBoundsException e) {
                client.player.sendMessage(Text.of("§1[ChatUtils] §cWrong input detected!"), false);
                info.cancel();
            }
        }

        //emojis
        if (emojisEnabled) {
            try {
                emojis = Utils.advancedStringToHM(Config.get().emojis.emojis);
                for (Map.Entry<String, String> entry : emojis.entrySet()) {
                    String tmp = fmsg.replace(trigger.get(0) + entry.getKey() + trigger.get(1), entry.getValue());
                    if (!fmsg.equals(tmp)) {
                        info.cancel();
                        client.player.sendChatMessage(tmp);
                    }
                }
            } catch (NullPointerException e) {
                client.player.sendMessage(Text.of("§1[Emojis] §cWrong input detected!"), false);
                info.cancel();
            }
        }
    }
}