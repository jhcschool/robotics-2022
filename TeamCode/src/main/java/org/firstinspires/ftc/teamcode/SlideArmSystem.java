package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.game.JunctionHeight;

import java.util.HashMap;

public class SlideArmSystem {
    // IN INCHES
    private static final HashMap<JunctionHeight, Integer> ARM_POSITIONS = new HashMap() {{
        put(JunctionHeight.NONE, 0);
        put(JunctionHeight.LOW, 13.5);
        put(JunctionHeight.MID, 23.5);
        put(JunctionHeight.HIGH, 34);
    }};

    private static final float WHEEL_DIAMETER = 1.5f;
    private static final float GEAR_RATIO = 1.0f;
    private static final float TICKS_PER_REVOLUTION = 28.0f;

    private DcMotorEx slideMotor;
    private float height = 0;
    private JunctionHeight targetHeight = JunctionHeight.NONE;
    private JunctionHeight currentHeight = JunctionHeight.NONE;
    private boolean setHeight = false;
    private boolean currentlyRunning = false;
    public SlideArmSystem(DcMotorEx slideMotor) {
        this.slideMotor = slideMotor;

        slideMotor.setMode(DcMotorEx.RunMode.STOP_AND_RESET_ENCODER);
        slideMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
    }

    public void setTargetHeight(JunctionHeight targetHeight) {
        this.targetHeight = targetHeight;
    }

    public void update() {
        if (!setHeight) {
            setHeight = true;
            moveToHeight();
        }

        if (currentlyRunning && !slideMotor.isBusy()) {
            currentlyRunning = false;
            currentHeight = targetHeight;

            slideMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);
        }
    }

    private void moveToHeight() {
        if (targetHeight == currentHeight) return;

        int currentMotorPosition = slideMotor.getCurrentPosition();
        int targetPositionInInches = ARM_POSITIONS.get(targetHeight);

        int targetPositionInTicks = (int) (targetPositionInInches * TICKS_PER_REVOLUTION * GEAR_RATIO / (WHEEL_DIAMETER * Math.PI)) + currentMotorPosition;

        slideMotor.setTargetPosition(targetPositionInTicks);
        slideMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);

        currentlyRunning = true;
    }
}
