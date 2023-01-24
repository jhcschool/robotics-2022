package org.firstinspires.ftc.teamcode.automated;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Application;

@Autonomous(name = "Blue opposite", group = "Automated")
public class BlueOpposite extends Application {

    @Override
    public void onInit() {
        super.onInit();

        Pose2d startingPose = new Pose2d(63, 36, Math.toRadians(180));
        addLayer(new AutomatedLayer(startingPose, AutomatedLayer.AllianceMember.BLUE));
    }

}