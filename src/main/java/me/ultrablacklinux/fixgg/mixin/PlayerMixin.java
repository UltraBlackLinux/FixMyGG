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
import java.util.HashMap;
import java.util.Map;

@Mixin(ClientPlayerEntity.class)
public abstract class PlayerMixin {
    @Shadow @Final
    protected MinecraftClient client;
    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onChatMessage(String fmsg, CallbackInfo info) {
        String itemSeperator = Config.get().misc.itemSeparator;
        String optItemSeperator = Config.get().misc.optItemSeparator;

        String[] trigger = Config.get().emojis.trigger.split(itemSeperator);
        boolean chatUtilsEnabled = Config.get().chatUtils.enabled;
        boolean emojisEnabled = Config.get().emojis.enabled;
        String[] emojisList = Config.get().emojis.emojis.split(itemSeperator);
        String[] variedChat = Config.get().chatUtils.varied.split(itemSeperator);
        String[] wideChat = Config.get().chatUtils.wide.split(itemSeperator);
        String[] fancyChat = Config.get().chatUtils.fancy.split(itemSeperator);

        HashMap<String, String> emojis = new HashMap<>(); //value: name; key: emoji
        for (String s : emojisList) {
            String[] split = s.split(optItemSeperator);
            emojis.put(split[0], split[1].replace(optItemSeperator, ""));
        }

        ArrayList<String> filterStrings = new ArrayList<>();
        for (String s : variedChat) filterStrings.add(s.replace(itemSeperator, ""));
        for (String s : wideChat) filterStrings.add(s.replace(itemSeperator, ""));
        for (String s : fancyChat) filterStrings.add(s.replace(itemSeperator, ""));

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

                } else if (fmsg.startsWith(fancyChat[0]) && fmsg.endsWith(fancyChat[1])) {
                    type = 2;
                    info.cancel();
                    client.player.sendChatMessage(Utils.formatChat(filterStrings, type, fmsg));
                }
            } catch (ArrayIndexOutOfBoundsException e) {
            client.player.sendMessage(Text.of("§1[ChatUtils] §cWrong input detected!"), false);
            }
        }

        //emojis
        if (emojisEnabled) {
            for (Map.Entry<String, String> entry : emojis.entrySet()) {
                String tmp = fmsg.replace((trigger[0] + entry.getKey() + trigger[1].replace(itemSeperator, "")), entry.getValue());
                if (fmsg != tmp) {
                    info.cancel();
                    client.player.sendChatMessage(tmp);
                }
            }
        }
    }
}