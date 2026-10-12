package com.nightslayer.client;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.ItemStack;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayDeque;
import java.util.Deque;

public class NightClient implements ClientModInitializer {

    private static final int TEXT = 0xFFFFFFFF;

    private final Deque<Long> clicks = new ArrayDeque<>();
    private boolean mouseWasDown;
    private boolean menuKeyWasDown;
    private boolean openMenu;
    private int savedFov = -1;

    @Override
    public void onInitializeClient() {
        ClientTickEvents.END_CLIENT_TICK.register(this::onTick);
        HudRenderCallback.EVENT.register(this::onHud);

        ClientCommandRegistrationCallback.EVENT.register((dispatcher, registryAccess) -> {
            dispatcher.register(ClientCommandManager.literal("nc").executes(ctx -> {
                openMenu = true;
                return 1;
            }));
            dispatcher.register(ClientCommandManager.literal("nightclient").executes(ctx -> {
                openMenu = true;
                return 1;
            }));
        });
    }

    private boolean keyDown(MinecraftClient client, int key) {
        return GLFW.glfwGetKey(client.getWindow().getHandle(), key) == GLFW.GLFW_PRESS;
    }

    private void onTick(MinecraftClient client) {
        if (client.player == null) {
            return;
        }

        if (openMenu && client.currentScreen == null) {
            openMenu = false;
            client.setScreen(new MenuScreen());
        }

        boolean menuDown = keyDown(client, GLFW.GLFW_KEY_RIGHT_SHIFT);
        if (menuDown && !menuKeyWasDown && client.currentScreen == null) {
            client.setScreen(new MenuScreen());
        }
        menuKeyWasDown = menuDown;

        boolean mouseDown = GLFW.glfwGetMouseButton(client.getWindow().getHandle(), GLFW.GLFW_MOUSE_BUTTON_LEFT) == GLFW.GLFW_PRESS;
        long now = System.currentTimeMillis();
        if (mouseDown && !mouseWasDown) {
            clicks.addLast(now);
        }
        mouseWasDown = mouseDown;
        while (!clicks.isEmpty() && now - clicks.peekFirst() > 1000) {
            clicks.removeFirst();
        }

        boolean zooming = Modules.ZOOM.isEnabled() && client.currentScreen == null && keyDown(client, GLFW.GLFW_KEY_C);
        if (zooming) {
            if (savedFov < 0) {
                savedFov = client.options.getFov().getValue();
            }
            client.options.getFov().setValue(30);
        } else if (savedFov >= 0) {
            client.options.getFov().setValue(savedFov);
            savedFov = -1;
        }

        ClientPlayerEntity player = client.player;
        if (Modules.FULLBRIGHT.isEnabled()) {
            player.addStatusEffect(new StatusEffectInstance(StatusEffects.NIGHT_VISION, 400, 0, false, false, false));
        } else if (player.hasStatusEffect(StatusEffects.NIGHT_VISION)) {
            StatusEffectInstance effect = player.getStatusEffect(StatusEffects.NIGHT_VISION);
            if (effect != null && effect.getDuration() <= 400 && !effect.shouldShowIcon()) {
                player.removeStatusEffect(StatusEffects.NIGHT_VISION);
            }
        }
    }

    private void onHud(DrawContext context, Object tickCounter) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null || client.options.hudHidden) {
            return;
        }

        int x = 6;
        int y = 6;

        if (Modules.FPS.isEnabled()) {
            line(context, client, "FPS: " + client.getCurrentFps(), x, y);
            y += 11;
        }
        if (Modules.CPS.isEnabled()) {
            line(context, client, "CPS: " + clicks.size(), x, y);
            y += 11;
        }
        if (Modules.COORDS.isEnabled()) {
            ClientPlayerEntity p = client.player;
            line(context, client, String.format("XYZ: %.1f / %.1f / %.1f", p.getX(), p.getY(), p.getZ()), x, y);
            y += 11;
        }
        if (Modules.ARMOR.isEnabled()) {
            EquipmentSlot[] slots = {EquipmentSlot.HEAD, EquipmentSlot.CHEST, EquipmentSlot.LEGS, EquipmentSlot.FEET};
            String[] names = {"Mu", "Ao", "Quan", "Giay"};
            for (int i = 0; i < slots.length; i++) {
                ItemStack stack = client.player.getEquippedStack(slots[i]);
                if (!stack.isEmpty() && stack.isDamageable()) {
                    int left = stack.getMaxDamage() - stack.getDamage();
                    line(context, client, names[i] + ": " + left + "/" + stack.getMaxDamage(), x, y);
                    y += 11;
                }
            }
        }
        if (Modules.KEYSTROKES.isEnabled()) {
            drawKeystrokes(context, client, x, y + 4);
        }
    }

    private void line(DrawContext context, MinecraftClient client, String text, int x, int y) {
        context.fill(x - 2, y - 2, x + client.textRenderer.getWidth(text) + 2, y + 9, 0x90000000);
        context.drawTextWithShadow(client.textRenderer, text, x, y, TEXT);
    }

    private void drawKeystrokes(DrawContext context, MinecraftClient client, int x, int y) {
        key(context, client, "W", client.options.forwardKey.isPressed(), x + 22, y);
        key(context, client, "A", client.options.leftKey.isPressed(), x, y + 22);
        key(context, client, "S", client.options.backKey.isPressed(), x + 22, y + 22);
        key(context, client, "D", client.options.rightKey.isPressed(), x + 44, y + 22);
    }

    private void key(DrawContext context, MinecraftClient client, String label, boolean down, int x, int y) {
        context.fill(x, y, x + 20, y + 20, down ? 0xC08B7CFF : 0x90000000);
        int tx = x + 10 - client.textRenderer.getWidth(label) / 2;
        context.drawTextWithShadow(client.textRenderer, label, tx, y + 6, down ? 0xFF000000 : TEXT);
    }
}
