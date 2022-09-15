package org.firstinspires.ftc.teamcode.controlled;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Hardware;

public class ControlSystem {

    public ControlSystem() {

    }

    public void tick(Gamepad gamepad, Hardware hardware) {

        int left = 0;
        int right = 0;

        left += gamepad.left_stick_y;
        right += gamepad.left_stick_y;

        left += gamepad.right_stick_x;
        right -= gamepad.right_stick_x;

        left = Math.min(1, Math.max(-1, left));
        right = Math.min(1, Math.max(-1, right));

        hardware.frontLeftMotor.setPower(left);
        hardware.backLeftMotor.setPower(left);
        hardware.frontRightMotor.setPower(right);
        hardware.backRightMotor.setPower(right);
    }

}
