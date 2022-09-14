package org.firstinspires.ftc.teamcode.input;

public class AxisInput implements UserInput {


    @Override
    public ActionState getState() {
        return intensity > 0 ? ActionState.PRESSED : ActionState.RELEASED;
    }

    @Override
    public float getIntensity() {
        return intensity;
    }

    @Override
    public void registerInput(float value) {
        intensity = value;
    }

    @Override
    public void registerInput(boolean value) {
        intensity = value ? 1 : 0;
    }

    @Override
    public void tick() {
        intensity = 0;
    }

    private float intensity = 0;
}
