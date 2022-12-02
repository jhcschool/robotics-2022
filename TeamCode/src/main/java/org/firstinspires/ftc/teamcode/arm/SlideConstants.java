package org.firstinspires.ftc.teamcode.arm;

public class SlideConstants {
    private static final double WHEEL_RADIUS = 0.75;
    private static final double MAX_RPM = 6000;
    private static final double GEAR_RATIO = 1.0 / 19.2;
    private static final double TICKS_PER_REV = 28.0;

    public static double encoderTicksToInches(double ticks) {
        return ticks / TICKS_PER_REV / GEAR_RATIO / (2 * Math.PI) * WHEEL_RADIUS;
    }

    public static double inchesToEncoderTicks(double inches) {
        return inches * TICKS_PER_REV * GEAR_RATIO * (2 * Math.PI) / WHEEL_RADIUS;
    }
}
