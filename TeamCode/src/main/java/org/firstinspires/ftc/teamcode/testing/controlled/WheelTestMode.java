package org.firstinspires.ftc.teamcode.testing.controlled;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Mode;

@TeleOp(name = "Wheel Test", group = "Iterative Opmode")
public class WheelTestMode extends Mode {

    private final DcMotorSimple[] motors = new DcMotorSimple[4];
    private int motorIndex;

    @Override
    public void onInit() {
        super.onInit();

        motors[0] = hardwareMap.get(DcMotorSimple.class, "frontLeftMotor");
        motors[1] = hardwareMap.get(DcMotorSimple.class, "rearLeftMotor");
        motors[2] = hardwareMap.get(DcMotorSimple.class, "rearRightMotor");
        motors[3] = hardwareMap.get(DcMotorSimple.class, "frontRightMotor");

        motors[2].setDirection(DcMotorSimple.Direction.REVERSE);
        motors[3].setDirection(DcMotorSimple.Direction.REVERSE);
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


        for (DcMotorSimple motor : motors) {
            motor.setPower(0);
        }
        motors[motorIndex].setPower(gamepad1.right_trigger - gamepad1.left_trigger);

        telemetry.addData("Motor index:", motorIndex);
    }

}
