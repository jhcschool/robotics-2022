package org.firstinspires.ftc.teamcode.arm;

import org.firstinspires.ftc.teamcode.arm.ClipperConstants;

import com.qualcomm.robotcore.hardware.Servo;

public class ClipperSystem {
    public static final double CLIP_TOLERANCE = 0.1;

    private final float clipPosition;
    private Runnable onClip = null;
    private Runnable onRelease = null;
    private float releasePosition;

    private Servo clipper = null;

    private Servo leftClipper = null;
    private Servo rightClipper = null;

    private boolean clipped = false;
    private boolean shouldRun = true;

    public ClipperSystem(Servo clipper) {
        this.clipper = clipper;

        clipPosition = ClipperConstants.SINGLE_CLIP_POSITION;
        releasePosition = ClipperConstants.SINGLE_INITIAL_POSITION;

        clipper.setPosition(releasePosition);
    }

    public ClipperSystem(Servo leftClipper, Servo rightClipper) {
        this.leftClipper = leftClipper;
        this.rightClipper = rightClipper;

        leftClipper.setDirection(Servo.Direction.REVERSE);

        clipPosition = ClipperConstants.DOUBLE_CLIP_POSITION;
        releasePosition = ClipperConstants.DOUBLE_INITIAL_POSITION;

        leftClipper.setPosition(releasePosition);
        rightClipper.setPosition(releasePosition);
    }

    public ClipperSystem(Servo clipper, Runnable onClip, Runnable onRelease) {
        this(clipper);
        this.onClip = onClip;
        this.onRelease = onRelease;
    }


    public ClipperSystem(Servo leftClipper, Servo rightClipper, Runnable onClip, Runnable onRelease) {
        this(leftClipper, rightClipper);
        this.onClip = onClip;
        this.onRelease = onRelease;
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
        double lowerBound = releasePosition - CLIP_TOLERANCE;
        double upperBound = releasePosition + CLIP_TOLERANCE;

        if (clipper == null) {
            return leftClipper.getPosition() >= lowerBound && leftClipper.getPosition() <= upperBound && rightClipper.getPosition() >= lowerBound && rightClipper.getPosition() <= upperBound;
        }

        return clipper.getPosition() >= lowerBound && clipper.getPosition() <= upperBound;
    }

    public boolean isClippedWithinTolerance() {
        double lowerBound = clipPosition - CLIP_TOLERANCE;
        double upperBound = clipPosition + CLIP_TOLERANCE;

        if (clipper == null) {
            return leftClipper.getPosition() >= lowerBound && leftClipper.getPosition() <= upperBound && rightClipper.getPosition() >= lowerBound && rightClipper.getPosition() <= upperBound;
        }

        return clipper.getPosition() >= lowerBound && clipper.getPosition() <= upperBound;
    }

    public void beginClip() {
        releasePosition = clipper == null ? ClipperConstants.DOUBLE_RELEASE_POSITION : ClipperConstants.SINGLE_RELEASE_POSITION;

        if (!clipped) {
            if (clipper != null) {
                clipper.setPosition(clipPosition);
            } else {
                leftClipper.setPosition(clipPosition);
                rightClipper.setPosition(clipPosition);
            }
            clipped = true;
            shouldRun = true;
        }
    }

    public void beginRelease() {
        if (clipped) {
            if (clipper != null) {
                clipper.setPosition(releasePosition);
            } else {
                leftClipper.setPosition(releasePosition);
                rightClipper.setPosition(releasePosition);
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

    public void adjustInitialPosition() {
        ClipperConstants.DOUBLE_INITIAL_POSITION = (float) ((leftClipper.getPosition() + rightClipper.getPosition()) / 2.0);
    }

}
