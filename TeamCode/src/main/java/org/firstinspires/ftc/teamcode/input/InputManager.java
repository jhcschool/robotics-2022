package org.firstinspires.ftc.teamcode.input;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.HashMap;

public class InputManager {
    private Gamepad gamepad1;
    private Gamepad gamepad2;

    private HashMap<Button, ButtonAction>[] actionStates = new HashMap[2];

    public InputManager(Gamepad gamepad1, Gamepad gamepad2) {
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;

        actionStates[0] = new HashMap<>();
        actionStates[1] = new HashMap<>();

        for (Button button : Button.values()) {
            actionStates[0].put(button, ButtonAction.NONE);
            actionStates[1].put(button, ButtonAction.NONE);
        }
    }

    public boolean getButton(InputDevice device, Button button) {
        Gamepad gamepad = getGamepad(device);

        String name = button.toString().toLowerCase();
        try {
            return gamepad.getClass().getDeclaredField(name).getBoolean(gamepad);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return false;
    }

    public boolean getButton(Button button) {
        return getButton(InputDevice.GAMEPAD1, button) || getButton(InputDevice.GAMEPAD2, button);
    }

    public float getAxis(InputDevice device, Axis axis) {
        Gamepad gamepad = getGamepad(device);

        String name = axis.toString().toLowerCase();
        try {
            return gamepad.getClass().getDeclaredField(name).getFloat(gamepad);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        return 0;
    }

    public float getAxis(Axis axis) {
        float axis1 = getAxis(InputDevice.GAMEPAD1, axis);
        float axis2 = getAxis(InputDevice.GAMEPAD2, axis);

        if (Math.abs(axis1) > Math.abs(axis2)) {
            return axis1;
        }

        return axis2;
    }

    private Gamepad getGamepad(InputDevice device) {
        switch (device) {
            case GAMEPAD1:
                return gamepad1;
            case GAMEPAD2:
                return gamepad2;
        }

        return null;
    }

    public void update() {
        updateGamepad(gamepad1, 0);
        updateGamepad(gamepad2, 1);
    }

    private void updateGamepad(Gamepad gamepad, int index) {
        for (Button button : Button.values()) {
            ButtonAction action = actionStates[index].get(button);

            boolean pressed = getButton(InputDevice.values()[index], button);

            if (pressed) {
                if (action == ButtonAction.NONE) {
                    actionStates[index].put(button, ButtonAction.PRESS);
                } else {
                    actionStates[index].put(button, ButtonAction.NONE);
                }
            } else {
                if (action == ButtonAction.PRESS) {
                    actionStates[index].put(button, ButtonAction.RELEASE);
                } else {
                    actionStates[index].put(button, ButtonAction.NONE);
                }
            }
        }
    }

    public ButtonAction getButtonAction(InputDevice device, Button button) {
        return actionStates[device.ordinal()].get(button);
    }

    public ButtonAction getButtonAction(Button button) {
        ButtonAction action = getButtonAction(InputDevice.GAMEPAD1, button);
        if (action == ButtonAction.NONE) {
            action = getButtonAction(InputDevice.GAMEPAD2, button);
        }

        return action;
    }
}
