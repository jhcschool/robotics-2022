package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Disabled
public class Mode extends LinearOpMode {

    @Override
    public void runOpMode() {
        onInit();
        waitForStart();
        onStart();
        while (opModeIsActive() && !isStopRequested()) {
            tick();
        }
        onEnd();
    }

    public void onInit() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    public void onStart() {
        telemetry.addData("Status", "Started");
        telemetry.update();
    }

    public void tick() {
        telemetry.update();
    }

    public void onEnd() {
        telemetry.addData("Status", "Ended");
        telemetry.update();
    }
}
