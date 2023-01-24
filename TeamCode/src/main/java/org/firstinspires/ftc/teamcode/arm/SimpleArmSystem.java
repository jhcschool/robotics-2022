package org.firstinspires.ftc.teamcode.arm;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.game.JunctionHeight;

public class SimpleArmSystem {
    private static final double CONSTANT_FORCE = -0.3;
    private static final double UPWARD_FORCE = 0.9;
    private static final double DOWNWARD_FORCE = -0.65;

    private static final double WAIT_TIME = 0.4;

    private final DcMotorSimple slideArmMotor;

    private boolean raised = false;

    private Runnable endCallback;
    private ElapsedTime timeSinceActivation;
    private boolean hasRunCallback = false;

    public SimpleArmSystem(DcMotorSimple slideArmMotor) {
        this.slideArmMotor = slideArmMotor;
    }

    public void update() {
        if (timeSinceActivation.milliseconds() < WAIT_TIME) {
            return;
        }

        if (!hasRunCallback) {
            endCallback.run();
            hasRunCallback = true;

            if (!raised) {
                slideArmMotor.setPower(CONSTANT_FORCE);
            }
        }
    }


    public void setRaised(boolean raised, Runnable endCallback)
    {
        this.endCallback = endCallback;
        this.raised = raised;

        timeSinceActivation.reset();
        hasRunCallback = false;

        slideArmMotor.setPower(raised? UPWARD_FORCE: DOWNWARD_FORCE);
    }





}
