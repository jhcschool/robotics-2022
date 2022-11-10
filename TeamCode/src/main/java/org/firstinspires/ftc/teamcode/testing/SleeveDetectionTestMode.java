package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.automated.SleeveDetector;


@Autonomous(name = "Sleeve Detection Test", group = "Iterative Opmode")
public class SleeveDetectionTestMode extends Mode {

    private SleeveDetector sleeveDetector;

    @Override
    public void onInit() {
        super.onInit();

        int viewId = hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardwareMap.appContext.getPackageName());

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
