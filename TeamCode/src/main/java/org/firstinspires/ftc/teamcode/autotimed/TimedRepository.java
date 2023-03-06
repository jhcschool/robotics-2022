package org.firstinspires.ftc.teamcode.autotimed;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.CustomSleeve;
import org.firstinspires.ftc.teamcode.drive.Drive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import java.util.HashMap;

public abstract class TimedRepository {
    public abstract boolean initialForward(Drive drive, ElapsedTime timeSinceActivation);
    public abstract boolean junctionStrafe(Drive drive, ElapsedTime timeSinceActivation);
    public abstract boolean junctionBackward(Drive drive, ElapsedTime timeSinceActivation);
    public abstract boolean rotateFaceStack(Drive drive, ElapsedTime timeSinceActivation);
    public abstract boolean stackForward(Drive drive, ElapsedTime timeSinceActivation);
    public abstract boolean stackBackward(Drive drive, ElapsedTime timeSinceActivation);
    public abstract boolean rotateAlignJunction(Drive drive, ElapsedTime timeSinceActivation);

    public abstract boolean parkingLocationMove(Drive drive, ElapsedTime timeSinceActivation, CustomSleeve sleeveResult);

    public double initialNavigationEstimatedTime = 2.7;
    public double coneStackMoveEstimatedTime = 2.3;
    public double junctionMoveEstimatedTime = 2.3;
}
