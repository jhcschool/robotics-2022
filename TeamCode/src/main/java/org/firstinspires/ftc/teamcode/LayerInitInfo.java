package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.input.InputManager;

public class LayerInitInfo {
    public HardwareMap hardwareMap;
    public Hardware hardware;
    public Telemetry telemetry;
    public InputManager inputManager;

    public LayerInitInfo() {
    }

    public LayerInitInfo(HardwareMap hardwareMap, Hardware hardware, Telemetry telemetry, InputManager inputManager) {
        this.hardwareMap = hardwareMap;
        this.hardware = hardware;
        this.telemetry = telemetry;
        this.inputManager = inputManager;
    }
}