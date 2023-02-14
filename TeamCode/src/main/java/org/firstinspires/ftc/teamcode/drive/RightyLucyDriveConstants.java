package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class RightyLucyDriveConstants implements DriveConstants {
    private static final boolean RUN_USING_ENCODER = false;
    private static final double MOTOR_REDUCER_RATIO = 1;
    private static final double TICKS_PER_REV = 28 * MOTOR_REDUCER_RATIO;
    private static final double MAX_RPM = 6000 / MOTOR_REDUCER_RATIO;
    private static final PIDFCoefficients MOTOR_VELO_PID = new PIDFCoefficients(0, 0, 0,
            getMotorVelocityFStatic(MAX_RPM / 60 * TICKS_PER_REV));
    private static final double WHEEL_RADIUS = 1.88976378; // in
    private static final double GEAR_RATIO = 0.052606; // output (wheel) speed / input (motor) speed
    //    private static final double GEAR_RATIO = 1.0 / 20.0; // output (wheel) speed / input (motor) speed
    private static final double kV = 0.018;
    private static final double TRACK_WIDTH = 9.9; // in
    private static final double kA = 0.0030;
    private static final double kStatic = 0.0725;
    private static final double MAX_VEL = 45;
    private static final double MAX_ACCEL = 25;
    private static final double MAX_ANG_VEL = 2.3;
    private static final double MAX_ANG_ACCEL = 2.3;

    private static final PIDCoefficients TRANSLATIONAL_PID = new PIDCoefficients(10, 0, 0);
    private static final PIDCoefficients HEADING_PID = new PIDCoefficients(9, 0, 0);
    private static final double LATERAL_MULTIPLIER = 1.147;

    private static double getMotorVelocityFStatic(double ticksPerSecond) {
        // see https://docs.google.com/document/d/1tyWrXDfMidwYyP_5H4mZyVgaEswhOC35gvdmP-V-5hA/edit#heading=h.61g9ixenznbx
        return 32767 / ticksPerSecond;
    }

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
    public PIDCoefficients getTranslationalPID() {
        return TRANSLATIONAL_PID;
    }

    @Override
    public PIDCoefficients getHeadingPID() {
        return HEADING_PID;
    }

    @Override
    public double getLateralMultiplier() {
        return LATERAL_MULTIPLIER;
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


}
