package org.firstinspires.ftc.teamcode.automated3;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.CustomSleeve;
import org.firstinspires.ftc.teamcode.drive.Drive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import java.util.HashMap;

public abstract class TrajectoryRepository {

    private final Pose2d initialPose;

    public TrajectorySequence initialNavigation = null;
    public TrajectorySequence coneStackMove = null;
    public TrajectorySequence junctionMove = null;
    public HashMap<CustomSleeve, TrajectorySequence> parkingLocationMove = new HashMap<>();

    public double initialNavigationEstimatedTime = 2.7; // estimated 4.2
    public double coneStackMoveEstimatedTime = 2.3; // estimated 3.6
    public double junctionMoveEstimatedTime = 2.3; // estimated 3.6

    public TrajectoryRepository(Pose2d initialPose) {
        this.initialPose = initialPose;
    }
    public Pose2d getInitialPose() {
        return initialPose;
    }

    public abstract void build(Drive drive);
}
