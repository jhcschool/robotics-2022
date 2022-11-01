package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.StandardMecanumDrive;


public class Hardware {

    public StandardMecanumDrive drive;

    public WebcamName webcamName;

    public Gamepad gamepad1;
    public Gamepad gamepad2;

//    public Servo clipper;

    public Hardware(HardwareMap map, Gamepad gamepad1, Gamepad gamepad2) {
        webcamName = map.get(WebcamName.class, "webcam");
        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;

//        clipper = map.get(clipper.getClass(), "clipper");

        drive = new StandardMecanumDrive(map);
    }

    public void update() {
        drive.update();
    }
}
