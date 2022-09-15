package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;

@Disabled
public abstract class Application extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private Hardware hardware;

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        hardware = new Hardware(hardwareMap);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        runtime.reset();
    }


    private double time;

    @Override
    public void loop() {

        FrameInfo frameInfo = new FrameInfo();
        {
            frameInfo.time = runtime.milliseconds();
            frameInfo.deltaTime = frameInfo.time - time;

            time = frameInfo.time;

            for (Layer layer : layers) {
                layer.tick(frameInfo);
            }


        }

        hardware.update(telemetry);

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

    ArrayList<Layer> layers = new ArrayList<Layer>();
}
