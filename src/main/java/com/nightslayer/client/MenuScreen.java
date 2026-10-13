package com.night.nightclient;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.text.Text;

public class MenuScreen extends Screen {

    public MenuScreen() {
        super(Text.literal("NightClient"));
    }

    @Override
    public void render(DrawContext ctx, int mouseX, int mouseY, float delta) {
        // 1.21.11: renderBackground(ctx, mouseX, mouseY, delta)
        this.renderBackground(ctx, mouseX, mouseY, delta);

        ctx.drawCenteredTextWithShadow(
                this.textRenderer,
                this.title,
                this.width / 2,
                20,
                0xFFFFFF
        );

        // TODO: vẽ các nút module tại đây

        super.render(ctx, mouseX, mouseY, delta);
    }

    @Override
    public boolean shouldPause() {
        return false;
    }
}
