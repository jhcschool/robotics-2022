package com.jhcs.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;

public class BlueOppositeRepositoryV2 extends TrajectoryRepositoryV2 {

    private static final Pose2d INITIAL_POSITION = new Pose2d(36, 63, Math.toRadians(270));
    private static final Pose2d JUNCTION_POSITION = new Pose2d(24, 10, Math.toRadians(270));
    private static final Pose2d STACK_POSITION = new Pose2d(61, 12, Math.toRadians(0));

    public BlueOppositeRepositoryV2() {
        super(INITIAL_POSITION);
    }

    @Override
    public void build(DriveShim drive) {
        initialNavigation = drive.trajectorySequenceBuilder(getInitialPose())
                .setTangent(Math.toRadians(260))
                .splineToLinearHeading(JUNCTION_POSITION, Math.toRadians(170))
                .build();

        coneStackMove = drive.trajectorySequenceBuilder(JUNCTION_POSITION)
                .setTangent(Math.toRadians(20))
                .splineToLinearHeading(STACK_POSITION, Math.toRadians(-15))
                .build();

        junctionMove = drive.trajectorySequenceBuilder(STACK_POSITION)
                .setTangent(Math.toRadians(165))
                .splineToLinearHeading(JUNCTION_POSITION, Math.toRadians(200))
                .build();

        {
            parkingLocationMove.put(CustomSleeve.LEFT,
                    drive.trajectorySequenceBuilder(JUNCTION_POSITION)
                            .strafeLeft(36)
                            .build()
            );
            parkingLocationMove.put(CustomSleeve.CENTER,
                    drive.trajectorySequenceBuilder(JUNCTION_POSITION)
                            .strafeLeft(12)
                            .build()
            );
            parkingLocationMove.put(CustomSleeve.RIGHT,
                    drive.trajectorySequenceBuilder(JUNCTION_POSITION)
                            .strafeRight(12)
                            .build()
            );
        }

    }
}
