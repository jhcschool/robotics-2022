package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;


public class Hardware {

    public DcMotor frontLeftMotor;
    public DcMotor frontRightMotor;
    public DcMotor backLeftMotor;
    public DcMotor backRightMotor;

    public Camera camera;

    public Gamepad gamepad;

    public Hardware(HardwareMap map) {
        frontLeftMotor = map.get(frontLeftMotor.getClass(), "frontLeftMotor");
        backLeftMotor = map.get(backLeftMotor.getClass(), "backLeftMotor");
        frontRightMotor = map.get(frontRightMotor.getClass(), "frontRightMotor");
        backRightMotor = map.get(backRightMotor.getClass(), "backRightMotor");

        camera = map.get(camera.getClass(), "camera");

        gamepad = map.get(gamepad.getClass(), "gamepad");
    }

    public void update(Telemetry telemetry) {
        telemetry.update();
    }
}
