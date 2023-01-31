package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;

import org.firstinspires.ftc.teamcode.game.AllianceMember;

// We need this to persist pose between autonomous and teleop
public class PoseStorage {
    public static Pose2d robotPose = new Pose2d();
    public static AllianceMember allianceMember = AllianceMember.RED;
}
