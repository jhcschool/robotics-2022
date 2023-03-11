package org.firstinspires.ftc.teamcode.autotimed;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.CustomSleeve;
import org.firstinspires.ftc.teamcode.drive.Drive;

public class RightRepository extends TimedRepository {
    // the same as the left repository, but with the strafes and rotations reversed

    private static final double MAX_POWER = 0.5;

    private static final double INITIAL_FORWARD_TIME = 5.0;
    private static final double JUNCTION_STRAFE_TIME = 2.0;
    private static final double JUNCTION_BACKWARD_TIME = 2.0;
    private static final double ROTATE_FACE_STACK_TIME = 2.0;
    private static final double STACK_FORWARD_TIME = 5.0;
    private static final double STACK_BACKWARD_TIME = 5.0;
    private static final double ROTATE_ALIGN_JUNCTION_TIME = 2.0;

    private static final double SLEEVE_TIME = 5.0;

    @Override
    public boolean initialForward(Drive drive, ElapsedTime timeSinceActivation) {
        drive.setWeightedDrivePower(new Pose2d(0.0, MAX_POWER, 0.0));
        return timeSinceActivation.seconds() > INITIAL_FORWARD_TIME;
    }

    @Override
    public boolean junctionStrafe(Drive drive, ElapsedTime timeSinceActivation) {
        drive.setWeightedDrivePower(new Pose2d(-MAX_POWER, 0.0, 0.0));
        return timeSinceActivation.seconds() > JUNCTION_STRAFE_TIME;
    }

    @Override
    public boolean junctionBackward(Drive drive, ElapsedTime timeSinceActivation) {
        drive.setWeightedDrivePower(new Pose2d(0.0, -MAX_POWER, 0.0));
        return timeSinceActivation.seconds() > JUNCTION_BACKWARD_TIME;
    }

    @Override
    public boolean rotateFaceStack(Drive drive, ElapsedTime timeSinceActivation) {
        drive.setWeightedDrivePower(new Pose2d(0.0, 0.0, -MAX_POWER));
        return timeSinceActivation.seconds() > ROTATE_FACE_STACK_TIME;
    }

    @Override
    public boolean stackForward(Drive drive, ElapsedTime timeSinceActivation) {
        drive.setWeightedDrivePower(new Pose2d(0.0, MAX_POWER, 0.0));
        return timeSinceActivation.seconds() > STACK_FORWARD_TIME;
    }

    @Override
    public boolean stackBackward(Drive drive, ElapsedTime timeSinceActivation) {
        drive.setWeightedDrivePower(new Pose2d(0.0, -MAX_POWER, 0.0));
        return timeSinceActivation.seconds() > STACK_BACKWARD_TIME;
    }

    @Override
    public boolean rotateAlignJunction(Drive drive, ElapsedTime timeSinceActivation) {
        drive.setWeightedDrivePower(new Pose2d(0.0, 0.0, MAX_POWER));
        return timeSinceActivation.seconds() > ROTATE_ALIGN_JUNCTION_TIME;
    }

    @Override
    public boolean parkingLocationMove(Drive drive, ElapsedTime timeSinceActivation, CustomSleeve sleeveResult) {
        switch (sleeveResult) {
            case LEFT:
                drive.setWeightedDrivePower(new Pose2d(-MAX_POWER, 0.0, 0.0));
                return timeSinceActivation.seconds() > SLEEVE_TIME;
            case RIGHT:
                drive.setWeightedDrivePower(new Pose2d(MAX_POWER, 0.0, 0.0));
                return timeSinceActivation.seconds() > SLEEVE_TIME;
            case CENTER:
            default:
                return true;
        }
    }
}