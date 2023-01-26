package com.jhcs.meepmeeptesting;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.core.colorscheme.scheme.ColorSchemeBlueDark;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveShim;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequence;

public class MeepMeepTesting {

    private static final MeepMeep meepMeep = new MeepMeep(800);

    public static void main(String[] args) {
        RoadRunnerBotEntity entity = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setColorScheme(new ColorSchemeBlueDark())
                .setConstraints(30, 15, 2, 2, 9.9)
                .setDimensions(15, 17.5).build();

        DriveShim drive = entity.getDrive();
        TrajectorySequence toStack = drive.trajectorySequenceBuilder(new Pose2d(-24, 12, Math.toRadians(270)))
                .setTangent(Math.toRadians(190))
                .splineToLinearHeading(new Pose2d(-58, 12, Math.toRadians(180)), Math.toRadians(195))
                .build();

        TrajectorySequence toJunction = drive.trajectorySequenceBuilder(new Pose2d(-58, 12, Math.toRadians(180)))
                .setTangent(Math.toRadians(15))
                .splineToLinearHeading(new Pose2d(-24, 12, Math.toRadians(270)), Math.toRadians(10))
                .build();

        TrajectorySequence fromStart = drive.trajectorySequenceBuilder(new Pose2d(-36, 63, Math.toRadians(270)))
                .setTangent(Math.toRadians(270))
                .lineToLinearHeading(new Pose2d(-36, 12, Math.toRadians(270)))
                .lineToLinearHeading(new Pose2d(-24, 12, Math.toRadians(270)))
                .build();

        TrajectorySequence signalOne = drive.trajectorySequenceBuilder(new Pose2d(-24, 12, Math.toRadians(270)))
                .strafeLeft(12)
                .build();

        TrajectorySequence signalTwo = drive.trajectorySequenceBuilder(new Pose2d(-24, 12, Math.toRadians(270)))
                .strafeRight(12)
                .build();

        TrajectorySequence signalThree = drive.trajectorySequenceBuilder(new Pose2d(-24, 12, Math.toRadians(270)))
                .strafeRight(36)
                .build();

        entity.followTrajectorySequence(fromStart);
//        entity.followTrajectorySequence(toStack);
//        entity.followTrajectorySequence(toJunction);
//        entity.followTrajectorySequence(signalOne);
//        entity.followTrajectorySequence(signalTwo);
//        entity.followTrajectorySequence(signalThree);

        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(entity)
                .start();
    }


}