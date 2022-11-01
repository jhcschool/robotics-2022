package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Mode;

@TeleOp(name = "Clipper Test", group = "Tests")
public class ClipperTestMode extends Mode {

    private Servo clipper;

    @Override
    public void onInit() {
        super.onInit();
        clipper = hardwareMap.get(Servo.class, "clipper");
    }

    @Override
    public void tick() {
        super.tick();
        telemetry.update();

        double servoPosition = gamepad1.right_trigger;

        telemetry.addData("Servo Position", servoPosition);

        clipper.setPosition(servoPosition);
    }
}
