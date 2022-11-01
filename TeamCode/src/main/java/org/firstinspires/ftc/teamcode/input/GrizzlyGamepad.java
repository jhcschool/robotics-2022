package org.firstinspires.ftc.teamcode.input;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.HashMap;

public class GrizzlyGamepad {

    private Gamepad gamepad;
    private ElapsedTime timer;

    private float debounceTime = 30;

    private HashMap<Button, ButtonAction> buttonActions;
    private HashMap<Button, Boolean> buttonStates;
    private HashMap<Button, Float> buttonTimes;

    public GrizzlyGamepad(Gamepad gamepad) {
        this.gamepad = gamepad;

        timer = new ElapsedTime();
        buttonActions = new HashMap<>();
        buttonStates = new HashMap<>();
        buttonTimes = new HashMap<>();

        timer.reset();

        for (Button button : Button.values()) {
            buttonActions.put(button, ButtonAction.NONE);
            buttonStates.put(button, false);
            buttonTimes.put(button, 0.0f);
        }
    }

    public void setDebounceTime(float debounceTime) {
        this.debounceTime = debounceTime;
    }

    public float getAxis(Axis axis) {
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

    public boolean getButton(Button button) {
        return buttonStates.get(button);
    }

    public ButtonAction getButtonAction(Button button) {
        return buttonActions.get(button);
    }

    private boolean shouldDebounce(Button button) {
        return timer.milliseconds() - buttonTimes.get(button) < debounceTime;
    }

    public void update() {
        for (Button button : Button.values()) {
            if (shouldDebounce(button)) {
                continue;
            }

            boolean pressed = getButton(button);
            ButtonAction action = buttonActions.get(button);

            if (pressed) {
                if (action == ButtonAction.NONE) {
                    buttonActions.put(button, ButtonAction.PRESS);
                    buttonTimes.put(button, (float) timer.milliseconds());
                } else {
                    buttonActions.put(button, ButtonAction.NONE);
                }
            } else {
                if (action == ButtonAction.PRESS) {
                    buttonActions.put(button, ButtonAction.RELEASE);
                    buttonTimes.put(button, (float) timer.milliseconds());
                } else {
                    buttonActions.put(button, ButtonAction.NONE);
                }
            }

            buttonStates.put(button, pressed);
        }
    }
}
