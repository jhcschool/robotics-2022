package org.firstinspires.ftc.teamcode.testing.controlled;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.drive.Drive;
import org.firstinspires.ftc.teamcode.drive.GyroDrive;
import org.firstinspires.ftc.teamcode.drive.RightyLucyDriveConstants;
import org.firstinspires.ftc.teamcode.input.Axis;
import org.firstinspires.ftc.teamcode.input.Button;
import org.firstinspires.ftc.teamcode.input.ButtonAction;
import org.firstinspires.ftc.teamcode.input.InputManager;

import java.util.HashMap;
import java.util.Map;

@TeleOp(name = "Drive Time Test", group = "Iterative Opmode")
public class DriveTimeTestMode extends Mode {

    private final HashMap<Modifier, Double> modifiers = new HashMap<Modifier, Double>() {{
        put(Modifier.POWER, 0.5);
        put(Modifier.TIME, 3.0);
    }};
    private Drive drive;
    private InputManager inputManager;
    private ElapsedTime elapsedTime;

    private Modifier modifier = Modifier.TIME;
    private State state = State.IDLE;

    @Override
    public void onInit() {
        super.onInit();

        drive = new GyroDrive(hardwareMap, new RightyLucyDriveConstants());
        inputManager = new InputManager(gamepad1, gamepad2);

        elapsedTime = new ElapsedTime();
        elapsedTime.reset();
    }

    double lastTime = 0.0;

    @Override
    public void tick() {
        super.tick();
        telemetry.update();
        inputManager.update();

        double deltaTime = elapsedTime.seconds() - lastTime;
        lastTime = elapsedTime.seconds();

        if (state != State.IDLE) {
            if (elapsedTime.seconds() > modifiers.get(Modifier.TIME)) {
                moveToState(State.IDLE);
            }
        }

        switch (state) {
            case IDLE:
                drive.setMotorPowers(0.0, 0.0, 0.0, 0.0);
                break;
            case FORWARD:
                drive.setWeightedDrivePower(new Pose2d(modifiers.get(Modifier.POWER), 0.0, 0.0));
                break;
            case STRAFE_LEFT:
                drive.setWeightedDrivePower(new Pose2d(0, -modifiers.get(Modifier.POWER), 0.0));
                break;
            case STRAFE_RIGHT:
                drive.setWeightedDrivePower(new Pose2d(0.0, modifiers.get(Modifier.POWER), 0.0));
                break;
            case BACKWARD:
                drive.setWeightedDrivePower(new Pose2d(-modifiers.get(Modifier.POWER), 0.0, 0.0));
                break;
            case TURN_LEFT:
                drive.setWeightedDrivePower(new Pose2d(0.0, 0.0, modifiers.get(Modifier.POWER)));
                break;
            case TURN_RIGHT:
                drive.setWeightedDrivePower(new Pose2d(0.0, 0.0, -modifiers.get(Modifier.POWER)));
                break;

        }

        if (state == State.IDLE) {
            if (inputManager.getButtonAction(Button.DPAD_UP) == ButtonAction.PRESS)
                moveToState(State.FORWARD);
            if (inputManager.getButtonAction(Button.DPAD_RIGHT) == ButtonAction.PRESS)
                moveToState(State.STRAFE_RIGHT);
            if (inputManager.getButtonAction(Button.DPAD_DOWN) == ButtonAction.PRESS)
                moveToState(State.BACKWARD);
            if (inputManager.getButtonAction(Button.DPAD_LEFT) == ButtonAction.PRESS)
                moveToState(State.STRAFE_LEFT);
            if (inputManager.getButtonAction(Button.X) == ButtonAction.PRESS)
                moveToState(State.TURN_LEFT);
            if (inputManager.getButtonAction(Button.B) == ButtonAction.PRESS)
                moveToState(State.TURN_RIGHT);
        }

        if (inputManager.getButton(Button.RIGHT_BUMPER)) {
            double value = modifiers.get(modifier);
            value += inputManager.getAxis(Axis.LEFT_STICK_X) * deltaTime;
            modifiers.put(modifier, value);
        }

        if (inputManager.getButtonAction(Button.LEFT_BUMPER) == ButtonAction.PRESS) {
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
        telemetry.addData("State", state.name());

        double value = modifiers.get(modifier);
        double normalized = Math.max(value, 0.0);
        modifiers.put(modifier, normalized);

        modifiers.put(Modifier.POWER, Math.min(modifiers.get(Modifier.POWER), 1.0));
    }

    private void moveToState(State state) {
        elapsedTime.reset();
        this.state = state;
    }

    private enum State {
        IDLE,
        STRAFE_LEFT,
        STRAFE_RIGHT,
        FORWARD,
        BACKWARD,
        TURN_LEFT,
        TURN_RIGHT
    }

    private enum Modifier {
        POWER,
        TIME
    }
}
