package com.ilabeu.modules;

import com.ilabeu.addon.AddonTemplate;
import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.math.Vec3d;

public class BoatFly extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> speed = sgGeneral.add(new DoubleSetting.Builder()
        .name("speed")
        .description("Horizontal boat speed.")
        .defaultValue(1.0)
        .min(0.1)
        .sliderMax(5.0)
        .build()
    );

    private final Setting<Double> verticalSpeed = sgGeneral.add(new DoubleSetting.Builder()
        .name("vertical-speed")
        .description("Vertical movement speed.")
        .defaultValue(0.8)
        .min(0.1)
        .sliderMax(5.0)
        .build()
    );

    public BoatFly() {
        super(AddonTemplate.CATEGORY, "boat-fly", "Allows boats to fly.");
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null) return;
        if (!(mc.player.getVehicle() instanceof BoatEntity)) return;

        BoatEntity boat = (BoatEntity) mc.player.getVehicle();
        boat.setNoGravity(true);

        Vec3d velocity = boat.getVelocity();

        double forward = 0;
        double vertical = 0;

        if (mc.options.forwardKey.isPressed()) forward += speed.get();
        if (mc.options.backKey.isPressed()) forward -= speed.get();

        if (mc.options.jumpKey.isPressed()) vertical += verticalSpeed.get();
        if (mc.options.sneakKey.isPressed()) vertical -= verticalSpeed.get();

        Vec3d look = mc.player.getRotationVec(1.0f);

        Vec3d newVelocity = new Vec3d(
            look.x * forward,
            vertical,
            look.z * forward
        );

        boat.setVelocity(newVelocity);
        boat.velocityModified = true;
    }

    @Override
    public void onDeactivate() {
        if (mc.player != null && mc.player.getVehicle() instanceof BoatEntity boat) {
            boat.setNoGravity(false);
        }
    }
}
