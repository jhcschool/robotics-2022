package org.firstinspires.ftc.teamcode.input;

public interface UserInput {

    float getIntensity();

    ActionState getState();

    void registerInput(boolean value);

    void registerInput(float value);

    void tick();
}
