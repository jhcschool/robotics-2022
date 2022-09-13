package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.List;

public class ObjectDetection {

    private static final String VUFORIA_KEY = "";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    public ObjectDetection(int viewId, CameraName cameraName, String modelFile, String[] labels) {
        initVuforia(cameraName);
        initTfod(viewId, modelFile, labels);
    }

    public void initVuforia(CameraName cameraName) {
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();

        parameters.vuforiaLicenseKey = VUFORIA_KEY;
        parameters.cameraName = cameraName;

        //  Instantiate the Vuforia engine
        vuforia = ClassFactory.getInstance().createVuforia(parameters);
    }

    public void initTfod(int viewId, String modelFile, String[] labels) {
        TFObjectDetector.Parameters tfodParameters = new TFObjectDetector.Parameters(viewId);
        tfodParameters.minResultConfidence = 0.75f;
        tfodParameters.isModelTensorFlow2 = true;
        tfodParameters.inputSize = 300;

        tfod = ClassFactory.getInstance().createTFObjectDetector(tfodParameters, vuforia);

         tfod.loadModelFromFile(modelFile, labels);
    }

    public Recognition[] getRecognitions() {
        return tfod.getRecognitions().toArray(new Recognition[0]);
    }

}