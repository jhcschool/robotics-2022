package org.firstinspires.ftc.teamcode.arm;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.game.JunctionHeight;

import java.util.HashMap;

public class TimedArmSystem {

    private static final HashMap<JunctionHeight, Double> upTimes = new HashMap<JunctionHeight, Double>() {
        {
            put(JunctionHeight.LOW, 0.2);
            put(JunctionHeight.MID, 0.4);
            put(JunctionHeight.HIGH, 0.6);
        }
    };
    private final DcMotorSimple slideArmMotor;
    private JunctionHeight height = JunctionHeight.NONE;
    private Runnable endCallback;
    private final ElapsedTime timeSinceActivation = new ElapsedTime();
    private boolean hasRunCallback = false;

    public TimedArmSystem(DcMotorSimple slideArmMotor) {
        this.slideArmMotor = slideArmMotor;

    }

    public void update() {
        if (height == null || height == JunctionHeight.NONE) {
            slideArmMotor.setPower(-0.2);
        }

        double time = upTimes.get(height);
        if (timeSinceActivation.milliseconds() < time) {
            return;
        }

        if (!hasRunCallback) {
            endCallback.run();
            hasRunCallback = true;
        }
    }

    public void signalBegin(JunctionHeight height, Runnable endCallback) {
        this.height = height;
        this.endCallback = endCallback;

        timeSinceActivation.reset();
        hasRunCallback = false;

    }

    private void onEnd() {
        height = null;
        endCallback.run();
    }
}
