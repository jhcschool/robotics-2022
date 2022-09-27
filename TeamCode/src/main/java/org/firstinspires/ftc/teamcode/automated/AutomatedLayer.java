package org.firstinspires.ftc.teamcode.automated;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ArmSystem;
import org.firstinspires.ftc.teamcode.FrameInfo;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.Layer;
import org.firstinspires.ftc.teamcode.LayerInitInfo;
import org.firstinspires.ftc.teamcode.LaneSystem;
import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.game.GameConstants;

public class AutomatedLayer extends Layer {

    private Telemetry telemetry;
    private Hardware hardware;

    private SleeveDetector.CustomSleeve sleeveColor;
    private SleeveDetector sleeveDetector;

    private LaneSystem laneSystem;
    private ArmSystem armSystem;

    private static final Pose2d STARTING_POSE = new Pose2d(0, 0, Math.toRadians(0));

    @Override
    public void init(LayerInitInfo initInfo) {
        super.init(initInfo);

        telemetry = initInfo.telemetry;
        hardware = initInfo.hardware;

        int viewId = initInfo.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", initInfo.hardwareMap.appContext.getPackageName());

        sleeveDetector = new SleeveDetector(viewId, hardware);
        laneSystem = new LaneSystem(hardware.drive, GameConstants.LANE_COORDINATES);
    }

    @Override
    public void onStart() {
        super.onStart();

        hardware.drive.setPoseEstimate(STARTING_POSE);

        sleeveColor = sleeveDetector.getSingleDetection();
    }

    @Override
    public void tick(FrameInfo frameInfo) {
        super.tick(frameInfo);

        laneSystem.tick();
        armSystem.tick();
    }

    @Override
    public void onEnd() {
        super.onEnd();

        PoseStorage.robotPose = hardware.drive.getPoseEstimate();
    }

    private void onJunctionArrival() {

    }

    private void navigateToNearestJunction() {
        Pose2d pose = hardware.drive.getPoseEstimate();

        Vector2d nearestJunction = GameConstants.HIGH_JUNCTIONS[0];

        for (Vector2d junction: GameConstants.HIGH_JUNCTIONS) {

            double distance = pose.vec().distTo(junction);
            if (distance < pose.vec().distTo(nearestJunction)) {
                nearestJunction = junction;
            }
        }

        float endDistance = laneSystem.beginNavigatingTo(nearestJunction, 0, this::onJunctionArrival);

//        double angleToNearestJunction = Math.atan2(nearestJunction.getY() - pose.getY(), nearestJunction.getX() - pose.getX());
//        // go to 10 inches away from the junction
//        distanceToNearestJunction -= 10;
//        Pose2d target = new Pose2d(Math.cos(angleToNearestJunction) * distanceToNearestJunction, Math.sin(angleToNearestJunction) * distanceToNearestJunction, angleToNearestJunction);
//
//        Trajectory trajectory = hardware.drive.trajectoryBuilder(pose).lineToSplineHeading(target).addDisplacementMarker(this::onJunctionArrival).build();
    }

    private Trajectory navigateToSignalZone() {
        Pose2d pose = hardware.drive.getPoseEstimate();
        TrajectoryBuilder builder = hardware.drive.trajectoryBuilder(pose).lineToSplineHeading(STARTING_POSE);

        if (sleeveColor == SleeveDetector.CustomSleeve.RED) {
            builder.strafeLeft(10);

        } else if (sleeveColor == SleeveDetector.CustomSleeve.BLUE) {
            builder.strafeRight(10);
        }

        return builder.build();
    }

}
