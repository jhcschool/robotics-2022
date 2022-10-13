package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

public interface ObjectDetector {
    Recognition[] getRecognitions();

    void start();

    void stop();
}
