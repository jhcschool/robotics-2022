package org.firstinspires.ftc.teamcode.testing.controlled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.input.Button;
import org.firstinspires.ftc.teamcode.input.ButtonAction;
import org.firstinspires.ftc.teamcode.input.InputManager;

@Disabled
//@TeleOp(name = "Servo Test", group = "Iterative Opmode")
public class ServoTestMode extends Mode {

    private Servo clipper;
    private InputManager inputManager;

    private double setting = 0.55;

    @Override
    public void onInit() {
        super.onInit();
        clipper = hardwareMap.get(Servo.class, "clipper");

        inputManager = new InputManager(gamepad1, gamepad2);
    }

    @Override
    public void onStart() {
        super.onStart();

        setting = clipper.getPosition();
    }

    @Override
    public void tick() {
        super.tick();
        inputManager.update();

        telemetry.addData("Setting", setting);
        clipper.setPosition(setting);

        if (inputManager.getButtonAction(Button.DPAD_RIGHT) == ButtonAction.PRESS) setting += 0.1;
        if (inputManager.getButtonAction(Button.DPAD_LEFT) == ButtonAction.PRESS) setting -= 0.1;

        setting = Math.max(setting, 0);
        setting = Math.min(setting, 1);
    }
}
