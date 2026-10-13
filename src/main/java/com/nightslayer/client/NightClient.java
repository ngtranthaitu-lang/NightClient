package com.zinhsaita.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public final class ZinhsaitaClient implements ClientModInitializer {
    public static boolean hudEnabled = true;
    public static boolean sprintEnabled = false;
    public static int theme = 0;
    private static KeyBinding menuKey;

    @Override
    public void onInitializeClient() {
        menuKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
                "key.zinhsaita.open_menu", InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_RIGHT_SHIFT, "category.zinhsaita"));

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (menuKey.wasPressed()) {
                if (client.currentScreen == null) client.setScreen(new ZinhsaitaScreen());
                else if (client.currentScreen instanceof ZinhsaitaScreen) client.setScreen(null);
            }
            if (sprintEnabled && client.player != null && client.currentScreen == null
                    && client.player.forwardSpeed > 0.0F && !client.player.isSneaking()) {
                client.player.setSprinting(true);
            }
        });

        HudRenderCallback.EVENT.register((drawContext, tickCounter) -> renderHud(drawContext));
    }

    private static void renderHud(DrawContext context) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (!hudEnabled || client.player == null || client.options.hudHidden) return;
        int accent = accentColor();
        int x = 8, y = 8;
        context.fill(x - 4, y - 4, x + 126, y + 34, 0xB010121B);
        context.fill(x - 4, y - 4, x - 1, y + 34, accent);
        context.drawTextWithShadow(client.textRenderer, Text.literal("ZINHSAITA"), x + 2, y, accent);
        context.drawTextWithShadow(client.textRenderer, Text.literal("FPS: " + client.getCurrentFps()), x + 2, y + 12, 0xFFE6E8F0);
        context.drawTextWithShadow(client.textRenderer, Text.literal("Sprint: " + (sprintEnabled ? "ON" : "OFF")), x + 2, y + 23, 0xFFB8C0D8);
    }

    static int accentColor() {
        return switch (theme) {
            case 1 -> 0xFF5BE7C4;
            case 2 -> 0xFFFFB454;
            default -> 0xFF9B7BFF;
        };
    }
}
