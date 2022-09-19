package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.ArrayList;

@Disabled
public abstract class Application extends LinearOpMode {

    private ElapsedTime runtime = new ElapsedTime();
    private Hardware hardware;

    @Override public void runOpMode() {
        hardware = new Hardware(hardwareMap);

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

        hardware = new Hardware(hardwareMap);
    }

    public void onStart() {
        runtime.reset();

        for (Layer layer : layers) {
            layer.onStart();
        }
    }


    private double time;

    public void tick() {

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

    ArrayList<Layer> layers = new ArrayList<Layer>();
}
