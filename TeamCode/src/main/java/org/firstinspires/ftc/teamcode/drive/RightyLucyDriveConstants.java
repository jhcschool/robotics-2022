package org.firstinspires.ftc.teamcode.drive;

import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class RightyLucyDriveConstants implements DriveConstants {
    public static final boolean RUN_USING_ENCODER = false;
    /*
     * These are motor constants that should be listed online for your motors.
     */
    private static final double MOTOR_REDUCER_RATIO = 1;
    public static final double TICKS_PER_REV = 28 * MOTOR_REDUCER_RATIO;
    public static final double MAX_RPM = 6000 / MOTOR_REDUCER_RATIO;
    public static PIDFCoefficients MOTOR_VELO_PID = new PIDFCoefficients(0, 0, 0,
            getMotorVelocityFStatic(MAX_RPM / 60 * TICKS_PER_REV));
    /*
     * These are physical constants that can be determined from your robot (including the track
     * width; it will be tune empirically later although a rough estimate is important). Users are
     * free to chose whichever linear distance unit they would like so long as it is consistently
     * used. The default values were selected with inches in mind. Road runner uses radians for
     * angular distances although most angular parameters are wrapped in Math.toRadians() for
     * convenience. Make sure to exclude any gear ratio included in MOTOR_CONFIG from GEAR_RATIO.
     */
    public static double WHEEL_RADIUS = 1.88976378; // in
    public static double GEAR_RATIO = 1.0 / 20.0; // output (wheel) speed / input (motor) speed
    /*
     * These are the feedforward parameters used to model the drive motor behavior. If you are using
     * the built-in velocity PID, *these values are fine as is*. However, if you do not have drive
     * motor encoders or have elected not to use them for velocity control, these values should be
     * empirically tuned.
     */
    public static double kV = 0.0227;
    public static double TRACK_WIDTH = 12.25; // in
    public static double kA = 0.0035;
    public static double kStatic = 0.0667;

    /*
     * These values are used to generate the trajectories for you robot. To ensure proper operation,
     * the constraints should never exceed ~80% of the robot's actual capabilities. While Road
     * Runner is designed to enable faster autonomous motion, it is a good idea for testing to start
     * small and gradually increase them later after everything is working. All distance units are
     * inches.
     */

    public static double MAX_VEL = 34.707;
    public static double MAX_ACCEL = 15;

    // 18.9979776
    public static double MAX_ANG_VEL = Math.toRadians(60);
    public static double MAX_ANG_ACCEL = Math.toRadians(60);


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
