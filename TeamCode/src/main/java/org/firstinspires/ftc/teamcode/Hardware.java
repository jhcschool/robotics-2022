package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.Drive;
import org.firstinspires.ftc.teamcode.drive.GyroDrive;


public class Hardware {

    public Drive drive;

    public WebcamName webcamName;

    public Gamepad gamepad1;
    public Gamepad gamepad2;

    public Servo clipper;

    public Hardware(HardwareMap map, Gamepad gamepad1, Gamepad gamepad2) {
        webcamName = map.get(WebcamName.class, "webcam");
        clipper = map.get(Servo.class, "clipper");

        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;

        drive = new GyroDrive(map);
    }

    public void update() {
        drive.update();
    }
}
