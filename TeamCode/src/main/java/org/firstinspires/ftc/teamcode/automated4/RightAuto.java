package org.firstinspires.ftc.teamcode.automated4;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Application;
import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.game.AllianceMember;

@Autonomous(name = "RIGHT MAIN!!!", group = "Automated")
public class RightAuto extends Application {
    @Override
    public void onInit() {
        super.onInit();

        PoseStorage.allianceMember = AllianceMember.BLUE;
        addLayer(new FastAutomatedStateLayer(new RightRepository()));
    }
}