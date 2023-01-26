package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.roadrunner.control.PIDCoefficients;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

/*
 * Constants shared between multiple drive types.
 *
 * TODO: Tune or adjust the following constants to fit your robot. Note that the non-final
 * fields may also be edited through the dashboard (connect to the robot's WiFi network and
 * navigate to https://192.168.49.1:8080/dash). Make sure to save the values here after you
 * adjust them in the dashboard; **config variable changes don't persist between app restarts**.
 *
 * These are not the only parameters; some are located in the localizer classes, drive base classes,
 * and op modes themselves.
 */
@Config
public interface DriveConstants {

    /*
     * Set RUN_USING_ENCODER to true to enable built-in hub velocity control using drive encoders.
     * Set this flag to false if drive encoders are not present and an alternative localization
     * method is in use (e.g., tracking wheels).
     *
     * If using the built-in motor velocity PID, update drive.getDriveConstants().getMotorVelocityPID() with the tuned coefficients
     * from DriveVelocityPIDTuner.
     */
    boolean shouldFeedForward();

    double getTicksPerRev();

    double getMaxRpm();

    PIDFCoefficients getMotorVelocityPID();

    PIDCoefficients getTranslationalPID();

    PIDCoefficients getHeadingPID();

    double getLateralMultiplier();

    double getWheelRadius();

    double getGearRatio();

    double getKV();

    double getTrackWidth();

    double getKA();

    double getKStatic();

    double getMaxVel();

    double getMaxAccel();

    double getMaxAngVel();

    double getMaxAngAccel();

    double encoderTicksToInches(double ticks);

    double rpmToVelocity(double rpm);

    double getMotorVelocityF(double ticksPerSecond);

}
