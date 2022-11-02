package org.firstinspires.ftc.teamcode.controlled;

import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.firstinspires.ftc.teamcode.FrameInfo;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.Layer;
import org.firstinspires.ftc.teamcode.LayerInitInfo;
import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.input.Button;
import org.firstinspires.ftc.teamcode.input.ButtonAction;
import org.firstinspires.ftc.teamcode.input.InputManager;

public class ControlledLayer extends Layer {

    private final UserMovementSystem userMovementSystem;
    private Hardware hardware;
    private InputManager inputManager;
    private ControlMode controlMode = ControlMode.DRIVER_CONTROL;
    private Trajectory trajectory = null;

    public ControlledLayer() {
        userMovementSystem = new UserMovementSystem();
    }

    @Override
    public void init(LayerInitInfo info) {
        super.init(info);
        hardware = info.hardware;
        inputManager = info.inputManager;

        hardware.drive.setPoseEstimate(PoseStorage.robotPose);
    }

    @Override
    public void tick(FrameInfo frameInfo) {
        switch (controlMode) {
            case DRIVER_CONTROL:
                tickDriver(frameInfo);
                break;
            case AUTONOMOUS_CONTROL:
                tickAutonomous(frameInfo);
                break;
        }
    }

    private void tickAutonomous(FrameInfo frameInfo) {
        if (inputManager.getButtonAction(Button.B) == ButtonAction.PRESS) {
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

    private enum ControlMode {
        DRIVER_CONTROL,
        AUTONOMOUS_CONTROL
    }
}
