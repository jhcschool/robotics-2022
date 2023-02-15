package org.firstinspires.ftc.teamcode.automated3;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.CustomSleeve;
import org.firstinspires.ftc.teamcode.drive.Drive;

public class BlueOppositeRepository extends TrajectoryRepository {

    public static Pose2d INITIAL_POSITION = new Pose2d(40.7, 64, Math.toRadians(270));
    public static Pose2d JUNCTION_POSITION = new Pose2d(24, 13.5, Math.toRadians(270));
//    private static final Pose2d JUNCTION_POSITION = new Pose2d(23.25, 11, Math.toRadians(270));
    public static Pose2d STACK_POSITION = new Pose2d(56.25, 12, Math.toRadians(0));

    public BlueOppositeRepository() {
        super(INITIAL_POSITION);
    }

    @Override
    public void build(Drive drive) {
        initialNavigation = drive.trajectorySequenceBuilder(getInitialPose())
                .setTangent(Math.toRadians(235))
                .splineToLinearHeading(JUNCTION_POSITION, Math.toRadians(170))
                .build();

        coneStackMove = drive.trajectorySequenceBuilder(JUNCTION_POSITION)
                .setTangent(Math.toRadians(10))
                .splineToSplineHeading(STACK_POSITION, Math.toRadians(-15))
                .build();

        junctionMove = drive.trajectorySequenceBuilder(STACK_POSITION)
                .setTangent(Math.toRadians(165))
                .splineToSplineHeading(JUNCTION_POSITION, Math.toRadians(190))
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
