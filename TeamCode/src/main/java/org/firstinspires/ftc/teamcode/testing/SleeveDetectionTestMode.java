package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.ObjectDetector;
import org.firstinspires.ftc.teamcode.opencv.ContourPipeline;
import org.firstinspires.ftc.teamcode.opencv.OpenObjectDetector;
import org.firstinspires.ftc.teamcode.opencv.ColorPipeline;
import org.opencv.core.Scalar;


@TeleOp(name = "Sleeve Detection Test", group = "Iterative Opmode")
public class SleeveDetectionTestMode extends LinearOpMode {

    enum CustomSleeve {
        RED,
        GREEN,
        BLUE,
    }

    // private static final String[] LABELS = {"Yellow", "Cyan", "Magenta"};
    // private static final Scalar[] lowerBounds = {new Scalar(200, 200, 0, 255), new Scalar(0, 200, 200, 255), new Scalar(170, 0, 170, 255)};
    // private static final Scalar[] upperBounds = {new Scalar(255, 255, 130, 255), new Scalar(150, 255, 255, 255), new Scalar(255, 60, 255, 255)};

    private static final String[] LABELS = {"Red", "Green", "Blue"};
    private static final Scalar[] lowerBounds = {new Scalar(100, 0, 0), new Scalar(0, 100, 0), new Scalar(0, 0, 100)};
    private static final Scalar[] upperBounds = {new Scalar(255, 100, 100), new Scalar(100, 255, 100), new Scalar(100, 100, 255)};

    ObjectDetector objectDetector;
    CustomSleeve sleeveColor;

    @Override
    public void runOpMode() {
        onInit();
        waitForStart();
        onStart();
        while (opModeIsActive() && !isStopRequested()) {
            tick();
        }
    }

    public void onInit() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();


        int viewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

        ContourPipeline pipeline = new ContourPipeline(lowerBounds, upperBounds, LABELS);

        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");

        objectDetector = new OpenObjectDetector(viewId, webcamName, pipeline, 320, 240);

    }

    public void onStart() {
        objectDetector.start();
    }

    public void tick() {
        telemetry.update();

        Recognition[] recognitions = objectDetector.getRecognitions();

        float maxConfidence = 0;
        for (Recognition recognition : recognitions) {
            if (recognition == null) continue;

            if (recognition.getConfidence() > maxConfidence) {
                maxConfidence = recognition.getConfidence();
                sleeveColor = CustomSleeve.valueOf(recognition.getLabel().toUpperCase());
            }
        }

        telemetry.addData("Sleeve Color", sleeveColor);
    }
}
