package me.ultrablacklinux.fixmygg.command;

import net.fabricmc.fabric.api.client.command.v1.ClientCommandManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.text.Text;

import static net.fabricmc.fabric.api.client.command.v1.ClientCommandManager.*;


public class FixMyGGCommand {
    static MinecraftClient client = MinecraftClient.getInstance();
    public static void registerCommands() {
        ClientCommandManager.DISPATCHER.register(literal("fixmygg")
                .then(literal("help").executes(ctx -> {
                    client.player.sendMessage(Text.of("/fixmygg placeholders: Available placeholders for AutoGG"), false);
                    return 1;
                }))
                .then(literal("placeholders").executes(ctx -> {
                    client.player.sendMessage(Text.of(
                            "--- AutoGG placeholders --- \n" +
                                    "PLAYER: Current player's name"), false);
                    return 1;
                }))
        );
    }
}