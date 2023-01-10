package org.firstinspires.ftc.teamcode.testing.autonomous;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.drive.Drive;
import org.firstinspires.ftc.teamcode.drive.GyroDrive;


@Autonomous(name = "Left On Start Test", group = "Iterative Opmode")
public class LeftOnStartTestMode extends Mode {

    private static final double DISTANCE_STRAFE = 150;
    private Drive drive;

    @Override
    public void onInit() {
        super.onInit();

        drive = new GyroDrive(hardwareMap);
    }

    @Override
    public void onStart() {
        super.onStart();

        Trajectory trajectory = drive.trajectoryBuilder()
                .strafeLeft(DISTANCE_STRAFE)
                .build();

        drive.followTrajectoryAsync(trajectory);
    }

    @Override
    public void tick() {
        super.tick();

        drive.update();
    }
}
