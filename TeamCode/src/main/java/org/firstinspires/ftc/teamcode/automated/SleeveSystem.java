package org.firstinspires.ftc.teamcode.automated;

import org.firstinspires.ftc.robotcore.external.Function;
import org.firstinspires.ftc.teamcode.FrameInfo;
import org.firstinspires.ftc.teamcode.Hardware;

import java.util.ArrayList;

public class SleeveSystem {
    private final int TARGET_DETECTIONS = 5;
    // Returns the distance that the robot move to the right to get into the signal zone
    Function<Float, Void> onNavigateBack;
    private SleeveDetector.CustomSleeve sleeveColor;
    private SleeveDetector sleeveDetector;
    private ArrayList<SleeveDetector.CustomSleeve> firstDetections = new ArrayList<>();

    SleeveSystem(int viewId, Hardware hardware, Function<Float, Void> onNavigateBack) {
        sleeveDetector = new SleeveDetector(viewId, hardware);
        this.onNavigateBack = onNavigateBack;
    }

    public void tick(FrameInfo frameInfo) {
        getFirstDetections();

        if (frameInfo.time > 25 && onNavigateBack != null) {
            float distanceRight = 0;

            if (sleeveColor == SleeveDetector.CustomSleeve.BLUE) {
                distanceRight += 12;
            } else if (sleeveColor == SleeveDetector.CustomSleeve.RED) {
                distanceRight -= 12;
            }

            onNavigateBack.apply(distanceRight);
            onNavigateBack = null;
        }
    }

    private void getFirstDetections() {
        if (firstDetections.size() < TARGET_DETECTIONS) return;
        firstDetections.add(sleeveDetector.getSingleDetection());

        if (firstDetections.size() == TARGET_DETECTIONS) {
            int redCount = 0;
            int greenCount = 0;
            int blueCount = 0;

            for (SleeveDetector.CustomSleeve sleeve : firstDetections) {
                if (sleeve == SleeveDetector.CustomSleeve.RED) redCount++;
                if (sleeve == SleeveDetector.CustomSleeve.GREEN) greenCount++;
                if (sleeve == SleeveDetector.CustomSleeve.BLUE) blueCount++;
            }

            if (redCount > greenCount && redCount > blueCount)
                sleeveColor = SleeveDetector.CustomSleeve.RED;
            if (greenCount > redCount && greenCount > blueCount)
                sleeveColor = SleeveDetector.CustomSleeve.GREEN;
            if (blueCount > redCount && blueCount > greenCount)
                sleeveColor = SleeveDetector.CustomSleeve.BLUE;
        }
    }
}
