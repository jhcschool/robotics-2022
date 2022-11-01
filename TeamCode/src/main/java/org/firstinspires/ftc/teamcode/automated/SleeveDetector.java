package org.firstinspires.ftc.teamcode.automated;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.CustomSleeve;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.ObjectDetector;
import org.firstinspires.ftc.teamcode.opencv.ContourPipeline;
import org.firstinspires.ftc.teamcode.opencv.OpenObjectDetector;
import org.opencv.core.Scalar;

public class SleeveDetector {

    private static final String[] LABELS = {"Red", "Green", "Blue"};
    // OpenCV Color Detection Bounds are in HSV
    private static final Scalar[] LOWER_BOUNDS = {new Scalar(-10, 100, 100), new Scalar(50, 100, 100), new Scalar(110, 100, 100)};
    private static final Scalar[] UPPER_BOUNDS = {new Scalar(10, 255, 255), new Scalar(70, 255, 255), new Scalar(130, 255, 255)};

    private ObjectDetector objectDetector;
    private CustomSleeve sleeveColor;

    public SleeveDetector(int viewId, Hardware hardware) {
        ContourPipeline pipeline = new ContourPipeline();
        pipeline.setLabels(LABELS);
        pipeline.setLowerBounds(LOWER_BOUNDS);
        pipeline.setUpperBounds(UPPER_BOUNDS);

        objectDetector = new OpenObjectDetector(viewId, hardware.webcamName, pipeline, 1280, 720);
        // objectDetector = new TensorflowObjectDetector(viewId, hardware.webcamName, "CustomSleeve.tflite", LABELS);
    }

    CustomSleeve getSingleDetection() {
        objectDetector.start();

        Recognition[] recognitions = objectDetector.getRecognitions();

        float maxConfidence = 0;
        for (Recognition recognition : recognitions) {
            if (recognition.getConfidence() > maxConfidence) {
                maxConfidence = recognition.getConfidence();
                sleeveColor = CustomSleeve.valueOf(recognition.getLabel().toUpperCase());
            }
        }

        objectDetector.stop();
        return sleeveColor;
    }
}
