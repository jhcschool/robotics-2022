package org.firstinspires.ftc.teamcode.autotimed;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.CustomSleeve;
import org.firstinspires.ftc.teamcode.drive.Drive;

public class RightRepository extends TimedRepository {
    @Override
    public boolean initialForward(Drive drive, ElapsedTime timeSinceActivation) {
        return false;
    }

    @Override
    public boolean junctionStrafe(Drive drive, ElapsedTime timeSinceActivation) {
        return false;
    }

    @Override
    public boolean junctionBackward(Drive drive, ElapsedTime timeSinceActivation) {
        return false;
    }

    @Override
    public boolean rotateFaceStack(Drive drive, ElapsedTime timeSinceActivation) {
        return false;
    }

    @Override
    public boolean stackForward(Drive drive, ElapsedTime timeSinceActivation) {
        return false;
    }

    @Override
    public boolean stackBackward(Drive drive, ElapsedTime timeSinceActivation) {
        return false;
    }

    @Override
    public boolean rotateAlignJunction(Drive drive, ElapsedTime timeSinceActivation) {
        return false;
    }

    @Override
    public boolean parkingLocationMove(Drive drive, ElapsedTime timeSinceActivation, CustomSleeve sleeveResult) {
        return false;
    }
}