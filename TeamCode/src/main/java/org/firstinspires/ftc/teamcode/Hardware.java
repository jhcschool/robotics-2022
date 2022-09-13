package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;


public class Hardware {

    public DcMotor frontLeftMotor;
    public DcMotor frontRightMotor;
    public DcMotor backLeftMotor;
    public DcMotor backRightMotor;

    public void init(HardwareMap map) {
        frontLeftMotor = map.get(frontLeftMotor.getClass(), "frontLeftMotor");
        backLeftMotor = map.get(backLeftMotor.getClass(), "backLeftMotor");
        frontRightMotor = map.get(frontRightMotor.getClass(), "frontRightMotor");
        backRightMotor = map.get(backRightMotor.getClass(), "backRightMotor");
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
