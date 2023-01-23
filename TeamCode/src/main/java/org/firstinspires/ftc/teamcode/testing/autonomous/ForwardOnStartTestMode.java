package org.firstinspires.ftc.teamcode.testing.autonomous;

import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.drive.Drive;
import org.firstinspires.ftc.teamcode.drive.GyroDrive;
import org.firstinspires.ftc.teamcode.drive.ToeBreakerDriveConstants;


@Autonomous(name = "Forward On Start Test", group = "Iterative Opmode")
public class ForwardOnStartTestMode extends Mode {

    private static final double DISTANCE_STRAFE = 36;
    private Drive drive;

    @Override
    public void onInit() {
        super.onInit();

        drive = new GyroDrive(hardwareMap, new ToeBreakerDriveConstants());
    }

    @Override
    public void onStart() {
        super.onStart();

        Trajectory trajectory = drive.trajectoryBuilder()
                .forward(DISTANCE_STRAFE)
                .build();

        drive.followTrajectoryAsync(trajectory);
    }

    @Override
    public void tick() {
        super.tick();

        drive.update();
    }
}
