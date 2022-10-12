package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Mode;

@TeleOp(name = "Basic Control Test", group = "Iterative Opmode")
public class BasicControlTestMode extends Mode {

    private DcMotorEx frontLeftMotor, rearLeftMotor, rearRightMotor, frontRightMotor;

    @Override
    public void onInit() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        frontLeftMotor = hardwareMap.get(DcMotorEx.class, "frontLeftMotor");
        rearLeftMotor = hardwareMap.get(DcMotorEx.class, "rearLeftMotor");
        rearRightMotor = hardwareMap.get(DcMotorEx.class, "rearRightMotor");
        frontRightMotor = hardwareMap.get(DcMotorEx.class, "frontRightMotor");
    }

    private double withinRange(double input) {
        return Math.max(-1, Math.min(1, input));
    }

    @Override
    public void tick() {
        telemetry.update();

        double left = 0;
        double right = 0;

        left += gamepad1.left_stick_y;
        right += gamepad1.left_stick_y;

        left += gamepad1.right_stick_x;
        right -= gamepad1.right_stick_x;

        double frontLeft = left - gamepad1.left_stick_x;
        double backLeft = left + gamepad1.left_stick_x;

        double frontRight = right + gamepad1.left_stick_x;
        double backRight = right - gamepad1.left_stick_x;

        frontLeft = withinRange(frontLeft);
        backLeft = withinRange(backLeft);
        frontRight = withinRange(frontRight);
        backRight = withinRange(backRight);

        setMotorPowers(frontLeft, backLeft, frontRight, backRight);
    }

    private void setMotorPowers(double frontLeft, double rearLeft, double frontRight, double rearRight) {
        frontLeftMotor.setPower(frontLeft);
        rearLeftMotor.setPower(rearLeft);
        frontRightMotor.setPower(frontRight);
        rearRightMotor.setPower(rearRight);
    }
}
