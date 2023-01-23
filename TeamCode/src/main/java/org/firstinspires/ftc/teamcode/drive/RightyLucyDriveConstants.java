package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class RightyLucyDriveConstants implements DriveConstants {
    private static final boolean RUN_USING_ENCODER = false;
    private static final double MOTOR_REDUCER_RATIO = 1;
    private static final double TICKS_PER_REV = 28 * MOTOR_REDUCER_RATIO;
    private static final double MAX_RPM = 6000 / MOTOR_REDUCER_RATIO;
    private static PIDFCoefficients MOTOR_VELO_PID = new PIDFCoefficients(0, 0, 0,
            getMotorVelocityFStatic(MAX_RPM / 60 * TICKS_PER_REV));
    private static double WHEEL_RADIUS = 1.88976378; // in
    private static double GEAR_RATIO = 1.0 / 20.0; // output (wheel) speed / input (motor) speed
    private static double kV = 0.0227;
    private static double TRACK_WIDTH = 12.25; // in
    private static double kA = 0.0035;
    private static double kStatic = 0.052;
    private static double MAX_VEL = 34.707;
    private static double MAX_ACCEL = 15;
    private static double MAX_ANG_VEL = Math.toRadians(60);
    private static double MAX_ANG_ACCEL = Math.toRadians(60);

    @Override
    public boolean shouldFeedForward() {
        return RUN_USING_ENCODER;
    }

    @Override
    public double getTicksPerRev() {
        return TICKS_PER_REV;
    }

    @Override
    public double getMaxRpm() {
        return MAX_RPM;
    }

    @Override
    public PIDFCoefficients getMotorVelocityPID() {
        return MOTOR_VELO_PID;
    }

    @Override
    public double getWheelRadius() {
        return WHEEL_RADIUS;
    }

    @Override
    public double getGearRatio() {
        return GEAR_RATIO;
    }

    @Override
    public double getKV() {
        return kV;
    }

    @Override
    public double getTrackWidth() {
        return TRACK_WIDTH;
    }

    @Override
    public double getKA() {
        return kA;
    }

    @Override
    public double getKStatic() {
        return kStatic;
    }

    @Override
    public double getMaxVel() {
        return MAX_VEL;
    }

    @Override
    public double getMaxAccel() {
        return MAX_ACCEL;
    }

    @Override
    public double getMaxAngVel() {
        return MAX_ANG_VEL;
    }

    @Override
    public double getMaxAngAccel() {
        return MAX_ANG_ACCEL;
    }

    @Override
    public double encoderTicksToInches(double ticks) {
        return WHEEL_RADIUS * 2 * Math.PI * GEAR_RATIO * ticks / TICKS_PER_REV;
    }

    @Override
    public double rpmToVelocity(double rpm) {
        return rpm * GEAR_RATIO * 2 * Math.PI * WHEEL_RADIUS / 60.0;
    }

    @Override
    public double getMotorVelocityF(double ticksPerSecond) {
        // see https://docs.google.com/document/d/1tyWrXDfMidwYyP_5H4mZyVgaEswhOC35gvdmP-V-5hA/edit#heading=h.61g9ixenznbx
        return 32767 / ticksPerSecond;
    }

    public static double getMotorVelocityFStatic(double ticksPerSecond) {
        // see https://docs.google.com/document/d/1tyWrXDfMidwYyP_5H4mZyVgaEswhOC35gvdmP-V-5hA/edit#heading=h.61g9ixenznbx
        return 32767 / ticksPerSecond;
    }



}
