package org.firstinspires.ftc.teamcode.automated;

import org.firstinspires.ftc.robotcore.external.Function;
import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.teamcode.CustomSleeve;
import org.firstinspires.ftc.teamcode.FrameInfo;

public class SleeveSystem {
    private final int TARGET_DETECTIONS = 15;
    private static final int DISTANCE_STRAFE = 27;

    private final SleeveDetector sleeveDetector;
    // Returns the distance that the robot move to the right to get into the signal zone
    private Function<Float, Void> onNavigateBack;

    public SleeveSystem(int viewId, WebcamName webcamName, Function<Float, Void> onNavigateBack) {
        sleeveDetector = new SleeveDetector(viewId, webcamName);
        this.onNavigateBack = onNavigateBack;

        sleeveDetector.start();
    }

    public void update() {
        sleeveDetector.update();
    }

    public void onGameStart() {
        sleeveDetector.onGameStart();
    }

    public void tick(FrameInfo frameInfo) {
        CustomSleeve sleeveColor = sleeveDetector.getResult();

        if (frameInfo.time > 25 && onNavigateBack != null) {
            float distanceRight = 0;

            if (sleeveColor == CustomSleeve.RIGHT) {
                distanceRight += DISTANCE_STRAFE;
            } else if (sleeveColor == CustomSleeve.LEFT) {
                distanceRight -= DISTANCE_STRAFE;
            }

            onNavigateBack.apply(distanceRight);
            onNavigateBack = null;
        }
    }
}
