package org.firstinspires.ftc.teamcode.autotimed;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Application;
import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.game.AllianceMember;

@Autonomous(name = "Left timed", group = "Automated")
public class LeftAuto extends Application {
    @Override
    public void onInit() {
        super.onInit();

        PoseStorage.allianceMember = AllianceMember.BLUE;
        addLayer(new TimedAutomatedStateLayer(new LeftRepository()));
    }
}
