package org.firstinspires.ftc.teamcode.arm;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

public class SimpleArmSystem {

    private static final double MAIN_WAIT_TIME = 1.1;
    private static final double MAIN_DOWN_TIME = 0.3;
    public static double CONE_FORCE = 0.5;

    public static double UPWARD_FORCE = 1.0;
    public static double DOWNWARD_FORCE = -0.68;
    public static double CONSTANT_FORCE = -0.2;
    public static double MAINTAIN_FORCE = 0.06;

    public static double[] STACK_TIMES_UP = {
            0.0,
            0.06,
            0.10,
            0.14,
            0.18
    };
    public static double POST_TIME = 0.08;
    private final DcMotorSimple slideArmMotor;
    private final ElapsedTime timeSinceActivation = new ElapsedTime();
    private Runnable endCallback;
    private State currentState = State.CONSTANT;
    private int stackedCones = 5;

    public SimpleArmSystem(DcMotorSimple slideArmMotor) {
        this.slideArmMotor = slideArmMotor;
    }

    public void update() {
        switch (currentState) {
            case CONSTANT:
                slideArmMotor.setPower(CONSTANT_FORCE);
                break;

            case MAINTAIN:
                slideArmMotor.setPower(MAINTAIN_FORCE);
                break;

            case MAIN_UPWARDS:
                slideArmMotor.setPower(UPWARD_FORCE);
                stateIfTime(MAIN_WAIT_TIME, State.MAINTAIN);
                break;

            case MAIN_DOWNWARDS:
                slideArmMotor.setPower(DOWNWARD_FORCE);
                idleIfTime(MAIN_DOWN_TIME);
                break;

            case CONE_UPWARDS:
                slideArmMotor.setPower(CONE_FORCE);
                stateIfTime(STACK_TIMES_UP[stackedCones], State.MAINTAIN);
                break;

            case CONE_POST:
                slideArmMotor.setPower(CONE_FORCE);
                idleIfTime(POST_TIME);
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

    private enum State {
        CONSTANT,
        MAINTAIN,

        MAIN_UPWARDS,
        MAIN_DOWNWARDS,

        CONE_UPWARDS,
        CONE_POST
    }
}
