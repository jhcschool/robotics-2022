package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.drive.Drive;
import org.firstinspires.ftc.teamcode.drive.GyroDrive;
import org.firstinspires.ftc.teamcode.drive.RightyLucyDriveConstants;
import org.firstinspires.ftc.teamcode.drive.ToeBreakerDriveConstants;


public class Hardware {

    public Drive drive;

    public WebcamName webcamName;

    public Gamepad gamepad1;
    public Gamepad gamepad2;

    public Servo leftServo;
    public Servo rightServo;

    public DcMotorSimple slideArmMotor;

    public Hardware(HardwareMap map, Gamepad gamepad1, Gamepad gamepad2) {
        webcamName = map.get(WebcamName.class, "webcam");

        leftServo = map.get(Servo.class, "leftServo");
        rightServo = map.get(Servo.class, "rightServo");

        this.gamepad1 = gamepad1;
        this.gamepad2 = gamepad2;

        drive = new GyroDrive(map, new RightyLucyDriveConstants());
//        drive = new GyroDrive(map, new ToeBreakerDriveConstants());

        slideArmMotor = map.get(DcMotorSimple.class, "slideArmMotor");
    }

    public void update() {
        drive.update();
    }
}
