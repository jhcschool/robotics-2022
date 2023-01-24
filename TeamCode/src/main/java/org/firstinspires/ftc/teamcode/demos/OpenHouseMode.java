package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.input.Axis;
import org.firstinspires.ftc.teamcode.input.Button;
import org.firstinspires.ftc.teamcode.input.ButtonAction;
import org.firstinspires.ftc.teamcode.input.InputManager;

@Disabled
//@TeleOp(name = "Open House Mode", group = "Iterative Opmode")
public class OpenHouseMode extends Mode {

    private DcMotorEx frontLeftMotor, rearLeftMotor, rearRightMotor, frontRightMotor;
    private InputManager inputManager;
    private float powerSetting = 1.0f;
    private double powerLimit = 1.0f;

    @Override
    public void onInit() {
        super.onInit();

        frontLeftMotor = hardwareMap.get(DcMotorEx.class, "frontLeftMotor");
        rearLeftMotor = hardwareMap.get(DcMotorEx.class, "rearLeftMotor");
        frontRightMotor = hardwareMap.get(DcMotorEx.class, "frontRightMotor");
        rearRightMotor = hardwareMap.get(DcMotorEx.class, "rearRightMotor");

        rearLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        inputManager = new InputManager(gamepad1, gamepad2);
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

        inputManager.update();

        telemetry.addData("Power Setting", powerSetting);
        telemetry.addData("Power Limit", powerLimit);

        double left = 0;
        double right = 0;

        left += inputManager.getAxis(Axis.LEFT_STICK_Y);
        right += inputManager.getAxis(Axis.LEFT_STICK_Y);

        left -= inputManager.getAxis(Axis.RIGHT_STICK_X);
        right += inputManager.getAxis(Axis.RIGHT_STICK_X);

        double frontLeft = left - inputManager.getAxis(Axis.LEFT_STICK_X);
        double backLeft = left + inputManager.getAxis(Axis.LEFT_STICK_X);

        double frontRight = right + inputManager.getAxis(Axis.LEFT_STICK_X);
        double backRight = right - inputManager.getAxis(Axis.LEFT_STICK_X);

        frontLeft = withinRange(frontLeft) * powerSetting;
        backLeft = withinRange(backLeft) * powerSetting;
        frontRight = withinRange(frontRight) * powerSetting;
        backRight = withinRange(backRight) * powerSetting;

        frontLeft = limitPower(frontLeft);
        backLeft = limitPower(backLeft);
        frontRight = limitPower(frontRight);
        backRight = limitPower(backRight);

        setMotorPowers(frontLeft, backLeft, frontRight, backRight);

        if (inputManager.getAxis(Axis.LEFT_TRIGGER) < 0.5 || inputManager.getAxis(Axis.RIGHT_TRIGGER) < 0.5) {
            return;
        }

        if (inputManager.getButtonAction(Button.DPAD_UP) == ButtonAction.PRESS) {
            powerSetting += 0.1f;
        } else if (inputManager.getButtonAction(Button.DPAD_DOWN) == ButtonAction.PRESS) {
            powerSetting -= 0.1f;
        }

        if (powerSetting > 1) {
            powerSetting = 0;
        } else if (powerSetting < 0) {
            powerSetting = 1;
        }

        if (inputManager.getButtonAction(Button.DPAD_LEFT) == ButtonAction.PRESS) {
            powerLimit -= 0.1f;
        } else if (inputManager.getButtonAction(Button.DPAD_RIGHT) == ButtonAction.PRESS) {
            powerLimit += 0.1f;
        }

        if (powerLimit > 1) {
            powerLimit = 0;
        } else if (powerLimit < 0) {
            powerLimit = 1;
        }
    }

    private void setMotorPowers(double frontLeft, double rearLeft, double frontRight, double rearRight) {
        frontLeftMotor.setPower(frontLeft);
        rearLeftMotor.setPower(rearLeft);
        frontRightMotor.setPower(frontRight);
        rearRightMotor.setPower(rearRight);
    }
}
