package org.firstinspires.ftc.teamcode.testing.autonomous;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.CustomSleeve;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.automated3.BlueOppositeRepository;
import org.firstinspires.ftc.teamcode.automated3.TrajectoryRepository;
import org.firstinspires.ftc.teamcode.drive.Drive;

@Autonomous(name = "Navigation Test", group = "Iterative Opmode")
public class NavigationTestMode extends Mode {

    private final TrajectoryRepository repository = new BlueOppositeRepository();
    private Hardware hardware;

    @Override
    public void onInit() {
        super.onInit();

        hardware = new Hardware(hardwareMap, gamepad1, gamepad2);
        hardware.drive.setPoseEstimate(repository.getInitialPose());
        repository.build(hardware.drive);
    }

    @Override
    public void onStart() {
        super.onStart();

        Drive drive = hardware.drive;
        drive.followTrajectorySequence(repository.initialNavigation);
//        drive.followTrajectorySequence(repository.inchForwardTrajectories.get(AutomatedState.INITIAL_NAVIGATION));
//        drive.followTrajectorySequence(repository.inchBackwardTrajectories.get(AutomatedState.INITIAL_NAVIGATION));
        drive.followTrajectorySequence(repository.coneStackMove);
//        drive.followTrajectorySequence(repository.inchForwardTrajectories.get(AutomatedState.CONE_STACK_MOVE));
//        drive.followTrajectorySequence(repository.inchBackwardTrajectories.get(AutomatedState.CONE_STACK_MOVE));
        drive.followTrajectorySequence(repository.junctionMove);
//        drive.followTrajectorySequence(repository.inchForwardTrajectories.get(AutomatedState.JUNCTION_MOVE));
//        drive.followTrajectorySequence(repository.inchBackwardTrajectories.get(AutomatedState.JUNCTION_MOVE));
        drive.followTrajectorySequence(repository.parkingLocationMove.get(CustomSleeve.LEFT));
//        drive.followTrajectorySequence(repository.parkingLocationMove.get(CustomSleeve.CENTER));
//        drive.followTrajectorySequence(repository.parkingLocationMove.get(CustomSleeve.RIGHT));
    }
}
