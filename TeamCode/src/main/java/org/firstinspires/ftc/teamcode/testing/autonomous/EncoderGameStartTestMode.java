package org.firstinspires.ftc.teamcode.testing.autonomous;

import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.CustomSleeve;
import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.automated.SleeveDetector;
import org.firstinspires.ftc.teamcode.drive.Drive;
import org.firstinspires.ftc.teamcode.drive.GyroDrive;


@Autonomous(name = "Encoder Game Start Test", group = "Iterative Opmode")
public class EncoderGameStartTestMode extends Mode {

    private static final int WAIT_TIME = 4000;
    private static final int DISTANCE_STRAFE = 65;
    private static final int FORWARD_DISTANCE = 65;
    private final ElapsedTime runtime = new ElapsedTime();
    private SleeveDetector sleeveDetector;
    private Drive drive;
    private boolean hasCompleted = false;

    @Override
    public void onInit() {
        super.onInit();

        int viewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");

        drive = new GyroDrive(hardwareMap);

        sleeveDetector = new SleeveDetector(viewId, webcamName);
        sleeveDetector.start();
    }

    @Override
    public void beforeStartLoop() {
        super.beforeStartLoop();

        sleeveDetector.update();
    }

    @Override
    public void onStart() {
        super.onStart();

        sleeveDetector.onGameStart();
        runtime.reset();
    }

    @Override
    public void tick() {
        super.tick();

        drive.update();

        if (runtime.milliseconds() < WAIT_TIME) {
            sleep(50);
            return;
        }

        if (!hasCompleted && !drive.isBusy()) {
            TrajectoryBuilder trajectoryBuilder = drive.trajectoryBuilder();

            CustomSleeve sleeve = sleeveDetector.getResult();
            telemetry.addData("Direction", sleeve.toString());

            switch (sleeve) {
                case LEFT:
                    trajectoryBuilder.strafeLeft(DISTANCE_STRAFE);
                    break;
                case RIGHT:
                    trajectoryBuilder.strafeRight(DISTANCE_STRAFE);
                    break;
                case CENTER:
                default:
                    break;
            }

            trajectoryBuilder.forward(FORWARD_DISTANCE);

            drive.followTrajectoryAsync(trajectoryBuilder.build());
            hasCompleted = true;
        }
    }
}
