package org.firstinspires.ftc.teamcode.automated;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.ArmSystem;
import org.firstinspires.ftc.teamcode.FrameInfo;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.LaneSystem;
import org.firstinspires.ftc.teamcode.Layer;
import org.firstinspires.ftc.teamcode.LayerInitInfo;
import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.game.GameConstants;
import org.firstinspires.ftc.teamcode.game.JunctionHeight;

public class AutomatedLayer extends Layer {

    private final Vector2d CONE_POSITION_BLUE = new Vector2d(-60, 0);
    private final Vector2d CONE_POSITION_RED = new Vector2d(60, 0);
    protected Pose2d startingPose = new Pose2d(0, 0, Math.toRadians(0));
    private SleeveSystem sleeveSystem;
    private Trajectory currentTrajectory = null;
    private Telemetry telemetry;
    private Hardware hardware;
    private LaneSystem laneSystem;
    private ArmSystem armSystem;

    public AutomatedLayer(Pose2d startingPose) {
        super();

        this.startingPose = startingPose;
    }

    @Override
    public void init(LayerInitInfo initInfo) {
        super.init(initInfo);

        telemetry = initInfo.telemetry;
        hardware = initInfo.hardware;

        int viewId = initInfo.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", initInfo.hardwareMap.appContext.getPackageName());

        laneSystem = new LaneSystem(hardware.drive, GameConstants.LANE_COORDINATES);
        armSystem = new ArmSystem();

        sleeveSystem = new SleeveSystem(viewId, hardware, (Float f) -> {
            navigateBack(f);
            return null;
        });

        currentTrajectory = laneSystem.getToLane(() -> {
            navigateToNearestJunction();
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        hardware.drive.setPoseEstimate(startingPose);

    }

    @Override
    public void tick(FrameInfo frameInfo) {
        super.tick(frameInfo);

        armSystem.tick();

        if (currentTrajectory != null && !hardware.drive.isBusy()) {
            hardware.drive.followTrajectoryAsync(currentTrajectory);
        }
    }

    @Override
    public void onEnd() {
        super.onEnd();

        PoseStorage.robotPose = hardware.drive.getPoseEstimate();
        hardware.drive.breakFollowing();

    }

    private void onJunctionArrival() {
        armSystem.signalBegin(JunctionHeight.HIGH, () -> {
            currentTrajectory = null;
            acquireCone();
        });
    }

    private void acquireCone() {
        currentTrajectory = laneSystem.beginNavigatingTo(CONE_POSITION_BLUE, 0, () -> {
            float distance = (float) hardware.drive.getPoseEstimate().vec().distTo(CONE_POSITION_BLUE);
            currentTrajectory = hardware.drive.trajectoryBuilder(hardware.drive.getPoseEstimate()).forward(distance).addDisplacementMarker(() -> {
                currentTrajectory = null;
                pickUpCone();
            }).build();
        });
    }

    private void navigateToNearestJunction() {
        Pose2d pose = hardware.drive.getPoseEstimate();

        Vector2d nearestJunction = GameConstants.HIGH_JUNCTIONS[0];

        for (Vector2d junction : GameConstants.HIGH_JUNCTIONS) {

            double distance = pose.vec().distTo(junction);
            if (distance < pose.vec().distTo(nearestJunction)) {
                nearestJunction = junction;
            }
        }

        currentTrajectory = laneSystem.beginNavigatingTo(nearestJunction, 0, () -> {
            currentTrajectory = null;
            onJunctionArrival();
        });

//        double angleToNearestJunction = Math.atan2(nearestJunction.getY() - pose.getY(), nearestJunction.getX() - pose.getX());
//        // go to 10 inches away from the junction
//        distanceToNearestJunction -= 10;
//        Pose2d target = new Pose2d(Math.cos(angleToNearestJunction) * distanceToNearestJunction, Math.sin(angleToNearestJunction) * distanceToNearestJunction, angleToNearestJunction);
//
//        Trajectory trajectory = hardware.drive.trajectoryBuilder(pose).lineToSplineHeading(target).addDisplacementMarker(this::onJunctionArrival).build();
    }

    private void navigateBack(float distanceRight) {
        currentTrajectory = laneSystem.beginNavigatingTo(startingPose.vec(), 0, () -> {
            float left = distanceRight > 0 ? 0 : -distanceRight;
            float right = distanceRight < 0 ? 0 : distanceRight;
            currentTrajectory = hardware.drive.trajectoryBuilder(hardware.drive.getPoseEstimate()).lineToLinearHeading(startingPose)
                    .forward(10).strafeLeft(left).strafeRight(right).build();
        });
    }

    private void pickUpCone() {

    }
}
