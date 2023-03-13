package org.firstinspires.ftc.teamcode.arm;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

public class TimedArmSystem {

    public double mainWaitTime = 1.7;
    public double mainDownTime = 0.1;
    public double coneForce = 0.5;

    public double upwardForce = 1.0;
    public double downwardForce = -0.70;
    public double constantForce = -0.3;
    public double maintainForce = 0.06;

    public double[] stackTimesUp = {
            0.0,
            0.08,
            0.12,
            0.16,
            0.20
    };
    public static double postTime = 0.19;
    private final DcMotorSimple slideArmMotor;
    private final ElapsedTime timeSinceActivation = new ElapsedTime();
    private Runnable endCallback;
    private State currentState = State.MAINTAIN;
    private int stackedCones = 5;

    public TimedArmSystem(DcMotorSimple slideArmMotor) {
        this.slideArmMotor = slideArmMotor;
    }

    public TimedArmSystem(DcMotorSimple slideArmMotor, double mainWaitTime, double mainDownTime, double coneForce, double upwardForce, double downwardForce) {
        this(slideArmMotor);

        this.mainWaitTime = mainWaitTime;
        this.mainDownTime = mainDownTime;
        this.coneForce = coneForce;
        this.upwardForce = upwardForce;
        this.downwardForce = downwardForce;
    }

    public void onStart()
    {
        timeSinceActivation.reset();
        currentState = State.INITIAL;
    }


    public void update() {
        switch (currentState) {
            case INITIAL:
                slideArmMotor.setPower(coneForce);
                stateIfTime(0.25, State.MAINTAIN);
                break;

            case CONSTANT:
                slideArmMotor.setPower(constantForce);
                break;

            case MAINTAIN:
                slideArmMotor.setPower(maintainForce);
                break;

            case MAIN_UPWARDS:
                slideArmMotor.setPower(upwardForce);
                stateIfTime(mainWaitTime, State.MAINTAIN);
                break;

            case MAIN_DOWNWARDS:
                slideArmMotor.setPower(downwardForce);
                idleIfTime(mainDownTime);
                break;

            case CONE_UPWARDS:
                slideArmMotor.setPower(coneForce);
                stateIfTime(stackTimesUp[stackedCones], State.MAINTAIN);
                break;

            case CONE_POST:
                slideArmMotor.setPower(coneForce);
                stateIfTime(postTime, State.MAINTAIN);
                break;
        }
    }

    private void stateIfTime(double time, State state) {
        if (timeSinceActivation.seconds() > time) {
            moveToState(state);

            if (endCallback != null) {
                endCallback.run();
            }
        }
    }

    private void idleIfTime(double time) {
        stateIfTime(time, State.CONSTANT);
    }

    private void moveToState(State state) {
        timeSinceActivation.reset();
        currentState = state;
    }

    public void setRaised(boolean raised, Runnable endCallback) {
        this.endCallback = endCallback;
        timeSinceActivation.reset();

        currentState = raised ? State.MAIN_UPWARDS : State.MAIN_DOWNWARDS;
        slideArmMotor.setPower(raised ? upwardForce : downwardForce);
    }

    public void liftToCone(Runnable endCallback) {
        this.endCallback = endCallback;
        moveToState(State.CONE_UPWARDS);
        stackedCones--;
    }

    public void liftPastCone(Runnable endCallback) {
        this.endCallback = endCallback;
        moveToState(State.CONE_POST);
    }

    public double getNextConeTime() {
        return stackTimesUp[stackedCones - 1];
    }

    private enum State {
        INITIAL,
        CONSTANT,
        MAINTAIN,

        MAIN_UPWARDS,
        MAIN_DOWNWARDS,

        CONE_UPWARDS,
        CONE_POST
    }
}
