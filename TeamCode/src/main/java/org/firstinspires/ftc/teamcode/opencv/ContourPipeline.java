package org.firstinspires.ftc.teamcode.opencv;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;

public class ContourPipeline extends OpenPipeline {

    Object sync = new Object();
    private final ArrayList<OpenRecognition> recognitions = new ArrayList<>();
    private int cameraWidth;
    private int cameraHeight;

    private Scalar[] lowerBounds;
    private Scalar[] upperBounds;

    private String[] labels;

    public ContourPipeline() {
    }

    public ContourPipeline(Scalar[] lowerBounds, Scalar[] upperBounds, String[] labels) {
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
        this.labels = labels;
    }

    public void setLowerBounds(Scalar[] lowerBounds) {
        this.lowerBounds = lowerBounds;
    }

    public void setUpperBounds(Scalar[] upperBounds) {
        this.upperBounds = upperBounds;
    }

    public void setLabels(String[] labels) {
        this.labels = labels;
    }

    @Override
    public Recognition[] getRecognitions() {
        synchronized (sync) {
            return recognitions.toArray(new Recognition[0]);
        }
    }

    @Override
    public Mat processFrame(Mat input) {
        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGBA2RGB);
        Imgproc.cvtColor(input, input, Imgproc.COLOR_RGB2HSV);

        Mat processed = new Mat();

        cameraWidth = input.width();
        cameraHeight = input.height();

        for (int i = 0; i < lowerBounds.length; i++) {

            Scalar lowerBound = lowerBounds[i];
            Scalar upperBound = upperBounds[i];
            String label = labels[i];

            Core.inRange(input, lowerBound, upperBound, processed);

            Imgproc.morphologyEx(processed, processed, Imgproc.MORPH_OPEN, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new org.opencv.core.Size(5, 5)));
            Imgproc.morphologyEx(processed, processed, Imgproc.MORPH_CLOSE, Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new org.opencv.core.Size(5, 5)));

            Imgproc.GaussianBlur(processed, processed, new org.opencv.core.Size(5, 5), 0);

            ArrayList<MatOfPoint> contours = new ArrayList<>();

            Mat contourMat = new Mat();
            Imgproc.findContours(processed, contours, contourMat, Imgproc.RETR_LIST, Imgproc.CHAIN_APPROX_SIMPLE);

            synchronized (sync) {
                recognitions.clear();

                for (MatOfPoint contour : contours) {
                    Rect rect = Imgproc.boundingRect(contour);

                    recognitions.add(new OpenRecognition(label, 1, rect.x, rect.x + rect.width, rect.y, rect.y + rect.height, rect.width, rect.height, cameraWidth, cameraHeight));
                }
            }

            contourMat.release();
        }

        processed.release();

        return input;
    }

}
