package com.ilabeu.mixin;

import com.ilabeu.addon.AddonTemplate;
import net.minecraft.client.gui.screen.TitleScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(TitleScreen.class)
public class Exampleilabeu {
    @Inject(method = "init", at = @At("TAIL"))
    private void onInit(CallbackInfo info) {
        AddonTemplate.LOG.info("This line is printed by an example mixin!");
    }
}
