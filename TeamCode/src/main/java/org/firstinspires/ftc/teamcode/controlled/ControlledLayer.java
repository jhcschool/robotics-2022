package org.firstinspires.ftc.teamcode.controlled;

import org.firstinspires.ftc.teamcode.FrameInfo;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.Layer;
import org.firstinspires.ftc.teamcode.LayerInitInfo;
import org.firstinspires.ftc.teamcode.PoseStorage;

public class ControlledLayer extends Layer {

    enum ControlMode {
        DRIVER_CONTROL,
        AUTONOMOUS_CONTROL
    }

    private final ControlSystem controlSystem;
    private Hardware hardware;

    ControlMode controlMode = ControlMode.DRIVER_CONTROL;

    public ControlledLayer() {
        controlSystem = new ControlSystem();
    }



    @Override
    public void init(LayerInitInfo info) {
        super.init(info);
        hardware = info.hardware;
        hardware.drive.setPoseEstimate(PoseStorage.robotPose);
    }

    @Override
    public void tick(FrameInfo frameInfo) {
        controlSystem.tick(hardware.gamepad1, hardware.drive);
    }
}
