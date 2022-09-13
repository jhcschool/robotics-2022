package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.hardware.camera.Camera;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;


public class Hardware {

    public DcMotor frontLeftMotor;
    public DcMotor frontRightMotor;
    public DcMotor backLeftMotor;
    public DcMotor backRightMotor;

    Camera camera;

    public void init(HardwareMap map) {
        frontLeftMotor = map.get(frontLeftMotor.getClass(), "frontLeftMotor");
        backLeftMotor = map.get(backLeftMotor.getClass(), "backLeftMotor");
        frontRightMotor = map.get(frontRightMotor.getClass(), "frontRightMotor");
        backRightMotor = map.get(backRightMotor.getClass(), "backRightMotor");

        camera = map.get(camera.getClass(), "camera");
    }

    public void update(Telemetry telemetry) {
        telemetry.update();
    }

    public static Hardware getInstance() {
        return instance;
    }

    private static Hardware instance = new Hardware();

    private Hardware() {}

}
