package com.example.addon.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.DoubleSetting;
import meteordevelopment.meteorclient.settings.Setting;
import meteordevelopment.meteorclient.settings.SettingGroup;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.vehicle.AbstractBoatEntity;
import net.minecraft.util.math.Vec3d;

/**
 * Boat Fly - fly in a boat. For servers with no anticheat.
 */
public class BoatFly extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    private final Setting<Double> speed = sgGeneral.add(new DoubleSetting.Builder()
        .name("speed")
        .description("Horizontal flight speed in blocks per second.")
        .defaultValue(10)
        .min(1)
        .max(50)
        .sliderMax(30)
        .build()
    );

    private final Setting<Double> verticalSpeed = sgGeneral.add(new DoubleSetting.Builder()
        .name("vertical-speed")
        .description("Vertical speed when holding jump or sneak.")
        .defaultValue(5)
        .min(1)
        .max(20)
        .sliderMax(15)
        .build()
    );

    private static final double GRAVITY_PER_TICK = 0.08;

    public BoatFly() {
        super(Categories.Movement, "boat-fly", "Fly while in a boat. For servers with no anticheat.");
    }

    @Override
    public void onActivate() {
        if (mc.player != null && !(mc.player.getVehicle() instanceof AbstractBoatEntity)) {
            info("Get in a boat first.");
        }
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null) return;
        if (!(mc.player.getVehicle() instanceof AbstractBoatEntity boat)) return;

        double horizontal = speed.get() / 20.0;
        double vertical = verticalSpeed.get() / 20.0;

        float forward = mc.player.input.movementForward;
        float strafe = mc.player.input.movementSideways;

        float yaw = boat.getYaw() * (float) (Math.PI / 180);
        double dx = -Math.sin(yaw) * forward - Math.cos(yaw) * strafe;
        double dz = Math.cos(yaw) * forward - Math.sin(yaw) * strafe;

        double hLen = Math.sqrt(dx * dx + dz * dz);
        if (hLen > 1e-6) {
            dx = dx / hLen * horizontal;
            dz = dz / hLen * horizontal;
        } else {
            dx = 0;
            dz = 0;
        }

        double dy;
        if (mc.options.jumpKey.isPressed()) {
            dy = vertical;
        } else if (mc.options.sneakKey.isPressed()) {
            dy = -vertical;
        } else {
            dy = GRAVITY_PER_TICK;
        }

        boat.setVelocity(new Vec3d(dx, dy, dz));
    }
}
