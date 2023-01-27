package org.firstinspires.ftc.teamcode.arm;

import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

public class TimedClipperSystem {

    private static final float SINGLE_CLIP_POSITION = 0.55f;
    private static final float SINGLE_RELEASE_POSITION = 0.2f;
    private static final float DOUBLE_CLIP_POSITION = 1.f;
    private static final float DOUBLE_RELEASE_POSITION = 0.6f;
    public static float SINGLE_INITIAL_POSITION = 0.3f;
    public static float DOUBLE_INITIAL_POSITION = 0.4f;
    public static double CLIP_TIME = 0.1;


    private final float clipPosition;
    private float releasePosition;

    private Servo clipper = null;

    private Servo leftClipper = null;
    private Servo rightClipper = null;
    private Runnable endCallback;
    private final ElapsedTime timeSinceActivation = new ElapsedTime();
    private State currentState = State.IDLE;
    private boolean clipped = false;

    public TimedClipperSystem(Servo clipper) {
        this.clipper = clipper;

        clipPosition = SINGLE_CLIP_POSITION;
        releasePosition = SINGLE_INITIAL_POSITION;

        clipper.setPosition(releasePosition);
    }
    public TimedClipperSystem(Servo leftClipper, Servo rightClipper) {
        this.leftClipper = leftClipper;
        this.rightClipper = rightClipper;

        leftClipper.setDirection(Servo.Direction.REVERSE);

        clipPosition = DOUBLE_CLIP_POSITION;
        releasePosition = DOUBLE_INITIAL_POSITION;

        leftClipper.setPosition(releasePosition);
        rightClipper.setPosition(releasePosition);
    }

    public static void resetInitialPosition() {
        SINGLE_INITIAL_POSITION = 0.3f;
        DOUBLE_INITIAL_POSITION = 0.4f;
    }

    public void update() {
        switch (currentState) {
            case CLIPPED:
            case RELEASED:
                if (timeSinceActivation.seconds() > CLIP_TIME) {
                    endCallback.run();
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

        releasePosition = clipper == null ? DOUBLE_RELEASE_POSITION : SINGLE_RELEASE_POSITION;

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

    private enum State {
        IDLE,
        CLIPPED,
        RELEASED
    }

}
