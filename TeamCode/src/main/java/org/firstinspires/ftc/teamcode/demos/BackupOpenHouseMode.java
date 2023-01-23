package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Mode;

@Disabled
//@TeleOp(name = "Backup Open House Mode", group = "Iterative Opmode")
public class BackupOpenHouseMode extends Mode {

    private DcMotorEx frontLeftMotor, rearLeftMotor, rearRightMotor, frontRightMotor;
    private float powerSetting = 1.0f;
    private float powerLimit = 1.0f;

    @Override
    public void onInit() {
        frontLeftMotor = hardwareMap.get(DcMotorEx.class, "frontLeftMotor");
        rearLeftMotor = hardwareMap.get(DcMotorEx.class, "rearLeftMotor");
        frontRightMotor = hardwareMap.get(DcMotorEx.class, "frontRightMotor");
        rearRightMotor = hardwareMap.get(DcMotorEx.class, "rearRightMotor");

        rearLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    private double withinRange(double input) {
        return Math.max(-1, Math.min(1, input));
    }

    private double limitPower(double power) {
        return Math.max(-powerLimit, Math.min(powerLimit, power));
    }

    @Override
    public void tick() {
        super.tick();

        telemetry.addData("Power Setting", powerSetting);
        telemetry.addData("Power Limit", powerLimit);

        double left = 0;
        double right = 0;

        left += gamepad1.left_stick_y;
        right += gamepad1.left_stick_y;

        left -= gamepad1.right_stick_x;
        right += gamepad1.right_stick_x;

        double frontLeft = left - gamepad1.left_stick_x;
        double backLeft = left + gamepad1.left_stick_x;

        double frontRight = right + gamepad1.left_stick_x;
        double backRight = right - gamepad1.left_stick_x;

        frontLeft = withinRange(frontLeft) * powerSetting;
        backLeft = withinRange(backLeft) * powerSetting;
        frontRight = withinRange(frontRight) * powerSetting;
        backRight = withinRange(backRight) * powerSetting;

        frontLeft = limitPower(frontLeft);
        backLeft = limitPower(backLeft);
        frontRight = limitPower(frontRight);
        backRight = limitPower(backRight);

        setMotorPowers(frontLeft, backLeft, frontRight, backRight);

        if (gamepad1.left_trigger < 0.5 || gamepad1.right_trigger < 0.5) return;

        if (gamepad1.dpad_left) {
            powerSetting = 0.25f;
        }
        if (gamepad1.dpad_right) {
            powerSetting = 0.5f;
        }
        if (gamepad1.dpad_up) {
            powerSetting = 0.75f;
        }
        if (gamepad1.dpad_down) {
            powerSetting = 1f;
        }

        if (gamepad1.x) {
            powerLimit = 0.25f;
        }
        if (gamepad1.b) {
            powerLimit = 0.5f;
        }
        if (gamepad1.y) {
            powerLimit = 0.75f;
        }
        if (gamepad1.a) {
            powerLimit = 1f;
        }
    }

    private void setMotorPowers(double frontLeft, double rearLeft, double frontRight, double rearRight) {
        frontLeftMotor.setPower(frontLeft);
        rearLeftMotor.setPower(rearLeft);
        frontRightMotor.setPower(frontRight);
        rearRightMotor.setPower(rearRight);
    }
}
