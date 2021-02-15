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

@Mixin(ClientPlayerEntity.class)
public abstract class PlayerMixin {
    @Shadow @Final
    protected MinecraftClient client;

    @Shadow @Final
    public ClientPlayNetworkHandler networkHandler;

    @Shadow
    public abstract void sendMessage(Text text_1, boolean boolean_1);

    @Inject(method = "sendChatMessage", at = @At("HEAD"), cancellable = true)
    private void onChatMessage(String msg, CallbackInfo info) {

        if (msg.toLowerCase().contains("gg") && msg.toLowerCase().indexOf("gg") < 3 && !msg.toLowerCase().matches("gg")) {
            info.cancel();
            if (msg.contains("GG")) {
                this.networkHandler.sendPacket(new ChatMessageC2SPacket("GG"));
            }
            else if (msg.contains("gg")) {
                this.networkHandler.sendPacket(new ChatMessageC2SPacket("gg"));
            }
            client.player.sendMessage(Text.of("Fixed your gg!"), true);
        }
    }
}