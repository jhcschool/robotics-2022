package org.firstinspires.ftc.teamcode.automated;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.FrameInfo;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.Layer;
import org.firstinspires.ftc.teamcode.LayerInitInfo;
import org.firstinspires.ftc.teamcode.ObjectDetector;
import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.TensorflowObjectDetector;
import org.firstinspires.ftc.teamcode.opencv.ContourPipeline;
import org.firstinspires.ftc.teamcode.opencv.OpenObjectDetector;
import org.opencv.core.Scalar;

public class AutomatedLayer extends Layer {

    enum CustomSleeve {
        RED,
        GREEN,
        BLUE,
    }

    private static final String[] LABELS = {"Red", "Green", "Blue"};
    private static final Scalar[] lowerBounds = {new Scalar(0, 0, 0), new Scalar(0, 0, 0), new Scalar(0, 0, 0)};
    private static final Scalar[] upperBounds = {new Scalar(0, 0, 0), new Scalar(0, 0, 0), new Scalar(0, 0, 0)};

    Telemetry telemetry;
    Hardware hardware;

    ObjectDetector objectDetector;
    CustomSleeve sleeveColor;

    @Override
    public void init(LayerInitInfo initInfo) {
        super.init(initInfo);

        telemetry = initInfo.telemetry;
        hardware = initInfo.hardware;

        int viewId = initInfo.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", initInfo.hardwareMap.appContext.getPackageName());

        ContourPipeline pipeline = new ContourPipeline();
        pipeline.setLabels(LABELS);
        pipeline.setLowerBounds(lowerBounds);
        pipeline.setUpperBounds(upperBounds);

        objectDetector = new OpenObjectDetector(viewId, hardware.webcamName, pipeline, 1280, 720);
        // objectDetector = new TensorflowObjectDetector(viewId, hardware.webcamName, "CustomSleeve.tflite", LABELS);


    }

    @Override
    public void onStart() {
        super.onStart();

        objectDetector.start();

        Recognition[] recognitions = objectDetector.getRecognitions();

        float maxConfidence = 0;
        for (Recognition recognition : recognitions) {
            telemetry.addData(recognition.getLabel(), recognition.getConfidence());
            if (recognition.getConfidence() > maxConfidence) {
                maxConfidence = recognition.getConfidence();
                sleeveColor = CustomSleeve.valueOf(recognition.getLabel().toUpperCase());
            }
        }

        telemetry.addData("Sleeve Color", sleeveColor);
    }

    @Override
    public void tick(FrameInfo frameInfo) {
        super.tick(frameInfo);

        Recognition[] recognitions = objectDetector.getRecognitions();
        String[] labels = new String[recognitions.length];

        for (int i = 0; i < recognitions.length; i++) {
            labels[i] = recognitions[i].getLabel();
        }

        telemetry.addData("Recognitions", recognitions.length);
    }

    @Override
    public void onEnd() {
        super.onEnd();

        objectDetector.stop();
        PoseStorage.robotPose = hardware.drive.getPoseEstimate();
    }
}
