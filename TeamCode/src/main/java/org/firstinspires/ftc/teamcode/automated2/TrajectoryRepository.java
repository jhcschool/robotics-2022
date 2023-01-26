package org.firstinspires.ftc.teamcode.automated2;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;

import org.firstinspires.ftc.teamcode.CustomSleeve;
import org.firstinspires.ftc.teamcode.drive.Drive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

import java.util.HashMap;
import java.util.Map;

public abstract class TrajectoryRepository {

    public static double INCH_DISTANCE = 3;

    public TrajectoryRepository(Pose2d initialPose)
    {
        this.initialPose = initialPose;
    }

    public TrajectorySequence initialNavigation = null;
    public TrajectorySequence coneStackMove = null;
    public TrajectorySequence junctionMove = null;
    public HashMap<CustomSleeve, TrajectorySequence> parkingLocationMove = new HashMap<>();
    public HashMap<AutomatedState, TrajectorySequence> inchForwardTrajectories = new HashMap<>();
    public HashMap<AutomatedState, TrajectorySequence> inchBackwardTrajectories = new HashMap<>();


    public abstract void build(Drive drive);

    public Pose2d getInitialPose() { return initialPose; }
    private Pose2d initialPose;
}
