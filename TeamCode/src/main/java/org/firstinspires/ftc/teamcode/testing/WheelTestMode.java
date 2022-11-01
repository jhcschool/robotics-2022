package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Mode;

@TeleOp(name = "Wheel Test", group = "Iterative Opmode")
public class WheelTestMode extends Mode {

    private final DcMotorEx[] motors = new DcMotorEx[4];
    private int motorIndex;

    @Override
    public void onInit() {
        super.onInit();

        motors[0] = hardwareMap.get(DcMotorEx.class, "frontLeftMotor");
        motors[1] = hardwareMap.get(DcMotorEx.class, "rearLeftMotor");
        motors[2] = hardwareMap.get(DcMotorEx.class, "frontRightMotor");
        motors[3] = hardwareMap.get(DcMotorEx.class, "rearRightMotor");

        motors[0].setDirection(DcMotorSimple.Direction.REVERSE);
        motors[1].setDirection(DcMotorSimple.Direction.REVERSE);
    }

    @Override
    public void tick() {
        super.tick();

        if (gamepad1.dpad_up) {
            motorIndex = 0;
        } else if (gamepad1.dpad_right) {
            motorIndex = 1;
        } else if (gamepad1.dpad_down) {
            motorIndex = 2;
        } else if (gamepad1.dpad_left) {
            motorIndex = 3;
        }


        for (DcMotorEx motor : motors) {
            motor.setPower(0);
        }
        motors[motorIndex].setPower(gamepad1.right_trigger - gamepad1.left_trigger);

        telemetry.addData("Motor index:", motorIndex);
    }
}
