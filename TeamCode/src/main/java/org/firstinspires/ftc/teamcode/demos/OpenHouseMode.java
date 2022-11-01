package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.input.Axis;
import org.firstinspires.ftc.teamcode.input.Button;
import org.firstinspires.ftc.teamcode.input.ButtonAction;
import org.firstinspires.ftc.teamcode.input.InputDevice;
import org.firstinspires.ftc.teamcode.input.InputManager;

@TeleOp(name = "Open House Mode", group = "Demos")
public class OpenHouseMode extends Mode {

    private DcMotorEx frontLeftMotor, rearLeftMotor, rearRightMotor, frontRightMotor;
    private InputManager inputManager;

    public OpenHouseMode() {
        inputManager = new InputManager(gamepad1, gamepad2);
    }

    @Override
    public void onInit() {
        super.onInit();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

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

    private float powerSetting = 0.5f;

    @Override
    public void tick() {
        super.tick();

        telemetry.update();
        inputManager.update();

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

        setMotorPowers(frontLeft, backLeft, frontRight, backRight);

        if (inputManager.getButtonAction(InputDevice.GAMEPAD1, Button.A) == ButtonAction.PRESS) {
            powerSetting += 0.1f;
        } else if (inputManager.getButtonAction(InputDevice.GAMEPAD1, Button.B) == ButtonAction.PRESS) {
            powerSetting -= 0.1f;
        }

        if (powerSetting > 1) {
            powerSetting = 0;
        } else if (powerSetting < 0) {
            powerSetting = 1;
        }

        telemetry.addData("Power Setting", powerSetting);
    }

    private void setMotorPowers(double frontLeft, double rearLeft, double frontRight, double rearRight) {
        frontLeftMotor.setPower(frontLeft);
        rearLeftMotor.setPower(rearLeft);
        frontRightMotor.setPower(frontRight);
        rearRightMotor.setPower(rearRight);
    }
}
