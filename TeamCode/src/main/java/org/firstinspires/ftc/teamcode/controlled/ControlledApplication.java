package org.firstinspires.ftc.teamcode.controlled;

import org.firstinspires.ftc.teamcode.Application;

public class ControlledApplication extends Application {

    @Override
    public void init() {
        super.init();

        addLayer(new ControlledLayer());
    }

}
