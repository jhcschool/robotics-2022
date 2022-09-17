package org.firstinspires.ftc.teamcode.automated;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.FrameInfo;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.Layer;
import org.firstinspires.ftc.teamcode.LayerInitInfo;
import org.firstinspires.ftc.teamcode.ObjectDetector;
import org.firstinspires.ftc.teamcode.TensorflowObjectDetector;
import org.firstinspires.ftc.teamcode.opencv.ContourPipeline;
import org.firstinspires.ftc.teamcode.opencv.OpenObjectDetector;
import org.opencv.core.Scalar;

public class AutomatedLayer extends Layer {

    static final String[] LABELS = {"Red", "Green", "Blue"};
    static final Scalar[] lowerBounds = {new Scalar(0, 0, 0), new Scalar(0, 0, 0), new Scalar(0, 0, 0)};
    static final Scalar[] upperBounds = {new Scalar(0, 0, 0), new Scalar(0, 0, 0), new Scalar(0, 0, 0)};

    Telemetry telemetry;
    Hardware hardware;

    ObjectDetector objectDetector;

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
        // objectDetector = new TensorflowObjectDetector(viewId, hardware.webcamName, "CustomDetector.tflite", LABELS);


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
}
