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
        TrajectoryRepositoryV2 repository = new BlueOppositeRepositoryV2();
        repository.build(drive);

        entity.followTrajectorySequence(repository.initialNavigation);
//        entity.followTrajectorySequence(repository.inchForwardTrajectories.get(TrajectoryRepository.AutomatedState.INITIAL_NAVIGATION));
//        entity.followTrajectorySequence(repository.inchBackwardTrajectories.get(TrajectoryRepository.AutomatedState.INITIAL_NAVIGATION));
//        entity.followTrajectorySequence(repository.coneStackMove);
//        entity.followTrajectorySequence(repository.inchForwardTrajectories.get(TrajectoryRepository.AutomatedState.CONE_STACK_MOVE));
//        entity.followTrajectorySequence(repository.inchBackwardTrajectories.get(TrajectoryRepository.AutomatedState.CONE_STACK_MOVE));
//        entity.followTrajectorySequence(repository.junctionMove);
//                entity.followTrajectorySequence(repository.inchForwardTrajectories.get(TrajectoryRepository.AutomatedState.JUNCTION_MOVE));
//        entity.followTrajectorySequence(repository.inchBackwardTrajectories.get(TrajectoryRepository.AutomatedState.JUNCTION_MOVE));
//        entity.followTrajectorySequence(repository.parkingLocationMove.get(TrajectoryRepository.CustomSleeve.LEFT));
//        entity.followTrajectorySequence(repository.parkingLocationMove.get(TrajectoryRepository.CustomSleeve.CENTER));
//        entity.followTrajectorySequence(repository.parkingLocationMove.get(TrajectoryRepository.CustomSleeve.RIGHT));

        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(entity)
                .start();
    }


}