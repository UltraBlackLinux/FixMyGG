package me.ultrablacklinux.fixgg.mixin;

import com.mojang.authlib.GameProfile;
import me.ultrablacklinux.fixgg.util.Utils;
import net.minecraft.network.packet.c2s.login.LoginHelloC2SPacket;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(LoginHelloC2SPacket.class)
public class LoginHelloMixin {

    @Inject(method = "<init>(Lcom/mojang/authlib/GameProfile;)V", at = @At("TAIL"))
    public void getProfile(GameProfile profile, CallbackInfo ci) {
        Utils.playername = profile.getName();
    }
}
