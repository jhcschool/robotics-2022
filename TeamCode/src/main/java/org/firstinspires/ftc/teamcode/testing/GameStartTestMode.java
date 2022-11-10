package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.automated.SleeveDetector;


@Autonomous(name = "Game Start Test", group = "Iterative Opmode")
public class GameStartTestMode extends Mode {

    private static final int WAIT_TIME = 1000;
    private static final int MOVE_TIME = 1000;
    private final ElapsedTime runtime = new ElapsedTime();
    private SleeveDetector sleeveDetector;
    private DcMotorEx frontLeftMotor, rearLeftMotor, rearRightMotor, frontRightMotor;

    @Override
    public void onInit() {
        super.onInit();

        int viewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");

        frontLeftMotor = hardwareMap.get(DcMotorEx.class, "frontLeftMotor");
        rearLeftMotor = hardwareMap.get(DcMotorEx.class, "rearLeftMotor");
        frontRightMotor = hardwareMap.get(DcMotorEx.class, "frontRightMotor");
        rearRightMotor = hardwareMap.get(DcMotorEx.class, "rearRightMotor");

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
        frontLeftMotor.setPower(-1);
        rearLeftMotor.setPower(1);
        frontRightMotor.setPower(1);
        rearRightMotor.setPower(-1);
    }

    private void strafeRight() {
        frontLeftMotor.setPower(1);
        rearLeftMotor.setPower(-1);
        frontRightMotor.setPower(-1);
        rearRightMotor.setPower(1);
    }

    private void stopMotors() {
        frontLeftMotor.setPower(0);
        rearLeftMotor.setPower(0);
        frontRightMotor.setPower(0);
        rearRightMotor.setPower(0);
    }

    private void moveForward() {
        frontLeftMotor.setPower(1);
        rearLeftMotor.setPower(1);
        frontRightMotor.setPower(1);
        rearRightMotor.setPower(1);
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
            switch (sleeveDetector.getResult()) {
                case LEFT:
                    strafeLeft();
                    break;
                case CENTER:
                    moveForward();
                    break;
                case RIGHT:
                    strafeRight();
                    break;
            }
        }
    }
}
