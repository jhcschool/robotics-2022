package org.firstinspires.ftc.teamcode.opencv;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class ColorPipeline extends OpenPipeline {

    private static final Point SLEEVE_TOPLEFT_ANCHOR_POINT = new Point(145, 168);
    public static int REGION_WIDTH = 30;
    public static int REGION_HEIGHT = 50;
    // Anchor point definitions
    private Point SLEEVE_POINT_A = new Point(
            SLEEVE_TOPLEFT_ANCHOR_POINT.x,
            SLEEVE_TOPLEFT_ANCHOR_POINT.y);
    private Point SLEEVE_POINT_B = new Point(
            SLEEVE_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            SLEEVE_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

    private Mat[] colorMatrices;
    private Mat blurredMat = new Mat();
    private Scalar[] lowerBounds;
    private Scalar[] upperBounds;
    private String[] labels;
    private double[] percentages;

    public ColorPipeline(Scalar[] lowerBounds, Scalar[] upperBounds, String[] labels) {
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
        this.labels = labels;

        colorMatrices = new Mat[lowerBounds.length];
        percentages = new double[colorMatrices.length];

        for (int i = 0; i < colorMatrices.length; i++) {
            colorMatrices[i] = new Mat();
            percentages[i] = 0;
        }
    }

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGBA2RGB);
        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2HSV);

        // Noise reduction
        Imgproc.blur(input, blurredMat, new Size(5, 5));
        Mat blurred = blurredMat.submat(new Rect(SLEEVE_POINT_A, SLEEVE_POINT_B));

        // Apply Morphology
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Imgproc.morphologyEx(blurredMat, blurredMat, Imgproc.MORPH_CLOSE, kernel);

        // Gets channels from given source mat
        for (int i = 0; i < colorMatrices.length; i++) {
            Core.inRange(blurred, lowerBounds[i], upperBounds[i], colorMatrices[i]);
            percentages[i] = Core.countNonZero(colorMatrices[i]);

            colorMatrices[i].release();
        }

        blurredMat.release();
        kernel.release();
        blurred.release();

        return input;
    }

    @Override
    public Recognition[] getRecognitions() {
        Recognition[] recognitions = new Recognition[colorMatrices.length];

        for (int i = 0; i < recognitions.length; i++) {
            recognitions[i] = new OpenRecognition("Blue", (float) percentages[i], 0, 0, 0, 0, 0, 0, 0, 0);
        }

        return recognitions;

    }
}
