package org.firstinspires.ftc.teamcode.automated4;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.CustomSleeve;
import org.firstinspires.ftc.teamcode.drive.Drive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

public class BlueAlignedRepository extends TrajectoryRepository {

    public static Pose2d INITIAL_POSITION = new Pose2d(-31.5, 64, Math.toRadians(270));
    public static Pose2d JUNCTION_POSITION = new Pose2d(-24, 12.5, Math.toRadians(270));
    //    private static final Pose2d JUNCTION_POSITION = new Pose2d(23.25, 11, Math.toRadians(270));
    public static Pose2d STACK_POSITION = new Pose2d(-58, 14.5, Math.toRadians(180));

    public BlueAlignedRepository() {
        super(INITIAL_POSITION);
    }

    @Override
    public TrajectorySequence getConeStackMove(Drive drive) {
        return drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .setTangent(Math.toRadians(165))
                .splineToSplineHeading(STACK_POSITION, Math.toRadians(190))
                .build();
    }

    @Override
    public TrajectorySequence getJunctionMove(Drive drive) {
        return drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .setTangent(Math.toRadians(10))
                .splineToSplineHeading(JUNCTION_POSITION, Math.toRadians(-15))
                .build();
    }

    @Override
    public boolean canDoFourthCycle(CustomSleeve sleeve) {
        if (sleeve == CustomSleeve.RIGHT) return false;
        return true;
    }

    @Override
    public void build(Drive drive) {
        initialNavigation = drive.trajectorySequenceBuilder(getInitialPose())
                .setTangent(Math.toRadians(250))
                .splineToLinearHeading(JUNCTION_POSITION, Math.toRadians(10))
                .build();

        {
            parkingLocationMove.put(CustomSleeve.RIGHT,
                    drive.trajectorySequenceBuilder(JUNCTION_POSITION)
                            .lineToLinearHeading(new Pose2d(-60, 17.75, Math.toRadians(270)))
                            .build()
            );
            parkingLocationMove.put(CustomSleeve.CENTER,
                    drive.trajectorySequenceBuilder(JUNCTION_POSITION)
                            .lineToLinearHeading(new Pose2d(-36, 17.75, Math.toRadians(270)))
                            .build()
            );
            parkingLocationMove.put(CustomSleeve.LEFT,
                    drive.trajectorySequenceBuilder(JUNCTION_POSITION)
                            .lineToLinearHeading(new Pose2d(-12, 17.75, Math.toRadians(270)))
                            .build()
            );
        }

    }
}