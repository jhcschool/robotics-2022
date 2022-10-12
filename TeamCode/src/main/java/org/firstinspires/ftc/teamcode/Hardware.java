package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
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

<<<<<<< HEAD
//        clipper = map.get(Servo.class, "clipper");
=======
        gamepad = map.get(gamepad.getClass(), "gamepad");

//        clipper = map.get(clipper.getClass(), "clipper");
>>>>>>> c0cc690e56f32f219a7547facd4220a4e9202a7a

        drive = new StandardMecanumDrive(map);
    }

    public void update(Telemetry telemetry) {
        drive.update();
        telemetry.update();
    }
}
