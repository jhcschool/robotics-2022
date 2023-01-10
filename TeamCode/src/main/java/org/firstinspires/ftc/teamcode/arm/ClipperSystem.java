package org.firstinspires.ftc.teamcode.arm;

import com.qualcomm.robotcore.hardware.Servo;

public class ClipperSystem {

    private static final float SINGLE_CLIP_POSITION = 0.55f;
    private static final float SINGLE_RELEASE_POSITION = 0.2f;

    private static final float DOUBLE_CLIP_POSITION = 0.9f;
    private static final float DOUBLE_RELEASE_POSITION = 0.4f;

    private static final double CLIP_TOLERANCE = 0.1;

    private Runnable onClip = null;
    private Runnable onRelease = null;

    private Servo clipper = null;

    private Servo leftClipper = null;
    private Servo rightClipper = null;

    private boolean clipped = false;
    private boolean shouldRun = true;

    public ClipperSystem(Servo clipper) {
        this.clipper = clipper;

        clipper.setPosition(SINGLE_RELEASE_POSITION);
    }

    public ClipperSystem(Servo leftClipper, Servo rightClipper) {
        this.leftClipper = leftClipper;
        this.rightClipper = rightClipper;

        leftClipper.setDirection(Servo.Direction.REVERSE);

        leftClipper.setPosition(DOUBLE_RELEASE_POSITION);
        rightClipper.setPosition(DOUBLE_RELEASE_POSITION);
    }

    public ClipperSystem(Servo clipper, Runnable onClip, Runnable onRelease) {
        this.clipper = clipper;
        this.onClip = onClip;
        this.onRelease = onRelease;

        clipper.setPosition(SINGLE_RELEASE_POSITION);
    }


    public ClipperSystem(Servo leftClipper, Servo rightClipper, Runnable onClip, Runnable onRelease) {
        this.leftClipper = leftClipper;
        this.rightClipper = rightClipper;
        this.onClip = onClip;
        this.onRelease = onRelease;

        leftClipper.setPosition(DOUBLE_RELEASE_POSITION);
        rightClipper.setPosition(DOUBLE_RELEASE_POSITION);
    }

    public void setOnClip(Runnable onClip) {
        this.onClip = onClip;
    }

    public void setOnRelease(Runnable onRelease) {
        this.onRelease = onRelease;
    }

    public void update() {
        if (clipped && isClippedWithinTolerance() && onClip != null && shouldRun) {
            onClip.run();
            shouldRun = false;
        } else if (!clipped && isReleasedWithinTolerance() && onRelease != null && shouldRun) {
            onRelease.run();
            shouldRun = false;
        }
    }

    public boolean isReleasedWithinTolerance() {
        if (clipper == null) {
            double lowerBound = DOUBLE_RELEASE_POSITION - CLIP_TOLERANCE;
            double upperBound = DOUBLE_RELEASE_POSITION + CLIP_TOLERANCE;

            return leftClipper.getPosition() >= lowerBound && leftClipper.getPosition() <= upperBound && rightClipper.getPosition() >= lowerBound && rightClipper.getPosition() <= upperBound;
        }

        double lowerBound = SINGLE_RELEASE_POSITION - CLIP_TOLERANCE;
        double upperBound = SINGLE_RELEASE_POSITION + CLIP_TOLERANCE;

        return clipper.getPosition() >= lowerBound && clipper.getPosition() <= upperBound;
    }

    public boolean isClippedWithinTolerance() {
        if (clipper == null) {
            double lowerBound = DOUBLE_CLIP_POSITION - CLIP_TOLERANCE;
            double upperBound = DOUBLE_CLIP_POSITION + CLIP_TOLERANCE;

            return leftClipper.getPosition() >= lowerBound && leftClipper.getPosition() <= upperBound && rightClipper.getPosition() >= lowerBound && rightClipper.getPosition() <= upperBound;
        }

        double lowerBound = SINGLE_CLIP_POSITION - CLIP_TOLERANCE;
        double upperBound = SINGLE_CLIP_POSITION + CLIP_TOLERANCE;

        return clipper.getPosition() >= lowerBound && clipper.getPosition() <= upperBound;
    }

    public void beginClip() {
        if (!clipped) {
            if (clipper != null) {
                clipper.setPosition(SINGLE_CLIP_POSITION);
            } else {
                leftClipper.setPosition(DOUBLE_CLIP_POSITION);
                rightClipper.setPosition(DOUBLE_CLIP_POSITION);
            }
            clipped = true;
            shouldRun = true;
        }
    }

    public void beginRelease() {
        if (clipped) {
            if (clipper != null) {
                clipper.setPosition(SINGLE_RELEASE_POSITION);
            } else {
                leftClipper.setPosition(DOUBLE_RELEASE_POSITION);
                rightClipper.setPosition(DOUBLE_RELEASE_POSITION);
            }
            clipped = false;
            shouldRun = true;
        }
    }

    public void toggle() {
        if (clipped) {
            beginRelease();
        } else {
            beginClip();
        }
    }

    public void setClipped(boolean clipped) {
        if (clipped) {
            beginClip();
        } else {
            beginRelease();
        }
    }

}
