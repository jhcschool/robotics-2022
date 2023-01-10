package org.firstinspires.ftc.teamcode.automated;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Application;

@Autonomous(name = "Blue top", group = "Automated")
public class BlueTop extends Application {

    @Override
    public void onInit() {
        super.onInit();

        Pose2d startingPose = new Pose2d(-63, 36);
        addLayer(new AutomatedLayer(startingPose));
    }

}