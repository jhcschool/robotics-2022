package org.firstinspires.ftc.teamcode.testing.controlled;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.Mode;
import org.firstinspires.ftc.teamcode.input.Axis;
import org.firstinspires.ftc.teamcode.input.InputManager;

@Disabled
//@TeleOp(name = "Simple Slide Arm Test", group = "Iterative Opmode")
public class SimpleSlideArmTestMode extends Mode {

    private DcMotorSimple slideArmMotor;
    private InputManager inputManager;

    @Override
    public void onInit() {
        super.onInit();

        slideArmMotor = hardwareMap.get(DcMotorSimple.class, "slideArmMotor");

        inputManager = new InputManager(gamepad1, gamepad2);
    }

    @Override
    public void tick() {
        super.tick();
        inputManager.update();

        float total = inputManager.getAxis(Axis.RIGHT_TRIGGER) - inputManager.getAxis(Axis.LEFT_TRIGGER);
        slideArmMotor.setPower(total);

//        telemetry.addData("Current Position in Ticks", slideArmMotor.getCurrentPosition());
//        telemetry.addData("Current Position in Inches", SlideConstants.encoderTicksToInches(slideArmMotor.getCurrentPosition()));
    }
}
