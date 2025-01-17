package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.input.InputManager;

import java.util.ArrayList;

@Disabled
public abstract class Application extends Mode {

    private final ElapsedTime runtime = new ElapsedTime();
    private final ArrayList<Layer> layers = new ArrayList<Layer>();
    private Hardware hardware;
    private double time;

    private InputManager inputManager;

    @Override
    public void onInit() {
        super.onInit();

        hardware = new Hardware(hardwareMap, gamepad1, gamepad2);
        inputManager = new InputManager(gamepad1, gamepad2);

        runtime.reset();
    }

    @Override
    public void beforeStartLoop() {
        super.beforeStartLoop();

        FrameInfo frameInfo = new FrameInfo();
        {
            frameInfo.time = runtime.seconds();
            frameInfo.deltaTime = frameInfo.time - time;

            telemetry.addData("Frame Time", frameInfo.deltaTime);

            time = frameInfo.time;

            for (Layer layer : layers) {
                layer.beforeStartLoop(frameInfo);
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        runtime.reset();

        for (Layer layer : layers) {
            layer.onStart();
        }
    }

    @Override
    public void tick() {
        super.tick();

        hardware.update();
        inputManager.update();

        FrameInfo frameInfo = new FrameInfo();
        {
            frameInfo.time = runtime.seconds();
            frameInfo.deltaTime = frameInfo.time - time;

            telemetry.addData("Frame Time", frameInfo.deltaTime);

            time = frameInfo.time;

            for (Layer layer : layers) {
                layer.tick(frameInfo);
            }
        }
    }

    @Override
    public void onEnd() {
        super.onEnd();

        for (Layer layer : layers) {
            layer.onEnd();
        }
    }

    public void addLayer(Layer layer) {
        LayerInitInfo info = new LayerInitInfo();
        info.hardwareMap = hardwareMap;
        info.hardware = hardware;
        info.telemetry = telemetry;
        info.inputManager = inputManager;

        layer.init(info);

        layers.add(layer);
    }

    public void removeLayer(Layer layer) {
        layers.remove(layer);
    }
}
