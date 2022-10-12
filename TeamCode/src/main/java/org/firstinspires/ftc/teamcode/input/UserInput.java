package org.firstinspires.ftc.teamcode.input;

public interface UserInput {

    public float getIntensity();

    public ActionState getState();

    public void registerInput(boolean value);

    public void registerInput(float value);

    public void tick();
}
