package org.firstinspires.ftc.teamcode.controlled;

import org.firstinspires.ftc.teamcode.FrameInfo;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.Layer;
import org.firstinspires.ftc.teamcode.LayerInitInfo;
import org.firstinspires.ftc.teamcode.PoseStorage;

public class ControlledLayer extends Layer {

    private ControlSystem controlSystem;
    private Hardware hardware;

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
<<<<<<<HEAD
        controlSystem.tick(hardware.gamepad1, hardware.drive);
=======
        controlSystem.tick(hardware.gamepad, hardware);
>>>>>>>c0cc690e56f32f219a7547facd4220a4e9202a7a
    }
}
