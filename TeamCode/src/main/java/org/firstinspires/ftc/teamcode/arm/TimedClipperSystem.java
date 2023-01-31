package org.firstinspires.ftc.teamcode.arm;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class TimedClipperSystem {

    public static double CLIP_TIME = 0.35;

    private final float clipPosition;
    private final ElapsedTime timeSinceActivation = new ElapsedTime();
    private float releasePosition;
    private Servo clipper = null;
    private Servo leftClipper = null;
    private Servo rightClipper = null;
    private Runnable endCallback;
    private State currentState = State.IDLE;
    private boolean clipped = false;

    public TimedClipperSystem(Servo clipper) {
        this.clipper = clipper;

        clipPosition = ClipperConstants.SINGLE_CLIP_POSITION;
        releasePosition = ClipperConstants.SINGLE_INITIAL_POSITION;

        clipper.setPosition(releasePosition);
    }

    public TimedClipperSystem(Servo leftClipper, Servo rightClipper) {
        this.leftClipper = leftClipper;
        this.rightClipper = rightClipper;

        leftClipper.setDirection(Servo.Direction.REVERSE);

        clipPosition = ClipperConstants.DOUBLE_CLIP_POSITION;
        releasePosition = ClipperConstants.DOUBLE_INITIAL_POSITION;

        leftClipper.setPosition(releasePosition);
        rightClipper.setPosition(releasePosition);
    }

    public void update() {
        switch (currentState) {
            case CLIPPED:
            case RELEASED:
                if (timeSinceActivation.seconds() > CLIP_TIME) {
                    if (endCallback != null) {
                        endCallback.run();
                    }
                    moveToState(State.IDLE);
                }
                break;
            case IDLE:
            default:
                break;
        }
    }

    private void moveToState(State state) {
        timeSinceActivation.reset();
        currentState = state;
    }

    public void beginClip(Runnable runnable) {
        clipped = true;
        endCallback = runnable;
        moveToState(State.CLIPPED);

        releasePosition = clipper == null ? ClipperConstants.DOUBLE_RELEASE_POSITION : ClipperConstants.SINGLE_RELEASE_POSITION;

        if (clipper != null) {
            clipper.setPosition(clipPosition);
        } else {
            leftClipper.setPosition(clipPosition);
            rightClipper.setPosition(clipPosition);
        }
    }

    public void beginRelease(Runnable runnable) {
        clipped = false;
        endCallback = runnable;
        moveToState(State.RELEASED);

        if (clipper != null) {
            clipper.setPosition(releasePosition);
        } else {
            leftClipper.setPosition(releasePosition);
            rightClipper.setPosition(releasePosition);
        }
    }

    public void toggle() {
        if (clipped) {
            beginRelease(null);
        } else {
            beginClip(null);
        }
    }

    public void setClipped(boolean clipped) {
        if (clipped) {
            beginClip(null);
        } else {
            beginRelease(null);
        }
    }

    public void adjustInitialPosition() {
        ClipperConstants.DOUBLE_INITIAL_POSITION = (float) ((leftClipper.getPosition() + rightClipper.getPosition()) / 2.0);
    }

    private enum State {
        IDLE,
        CLIPPED,
        RELEASED
    }

}
