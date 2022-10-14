package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.ObjectDetector;
import org.firstinspires.ftc.teamcode.opencv.ColorPipeline;
import org.firstinspires.ftc.teamcode.opencv.ContourPipeline;
import org.firstinspires.ftc.teamcode.opencv.OpenObjectDetector;
import org.firstinspires.ftc.teamcode.opencv.OpenPipeline;
import org.opencv.core.Scalar;


@TeleOp(name = "Sleeve Detection Test", group = "Iterative Opmode")
public class SleeveDetectionTestMode extends LinearOpMode {

    private static final String[] LABELS = {"Red", "Green", "Blue"};

    // OpenCV Color Detection Bounds are in HSV
    private static final Scalar[] LOWER_BOUNDS = {new Scalar(-10, 100, 100), new Scalar(50, 100, 100), new Scalar(110, 100, 100)};
    private static final Scalar[] UPPER_BOUNDS = {new Scalar(10, 255, 255), new Scalar(70, 255, 255), new Scalar(130, 255, 255)};
    private ObjectDetector objectDetector;
    private CustomSleeve sleeveColor;

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

        OpenPipeline pipeline = new ColorPipeline(LOWER_BOUNDS, UPPER_BOUNDS, LABELS);

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

    enum CustomSleeve {
        RED,
        GREEN,
        BLUE,
    }
}
