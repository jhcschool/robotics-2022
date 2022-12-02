package org.firstinspires.ftc.teamcode.testing.controlled;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.input.Button;
import org.firstinspires.ftc.teamcode.input.ButtonAction;
import org.firstinspires.ftc.teamcode.input.GrizzlyGamepad;

@TeleOp(name = "Basic Control Test", group = "Iterative Opmode")
public class BasicControlTestMode extends Mode {

    private DcMotorEx frontLeftMotor, rearLeftMotor, rearRightMotor, frontRightMotor;

    private Servo clipper;
    private static double CLIPPED = 0.3;
    private static double RELEASED = 0.55;

    private boolean clip = false;

    GrizzlyGamepad gamepad;

    @Override
    public void onInit() {
        super.onInit();

        frontLeftMotor = hardwareMap.get(DcMotorEx.class, "frontLeftMotor");
        rearLeftMotor = hardwareMap.get(DcMotorEx.class, "rearLeftMotor");
        frontRightMotor = hardwareMap.get(DcMotorEx.class, "frontRightMotor");
        rearRightMotor = hardwareMap.get(DcMotorEx.class, "rearRightMotor");

        rearRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        gamepad = new GrizzlyGamepad(gamepad1);
        clipper = hardwareMap.get(Servo.class, "clipper");
    }

    private double withinRange(double input) {
        return Math.max(-1, Math.min(1, input));
    }

    @Override
    public void tick() {
        super.tick();
        gamepad.update();

        telemetry.addData("Clipped", clip);

        clipper.setPosition(clip?CLIPPED:RELEASED);

        if (gamepad.getButtonAction(Button.A) == ButtonAction.PRESS) {
            clip = !clip;
        }

        double left = 0;
        double right = 0;

        left -= gamepad1.left_stick_y;
        right -= gamepad1.left_stick_y;

        left += gamepad1.right_stick_x;
        right -= gamepad1.right_stick_x;

        double frontLeft = left + gamepad1.left_stick_x;
        double backLeft = left - gamepad1.left_stick_x;

        double frontRight = right - gamepad1.left_stick_x;
        double backRight = right + gamepad1.left_stick_x;

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
