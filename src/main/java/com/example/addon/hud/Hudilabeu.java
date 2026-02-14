package com.ilabeu.addon.hud;

import com.ilabeu.addon.AddonTemplate;
import meteordevelopment.meteorclient.systems.hud.HudElement;
import meteordevelopment.meteorclient.systems.hud.HudElementInfo;
import meteordevelopment.meteorclient.systems.hud.HudRenderer;
import meteordevelopment.meteorclient.utils.render.color.Color;

public class Hudilabeu extends HudElement {
    /**
     * The {@code name} parameter should be in kebab-case.
     */
    public static final HudElementInfo<Hudilabeu> INFO = new HudElementInfo<>(AddonTemplate.HUD_GROUP, "ilabeu", "HUD element ilabeu.", Hudilabeu::new);

    public Hudilabeu() {
        super(INFO);
    }

    @Override
    public void render(HudRenderer renderer) {
        setSize(renderer.textWidth("ilabeu element", true), renderer.textHeight(true));

        // Render background
        renderer.quad(x, y, getWidth(), getHeight(), Color.LIGHT_GRAY);

        // Render text
        renderer.text("ilabeu element", x, y, Color.WHITE, true);
    }
}
