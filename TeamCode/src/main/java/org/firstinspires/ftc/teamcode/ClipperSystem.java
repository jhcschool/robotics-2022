package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Servo;

public class ClipperSystem {

    private static final float CLIP_POSITION = 0.5f;
    private static final float RELEASE_POSITION = 0.0f;

    private Runnable onClip = null;
    private Runnable onRelease = null;

    private Servo clipper;

    private boolean clipped = false;

    public ClipperSystem(Servo clipper) {
        this.clipper = clipper;

        clipper.setPosition(RELEASE_POSITION);
    }

    public ClipperSystem(Servo clipper, Runnable onClip, Runnable onRelease) {
        this.clipper = clipper;
        this.onClip = onClip;
        this.onRelease = onRelease;

        clipper.setPosition(RELEASE_POSITION);
    }

    public void setOnClip(Runnable onClip) {
        this.onClip = onClip;
    }

    public void setOnRelease(Runnable onRelease) {
        this.onRelease = onRelease;
    }

    public void update() {
        double position = clipper.getPosition();

        if (clipped && isClippedWithinTolerance() && onClip != null) {
            onClip.run();
        } else if (!clipped && isReleasedWithinTolerance() && onRelease != null) {
            onRelease.run();
        }
    }

    public boolean isReleasedWithinTolerance() {
        float lowerBound = RELEASE_POSITION - 0.1f;
        float upperBound = RELEASE_POSITION + 0.1f;

        return clipper.getPosition() >= lowerBound && clipper.getPosition() <= upperBound;
    }

    public boolean isClippedWithinTolerance() {
        float lowerBound = CLIP_POSITION - 0.1f;
        float upperBound = CLIP_POSITION + 0.1f;

        return clipper.getPosition() >= lowerBound && clipper.getPosition() <= upperBound;
    }

    public void beginClip() {
        if (!clipped) {
            clipper.setPosition(CLIP_POSITION);
            clipped = true;
        }
    }

    public void beginRelease() {
        if (clipped) {
            clipper.setPosition(RELEASE_POSITION);
            clipped = false;
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
