package org.firstinspires.ftc.teamcode.automated;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.Application;

@Disabled
//@Autonomous(name = "Blue substation", group = "Automated")
public class BlueSubstation extends Application {

    @Override
    public void onInit() {
        super.onInit();

        Pose2d startingPose = new Pose2d(-63, -36, Math.toRadians(0));
        addLayer(new AutomatedLayer(startingPose, AutomatedLayer.AllianceMember.BLUE));
    }

}
