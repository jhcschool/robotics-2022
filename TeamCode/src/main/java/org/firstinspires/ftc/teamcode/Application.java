package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;

@Disabled
public abstract class Application extends Mode {

    private final ElapsedTime runtime = new ElapsedTime();
    private Hardware hardware;
    private double time;
    private final ArrayList<Layer> layers = new ArrayList<Layer>();

    @Override
    public void onInit() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        hardware = new Hardware(hardwareMap, gamepad1, gamepad2);
    }

    @Override
    public void onStart() {
        runtime.reset();

        for (Layer layer : layers) {
            layer.onStart();
        }
    }

    @Override
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

    @Override
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
