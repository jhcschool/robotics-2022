package org.firstinspires.ftc.teamcode.arm;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.game.JunctionHeight;

public class SimpleArmSystem {

    private enum State {
        IDLE,

        MAIN_UPWARDS,
        MAIN_DOWNWARDS,

        CONE_UPWARDS,
        CONE_POST
    }

    public static double CONE_FORCE = 0.5;

    public static double UPWARD_FORCE = 0.9;
    public static double DOWNWARD_FORCE = -0.65;
    public static double CONSTANT_FORCE = -0.2;

    public static double STACK_TIMES_UP[] = {
            0.1,
            0.1,
            0.1,
            0.1,
            0.1
    };
    public static double POST_TIME = 0.1;

    private static final double MAIN_WAIT_TIME = 0.4;

    private final DcMotorSimple slideArmMotor;

    private Runnable endCallback;
    private ElapsedTime timeSinceActivation;
    private State currentState = State.IDLE;

    private int stackedCones = 5;

    public SimpleArmSystem(DcMotorSimple slideArmMotor) {
        this.slideArmMotor = slideArmMotor;
    }

    public void update() {
        switch (currentState)
        {
            case IDLE:
                slideArmMotor.setPower(CONSTANT_FORCE);
                break;

            case MAIN_UPWARDS:
                slideArmMotor.setPower(UPWARD_FORCE);
                idleIfTime(MAIN_WAIT_TIME);
                break;

            case MAIN_DOWNWARDS:
                slideArmMotor.setPower(DOWNWARD_FORCE);
                idleIfTime(MAIN_WAIT_TIME);
                break;

            case CONE_UPWARDS:
                slideArmMotor.setPower(CONE_FORCE);
                idleIfTime(STACK_TIMES_UP[stackedCones]);
                break;

            case CONE_POST:
                slideArmMotor.setPower(CONE_FORCE);
                idleIfTime(POST_TIME);
                break;
        }
    }

    private void idleIfTime(double time)
    {
        if (time > timeSinceActivation.seconds()) {
            moveToState(State.IDLE);
        }
    }


    private void moveToState(State state)
    {
        timeSinceActivation.reset();
        currentState = state;
    }


    public void setRaised(boolean raised, Runnable endCallback) {
        this.endCallback = endCallback;
        timeSinceActivation.reset();

        currentState = raised ? State.MAIN_UPWARDS : State.MAIN_DOWNWARDS;
    }

    public void liftToCone(Runnable endCallback)
    {
        moveToState(State.CONE_UPWARDS);
        stackedCones--;
    }

    public void liftPastCone(Runnable endCallback)
    {
        moveToState(State.CONE_POST);
    }
}
