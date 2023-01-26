package org.firstinspires.ftc.teamcode.arm;

import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.game.JunctionHeight;

import java.util.HashMap;

public class EncoderArmSystem {
    // IN INCHES
    private static final HashMap<JunctionHeight, Integer> ARM_POSITIONS = new HashMap() {{
        put(JunctionHeight.NONE, 0);
        put(JunctionHeight.LOW, 13.5);
        put(JunctionHeight.MID, 23.5);
        put(JunctionHeight.HIGH, 34);
    }};

    private final DcMotorEx slideMotor;

    private JunctionHeight targetHeight = JunctionHeight.NONE;
    private JunctionHeight currentHeight = JunctionHeight.NONE;
    private boolean setHeight = false;
    private boolean currentlyMoving = false;

    public EncoderArmSystem(DcMotorEx slideMotor) {
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

        if (currentlyMoving && !slideMotor.isBusy()) {
            currentHeight = targetHeight;
            slideMotor.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

            currentlyMoving = false;
        }
    }

    private double getTargetWheelPosition() {
        return SlideConstants.inchesToEncoderTicks(ARM_POSITIONS.get(targetHeight));
    }

    private void moveToHeight() {
        if (targetHeight == currentHeight) return;

        slideMotor.setMode(DcMotorEx.RunMode.RUN_TO_POSITION);
        slideMotor.setTargetPosition((int) getTargetWheelPosition());

        currentlyMoving = true;
    }
}
