package com.jhcs.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;

public class BlueSubstationRepository extends TrajectoryRepository {

    private static final Pose2d INITIAL_POSITION = new Pose2d(-36, 63, Math.toRadians(270));
    private static final Pose2d JUNCTION_POSITION = new Pose2d(-24, 12, Math.toRadians(270));
    private static final Pose2d STACK_POSITION = new Pose2d(-58, 12, Math.toRadians(180));

    public BlueSubstationRepository() {
        super(INITIAL_POSITION);
    }

    @Override
    public void build(DriveShim drive) {
        initialNavigation = drive.trajectorySequenceBuilder(getInitialPose())
                .setTangent(Math.toRadians(270))
                .lineToLinearHeading(new Pose2d(-36, 12, Math.toRadians(270)))
                .lineToLinearHeading(JUNCTION_POSITION)
                .build();

        coneStackMove = drive.trajectorySequenceBuilder(JUNCTION_POSITION)
                .setTangent(Math.toRadians(170))
                .splineToLinearHeading(STACK_POSITION, Math.toRadians(195))
                .build();

        junctionMove = drive.trajectorySequenceBuilder(STACK_POSITION)
                .setTangent(Math.toRadians(15))
                .splineToLinearHeading(JUNCTION_POSITION, Math.toRadians(-10))
                .build();

        {
            parkingLocationMove.put(CustomSleeve.LEFT,
                    drive.trajectorySequenceBuilder(JUNCTION_POSITION)
                            .strafeLeft(12)
                            .build()
            );
            parkingLocationMove.put(CustomSleeve.CENTER,
                    drive.trajectorySequenceBuilder(JUNCTION_POSITION)
                            .strafeRight(12)
                            .build()
            );
            parkingLocationMove.put(CustomSleeve.RIGHT,
                    drive.trajectorySequenceBuilder(JUNCTION_POSITION)
                            .strafeRight(36)
                            .build()
            );
        }

        {
            TrajectorySequence junctionForward = drive.trajectorySequenceBuilder(JUNCTION_POSITION).forward(INCH_DISTANCE).build();
            TrajectorySequence stackForward = drive.trajectorySequenceBuilder(STACK_POSITION).forward(INCH_DISTANCE).build();
            TrajectorySequence junctionBackward = drive.trajectorySequenceBuilder(junctionForward.end()).back(INCH_DISTANCE).build();
            TrajectorySequence stackBackward = drive.trajectorySequenceBuilder(stackForward.end()).back(INCH_DISTANCE).build();

            // Both take us to the junction
            inchForwardTrajectories.put(AutomatedState.INITIAL_NAVIGATION, junctionForward);
            inchForwardTrajectories.put(AutomatedState.JUNCTION_MOVE, junctionForward);
            inchBackwardTrajectories.put(AutomatedState.INITIAL_NAVIGATION, junctionBackward);
            inchBackwardTrajectories.put(AutomatedState.JUNCTION_MOVE, junctionBackward);

            inchForwardTrajectories.put(AutomatedState.CONE_STACK_MOVE, stackForward);
            inchBackwardTrajectories.put(AutomatedState.CONE_STACK_MOVE, stackBackward);
        }

    }
}