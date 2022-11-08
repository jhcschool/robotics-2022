package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.CustomSleeve;
import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.automated.SleeveDetector;

import java.util.HashMap;


@Autonomous(name = "Sleeve Detection Test", group = "Iterative Opmode")
public class SleeveDetectionTestMode extends Mode {

//    private static final String[] LABELS = {"Left", "Center", "Right"};

    // OpenCV Color Detection Bounds are in HSV
//    private static final Scalar[] LOWER_BOUNDS = {new Scalar(-10, 100, 100), new Scalar(50, 100, 100), new Scalar(110, 100, 100)};
//    private static final Scalar[] UPPER_BOUNDS = {new Scalar(10, 255, 255), new Scalar(70, 255, 255), new Scalar(130, 255, 255)};
//    private static final Scalar[] LOWER_BOUNDS = {new Scalar(30, 255, 200), new Scalar(90, 255, 200), new Scalar(150, 255, 160)};
//    private static final Scalar[] UPPER_BOUNDS = {new Scalar(30, 125, 255), new Scalar(90, 105, 255), new Scalar(150, 155, 255)};

    private static final HashMap<Integer, String> ID_TO_LABEL = new HashMap<Integer, String>() {{
        put(1, "Left");
        put(2, "Center");
        put(3, "Right");
    }};

    private CustomSleeve sleeveColor;
    private SleeveDetector sleeveDetector;

    @Override
    public void onInit() {
        super.onInit();

        int viewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

//        OpenPipeline pipeline = new ContourPipeline(LOWER_BOUNDS, UPPER_BOUNDS, LABELS);
        WebcamName webcamName = hardwareMap.get(WebcamName.class, "webcam");

        sleeveDetector = new SleeveDetector(viewId, webcamName);
        sleeveDetector.start();
    }

    @Override
    public void beforeStartLoop() {
        super.beforeStartLoop();

        sleeveDetector.update();
    }

    @Override
    public void onStart() {
        super.onStart();

        sleeveDetector.onGameStart();
    }

    @Override
    public void tick() {
        super.tick();
        telemetry.addData("Sleeve Color with limited time", sleeveDetector.getResult());
    }
}
