package org.firstinspires.ftc.teamcode.opencv;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.openftc.easyopencv.OpenCvPipeline;

public abstract class OpenPipeline extends OpenCvPipeline {

    public abstract Recognition[] getRecognitions();
}
