package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Mode;

@TeleOp(name = "Clipper Test", group = "Iterative Opmode")
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
        float position = gamepad1.right_trigger;

        telemetry.addData("Clipped", position);
        clipper.setPosition(position);
    }
}
