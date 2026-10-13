package com.nightslayer.client;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.gui.widget.TextFieldWidget;
import net.minecraft.client.input.KeyInput;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class MenuScreen extends Screen {

    private static final int W = 420;
    private static final int H = 300;
    private static final int SIDE = 100;
    private static final int ROW = 30;
    private static final String[] TABS = {"HUD", "PvP", "Visual", "Theme"};

    private int tab = 0;
    private String query = "";
    private TextFieldWidget search;
    private final Map<Module, ButtonWidget> toggles = new LinkedHashMap<>();
    private final List<ButtonWidget> themeButtons = new ArrayList<>();
    private List<Module> shown = new ArrayList<>();

    public MenuScreen() {
        super(Text.literal("Night Client"));
    }

    private int left() {
        return (width - W) / 2;
    }

    private int top() {
        return (height - H) / 2;
    }

    private int rowY(int index) {
        return 60 + index * ROW;
    }

    private List<Module> modulesFor(int t) {
        if (t == 0) {
            return Modules.HUD;
        }
        if (t == 1) {
            return Modules.PVP;
        }
        if (t == 2) {
            return Modules.VISUAL;
        }
        return List.of();
    }

    @Override
    protected void init() {
        toggles.clear();
        themeButtons.clear();
        int l = left();
        int t = top();

        for (int i = 0; i < TABS.length; i++) {
            final int idx = i;
            addDrawableChild(ButtonWidget.builder(Text.literal(TABS[i]), button -> {
                tab = idx;
                if (search != null) {
                    search.setText("");
                }
                query = "";
                layout();
            }).dimensions(l + 8, t + 50 + i * 26, SIDE - 16, 20).build());
        }

        search = new TextFieldWidget(textRenderer, l + W - 140, t + 8, 130, 16, Text.literal("Tim module"));
        search.setPlaceholder(Text.literal("Tim module..."));
        search.setChangedListener(value -> {
            query = value;
            layout();
        });
        addDrawableChild(search);

        for (Module module : Modules.ALL) {
            ButtonWidget button = ButtonWidget.builder(label(module), b -> {
                module.toggle();
                b.setMessage(label(module));
            }).dimensions(l + W - 52, t, 40, 20).build();
            addDrawableChild(button);
            toggles.put(module, button);
        }

        for (int k = 0; k < Modules.THEME_NAMES.length; k++) {
            final int idx = k;
            ButtonWidget button = ButtonWidget.builder(themeLabel(k), b -> {
                Modules.theme = idx;
                refreshThemeLabels();
            }).dimensions(l + SIDE + 12, t + 60 + k * 28, W - SIDE - 20, 20).build();
            addDrawableChild(button);
            themeButtons.add(button);
        }

        layout();
    }

    private Text label(Module module) {
        return module.isEnabled()
                ? Text.literal("ON").formatted(Formatting.GREEN)
                : Text.literal("OFF").formatted(Formatting.RED);
    }

    private Text themeLabel(int k) {
        return Text.literal((k == Modules.theme ? "[x] " : "[ ] ") + Modules.THEME_NAMES[k]);
    }

    private void refreshThemeLabels() {
        for (int k = 0; k < themeButtons.size(); k++) {
            themeButtons.get(k).setMessage(themeLabel(k));
        }
    }

    private boolean searching() {
        return !query.trim().isEmpty();
    }

    private void layout() {
        int l = left();
        int t = top();
        shown = new ArrayList<>();

        if (searching()) {
            String q = query.trim().toLowerCase();
            for (Module m : Modules.ALL) {
                if (m.name.toLowerCase().contains(q) && shown.size() < 8) {
                    shown.add(m);
                }
            }
        } else if (tab < 3) {
            shown.addAll(modulesFor(tab));
        }

        for (Map.Entry<Module, ButtonWidget> entry : toggles.entrySet()) {
            int idx = shown.indexOf(entry.getKey());
            ButtonWidget button = entry.getValue();
            boolean visible = idx >= 0;
            button.visible = visible;
            button.active = visible;
            if (visible) {
                button.setX(l + W - 52);
                button.setY(t + rowY(idx) + 3);
            }
        }

        boolean themeTab = !searching() && tab == 3;
        for (ButtonWidget button : themeButtons) {
            button.visible = themeTab;
            button.active = themeTab;
        }
    }

    @Override
    public void render(DrawContext context, int mouseX, int mouseY, float delta) {
        int l = left();
        int t = top();
        int accent = Modules.accent();

        context.fill(l, t, l + W, t + H, 0xE0090D1A);
        context.fill(l, t, l + SIDE, t + H, 0xF0070A14);
        context.fill(l, t, l + W, t + 2, accent);

        context.drawTextWithShadow(textRenderer, "Night Client", l + 10, t + 12, accent);
        context.drawTextWithShadow(textRenderer, "v1.2", l + 10, t + 24, 0xFF6B7088);
        context.drawTextWithShadow(textRenderer, "MODULES", l + 10, t + 38, 0xFF6B7088);
        context.fill(l, t + 50 + tab * 26, l + 3, t + 70 + tab * 26, accent);
        context.drawTextWithShadow(textRenderer, "Dong: Right Shift hoac /nc", l + 6, t + H - 12, 0xFF6B7088);

        String header;
        if (searching()) {
            header = "Ket qua tim kiem";
        } else {
            header = TABS[tab] + (tab == 3 ? "" : " Modules");
        }
        if (tab < 3 || searching()) {
            int on = 0;
            for (Module m : shown) {
                if (m.isEnabled()) {
                    on++;
                }
            }
            header += "  (" + on + "/" + shown.size() + ")";
        }
        context.drawTextWithShadow(textRenderer, header, l + SIDE + 12, t + 12, 0xFFFFFFFF);

        for (int i = 0; i < shown.size(); i++) {
            Module m = shown.get(i);
            int y = t + rowY(i);
            context.fill(l + SIDE + 10, y, l + W - 8, y + 26, 0xFF11162A);
            if (m.isEnabled()) {
                context.fill(l + SIDE + 10, y, l + SIDE + 12, y + 26, accent);
            }
            context.drawTextWithShadow(textRenderer, m.name, l + SIDE + 18, y + 4, 0xFFFFFFFF);
            context.drawTextWithShadow(textRenderer, m.description, l + SIDE + 18, y + 15, 0xFF7A8098);
        }

        if (!searching() && tab == 3) {
            context.drawTextWithShadow(textRenderer, "Chon mau giao dien", l + SIDE + 12, t + 44, 0xFF8A90A8);
        }
        if (searching() && shown.isEmpty()) {
            context.drawTextWithShadow(textRenderer, "Khong tim thay module nao", l + SIDE + 12, t + 64, 0xFF8A90A8);
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
