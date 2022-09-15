package org.firstinspires.ftc.teamcode.opencv;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.tfod.Recognition;

public class OpenRecognition implements Recognition {

    private String label;
    private float confidence;
    private float left;
    private float right;
    private float top;
    private float bottom;
    private float width;
    private float height;
    private int imageWidth;
    private int imageHeight;

    public OpenRecognition(String label, float confidence, float left, float right, float top, float bottom, float width, float height, int imageWidth, int imageHeight) {
        this.label = label;
        this.confidence = confidence;
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.width = width;
        this.height = height;
        this.imageWidth = imageWidth;
        this.imageHeight = imageHeight;
    }

    @Override
    public double estimateAngleToObject(AngleUnit angleUnit) {
        return 0;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public float getConfidence() {
        return confidence;
    }

    @Override
    public float getLeft() {
        return left;
    }

    @Override
    public float getRight() {
        return right;
    }

    @Override
    public float getTop() {
        return top;
    }

    @Override
    public float getBottom() {
        return bottom;
    }

    @Override
    public float getWidth() {
        return width;
    }

    @Override
    public float getHeight() {
        return height;
    }

    @Override
    public int getImageWidth() {
        return imageWidth;
    }

    @Override
    public int getImageHeight() {
        return imageHeight;
    }
}
