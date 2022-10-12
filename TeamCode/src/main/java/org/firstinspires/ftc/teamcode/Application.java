package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;

@Disabled
public abstract class Application extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private Hardware hardware;
    private double time;
    private ArrayList<Layer> layers = new ArrayList<Layer>();

    @Override
    public void runOpMode() {
<<<<<<< HEAD
        hardware = new Hardware(hardwareMap, gamepad1, gamepad2);
=======
        hardware = new Hardware(hardwareMap);
>>>>>>> c0cc690e56f32f219a7547facd4220a4e9202a7a

        onInit();
        waitForStart();
        onStart();
        while (opModeIsActive() && !isStopRequested()) {
            tick();
        }
    }

    public void onInit() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    public void onStart() {
        runtime.reset();

        for (Layer layer : layers) {
            layer.onStart();
        }
    }

    public void tick() {

        hardware.update(telemetry);

        FrameInfo frameInfo = new FrameInfo();
        {
            frameInfo.time = runtime.milliseconds();
            frameInfo.deltaTime = frameInfo.time - time;

            time = frameInfo.time;

            for (Layer layer : layers) {
                layer.tick(frameInfo);
            }


        }
    }

    public void onEnd() {
        for (Layer layer : layers) {
            layer.onEnd();
        }
    }

    public void addLayer(Layer layer) {
        layers.add(layer);

        LayerInitInfo info = new LayerInitInfo();
        info.hardwareMap = hardwareMap;
        info.hardware = hardware;
        info.telemetry = telemetry;

        layer.init(info);
    }

    public void removeLayer(Layer layer) {
        layers.remove(layer);
    }
}
