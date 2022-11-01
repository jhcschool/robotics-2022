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

    boolean clipped = false;

    @Override
    public void tick() {
        super.tick();

        telemetry.addData("Clipped", clipped);

        if (clipped) {
            clipper.setPosition(0);
        } else {
            clipper.setPosition(0.5);
        }

        if (gamepad1.a) {
            clipped = true;
        }

        if (gamepad1.b) {
            clipped = false;
        }
    }
}
