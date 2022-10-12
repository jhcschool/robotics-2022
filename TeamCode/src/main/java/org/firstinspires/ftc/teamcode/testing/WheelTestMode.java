package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.Mode;

@TeleOp(name = "Wheel Test", group = "Iterative Opmode")
public class WheelTestMode extends Mode {

    private DcMotorEx[] motors = new DcMotorEx[4];

    @Override
    public void onInit() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        motors[0] = hardwareMap.get(DcMotorEx.class, "frontLeftMotor");
        motors[1] = hardwareMap.get(DcMotorEx.class, "rearLeftMotor");
        motors[2] = hardwareMap.get(DcMotorEx.class, "rearRightMotor");
        motors[3] = hardwareMap.get(DcMotorEx.class, "frontRightMotor");
    }

    private int motorIndex = 0;
    private boolean reverse = false;

    @Override
    public void tick() {
        if (gamepad1.a) {
            reverse = !reverse;
        }

        if (gamepad1.dpad_up) {
            motorIndex = (motorIndex + 1) % motors.length;
            changePower();
        }

        if (gamepad1.dpad_down) {
            motorIndex = (motorIndex - 1 + motors.length) % motors.length;
            changePower();
        }
    }

    private void changePower() {
        for (DcMotorEx motor : motors) {
            motor.setPower(0);
        }

        if (reverse) {
            motors[motorIndex].setPower(-1);
        } else {
            motors[motorIndex].setPower(1);
        }
    }



}
