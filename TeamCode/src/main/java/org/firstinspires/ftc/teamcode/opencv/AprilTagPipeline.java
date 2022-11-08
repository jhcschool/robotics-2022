package org.firstinspires.ftc.teamcode.opencv;

import org.firstinspires.ftc.robotcore.external.tfod.Recognition;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.imgproc.Imgproc;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.apriltag.AprilTagDetectorJNI;

import java.util.ArrayList;
import java.util.HashMap;


// mostly taken from the apriltag example
public class AprilTagPipeline extends OpenPipeline {

    // Camera constants
    private static final double fx = 578.272;
    private static final double fy = 578.272;
    private static final double cx = 402.145;
    private static final double cy = 221.506;
    private static final double tagSize = 0.166;

    private final Object newDetectionsSync = new Object();
    private final Object decimationSync = new Object();
    private final Mat gray = new Mat();
    private final Mat cameraMatrix;
    private final HashMap<Integer, String> idToLabel;
    private long nativeAprilTagPointer;
    private ArrayList<AprilTagDetection> detections = new ArrayList<>();
    private ArrayList<AprilTagDetection> newDetections = new ArrayList<>();
    private float decimation = 3;
    private boolean needToSetDecimation = true;

    public AprilTagPipeline(HashMap<Integer, String> idToLabel) {
        this.idToLabel = idToLabel;

        cameraMatrix = new Mat(3, 3, CvType.CV_32FC1);

        cameraMatrix.put(0, 0, fx);
        cameraMatrix.put(0, 1, 0);
        cameraMatrix.put(0, 2, cx);

        cameraMatrix.put(1, 0, 0);
        cameraMatrix.put(1, 1, fy);
        cameraMatrix.put(1, 2, cy);

        cameraMatrix.put(2, 0, 0);
        cameraMatrix.put(2, 1, 0);
        cameraMatrix.put(2, 2, 1);

        // Allocate a native context object. See the corresponding deletion in the destructor.
        nativeAprilTagPointer = AprilTagDetectorJNI.createApriltagDetector(AprilTagDetectorJNI.TagFamily.TAG_36h11.string, 3, 3);
    }

    @Override
    public void finalize() {
        AprilTagDetectorJNI.releaseApriltagDetector(nativeAprilTagPointer);
        nativeAprilTagPointer = 0;
    }

    @Override
    public Mat processFrame(Mat input) {
        // Convert to greyscale
        Imgproc.cvtColor(input, gray, Imgproc.COLOR_RGBA2GRAY);

        synchronized (decimationSync) {
            if (needToSetDecimation) {
                AprilTagDetectorJNI.setApriltagDetectorDecimation(nativeAprilTagPointer, decimation);
                needToSetDecimation = false;
            }
        }

        // Run AprilTag
        detections = AprilTagDetectorJNI.runAprilTagDetectorSimple(nativeAprilTagPointer, gray, tagSize, fx, fy, cx, cy);

        synchronized (newDetectionsSync) {
            newDetections = detections;
        }

        return input;
    }

    public void setDecimation(float decimation) {
        synchronized (decimationSync) {
            this.decimation = decimation;
            needToSetDecimation = true;
        }
    }

    @Override
    public Recognition[] getRecognitions() {
        synchronized (newDetectionsSync) {
            Recognition[] recognitions = new OpenRecognition[newDetections.size()];
            for (int i = 0; i < recognitions.length; i++) {
                AprilTagDetection detection = newDetections.get(i);

                if (!idToLabel.containsKey(detection.id)) {
                    continue;
                }

                String label = idToLabel.get(detection.id);

                float top = (float) Math.min(detection.corners[0].y, detection.corners[1].y);
                float bottom = (float) Math.max(detection.corners[2].y, detection.corners[3].y);
                float left = (float) Math.min(detection.corners[0].x, detection.corners[3].x);
                float right = (float) Math.max(detection.corners[1].x, detection.corners[2].x);

                float width = right - left;
                float height = bottom - top;

                int imageWidth = cameraMatrix.width();
                int imageHeight = cameraMatrix.height();

                recognitions[i] = new OpenRecognition(label, detection.decisionMargin, left, right, top, bottom, width, height, imageWidth, imageHeight);
            }

            return recognitions;
        }
    }
}
