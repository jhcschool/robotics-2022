package org.firstinspires.ftc.teamcode.controlled;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Application;

@TeleOp(name="Controlled", group="Iterative Opmode")
public class ControlledApplication extends Application {

    @Override
    public void onInit() {
        super.init();

        addLayer(new ControlledLayer());
    }

}
