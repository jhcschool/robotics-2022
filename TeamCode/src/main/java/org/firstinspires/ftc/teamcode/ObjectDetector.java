package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

public interface ObjectDetector {
    public Recognition[] getRecognitions();
}
