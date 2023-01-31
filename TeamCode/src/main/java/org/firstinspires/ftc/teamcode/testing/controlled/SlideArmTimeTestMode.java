package org.firstinspires.ftc.teamcode.testing.controlled;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.input.Button;
import org.firstinspires.ftc.teamcode.input.ButtonAction;
import org.firstinspires.ftc.teamcode.input.InputManager;

import java.util.HashMap;
import java.util.Map;

//@Disabled
@TeleOp(name = "Slide Arm Time Test", group = "Iterative Opmode")
public class SlideArmTimeTestMode extends Mode {

    private final HashMap<Modifier, Double> modifiers = new HashMap<Modifier, Double>() {{
        put(Modifier.DOWN, 0.4);
        put(Modifier.UP, 0.5);
        put(Modifier.IDLE, 0.05);
        put(Modifier.CONSTANT, 0.2);
        put(Modifier.TIME, 0.2);
    }};
    private DcMotorSimple slideArmMotor;
    private InputManager inputManager;
    private ElapsedTime elapsedTime;
    private Modifier modifier = Modifier.TIME;

    // Times: 0.18 for fifth cone, 0.14 for fourth, 0.10 for three, 0.06 for two,
    private State state = State.MAINTAIN;
    private boolean up = true;

    @Override
    public void onInit() {
        super.onInit();

        slideArmMotor = hardwareMap.get(DcMotorSimple.class, "slideArmMotor");
        inputManager = new InputManager(gamepad1, gamepad2);

        elapsedTime = new ElapsedTime();
        elapsedTime.reset();
    }

    @Override
    public void tick() {
        super.tick();
        telemetry.update();
        inputManager.update();

        if (inputManager.getButtonAction(Button.RIGHT_BUMPER) == ButtonAction.PRESS) {
            up = true;
        } else if (inputManager.getButtonAction(Button.LEFT_BUMPER) == ButtonAction.PRESS) {
            up = false;
        }

        switch (state) {
            case MAINTAIN:
                slideArmMotor.setPower(modifiers.get(Modifier.IDLE));
                break;
            case CONSTANT:
                slideArmMotor.setPower(-modifiers.get(Modifier.CONSTANT));
                break;
            case MOVING:
                if (up) {
                    slideArmMotor.setPower(modifiers.get(Modifier.UP));
                } else {
                    slideArmMotor.setPower(-modifiers.get(Modifier.DOWN));
                }

                if (elapsedTime.seconds() > modifiers.get(Modifier.TIME)) {
                    moveToState(State.MAINTAIN);
                }

                break;

        }

        if (state == State.MAINTAIN || state == State.CONSTANT) {
            if (inputManager.getButtonAction(Button.A) == ButtonAction.PRESS) {
                moveToState(State.MOVING);
            }

            if (inputManager.getButtonAction(Button.X) == ButtonAction.PRESS) {
                if (state == State.MAINTAIN) moveToState(State.CONSTANT);
                if (state == State.CONSTANT) moveToState(State.MAINTAIN);
            }
        }

        if (inputManager.getButtonAction(Button.DPAD_RIGHT) == ButtonAction.PRESS) {
            modifiers.put(modifier, modifiers.get(modifier) + 0.01);
        } else if (inputManager.getButtonAction(Button.DPAD_LEFT) == ButtonAction.PRESS) {
            modifiers.put(modifier, modifiers.get(modifier) - 0.01);
        }

        if (inputManager.getButtonAction(Button.DPAD_UP) == ButtonAction.PRESS) {
            int index = modifier.ordinal();
            index++;
            if (index >= Modifier.values().length) {
                index = 0;
            }
            modifier = Modifier.values()[index];
        }

        for (Map.Entry<Modifier, Double> entry : modifiers.entrySet()) {
            telemetry.addData(entry.getKey().name(), entry.getValue());
        }

        telemetry.addData("Modifying", modifier.name());

        double value = modifiers.get(modifier);
        double normalized = Math.max(value, 0.0);
        modifiers.put(modifier, normalized);
    }

    private void moveToState(State state) {
        elapsedTime.reset();
        this.state = state;
    }

    private enum State {
        MAINTAIN,
        CONSTANT,
        MOVING
    }

    private enum Modifier {
        DOWN,
        UP,
        IDLE,
        CONSTANT,
        TIME
    }

}
