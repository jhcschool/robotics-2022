package org.firstinspires.ftc.teamcode.automated3;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Application;
import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.automated3.BlueOppositeRepository;
import org.firstinspires.ftc.teamcode.game.AllianceMember;

@Autonomous(name = "Blue Opposite 3", group = "Automated")
public class BlueOpposite3 extends Application {
    @Override
    public void onInit() {
        super.onInit();

        PoseStorage.allianceMember = AllianceMember.BLUE;
        addLayer(new FastAutomatedStateLayer(new BlueOppositeRepository()));
    }
}
