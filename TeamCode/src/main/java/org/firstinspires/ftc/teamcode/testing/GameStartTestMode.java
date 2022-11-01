package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.CustomSleeve;
import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.ObjectDetector;
import org.firstinspires.ftc.teamcode.opencv.ContourPipeline;
import org.firstinspires.ftc.teamcode.opencv.OpenObjectDetector;
import org.firstinspires.ftc.teamcode.opencv.OpenPipeline;
import org.opencv.core.Scalar;


@Autonomous(name = "Game Start Test", group = "Iterative Opmode")
public class GameStartTestMode extends Mode {

    private static final String[] LABELS = {"Left", "Center", "Right"};

//     OpenCV Color Detection Bounds are in HSV

//     private static final Scalar[] LOWER_BOUNDS = {new Scalar(-10, 100, 100), new Scalar(50, 100, 100), new Scalar(110, 100, 100)};
//     private static final Scalar[] UPPER_BOUNDS = {new Scalar(10, 255, 255), new Scalar(70, 255, 255), new Scalar(130, 255, 255)};

    private static final Scalar[] LOWER_BOUNDS = {new Scalar(60, 255, 200), new Scalar(180, 255, 200), new Scalar(300, 255, 180)};
    private static final Scalar[] UPPER_BOUNDS = {new Scalar(60, 120, 255), new Scalar(180, 120, 255), new Scalar(300, 190, 255)};

    private static final int COLLECTED_FRAMES = 30;
    private static final int WAIT_TIME = 1000;
    private static final int MOVE_TIME = 1000;
    private final ElapsedTime runtime = new ElapsedTime();
    private CustomSleeve[] detections = new CustomSleeve[COLLECTED_FRAMES];
    private int detectionIndex = 0;
    private ObjectDetector objectDetector;
    private DcMotorEx frontLeftMotor, rearLeftMotor, rearRightMotor, frontRightMotor;
    private CustomSleeve averagedResult;

    @Override
    public void onInit() {
        super.onInit();

        int viewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        OpenPipeline pipeline = new ContourPipeline(LOWER_BOUNDS, UPPER_BOUNDS, LABELS);

        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");

        objectDetector = new OpenObjectDetector(viewId, webcamName, pipeline, 320, 240);

        frontLeftMotor = hardwareMap.get(DcMotorEx.class, "frontLeftMotor");
        rearLeftMotor = hardwareMap.get(DcMotorEx.class, "rearLeftMotor");
        frontRightMotor = hardwareMap.get(DcMotorEx.class, "frontRightMotor");
        rearRightMotor = hardwareMap.get(DcMotorEx.class, "rearRightMotor");
    }

    @Override
    public void onStart() {
        super.onStart();

        runtime.reset();
        objectDetector.start();
    }

    private void collectFrame() {
        if (detectionIndex >= COLLECTED_FRAMES) return;

        Recognition[] recognitions = objectDetector.getRecognitions();

        float maxConfidence = 0;
        for (Recognition recognition : recognitions) {
            if (recognition == null) continue;

            if (recognition.getConfidence() > maxConfidence) {
                maxConfidence = recognition.getConfidence();
                detections[detectionIndex] = CustomSleeve.valueOf(recognition.getLabel().toUpperCase());
            }
        }
        detectionIndex++;

        if (detectionIndex >= COLLECTED_FRAMES) {
            telemetry.addData("Status", "Finished");

            int[] counts = new int[LABELS.length];

            for (CustomSleeve detection : detections) {
                if (detection == null) continue;
                counts[detection.ordinal()]++;
            }

            int maxCount = 0;
            for (int i = 0; i < counts.length; i++) {
                if (counts[i] > maxCount) {
                    maxCount = counts[i];
                    averagedResult = CustomSleeve.values()[i];
                }
            }

            telemetry.addData("Result", averagedResult);
        }
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
        collectFrame();

        if (runtime.milliseconds() < WAIT_TIME) {
            return;
        }

        if (averagedResult == null) {
            return;
        }

        if (runtime.milliseconds() > WAIT_TIME + MOVE_TIME) {
            stopMotors();
        } else {
            switch (averagedResult) {
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
