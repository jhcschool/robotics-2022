package org.firstinspires.ftc.teamcode.demos;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.arm.ClipperSystem;
import org.firstinspires.ftc.teamcode.controlled.UserMovementSystem;
import org.firstinspires.ftc.teamcode.drive.Drive;
import org.firstinspires.ftc.teamcode.drive.GyroDrive;
import org.firstinspires.ftc.teamcode.drive.ToeBreakerDriveConstants;
import org.firstinspires.ftc.teamcode.input.Axis;
import org.firstinspires.ftc.teamcode.input.Button;
import org.firstinspires.ftc.teamcode.input.ButtonAction;
import org.firstinspires.ftc.teamcode.input.InputManager;

@TeleOp(name = "(Main Powell) First Simple Two Servos", group = "Iterative Opmode")
public class FirstGameModeWithTwoServos extends Mode {
    private Drive drive;
    private UserMovementSystem userMovementSystem;

    private ClipperSystem clipperSystem;
    private InputManager inputManager;

    private DcMotorSimple slideArmMotor;

    @Override
    public void onInit() {
        super.onInit();

        drive = new GyroDrive(hardwareMap, new ToeBreakerDriveConstants());
        slideArmMotor = hardwareMap.get(DcMotorSimple.class, "slideArmMotor");

        userMovementSystem = new UserMovementSystem(gamepad1, drive);

        Servo leftServo = hardwareMap.get(Servo.class, "leftServo");
        Servo rightServo = hardwareMap.get(Servo.class, "rightServo");

        clipperSystem = new ClipperSystem(leftServo, rightServo);

        inputManager = new InputManager(gamepad1, gamepad2);
    }

    @Override
    public void tick() {
        super.tick();
        inputManager.update();

        userMovementSystem.update();

        if (inputManager.getButtonAction(Button.RIGHT_BUMPER) == ButtonAction.PRESS) {
            clipperSystem.toggle();
        }

        float total = inputManager.getAxis(Axis.RIGHT_TRIGGER) - inputManager.getAxis(Axis.LEFT_TRIGGER);
        slideArmMotor.setPower(total);

    }

    @Override
    public void onEnd() {
        super.onEnd();
        ClipperSystem.resetInitialPosition();
    }
}
