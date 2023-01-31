package com.jhcs.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;

import java.util.HashMap;

public abstract class TrajectoryRepository {

    public enum AutomatedState {
        // Trajectory states
        INITIAL_NAVIGATION,
        CONE_STACK_MOVE,
        JUNCTION_MOVE,
    }

    public enum CustomSleeve {
        LEFT,
        CENTER,
        RIGHT
    }


    public static double INCH_DISTANCE = 3;
    public TrajectorySequence initialNavigation = null;
    public TrajectorySequence coneStackMove = null;
    public TrajectorySequence junctionMove = null;
    public HashMap<CustomSleeve, TrajectorySequence> parkingLocationMove = new HashMap<>();
    public HashMap<AutomatedState, TrajectorySequence> inchForwardTrajectories = new HashMap<>();
    public HashMap<AutomatedState, TrajectorySequence> inchBackwardTrajectories = new HashMap<>();
    private final Pose2d initialPose;


    protected TrajectoryRepository(Pose2d initialPose) {
        this.initialPose = initialPose;
    }

    public abstract void build(DriveShim drive);

    public Pose2d getInitialPose() {
        return initialPose;
    }
}
