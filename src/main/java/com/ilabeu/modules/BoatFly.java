package com.ilabeu.addon.modules;

import meteordevelopment.meteorclient.events.world.TickEvent;
import meteordevelopment.meteorclient.settings.*;
import meteordevelopment.meteorclient.systems.modules.Module;
import meteordevelopment.meteorclient.systems.modules.Categories;
import meteordevelopment.orbit.EventHandler;
import net.minecraft.entity.vehicle.BoatEntity;

public class BoatFly extends Module {
    private final SettingGroup sgGeneral = settings.getDefaultGroup();

    // Speed settings
    private final Setting<Double> speed = sgGeneral.add(new DoubleSetting.Builder()
        .name("speed")
        .description("The horizontal speed of the boat.")
        .defaultValue(1.0)
        .min(0.1)
        .sliderMax(5.0)
        .build()
    );

    private final Setting<Double> verticalSpeed = sgGeneral.add(new DoubleSetting.Builder()
        .name("vertical-speed")
        .description("The vertical speed when holding spacebar.")
        .defaultValue(1.0)
        .min(0.1)
        .sliderMax(5.0)
        .build()
    );

    // Acceleration settings - similar to ElytraFly
    private final Setting<Boolean> acceleration = sgGeneral.add(new BoolSetting.Builder()
        .name("acceleration")
        .description("Enables smooth acceleration for boat movement.")
        .defaultValue(false)
        .build()
    );

    private final Setting<Double> accelerationSpeed = sgGeneral.add(new DoubleSetting.Builder()
        .name("acceleration-speed")
        .description("How fast the boat accelerates.")
        .defaultValue(0.1)
        .min(0.01)
        .max(1.0)
        .sliderMax(0.5)
        .visible(acceleration::get)
        .build()
    );

    private final Setting<Double> maxSpeed = sgGeneral.add(new DoubleSetting.Builder()
        .name("max-speed")
        .description("Maximum speed when using acceleration.")
        .defaultValue(2.0)
        .min(0.1)
        .sliderMax(10.0)
        .visible(acceleration::get)
        .build()
    );

    private final Setting<Boolean> deceleration = sgGeneral.add(new BoolSetting.Builder()
        .name("deceleration")
        .description("Enables smooth deceleration when stopping.")
        .defaultValue(true)
        .visible(acceleration::get)
        .build()
    );

    private final Setting<Double> decelerationSpeed = sgGeneral.add(new DoubleSetting.Builder()
        .name("deceleration-speed")
        .description("How fast the boat decelerates.")
        .defaultValue(0.05)
        .min(0.01)
        .max(1.0)
        .sliderMax(0.5)
        .visible(() -> acceleration.get() && deceleration.get())
        .build()
    );

    // Current velocity for acceleration
    private double currentVelocity = 0.0;

    public BoatFly() {
        super(Categories.Movement, "boat-destroyer", "Destroys boats with style.");
    }

    @Override
    public void onActivate() {
        currentVelocity = 0.0;
    }

    @Override
    public void onDeactivate() {
        currentVelocity = 0.0;
    }

    @EventHandler
    private void onTick(TickEvent.Pre event) {
        if (mc.player == null || !(mc.player.getVehicle() instanceof BoatEntity)) {
            return;
        }

        BoatEntity boat = (BoatEntity) mc.player.getVehicle();

        // Get movement input
        boolean isMoving = mc.options.forwardKey.isPressed() || 
                          mc.options.backKey.isPressed() || 
                          mc.options.leftKey.isPressed() || 
                          mc.options.rightKey.isPressed();

        // Calculate target velocity
        double targetSpeed = speed.get();

        if (acceleration.get()) {
            // Acceleration logic
            if (isMoving) {
                // Accelerate
                currentVelocity = Math.min(currentVelocity + accelerationSpeed.get(), maxSpeed.get());
            } else if (deceleration.get()) {
                // Decelerate
                currentVelocity = Math.max(currentVelocity - decelerationSpeed.get(), 0.0);
            } else {
                // Instant stop if deceleration is disabled
                currentVelocity = 0.0;
            }
            targetSpeed = currentVelocity;
        } else {
            // No acceleration - instant speed
            targetSpeed = isMoving ? speed.get() : 0.0;
        }

        // Apply movement - WASD strafes in all directions, boat stays still otherwise
        double velocityX = 0;
        double velocityZ = 0;
        double velocityY = 0;

        // Get current yaw for directional movement
        double yaw = Math.toRadians(mc.player.getYaw());

        // W = Forward strafe (relative to player facing)
        if (mc.options.forwardKey.isPressed()) {
            velocityX -= Math.sin(yaw) * targetSpeed;
            velocityZ += Math.cos(yaw) * targetSpeed;
        }

        // S = Backward strafe (relative to player facing)
        if (mc.options.backKey.isPressed()) {
            velocityX += Math.sin(yaw) * targetSpeed;
            velocityZ -= Math.cos(yaw) * targetSpeed;
        }

        // A = Left strafe (relative to player facing)
        if (mc.options.leftKey.isPressed()) {
            velocityX -= Math.cos(yaw) * targetSpeed;
            velocityZ -= Math.sin(yaw) * targetSpeed;
        }

        // D = Right strafe (relative to player facing)
        if (mc.options.rightKey.isPressed()) {
            velocityX += Math.cos(yaw) * targetSpeed;
            velocityZ += Math.sin(yaw) * targetSpeed;
        }

        // Vertical movement - only spacebar goes up
        if (mc.options.jumpKey.isPressed()) {
            velocityY = verticalSpeed.get();
        } else if (mc.options.sneakKey.isPressed()) {
            velocityY = -verticalSpeed.get();
        }

        boat.setVelocity(velocityX, velocityY, velocityZ);

        // Disable gravity
        boat.setNoGravity(true);
    }
}
