package com.nightslayer.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public class MenuScreen extends Screen {

    private static final int W = 380;
    private static final int H = 240;
    private static final int SIDE = 100;
    private static final int ROW = 34;
    private static final String[] TABS = {"HUD", "Visual"};

    private int tab = 0;

    public MenuScreen() {
        super(Text.literal("Night Client"));
    }

    private int left() {
        return (width - W) / 2;
    }

    private int top() {
        return (height - H) / 2;
    }

    private List<Module> modulesFor(int t) {
        if (t == 0) {
            return List.of(Modules.FPS, Modules.CPS, Modules.COORDS, Modules.ARMOR, Modules.KEYSTROKES);
        }
        return List.of(Modules.ZOOM, Modules.FULLBRIGHT);
    }

    @Override
    protected void init() {
        int l = left();
        int t = top();

        for (int i = 0; i < TABS.length; i++) {
            final int idx = i;
            String label = (i == tab ? "> " : "  ") + TABS[i];
            addDrawableChild(ButtonWidget.builder(Text.literal(label), button -> {
                tab = idx;
                clearChildren();
                init();
            }).dimensions(l + 8, t + 50 + i * 26, SIDE - 16, 20).build());
        }

        List<Module> list = modulesFor(tab);
        for (int j = 0; j < list.size(); j++) {
            Module module = list.get(j);
            addDrawableChild(ButtonWidget.builder(label(module), button -> {
                module.toggle();
                button.setMessage(label(module));
            }).dimensions(l + SIDE + 12, t + 44 + j * ROW, W - SIDE - 24, 20).build());
        }
    }

    private Text label(Module module) {
        Text status = module.isEnabled()
                ? Text.literal("BAT").formatted(Formatting.GREEN)
                : Text.literal("TAT").formatted(Formatting.RED);
        return Text.literal(module.name + "   ").append(status);
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int l = left();
        int t = top();

        context.fill(l, t, l + W, t + H, 0xE0090D1A);
        context.fill(l, t, l + SIDE, t + H, 0xF0070A14);
        context.fill(l, t, l + W, t + 2, 0xFF8B7CFF);

        context.drawTextWithShadow(textRenderer, "Night Client", l + 10, t + 12, 0xFF8B7CFF);
        context.drawTextWithShadow(textRenderer, "v1.0", l + 10, t + 24, 0xFF6B7088);
        context.drawTextWithShadow(textRenderer, "MODULES", l + 10, t + 38, 0xFF6B7088);
        context.drawTextWithShadow(textRenderer, TABS[tab] + " Modules", l + SIDE + 12, t + 14, 0xFFFFFFFF);
        context.drawTextWithShadow(textRenderer, "Right Shift / lenh /nc de dong", l + 10, t + H - 14, 0xFF6B7088);

        List<Module> list = modulesFor(tab);
        for (int j = 0; j < list.size(); j++) {
            context.drawTextWithShadow(textRenderer, list.get(j).description,
                    l + SIDE + 14, t + 44 + j * ROW + 22, 0xFF8A90A8);
        }

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
