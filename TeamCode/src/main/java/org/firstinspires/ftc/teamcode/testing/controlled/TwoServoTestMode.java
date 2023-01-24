package org.firstinspires.ftc.teamcode.testing.controlled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.input.Button;
import org.firstinspires.ftc.teamcode.input.ButtonAction;
import org.firstinspires.ftc.teamcode.input.InputManager;

@Disabled
//@TeleOp(name = "Two Servo Test", group = "Iterative Opmode")
public class TwoServoTestMode extends Mode {

    private Servo leftServo;
    private Servo rightServo;
    private InputManager inputManager;

    private double settingLeft = 0.55;
    private double settingRight = 0.55;

    @Override
    public void onInit() {
        super.onInit();
        leftServo = hardwareMap.get(Servo.class, "leftServo");
        rightServo = hardwareMap.get(Servo.class, "rightServo");

        inputManager = new InputManager(gamepad1, gamepad2);
    }

    @Override
    public void onStart() {
        super.onStart();

        leftServo.setDirection(Servo.Direction.REVERSE);

        settingLeft = leftServo.getPosition();
        settingRight = rightServo.getPosition();
    }

    @Override
    public void tick() {
        super.tick();
        inputManager.update();

        telemetry.addData("Setting left", settingLeft);
        telemetry.addData("Setting right", settingRight);
        leftServo.setPosition(settingLeft);
        rightServo.setPosition(settingRight);

        if (inputManager.getButtonAction(Button.DPAD_RIGHT) == ButtonAction.PRESS)
            settingLeft += 0.1;
        if (inputManager.getButtonAction(Button.DPAD_LEFT) == ButtonAction.PRESS)
            settingLeft -= 0.1;

        if (inputManager.getButtonAction(Button.DPAD_UP) == ButtonAction.PRESS) settingRight += 0.1;
        if (inputManager.getButtonAction(Button.DPAD_DOWN) == ButtonAction.PRESS)
            settingRight -= 0.1;


        settingLeft = Math.max(settingLeft, 0);
        settingLeft = Math.min(settingLeft, 1);
        settingRight = Math.max(settingRight, 0);
        settingRight = Math.min(settingRight, 1);
    }
}
