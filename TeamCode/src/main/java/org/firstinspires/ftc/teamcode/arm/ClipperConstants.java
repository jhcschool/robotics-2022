package org.firstinspires.ftc.teamcode.arm;

public class ClipperConstants {
    public static final float SINGLE_CLIP_POSITION = 0.55f;
    public static final float SINGLE_RELEASE_POSITION = 0.2f;
    public static final float DOUBLE_CLIP_POSITION = 0.93f;
    public static final float DOUBLE_RELEASE_POSITION = 0.525f;
    public static float SINGLE_INITIAL_POSITION = 0.3f;
    public static float DOUBLE_INITIAL_POSITION = 0.5f;

    public static void resetInitialPosition() {
        ClipperConstants.SINGLE_INITIAL_POSITION = 0.3f;
        ClipperConstants.DOUBLE_INITIAL_POSITION = 0.5f;
    }
}
