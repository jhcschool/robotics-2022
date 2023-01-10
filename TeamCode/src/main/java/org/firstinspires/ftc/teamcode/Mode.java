package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

@Disabled
public class Mode extends LinearOpMode {

    private final Object beforeStartLoopSync = new Object();
    private Thread beforeStartLoopThread;
    private boolean isBeforeStart = true;

    @Override
    public void runOpMode() {
        onInit();
        beforeStart();
        onStart();
        while (opModeIsActive() && !isStopRequested()) {
            tick();
        }
        onEnd();
    }

    public void onInit() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();
    }

    public void beforeStart() {
        beforeStartLoopThread = new Thread(() -> {
            while (true) {
                synchronized (beforeStartLoopSync) {
                    if (!isBeforeStart) {
                        break;
                    }
                }
                beforeStartLoop();
            }
        });
        beforeStartLoopThread.start();
        waitForStart();
        synchronized (beforeStartLoopSync) {
            isBeforeStart = false;
        }
        try {
            beforeStartLoopThread.join(50);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void beforeStartLoop() {
        telemetry.update();
    }


    public void onStart() {
        telemetry.addData("Status", "Started");
        telemetry.update();
    }

    public void tick() {
        telemetry.update();
    }

    public void onEnd() {
        telemetry.addData("Status", "Ended");
        telemetry.update();
    }
}
