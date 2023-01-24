package org.firstinspires.ftc.teamcode.testing.controlled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.input.Button;
import org.firstinspires.ftc.teamcode.input.ButtonAction;
import org.firstinspires.ftc.teamcode.input.GrizzlyGamepad;

@Disabled
//@TeleOp(name = "Clipper Test", group = "Iterative Opmode")
public class ClipperTestMode extends Mode {

    private static final double CLIPPED = 0.3;
    private static final double RELEASED = 0.55;
    GrizzlyGamepad gamepad;
    private Servo clipper;
    private boolean clip = false;

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

        clipper.setPosition(clip ? CLIPPED : RELEASED);

        if (gamepad.getButtonAction(Button.A) == ButtonAction.PRESS) {
            clip = !clip;
        }
    }
}
