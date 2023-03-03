package org.firstinspires.ftc.teamcode.automated2;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Application;
import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.automated.AutomatedLayer;
import org.firstinspires.ftc.teamcode.game.AllianceMember;

@Disabled
//@Autonomous(name = "Blue Substation 2", group = "Automated")
public class BlueSubstation2 extends Application {
    @Override
    public void onInit() {
        super.onInit();

        PoseStorage.allianceMember = AllianceMember.BLUE;
        addLayer(new AutomatedStateLayer(new BlueSubstationRepository()));
    }
}
