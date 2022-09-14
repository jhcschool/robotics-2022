package org.firstinspires.ftc.teamcode.automated;

import org.firstinspires.ftc.teamcode.Application;

public class AutomatedApplication extends Application {

    @Override
    public void init() {
        super.init();

        addLayer(new AutomatedLayer());
    }

}
