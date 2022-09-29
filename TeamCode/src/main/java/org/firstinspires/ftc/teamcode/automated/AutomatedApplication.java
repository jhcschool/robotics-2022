package org.firstinspires.ftc.teamcode.automated;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Application;

@Autonomous(name = "Automated", group = "Iterative Opmode")
public class AutomatedApplication extends Application {

    @Override
    public void onInit() {
        super.init();

        addLayer(new AutomatedLayer());
    }

}
