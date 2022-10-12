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

    }

    public void onStart() {

    }

    public void tick() {

    }

    public void onEnd() {

    }
}
