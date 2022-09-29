package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

public class TensorflowObjectDetector implements ObjectDetector {

    private static final String VUFORIA_KEY = "AYz3cgf/////AAABma+WDnhtL033gWAJtplg3fpuI61DBp5XPjU0nVdmgTGfW6TDs4O1njj5m161MEBsneA41OCmCa9APvr1nya5XVzJ9OaTNeKd/xHuYEDGgW8G0+IXhwhJ0DaTWHgnDv0UfMh/qTvIvpvoqXM2OKmlzZA+P1FF/vO75lqUcyEsRdh3L7/qtcJEKplAIPldr1uhGku9ZEdckjIFfl/Dhf/G1plF3R6RVOlW8oUHf4JpJTPMw02MdvVxhGnFyHQ4lF6lpZ0pZNJKJY1vQ35gn8WnH9mydbAtfZRSv+jSSmv+rgho7pacbktP9P6chMcMA8vZJWA1vBdXHtfTZI6IFEEV4xpaA4A4L8s7WQvGMgTYlFiZ";

    private VuforiaLocalizer vuforia;
    private TFObjectDetector tfod;

    public TensorflowObjectDetector(int viewId, CameraName cameraName, String modelFile, String[] labels) {
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

    @Override
    public void start() {
        tfod.activate();
    }

    @Override
    public void stop() {
        tfod.shutdown();
    }
}