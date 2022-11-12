package org.firstinspires.ftc.teamcode.controlled;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.drive.StandardMecanumDrive;

public class UserMovementSystem {

    private Gamepad gamepad;
    private StandardMecanumDrive drive = null;
    private DcMotorEx frontLeftMotor, rearLeftMotor, rearRightMotor, frontRightMotor;

    public UserMovementSystem(Gamepad gamepad, StandardMecanumDrive drive) {
        this.gamepad = gamepad;
        this.drive = drive;
    }

    public UserMovementSystem(Gamepad gamepad, DcMotorEx frontLeft, DcMotorEx rearLeft, DcMotorEx rearRight, DcMotorEx frontRight) {
        this.gamepad = gamepad;
        this.frontLeftMotor = frontLeft;
        this.rearLeftMotor = rearLeft;
        this.rearRightMotor = rearRight;
        this.frontRightMotor = frontRight;
    }

    public void tick() {

        double left = 0;
        double right = 0;

        left += gamepad.left_stick_y;
        right += gamepad.left_stick_y;

        left -= gamepad.right_stick_x;
        right += gamepad.right_stick_x;

        double frontLeft = left - gamepad.left_stick_x;
        double backLeft = left + gamepad.left_stick_x;

        double frontRight = right + gamepad.left_stick_x;
        double backRight = right - gamepad.left_stick_x;

        frontLeft = withinRange(frontLeft);
        backLeft = withinRange(backLeft);
        frontRight = withinRange(frontRight);
        backRight = withinRange(backRight);

        if (drive != null)
        drive.setMotorPowers(frontLeft, backLeft, frontRight, backRight);
        else setPowers(frontLeft, backLeft, frontRight, backRight);
    }

    private void setPowers(double fl, double bl, double fr, double br) {
        frontLeftMotor.setPower(fl);
        rearLeftMotor.setPower(bl);
        frontRightMotor.setPower(fr);
        rearRightMotor.setPower(br);
    }

    private double withinRange(double input) {
        return Math.max(-1, Math.min(1, input));
    }

}
