package com.ilabeu.addon;

import com.ilabeu.addon.commands.Commandilabeu;
import com.ilabeu.addon.hud.Hudilabeu;
import com.ilabeu.modules.BoatFly;
import com.ilabeu.modules.Moduleilabeu;
import com.mojang.logging.LogUtils;
import meteordevelopment.meteorclient.addons.MeteorAddon;
import meteordevelopment.meteorclient.commands.Commands;
import meteordevelopment.meteorclient.systems.hud.Hud;
import meteordevelopment.meteorclient.systems.modules.Category;
import meteordevelopment.meteorclient.systems.modules.Modules;
import org.slf4j.Logger;

public class AddonTemplate extends MeteorAddon {
    public static final Logger LOG = LogUtils.getLogger();
    public static final Category CATEGORY = new Category("ilabeu");

    @Override
    public void onInitialize() {
        LOG.info("Initializing ilabeu Addon");

        // Modules
        Modules.get().add(new Moduleilabeu());
        Modules.get().add(new BoatFly());

        // Commands
        Commands.add(new Commandilabeu());

        // HUD
        Hud.get().register(Hudilabeu.INFO);
    }

    @Override
    public void onRegisterCategories() {
        Modules.registerCategory(CATEGORY);
    }

    @Override
    public String getPackage() {
        return "com.ilabeu.addon";
    }
}
