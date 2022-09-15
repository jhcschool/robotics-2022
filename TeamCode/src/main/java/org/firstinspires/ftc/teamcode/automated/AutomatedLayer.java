package org.firstinspires.ftc.teamcode.automated;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.FrameInfo;
import org.firstinspires.ftc.teamcode.Layer;
import org.firstinspires.ftc.teamcode.LayerInitInfo;
import org.firstinspires.ftc.teamcode.TensorflowObjectDetector;

public class AutomatedLayer extends Layer {

    static String[] LABELS = {"Large Pole", "Small Pole"};
    TensorflowObjectDetector tensorflowObjectDetector;

    Telemetry telemetry;

    @Override
    public void init(LayerInitInfo initInfo) {
        super.init(initInfo);

        telemetry = initInfo.telemetry;

        int viewId = initInfo.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", initInfo.hardwareMap.appContext.getPackageName());
        tensorflowObjectDetector = new TensorflowObjectDetector(viewId, initInfo.hardware.webcamName, "ObjectDetection-V1", LABELS);
    }

    @Override
    public void tick(FrameInfo frameInfo) {
        super.tick(frameInfo);

        Recognition[] recognitions = tensorflowObjectDetector.getRecognitions();
        String[] labels = new String[recognitions.length];

        for (int i = 0; i < recognitions.length; i++) {
            labels[i] = recognitions[i].getLabel();
        }

        telemetry.addData("Recognitions", recognitions.length);
    }
}
