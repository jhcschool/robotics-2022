package org.firstinspires.ftc.teamcode.automated4;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.CustomSleeve;
import org.firstinspires.ftc.teamcode.drive.Drive;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

public class LeftRepository extends TrajectoryRepository {

    public static Pose2d INITIAL_POSITION = new Pose2d(40.5, 64.5, Math.toRadians(270));
    public static Pose2d JUNCTION_POSITION = new Pose2d(24, 12.5, Math.toRadians(270));
    public static Pose2d STACK_POSITION = new Pose2d(59, 12.5, Math.toRadians(0));

    public LeftRepository() {
        super(INITIAL_POSITION);
    }

    @Override
    public TrajectorySequence getConeStackMove(Drive drive) {
        return drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .setTangent(Math.toRadians(10))
                .splineToSplineHeading(STACK_POSITION, Math.toRadians(-15))
                .build();
    }

    @Override
    public TrajectorySequence getJunctionMove(Drive drive, int currentCycle) {
        Pose2d junctionPosition = new Pose2d(JUNCTION_POSITION.getX(), JUNCTION_POSITION.getY() + currentCycle * 0.3, JUNCTION_POSITION.getHeading());
        return drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                .setTangent(Math.toRadians(165))
                .splineToSplineHeading(junctionPosition, Math.toRadians(190))
                .build();
    }

    @Override
    public boolean canDoFourthCycle(CustomSleeve sleeve) {
        if (sleeve == CustomSleeve.LEFT) return false;
        return true;
    }

    @Override
    public void build(Drive drive) {
        initialNavigation = drive.trajectorySequenceBuilder(getInitialPose())
                .setTangent(Math.toRadians(235))
                .splineToLinearHeading(JUNCTION_POSITION, Math.toRadians(170))
                .build();

        {
            parkingLocationMove.put(CustomSleeve.LEFT,
                    drive.trajectorySequenceBuilder(JUNCTION_POSITION)
//                            .strafeLeft(36)
                            .lineToLinearHeading(new Pose2d(63, 15.75, Math.toRadians(270)))
                            .build()
            );
            parkingLocationMove.put(CustomSleeve.CENTER,
                    drive.trajectorySequenceBuilder(JUNCTION_POSITION)
//                            .strafeLeft(12)
                            .lineToLinearHeading(new Pose2d(36, 15.75, Math.toRadians(270)))
                            .build()
            );
            parkingLocationMove.put(CustomSleeve.RIGHT,
                    drive.trajectorySequenceBuilder(JUNCTION_POSITION)
//                            .strafeRight(12)
                            .lineToLinearHeading(new Pose2d(12, 15.75, Math.toRadians(270)))
                            .build()
            );
        }

    }
}
