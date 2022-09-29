package org.firstinspires.ftc.teamcode.input;

import com.qualcomm.robotcore.hardware.Gamepad;

import java.util.ArrayList;
import java.util.Map;


// this input system was ported from my game engine, you don't need to use it if you don't want to

public class InputManager {

    Map<String, ActionMapping> actionMappings;
    Map<String, AxisMapping> axisMappings;
    Map<InputCode, UserInput> inputs;
    InputState state;
    ArrayList<Gamepad> managedGamepads;

    public void registerGamepadInput(Gamepad gamepad) {
        registerInput(InputCode.A, gamepad.a);
        registerInput(InputCode.B, gamepad.b);
        registerInput(InputCode.X, gamepad.x);
        registerInput(InputCode.Y, gamepad.y);
        registerInput(InputCode.DPAD_UP, gamepad.dpad_up);
        registerInput(InputCode.DPAD_DOWN, gamepad.dpad_down);
        registerInput(InputCode.DPAD_LEFT, gamepad.dpad_left);
        registerInput(InputCode.DPAD_RIGHT, gamepad.dpad_right);
        registerInput(InputCode.LEFT_BUMPER, gamepad.left_bumper);
        registerInput(InputCode.RIGHT_BUMPER, gamepad.right_bumper);
        registerInput(InputCode.LEFT_TRIGGER, gamepad.left_trigger);
        registerInput(InputCode.RIGHT_TRIGGER, gamepad.right_trigger);
        registerInput(InputCode.LEFT_STICK_X, gamepad.left_stick_x);
        registerInput(InputCode.LEFT_STICK_Y, gamepad.left_stick_y);
        registerInput(InputCode.RIGHT_STICK_X, gamepad.right_stick_x);
        registerInput(InputCode.RIGHT_STICK_Y, gamepad.right_stick_y);
        registerInput(InputCode.LEFT_STICK_BUTTON, gamepad.left_stick_button);
        registerInput(InputCode.RIGHT_STICK_BUTTON, gamepad.right_stick_button);
        registerInput(InputCode.START, gamepad.start);
        registerInput(InputCode.BACK, gamepad.back);
    }

    public void registerInput(InputCode code, double value) {
        if (!inputs.containsKey(code)) {
            inputs.put(code, new AxisInput());
        }
    }

    public void registerInput(InputCode code, boolean value) {
        if (!inputs.containsKey(code)) {
            inputs.put(code, new ActionInput());
        }
    }

    public void addManagedGamepad(Gamepad gamepad) {
        managedGamepads.add(gamepad);
    }

    public void addAxisMapping(String name, AxisMapping mapping) {
        axisMappings.put(name, mapping);
    }

    public void addActionMapping(String name, ActionMapping mapping) {
        actionMappings.put(name, mapping);
    }

    public InputState tick() {

        state.clear();

        for (Gamepad gamepad : managedGamepads) {
            registerGamepadInput(gamepad);
        }

        for (Map.Entry<String, ActionMapping> entry : actionMappings.entrySet()) {
            ActionState actionState = ActionState.INACTIVE;

            for (InputCode code : entry.getValue().codes) {
                if (inputs.containsKey(code)) {
                    actionState = inputs.get(code).getState();
                }
            }

            state.registerAction(entry.getKey(), actionState);
        }

        for (Map.Entry<String, AxisMapping> entry : axisMappings.entrySet()) {
            double axisState = 0;

            for (AxisActivation activation : entry.getValue().activations) {
                if (inputs.containsKey(activation.code)) {
                    double input = inputs.get(activation.code).getIntensity() * activation.intensityMultiplier;

                    if (Math.abs(input) > Math.abs(axisState)) {
                        axisState = input;
                    }
                }
            }

            state.registerAxis(entry.getKey(), axisState);
        }


        for (Map.Entry<InputCode, UserInput> entry : inputs.entrySet()) {
            entry.getValue().tick();
        }

        return state;
    }

    public class ActionMapping {
        ArrayList<InputCode> codes;
    }

    public class AxisActivation {
        double intensityMultiplier;
        InputCode code;
    }

    public class AxisMapping {
        ArrayList<AxisActivation> activations;
    }
}
