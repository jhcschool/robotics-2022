package org.firstinspires.ftc.teamcode.automated;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Application;

@Autonomous(name = "Red substation", group = "Automated")
public class RedSubstation extends Application {

    @Override
    public void onInit() {
        super.onInit();

        Pose2d startingPose = new Pose2d(63, -36, Math.toRadians(180));
        addLayer(new AutomatedLayer(startingPose, AutomatedLayer.AllianceMember.RED));
    }

}