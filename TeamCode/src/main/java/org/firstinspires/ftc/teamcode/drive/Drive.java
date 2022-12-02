package org.firstinspires.ftc.teamcode.drive;

import com.acmerobotics.roadrunner.drive.MecanumDrive;
import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequenceBuilder;

public abstract class Drive extends MecanumDrive {


    public Drive(double kV, double kA, double kStatic, double trackWidth, double wheelBase) {
        super(kV, kA, kStatic, trackWidth, wheelBase);
    }

    public Drive(double kV, double kA, double kStatic, double trackWidth) {
        super(kV, kA, kStatic, trackWidth);
    }

    public Drive(double kV, double kA, double kStatic, double trackWidth, double wheelBase, double lateralMultiplier) {
        super(kV, kA, kStatic, trackWidth, wheelBase, lateralMultiplier);
    }

    public abstract TrajectoryBuilder trajectoryBuilder();
    public abstract TrajectoryBuilder trajectoryBuilder(Pose2d startPose);
    public abstract TrajectoryBuilder trajectoryBuilder(Pose2d startPose, boolean reversed);
    public abstract TrajectoryBuilder trajectoryBuilder(Pose2d startPose, double startHeading);
    public abstract TrajectorySequenceBuilder trajectorySequenceBuilder();
    public abstract TrajectorySequenceBuilder trajectorySequenceBuilder(Pose2d startPose);

    public abstract void turnAsync(double angle);
    public abstract void turn(double angle);
    public abstract void followTrajectoryAsync(Trajectory trajectory);
    public abstract void followTrajectory(Trajectory trajectory);
    public abstract void followTrajectorySequenceAsync(TrajectorySequenceBuilder trajectorySequence);
    public abstract void followTrajectorySequence(TrajectorySequenceBuilder trajectorySequence);

    public abstract Pose2d getLastError();
    public abstract void update();
    public abstract void waitForIdle();
    public abstract boolean isBusy();

    public abstract void setMode(DcMotor.RunMode mode);
    public abstract void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior behavior);
    public abstract void setPIDFCoefficients(DcMotor.RunMode mode, PIDFCoefficients coefficients);
    public abstract void setWeightedDrivePower(Pose2d drivePower);

    public abstract void breakFollowing();
}
