package org.firstinspires.ftc.teamcode.automated;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.FrameInfo;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.Layer;
import org.firstinspires.ftc.teamcode.LayerInitInfo;
import org.firstinspires.ftc.teamcode.ObjectDetection;

public class AutomatedLayer extends Layer {

    static String[] LABELS = {"Large Pole", "Small Pole"};
    ObjectDetection objectDetection;

    @Override
    public void init(LayerInitInfo initInfo) {
        super.init(initInfo);

        int viewId = initInfo.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", initInfo.hardwareMap.appContext.getPackageName());
        objectDetection = new ObjectDetection(viewId, initInfo.hardware.camera.getCameraName(), "ObjectDetection-V1", LABELS);
    }

    @Override
    public void tick(FrameInfo frameInfo) {
        super.tick(frameInfo);

        Recognition[] recognitions = objectDetection.getRecognitions();
    }
}
