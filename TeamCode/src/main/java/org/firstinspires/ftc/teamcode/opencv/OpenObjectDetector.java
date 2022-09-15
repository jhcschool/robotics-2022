package org.firstinspires.ftc.teamcode.opencv;

import org.firstinspires.ftc.robotcore.external.hardware.camera.CameraName;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.openftc.easyopencv.OpenCvCamera;
import org.openftc.easyopencv.OpenCvCameraFactory;
import org.openftc.easyopencv.OpenCvCameraRotation;
import org.openftc.easyopencv.OpenCvPipeline;

public class OpenObjectDetector {

    private OpenCvCamera camera;

    private int cameraWidth;
    private int cameraHeight;

    private ContourPipeline pipeline;

    public OpenObjectDetector(int viewId, WebcamName webcamName, ContourPipeline pipeline, int cameraWidth, int cameraHeight) {
        camera = OpenCvCameraFactory.getInstance().createWebcam(webcamName, viewId);
        camera.setPipeline(pipeline);

        this.cameraWidth = cameraWidth;
        this.cameraHeight = cameraHeight;

        this.pipeline = pipeline;

        camera.setPipeline(pipeline);

        camera.openCameraDeviceAsync(new OpenCvCamera.AsyncCameraOpenListener()
        {
            @Override
            public void onOpened()
            {
                camera.startStreaming(cameraWidth, cameraHeight, OpenCvCameraRotation.UPRIGHT);
            }

            @Override
            public void onError(int errorCode)
            {
                // Handle error
            }
        });
    }

    public Recognition[] getRecognitions() {
        return pipeline.getRecognitions();
    }


}