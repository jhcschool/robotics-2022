package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="Linear Operations", group="Linear Operations")
@Disabled
public class Application extends OpMode {

    private ElapsedTime runtime = new ElapsedTime();

    @Override
    public void init() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        Hardware.getInstance().init(hardwareMap);
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
        runtime.reset();
    }

    double time = runtime.milliseconds();

    @Override
    public void loop() {

        FrameInfo frameInfo = new FrameInfo();
        {
            double newTime = runtime.milliseconds();
            frameInfo.deltaTime = newTime - time;
            time = newTime;
        }

        Hardware.getInstance().update(telemetry);


    }

    public void runLoop() {
    }
}