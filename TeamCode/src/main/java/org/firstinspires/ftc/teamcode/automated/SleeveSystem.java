package org.firstinspires.ftc.teamcode.automated;

import org.firstinspires.ftc.robotcore.external.Function;
import org.firstinspires.ftc.teamcode.CustomSleeve;
import org.firstinspires.ftc.teamcode.FrameInfo;
import org.firstinspires.ftc.teamcode.Hardware;

import java.util.ArrayList;

public class SleeveSystem {
    private final int TARGET_DETECTIONS = 15;
    // Returns the distance that the robot move to the right to get into the signal zone
    private Function<Float, Void> onNavigateBack;
    private CustomSleeve sleeveColor;
    private final SleeveDetector sleeveDetector;
    private final ArrayList<CustomSleeve> firstDetections = new ArrayList<>();

    SleeveSystem(int viewId, Hardware hardware, Function<Float, Void> onNavigateBack) {
        sleeveDetector = new SleeveDetector(viewId, hardware);
        this.onNavigateBack = onNavigateBack;
    }

    public void tick(FrameInfo frameInfo) {
        getFirstDetections();

        if (frameInfo.time > 25 && onNavigateBack != null) {
            float distanceRight = 0;

            if (sleeveColor == CustomSleeve.RIGHT) {
                distanceRight += 12;
            } else if (sleeveColor == CustomSleeve.LEFT) {
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
            int rightCount = 0;
            int centerCount = 0;
            int leftCount = 0;

            for (CustomSleeve sleeve : firstDetections) {
                if (sleeve == CustomSleeve.RIGHT) rightCount++;
                if (sleeve == CustomSleeve.CENTER) centerCount++;
                if (sleeve == CustomSleeve.LEFT) leftCount++;
            }

            if (rightCount > centerCount && rightCount > leftCount)
                sleeveColor = CustomSleeve.RIGHT;
            if (centerCount > rightCount && centerCount > leftCount)
                sleeveColor = CustomSleeve.CENTER;
            if (leftCount > rightCount && leftCount > centerCount)
                sleeveColor = CustomSleeve.LEFT;
        }
    }
}
