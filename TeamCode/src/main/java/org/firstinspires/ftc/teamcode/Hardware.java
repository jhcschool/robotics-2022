package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;


public class Hardware {

    public DcMotor frontLeftMotor;
    public DcMotor frontRightMotor;
    public DcMotor backLeftMotor;
    public DcMotor backRightMotor;

    public WebcamName webcamName;

    public Gamepad gamepad;

    public Hardware(HardwareMap map) {
        frontLeftMotor = map.get(frontLeftMotor.getClass(), "frontLeftMotor");
        backLeftMotor = map.get(backLeftMotor.getClass(), "backLeftMotor");
        frontRightMotor = map.get(frontRightMotor.getClass(), "frontRightMotor");
        backRightMotor = map.get(backRightMotor.getClass(), "backRightMotor");

        webcamName = map.get(webcamName.getClass(), "webcam");

        gamepad = map.get(gamepad.getClass(), "gamepad");
    }

    public void update(Telemetry telemetry) {
        telemetry.update();
    }
}
