package org.firstinspires.ftc.teamcode.controlled;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.checkerframework.checker.units.qual.C;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FrameInfo;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.Layer;
import org.firstinspires.ftc.teamcode.LayerInitInfo;
import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.arm.ClipperSystem;
import org.firstinspires.ftc.teamcode.game.AllianceMember;
import org.firstinspires.ftc.teamcode.input.Axis;
import org.firstinspires.ftc.teamcode.input.Button;
import org.firstinspires.ftc.teamcode.input.ButtonAction;
import org.firstinspires.ftc.teamcode.input.InputManager;

public class ControlledLayer extends Layer {

    private static final double DEFAULT_POWER_STATE = 0.60;
    private static final double TOGGLE_POWER_STATE = 1.0;

    private Hardware hardware;
    private Telemetry telemetry;
    private InputManager inputManager;
    private ControlMode controlMode = ControlMode.DRIVER_CONTROL;

    private UserMovementSystem userMovementSystem;
    private ClipperSystem clipperSystem;

    private Trajectory trajectory = null;
    private boolean clipped = false;

    @Override
    public void init(LayerInitInfo info) {
        super.init(info);
        hardware = info.hardware;
        inputManager = info.inputManager;
        telemetry = info.telemetry;

        hardware.drive.setPoseEstimate(PoseStorage.robotPose);
        PoseStorage.robotPose = new Pose2d();

        userMovementSystem = new UserMovementSystem(hardware.gamepad1, hardware.drive);
        clipperSystem = new ClipperSystem(hardware.leftServo, hardware.rightServo);
    }

    @Override
    public void tick(FrameInfo frameInfo) {
        super.tick(frameInfo);
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
        userMovementSystem.update();

        if (inputManager.getButtonAction(Button.X) == ButtonAction.PRESS) {
            userMovementSystem.toggleMovementMode();
        }

        if (inputManager.getButton(Button.LEFT_BUMPER)) {
            userMovementSystem.setPowerMultiplier(TOGGLE_POWER_STATE);
        } else {
            userMovementSystem.setPowerMultiplier(DEFAULT_POWER_STATE);
        }

        if (inputManager.getButtonAction(Button.RIGHT_BUMPER) == ButtonAction.PRESS) {
            clipped = !clipped;
            clipperSystem.setClipped(clipped);
        }

        {
            float total = inputManager.getAxis(Axis.RIGHT_TRIGGER) - inputManager.getAxis(Axis.LEFT_TRIGGER) * 0.7f;
            hardware.slideArmMotor.setPower(total);

            if (Math.abs(total) < 0.03) {
                if (clipped) {
                    hardware.slideArmMotor.setPower(0.13);
                } else {
                    hardware.slideArmMotor.setPower(0.07);
                }
            }
        }

        double allianceCorrection = userMovementSystem.getAllianceCorrection();
        if (inputManager.getButtonAction(Button.DPAD_RIGHT) == ButtonAction.PRESS) {
            allianceCorrection += Math.toRadians(90);
            userMovementSystem.setAllianceCorrection(allianceCorrection);
        } else if (inputManager.getButtonAction(Button.DPAD_LEFT) == ButtonAction.PRESS) {
            allianceCorrection -= Math.toRadians(90);
            userMovementSystem.setAllianceCorrection(allianceCorrection);
        }

        telemetry.addData("Movement Mode", userMovementSystem.getMovementMode().name());
        telemetry.addData("Alliance Correction (in degrees): ", Math.toDegrees(allianceCorrection));
    }

    private enum ControlMode {
        DRIVER_CONTROL,
        AUTONOMOUS_CONTROL
    }
}
