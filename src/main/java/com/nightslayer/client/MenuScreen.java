package com.zinhsaita.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public final class ZinhsaitaScreen extends Screen {
    private static final int PANEL_W = 250;
    private static final int PANEL_H = 190;
    private int left, top;

    public ZinhsaitaScreen() { super(Text.literal("ZinhsaitaClient")); }

    @Override
    protected void init() {
        left = (width - PANEL_W) / 2;
        top = (height - PANEL_H) / 2;
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        renderBackground(ctx, mouseX, mouseY, delta);
        int accent = ZinhsaitaClient.accentColor();
        ctx.fill(left, top, left + PANEL_W, top + PANEL_H, 0xF010121B);
        ctx.fill(left, top, left + PANEL_W, top + 3, accent);
        ctx.drawTextWithShadow(textRenderer, Text.literal("ZINHSAITA CLIENT"), left + 14, top + 13, accent);
        ctx.drawTextWithShadow(textRenderer, Text.literal("CLIENT UTILITIES  /  1.21.11"), left + 14, top + 27, 0xFF8F98B0);
        drawToggle(ctx, left + 14, top + 49, "HUD", ZinhsaitaClient.hudEnabled, mouseX, mouseY);
        drawToggle(ctx, left + 14, top + 78, "Auto Sprint", ZinhsaitaClient.sprintEnabled, mouseX, mouseY);
        ctx.fill(left + 14, top + 108, left + PANEL_W - 14, top + 136, hover(left + 14, top + 108, PANEL_W - 28, 28, mouseX, mouseY) ? 0xFF34364A : 0xFF242635);
        ctx.drawTextWithShadow(textRenderer, Text.literal("Theme: " + themeName()), left + 23, top + 118, 0xFFE6E8F0);
        ctx.drawTextWithShadow(textRenderer, Text.literal("Right Shift: toggle menu"), left + 14, top + 151, 0xFF8F98B0);
        ctx.drawTextWithShadow(textRenderer, Text.literal("Click a row to change it"), left + 14, top + 164, 0xFF8F98B0);
        super.render(ctx, mouseX, mouseY, delta);
    }

    private void drawToggle(DrawContext ctx, int x, int y, String label, boolean enabled, int mx, int my) {
        ctx.fill(x, y, x + PANEL_W - 28, y + 24, hover(x, y, PANEL_W - 28, 24, mx, my) ? 0xFF34364A : 0xFF242635);
        ctx.drawTextWithShadow(textRenderer, Text.literal(label), x + 9, y + 8, 0xFFE6E8F0);
        String state = enabled ? "ON" : "OFF";
        int color = enabled ? ZinhsaitaClient.accentColor() : 0xFF7B8297;
        ctx.drawTextWithShadow(textRenderer, Text.literal(state), x + PANEL_W - 62, y + 8, color);
    }

    private boolean hover(int x, int y, int w, int h, int mx, int my) {
        return mx >= x && mx < x + w && my >= y && my < y + h;
    }

    private String themeName() {
        return switch (ZinhsaitaClient.theme) { case 1 -> "Mint"; case 2 -> "Amber"; default -> "Violet"; };
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            if (hover(left + 14, top + 49, PANEL_W - 28, 24, (int) mouseX, (int) mouseY)) ZinhsaitaClient.hudEnabled = !ZinhsaitaClient.hudEnabled;
            else if (hover(left + 14, top + 78, PANEL_W - 28, 24, (int) mouseX, (int) mouseY)) ZinhsaitaClient.sprintEnabled = !ZinhsaitaClient.sprintEnabled;
            else if (hover(left + 14, top + 108, PANEL_W - 28, 28, (int) mouseX, (int) mouseY)) ZinhsaitaClient.theme = (ZinhsaitaClient.theme + 1) % 3;
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override public boolean shouldPause() { return false; }
}
