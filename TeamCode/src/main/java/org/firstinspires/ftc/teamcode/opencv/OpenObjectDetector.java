package org.firstinspires.ftc.teamcode.opencv;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.firstinspires.ftc.teamcode.ObjectDetector;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;

public class OpenObjectDetector implements ObjectDetector {

    private OpenCvCamera camera;

    private int cameraWidth;
    private int cameraHeight;

    private OpenPipeline pipeline;

    public OpenObjectDetector(int viewId, WebcamName webcamName, OpenPipeline pipeline, int cameraWidth, int cameraHeight) {
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, viewId);
        camera.setPipeline(pipeline);

        this.cameraWidth = cameraWidth;
        this.cameraHeight = cameraHeight;

        this.pipeline = pipeline;

        camera.setPipeline(pipeline);
    }

    @Override
    public void start() {
        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener() {
            @Override
            public void onOpened() {
                camera.startStreaming(cameraWidth, cameraHeight, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode) {
                // Handle error
                System.out.println("OpenCV Error: " + errorCode);
            }
        });
    }

    @Override
    public void stop() {
        camera.stopStreaming();
    }

    public Recognition[] getRecognitions() {
        return pipeline.getRecognitions();
    }


}
