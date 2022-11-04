package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.CustomSleeve;
import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.ObjectDetector;
import org.firstinspires.ftc.teamcode.opencv.AprilTagPipeline;
import org.firstinspires.ftc.teamcode.opencv.OpenObjectDetector;
import org.firstinspires.ftc.teamcode.opencv.OpenPipeline;

import java.util.HashMap;


@Autonomous(name = "Sleeve Detection Test", group = "Iterative Opmode")
public class SleeveDetectionTestMode extends Mode {

//    private static final String[] LABELS = {"Left", "Center", "Right"};

    // OpenCV Color Detection Bounds are in HSV
//    private static final Scalar[] LOWER_BOUNDS = {new Scalar(-10, 100, 100), new Scalar(50, 100, 100), new Scalar(110, 100, 100)};
//    private static final Scalar[] UPPER_BOUNDS = {new Scalar(10, 255, 255), new Scalar(70, 255, 255), new Scalar(130, 255, 255)};
//    private static final Scalar[] LOWER_BOUNDS = {new Scalar(30, 255, 200), new Scalar(90, 255, 200), new Scalar(150, 255, 160)};
//    private static final Scalar[] UPPER_BOUNDS = {new Scalar(30, 125, 255), new Scalar(90, 105, 255), new Scalar(150, 155, 255)};

    private HashMap<Integer, String> idToLabel = new HashMap<Integer, String>() {{
        put(0, "Left");
        put(1, "Center");
        put(2, "Right");
    }};

    private ObjectDetector objectDetector;
    private CustomSleeve sleeveColor;

    @Override
    public void onInit() {
        super.onInit();

        int viewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

//        OpenPipeline pipeline = new ContourPipeline(LOWER_BOUNDS, UPPER_BOUNDS, LABELS);
        OpenPipeline pipeline = new AprilTagPipeline(idToLabel);

        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");

        objectDetector = new OpenObjectDetector(viewId, webcamName, pipeline, 320, 240);

    }

    @Override
    public void onStart() {
        super.onStart();

        objectDetector.start();
    }

    @Override
    public void tick() {
        super.tick();

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
