package org.firstinspires.ftc.teamcode.controlled;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.drive.Drive;

public class UserMovementSystem {

    private final Gamepad gamepad;
    private Drive drive = null;
    private DcMotorEx frontLeftMotor, rearLeftMotor, rearRightMotor, frontRightMotor;
    private MovementMode movementMode = MovementMode.ROBOT_ORIENTED;
    public UserMovementSystem(Gamepad gamepad, Drive drive) {
        this.gamepad = gamepad;
        this.drive = drive;
    }

    public UserMovementSystem(Gamepad gamepad, Drive drive, MovementMode movementMode) {
        this(gamepad, drive);

        this.movementMode = movementMode;
    }

    public UserMovementSystem(Gamepad gamepad, DcMotorEx frontLeft, DcMotorEx rearLeft, DcMotorEx rearRight, DcMotorEx frontRight) {
        this.gamepad = gamepad;
        this.frontLeftMotor = frontLeft;
        this.rearLeftMotor = rearLeft;
        this.rearRightMotor = rearRight;
        this.frontRightMotor = frontRight;
    }

    public void update() {
        if (movementMode == MovementMode.FIELD_ORIENTED) {
            setFieldCentricPowers();
        }

        double left = 0;
        double right = 0;

        left -= gamepad.left_stick_y;
        right -= gamepad.left_stick_y;

        left += gamepad.right_stick_x;
        right -= gamepad.right_stick_x;

        double frontLeft = left + gamepad.left_stick_x;
        double backLeft = left - gamepad.left_stick_x;

        double frontRight = right - gamepad.left_stick_x;
        double backRight = right + gamepad.left_stick_x;

        frontLeft = withinRange(frontLeft);
        backLeft = withinRange(backLeft);
        frontRight = withinRange(frontRight);
        backRight = withinRange(backRight);

        if (drive != null)
            drive.setMotorPowers(frontLeft, backLeft, backRight, frontRight);
        else setPowers(frontLeft, backLeft, backRight, frontRight);
    }

    private void setPowers(double fl, double bl, double br, double fr) {
        frontLeftMotor.setPower(fl);
        rearLeftMotor.setPower(bl);
        rearRightMotor.setPower(br);
        frontRightMotor.setPower(fr);
    }

    private void setFieldCentricPowers() {
        Vector2d input = new Vector2d(
                -gamepad.left_stick_y,
                -gamepad.left_stick_x
        ).rotated(-drive.getPoseEstimate().getHeading());

        drive.setWeightedDrivePower(
                new Pose2d(
                        input.getX(),
                        input.getY(),
                        -gamepad.right_stick_x
                )
        );
    }

    private double withinRange(double input) {
        return Math.max(-1, Math.min(1, input));
    }


    public void setMovementMode(MovementMode movementMode) {
        this.movementMode = movementMode;
    }

    public void toggleMovementMode() {
        if (movementMode == MovementMode.ROBOT_ORIENTED) {
            movementMode = MovementMode.FIELD_ORIENTED;
        } else {
            movementMode = MovementMode.ROBOT_ORIENTED;
        }
    }



    enum MovementMode {
        ROBOT_ORIENTED,
        FIELD_ORIENTED
    }

}
