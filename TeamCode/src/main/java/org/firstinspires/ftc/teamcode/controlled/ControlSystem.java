package org.firstinspires.ftc.teamcode.controlled;

import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.Hardware;

public class ControlSystem {

    public ControlSystem() {

    }

    public void tick(Gamepad gamepad, Hardware hardware) {

        double left = 0;
        double right = 0;

        left += gamepad.left_stick_y;
        right += gamepad.left_stick_y;

        left += gamepad.right_stick_x;
        right -= gamepad.right_stick_x;

        double frontLeft = left - gamepad.left_stick_x;
        double backLeft = left + gamepad.left_stick_x;

        double frontRight = right + gamepad.left_stick_x;
        double backRight = right - gamepad.left_stick_x;

        frontLeft = withinRange(frontLeft);
        backLeft = withinRange(backLeft);
        frontRight = withinRange(frontRight);
        backRight = withinRange(backRight);

        hardware.drive.setMotorPowers(frontLeft, backLeft, frontRight, backRight);
    }

    private double withinRange(double input) {
        return Math.max(-1, Math.min(1, input));
    }

}
