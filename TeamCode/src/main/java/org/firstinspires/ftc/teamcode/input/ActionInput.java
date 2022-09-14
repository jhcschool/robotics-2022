package org.firstinspires.ftc.teamcode.input;

public class ActionInput implements  UserInput{

    @Override
    public float getIntensity() {
        if (state == ActionState.INACTIVE || state == ActionState.RELEASED) return 0;
        return 1;
    }

    @Override
    public ActionState getState() {
        return state;
    }

    @Override
    public void registerInput(float value) {
        registerInput(value > 0.5);
    }

    @Override
    public void registerInput(boolean value) {
        active = value;
    }

    public void tick() {
        if (active && state == ActionState.INACTIVE)
        {
            state = ActionState.PRESSED;
        }
        else if (active && state == ActionState.PRESSED)
        {
            state = ActionState.HELD;
        }
        else if (!active && state == ActionState.HELD)
        {
            state = ActionState.RELEASED;
        }
        else if (!active && state == ActionState.RELEASED)
        {
            state = ActionState.INACTIVE;
        }
    }

    boolean active = false;
    ActionState state = ActionState.INACTIVE;
}
