package org.firstinspires.ftc.teamcode.testing.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.CustomSleeve;
import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.automated.SleeveDetector;
import org.firstinspires.ftc.teamcode.drive.Drive;
import org.firstinspires.ftc.teamcode.drive.GyroDrive;


@Autonomous(name = "Game Start Test", group = "Iterative Opmode")
public class TimedGameStartTestMode extends Mode {
    private static final int WAIT_TIME = 1000;
    private static final int MOVE_TIME = 1000;
    private static final int STRAFE_TIME = 1200;
    private static final int TIME_BETWEEN_MOVES = 300;
    private static final double POWER = 0.5;
    private final ElapsedTime runtime = new ElapsedTime();
    private SleeveDetector sleeveDetector;
    private Drive drive;

    @Override
    public void onInit() {
        super.onInit();

        int viewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");

        drive = new GyroDrive(hardwareMap);
        drive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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

    private void strafeLeft() {
        final double frontLeft = -POWER;
        final double frontRight = POWER;
        final double backLeft = POWER;
        final double backRight = -POWER;
        drive.setMotorPowers(frontLeft, backLeft, backRight, frontRight);
    }

    private void strafeRight() {
        final double frontLeft = POWER;
        final double frontRight = -POWER;
        final double backLeft = -POWER;
        final double backRight = POWER;
        drive.setMotorPowers(frontLeft, backLeft, backRight, frontRight);
    }

    private void stopMotors() {
        drive.setMotorPowers(0, 0, 0, 0);
    }

    private void moveForward() {
        drive.setMotorPowers(POWER, POWER, POWER, POWER);
    }

    @Override
    public void tick() {
        super.tick();

        if (runtime.milliseconds() < WAIT_TIME) {
            return;
        }

        if (runtime.milliseconds() > WAIT_TIME + MOVE_TIME) {
            stopMotors();
        } else {
            CustomSleeve sleeve = sleeveDetector.getResult();
            telemetry.addData("Direction", sleeve.toString());

            switch (sleeve) {
                case LEFT:
                    strafeLeft();
                    sleep(STRAFE_TIME);
                    stopMotors();
                    sleep(TIME_BETWEEN_MOVES);
                    moveForward();
                    sleep(MOVE_TIME);
                    break;
                case CENTER:
                    moveForward();
                    sleep(MOVE_TIME);
                    break;
                case RIGHT:
                    strafeRight();
                    sleep(STRAFE_TIME);
                    stopMotors();
                    sleep(TIME_BETWEEN_MOVES);
                    moveForward();
                    sleep(MOVE_TIME);
                    break;
            }
        }
    }
}
