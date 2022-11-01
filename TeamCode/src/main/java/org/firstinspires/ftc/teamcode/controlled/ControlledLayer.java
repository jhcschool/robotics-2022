package org.firstinspires.ftc.teamcode.controlled;

import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.firstinspires.ftc.teamcode.FrameInfo;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.LaneSystem;
import org.firstinspires.ftc.teamcode.Layer;
import org.firstinspires.ftc.teamcode.LayerInitInfo;
import org.firstinspires.ftc.teamcode.PoseStorage;

public class ControlledLayer extends Layer {

    enum ControlMode {
        DRIVER_CONTROL,
        AUTONOMOUS_CONTROL
    }

    private final UserMovementSystem userMovementSystem;
    private Hardware hardware;

    private ControlMode controlMode = ControlMode.DRIVER_CONTROL;

    public ControlledLayer() {
        userMovementSystem = new UserMovementSystem();
    }

    @Override
    public void init(LayerInitInfo info) {
        super.init(info);
        hardware = info.hardware;
        hardware.drive.setPoseEstimate(PoseStorage.robotPose);
    }

    @Override
    public void tick(FrameInfo frameInfo) {
        switch(controlMode) {
            case DRIVER_CONTROL:
                tickDriver(frameInfo);
                break;
            case AUTONOMOUS_CONTROL:
                tickAutonomous(frameInfo);
                break;
        }
    }

    private Trajectory trajectory = null;

    private void tickAutonomous(FrameInfo frameInfo) {
        if (hardware.gamepad1.b) {
            controlMode = ControlMode.DRIVER_CONTROL;
            trajectory = null;
            return;
        }

        if (!hardware.drive.isBusy() && trajectory != null) {
            hardware.drive.followTrajectoryAsync(trajectory);
        }
    }

    private void tickDriver(FrameInfo frameInfo) {
        userMovementSystem.tick(hardware.gamepad1, hardware.drive);
    }
}
