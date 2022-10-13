package org.firstinspires.ftc.teamcode.automated;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.ObjectDetector;
import org.firstinspires.ftc.teamcode.opencv.ContourPipeline;
import org.firstinspires.ftc.teamcode.opencv.OpenObjectDetector;
import org.opencv.core.Scalar;

public class SleeveDetector {

    private static final String[] LABELS = {"Red", "Green", "Blue"};
    private static final Scalar[] lowerBounds = {new Scalar(0, 0, 0), new Scalar(0, 0, 0), new Scalar(0, 0, 0)};
    private static final Scalar[] upperBounds = {new Scalar(0, 0, 0), new Scalar(0, 0, 0), new Scalar(0, 0, 0)};
    ObjectDetector objectDetector;
    CustomSleeve sleeveColor;

    public SleeveDetector(int viewId, Hardware hardware) {
        ContourPipeline pipeline = new ContourPipeline();
        pipeline.setLabels(LABELS);
        pipeline.setLowerBounds(lowerBounds);
        pipeline.setUpperBounds(upperBounds);

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

    enum CustomSleeve {
        RED,
        GREEN,
        BLUE,
    }
}
