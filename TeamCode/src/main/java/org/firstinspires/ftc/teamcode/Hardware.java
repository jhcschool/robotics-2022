package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.StandardMecanumDrive;


public class Hardware {

    public StandardMecanumDrive drive;

    public WebcamName webcamName;

    public Gamepad gamepad;

    public Servo clipper;

    public Hardware(HardwareMap map) {
        webcamName = map.get(webcamName.getClass(), "webcam");

        gamepad = map.get(gamepad.getClass(), "gamepad");

        clipper = map.get(clipper.getClass(), "clipper");

        drive = new StandardMecanumDrive(map);
    }

    public void update(Telemetry telemetry) {
        telemetry.update();
    }
}
