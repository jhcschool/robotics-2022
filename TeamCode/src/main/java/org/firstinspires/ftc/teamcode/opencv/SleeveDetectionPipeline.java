package org.firstinspires.ftc.teamcode.opencv;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;

public class SleeveDetectionPipeline extends OpenPipeline {

    // TOPLEFT anchor point for the bounding box
    private static Point SLEEVE_TOPLEFT_ANCHOR_POINT = new Point(145, 168);

    // Width and height for the bounding box
    public static int REGION_WIDTH = 30;
    public static int REGION_HEIGHT = 50;

    // Lower and upper boundaries for colors
    private static final Scalar LOWER_RED_BOUNDS = new Scalar(100, 0, 0);
    private static final Scalar UPPER_RED_BOUNDS = new Scalar(255, 100, 100);
    private static final Scalar LOWER_GREEN_BOUNDS = new Scalar(0, 0, 100);
    private static final Scalar UPPER_GREEN_BOUNDS = new Scalar(100, 100, 255);
    private static final Scalar LOWER_BLUE_BOUNDS = new Scalar(0, 100, 0);
    private static final Scalar UPPER_BLUE_BOUNDS = new Scalar(100, 255, 100);

    // Color definitions
    private final Scalar
            YELLOW = new Scalar(200, 0, 0),
            CYAN = new Scalar(0, 200, 0),
            MAGENTA = new Scalar(0, 0, 200);

    // Percent and mat definitions
    private double yelPercent, cyaPercent, magPercent;
    private Mat yelMat = new Mat(), cyaMat = new Mat(), magMat = new Mat(), blurredMat = new Mat();

    // Anchor point definitions
    Point sleeve_pointA = new Point(
            SLEEVE_TOPLEFT_ANCHOR_POINT.x,
            SLEEVE_TOPLEFT_ANCHOR_POINT.y);
    Point sleeve_pointB = new Point(
            SLEEVE_TOPLEFT_ANCHOR_POINT.x + REGION_WIDTH,
            SLEEVE_TOPLEFT_ANCHOR_POINT.y + REGION_HEIGHT);

    // Running variable storing the parking position

    @Override
    public Mat processFrame(Mat input) {
        // Noise reduction
        Imgproc.blur(input, blurredMat, new Size(5, 5));
        Mat blurred = blurredMat.submat(new Rect(sleeve_pointA, sleeve_pointB));

        // Apply Morphology
        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
        Imgproc.morphologyEx(blurredMat, blurredMat, Imgproc.MORPH_CLOSE, kernel);

        // Gets channels from given source mat
        Core.inRange(blurred, LOWER_RED_BOUNDS, UPPER_RED_BOUNDS, yelMat);
        Core.inRange(blurred, LOWER_BLUE_BOUNDS, UPPER_BLUE_BOUNDS, cyaMat);
        Core.inRange(blurred, LOWER_GREEN_BOUNDS, UPPER_GREEN_BOUNDS, magMat);

        // Gets color specific values
        yelPercent = Core.countNonZero(yelMat);
        cyaPercent = Core.countNonZero(cyaMat);
        magPercent = Core.countNonZero(magMat);

        // Calculates the highest amount of pixels being covered on each side
        double maxPercent = Math.max(yelPercent, Math.max(cyaPercent, magPercent));

        // Memory cleanup
        blurredMat.release();
        yelMat.release();
        cyaMat.release();
        magMat.release();
        kernel.release();
        blurred.release();

        return input;
    }

    @Override
    public Recognition[] getRecognitions() {

        Recognition[] recognitions = new Recognition[3];

        // Yellow recognition
        Recognition yellow = new OpenRecognition("Red", (float) yelPercent, 0, 0, 0, 0, 0, 0, 0, 0);
        recognitions[0] = yellow;

        // Cyan recognition
        Recognition cyan = new OpenRecognition("Green", (float) cyaPercent, 0, 0, 0, 0, 0, 0, 0, 0);
        recognitions[1] = cyan;

        // Magenta recognition
        Recognition magenta = new OpenRecognition("Blue", (float) magPercent, 0, 0, 0, 0, 0, 0, 0, 0);
        recognitions[2] = magenta;

        return recognitions;
    }
}