package org.firstinspires.ftc.teamcode.testing.autonomous;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.CustomSleeve;
import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.arm.ClipperSystem;
import org.firstinspires.ftc.teamcode.automated.SleeveDetector;
import org.firstinspires.ftc.teamcode.drive.Drive;
import org.firstinspires.ftc.teamcode.drive.GyroDrive;
import org.firstinspires.ftc.teamcode.drive.ToeBreakerDriveConstants;


@Autonomous(name = "Encoder Game Start Test", group = "Iterative Opmode")
public class EncoderGameStartTestMode extends Mode {

    private static final int WAIT_TIME = 4000;
    private static final int DISTANCE_STRAFE = 33; // usually 27
    private static final int FORWARD_DISTANCE = 36;
    private final ElapsedTime runtime = new ElapsedTime();
    private SleeveDetector sleeveDetector;
    private Drive drive;
    private boolean hasCompleted = false;
    private ClipperSystem clipperSystem;
    private Servo leftServo;
    private Servo rightServo;
    private Trajectory trajectory = null;

    @Override
    public void onInit() {
        super.onInit();

        int viewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");

        drive = new GyroDrive(hardwareMap, new ToeBreakerDriveConstants());

        sleeveDetector = new SleeveDetector(viewId, webcamName);
        sleeveDetector.start();

        leftServo = hardwareMap.get(Servo.class, "leftServo");
        rightServo = hardwareMap.get(Servo.class, "rightServo");

        clipperSystem = new ClipperSystem(leftServo, rightServo);

        drive.setPoseEstimate(new Pose2d());
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

        clipperSystem.beginClip();
        clipperSystem.beginRelease();
    }

    @Override
    public void tick() {
        super.tick();

        drive.update();
        clipperSystem.update();

        if (runtime.milliseconds() < WAIT_TIME) {
            sleep(50);
            return;
        }

        while (trajectory != null && !drive.isBusy()) {
            drive.followTrajectoryAsync(trajectory);
        }

        if (!hasCompleted) {
            hasCompleted = true;

            TrajectoryBuilder trajectoryBuilder = drive.trajectoryBuilder();

            CustomSleeve sleeve = sleeveDetector.getResult();
            telemetry.addData("Direction", sleeve.toString());

            switch (sleeve) {
                case LEFT:
                    trajectoryBuilder.strafeLeft(DISTANCE_STRAFE).addDisplacementMarker(() -> {
                        moveForward();
                    });
                    break;
                case RIGHT:
                    trajectoryBuilder.strafeRight(DISTANCE_STRAFE).addDisplacementMarker(() -> {
                        moveForward();
                    });
                    break;
                case CENTER:
                    moveForward();
                    return;
                default:
                    break;
            }

            trajectory = trajectoryBuilder.build();
        }
    }

    public void moveForward() {
        trajectory = drive.trajectoryBuilder().forward(FORWARD_DISTANCE).addDisplacementMarker(() -> {
            trajectory = null;
        }).build();
    }

    @Override
    public void onEnd() {
        super.onEnd();

        clipperSystem.adjustInitialPosition();
    }
}
