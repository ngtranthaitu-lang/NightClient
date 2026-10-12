package com.nightslayer.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class MenuScreen extends Screen {

    private static final int PANEL_W = 260;
    private static final int ROW_H = 24;

    public MenuScreen() {
        super(Text.literal("Night Client"));
    }

    @Override
    protected void init() {
        int left = (width - PANEL_W) / 2;
        int top = (height - (Modules.ALL.size() * ROW_H + 50)) / 2 + 30;

        int i = 0;
        for (Module module : Modules.ALL) {
            int y = top + i * ROW_H;
            addDrawableChild(ButtonWidget.builder(label(module), button -> {
                module.toggle();
                button.setMessage(label(module));
            }).dimensions(left + 10, y, PANEL_W - 20, 20).build());
            i++;
        }
    }

    private Text label(Module module) {
        return Text.literal(module.name + ": " + (module.isEnabled() ? "BAT" : "TAT"));
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int left = (width - PANEL_W) / 2;
        int panelH = Modules.ALL.size() * ROW_H + 50;
        int top = (height - panelH) / 2;

        context.fill(left, top, left + PANEL_W, top + panelH, 0xE00B0F1E);
        context.fill(left, top, left + PANEL_W, top + 2, 0xFF8B7CFF);
        context.drawTextWithShadow(textRenderer, "Night Client", left + 10, top + 10, 0xFF8B7CFF);
        context.drawTextWithShadow(textRenderer, "Right Shift de dong", left + PANEL_W - 105, top + 10, 0xFF9AA0B5);

        super.render(context, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }

    @Override
    public boolean keyPressed(KeyInput input) {
        if (input.key() == GLFW.GLFW_KEY_RIGHT_SHIFT) {
            close();
            return true;
        }
        return super.keyPressed(input);
    }
}
