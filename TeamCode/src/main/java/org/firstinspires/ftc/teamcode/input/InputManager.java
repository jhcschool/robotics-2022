package org.firstinspires.ftc.teamcode.input;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.HashMap;

public class InputManager {

    private GrizzlyGamepad gamepad1;
    private GrizzlyGamepad gamepad2;

    public InputManager(Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1 = new GrizzlyGamepad(gamepad1);
        this.gamepad2 = new GrizzlyGamepad(gamepad2);
    }

    public boolean getButton(InputDevice device, Button button) {
        GrizzlyGamepad gamepad = getGamepad(device);
        return gamepad.getButton(button);
    }

    public boolean getButton(Button button) {
        return getButton(InputDevice.GAMEPAD1, button) || getButton(InputDevice.GAMEPAD2, button);
    }

    public float getAxis(InputDevice device, Axis axis) {
        GrizzlyGamepad gamepad = getGamepad(device);
        return gamepad.getAxis(axis);
    }

    public float getAxis(Axis axis) {
        float axis1 = getAxis(InputDevice.GAMEPAD1, axis);
        float axis2 = getAxis(InputDevice.GAMEPAD2, axis);

        if (Math.abs(axis1) > Math.abs(axis2)) {
            return axis1;
        }

        return axis2;
    }

    private GrizzlyGamepad getGamepad(InputDevice device) {
        switch (device) {
            case GAMEPAD1:
                return gamepad1;
            case GAMEPAD2:
                return gamepad2;
        }

        return null;
    }

    public void update() {
        gamepad1.update();
        gamepad2.update();
    }

    public ButtonAction getButtonAction(InputDevice device, Button button) {
        GrizzlyGamepad gamepad = getGamepad(device);
        return gamepad.getButtonAction(button);
    }

    public ButtonAction getButtonAction(Button button) {
        ButtonAction action = getButtonAction(InputDevice.GAMEPAD1, button);
        if (action == ButtonAction.NONE) {
            action = getButtonAction(InputDevice.GAMEPAD2, button);
        }

        return action;
    }
}
