package org.firstinspires.ftc.teamcode.automated;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.CustomSleeve;
import org.firstinspires.ftc.teamcode.ObjectDetector;
import org.firstinspires.ftc.teamcode.opencv.AprilTagPipeline;
import org.firstinspires.ftc.teamcode.opencv.OpenObjectDetector;
import org.firstinspires.ftc.teamcode.opencv.OpenPipeline;

import java.util.HashMap;

public class SleeveDetector {

    private static final HashMap<Integer, String> ID_TO_LABEL = new HashMap<Integer, String>() {{
        put(1, "Left");
        put(2, "Center");
        put(3, "Right");
    }};
    private static final int NUM_DETECTIONS = 20;

    private final ObjectDetector objectDetector;

    private CustomSleeve detectedSleeve = null;
    private CustomSleeve[] lastDetections = new CustomSleeve[NUM_DETECTIONS];
    private int detectionIndex = 0;

    public SleeveDetector(int viewId, WebcamName webcamName) {
        OpenPipeline pipeline = new AprilTagPipeline(ID_TO_LABEL);
        objectDetector = new OpenObjectDetector(viewId, webcamName, pipeline, 640, 480);
    }

    public CustomSleeve getResult() {
        if (detectedSleeve != null) return detectedSleeve;
        return CustomSleeve.CENTER;
    }

    public void start() {
        objectDetector.start();
    }

    public void update() {
        CustomSleeve newDetection = getSingleDetection();
        if (newDetection == null) return;

        lastDetections[detectionIndex] = newDetection;

        detectionIndex++;
        if (detectionIndex >= NUM_DETECTIONS) detectionIndex = 0;
    }

    public void onGameStart() {
        objectDetector.stop();

        int detectionIndex = 1;
        int maxDetection = 0;

        int[] numDetections = new int[CustomSleeve.values().length];

        for (int i = 0; i < numDetections.length; i++) {
            numDetections[i] = 0;
        }

        for (CustomSleeve sleeve: lastDetections) {
            if (sleeve == null) continue;
            numDetections[sleeve.ordinal()]++;
        }

        for (int i = 0; i < numDetections.length; i++) {
            if (numDetections[i] > maxDetection) {
                detectionIndex = i;
            }
        }

        detectedSleeve = CustomSleeve.values()[detectionIndex];
    }

    private CustomSleeve getSingleDetection() {
        Recognition[] recognitions = objectDetector.getRecognitions();
        CustomSleeve sleeveColor = null;

        float maxConfidence = 0;
        for (Recognition recognition : recognitions) {
            if (recognition.getConfidence() > maxConfidence) {
                maxConfidence = recognition.getConfidence();
                sleeveColor = CustomSleeve.valueOf(recognition.getLabel().toUpperCase());
            }
        }

        return sleeveColor;
    }
}
