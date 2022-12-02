package org.firstinspires.ftc.teamcode.testing.controlled;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.input.Button;
import org.firstinspires.ftc.teamcode.input.ButtonAction;
import org.firstinspires.ftc.teamcode.input.GrizzlyGamepad;

@TeleOp(name = "Clipper Test", group = "Iterative Opmode")
public class ClipperTestMode extends Mode {

    private Servo clipper;
    private static double CLIPPED = 0.3;
    private static double RELEASED = 0.55;

    private boolean clip = false;

    GrizzlyGamepad gamepad;

    @Override
    public void onInit() {
        super.onInit();
        clipper = hardwareMap.get(Servo.class, "clipper");
        gamepad = new GrizzlyGamepad(gamepad1);
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
    }
}
