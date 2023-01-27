package org.firstinspires.ftc.teamcode.automated;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FrameInfo;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.LaneSystem;
import org.firstinspires.ftc.teamcode.Layer;
import org.firstinspires.ftc.teamcode.LayerInitInfo;
import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.arm.ClipperSystem;
import org.firstinspires.ftc.teamcode.arm.SimpleArmSystem;
import org.firstinspires.ftc.teamcode.arm.TimedArmSystem;
import org.firstinspires.ftc.teamcode.game.GameConstants;
import org.firstinspires.ftc.teamcode.game.JunctionHeight;

public class AutomatedLayer extends Layer {

    private final Vector2d CONE_POSITION_BLUE = new Vector2d(-63, 0);
    private final Vector2d CONE_POSITION_RED = new Vector2d(63, 0);
    private final AllianceMember allianceMember;
    private Pose2d startingPose = new Pose2d(0, 0, Math.toRadians(0));
    private SleeveSystem sleeveSystem;
    private Trajectory currentTrajectory = null;
    private Telemetry telemetry;
    private Hardware hardware;
    private LaneSystem laneSystem;
    //    private TimedArmSystem timedArmSystem;
    private SimpleArmSystem simpleArmSystem;
    private ClipperSystem clipperSystem;
    private Vector2d currentJunction = null;

    public AutomatedLayer(Pose2d startingPose, AllianceMember allianceMember) {
        super();

        this.startingPose = startingPose;
        this.allianceMember = allianceMember;
    }

    @Override
    public void init(LayerInitInfo initInfo) {
        super.init(initInfo);

        telemetry = initInfo.telemetry;
        hardware = initInfo.hardware;

        int viewId = initInfo.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", initInfo.hardwareMap.appContext.getPackageName());

        laneSystem = new LaneSystem(hardware.drive, GameConstants.LANE_COORDINATES);
//        timedArmSystem = new TimedArmSystem(hardware.slideArmMotor);
        simpleArmSystem = new SimpleArmSystem(hardware.slideArmMotor);

        sleeveSystem = new SleeveSystem(viewId, hardware.webcamName, (Float f) -> {
            navigateBack(f);
            return null;
        });

        currentTrajectory = laneSystem.getToLane(() -> {
            navigateToNearestJunction();
        });

        clipperSystem = new ClipperSystem(hardware.leftServo, hardware.rightServo);
    }

    @Override
    public void beforeStartLoop(FrameInfo frameInfo) {
        super.beforeStartLoop(frameInfo);

        sleeveSystem.update();
    }

    @Override
    public void onStart() {
        super.onStart();

        hardware.drive.setPoseEstimate(startingPose);
        sleeveSystem.onGameStart();
    }

    @Override
    public void tick(FrameInfo frameInfo) {
        super.tick(frameInfo);

        simpleArmSystem.update();
        clipperSystem.update();

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
        simpleArmSystem.setRaised(true, () -> {
            moveOnTop();
        });
    }

    private void moveOnTop() {
        float distance = (float) hardware.drive.getPoseEstimate().vec().distTo(currentJunction);
        currentTrajectory = hardware.drive.trajectoryBuilder().forward(distance).addDisplacementMarker(() -> {
            currentTrajectory = null;
            releaseCone();
        }).build();
    }

    private void releaseCone() {
        simpleArmSystem.setRaised(false, () -> {
            clipperSystem.beginRelease();
            try {
                Thread.currentThread().sleep(100);
            } catch (InterruptedException exc) {
                navigateToNearestJunction();
            }
            acquireCone();
        });
    }

    private void acquireCone() {

        Vector2d conePosition = allianceMember == AllianceMember.RED ? CONE_POSITION_RED : CONE_POSITION_BLUE;

        currentTrajectory = laneSystem.beginNavigatingTo(conePosition, 0, () -> {
            float distance = (float) hardware.drive.getPoseEstimate().vec().distTo(conePosition);
            currentTrajectory = hardware.drive.trajectoryBuilder(hardware.drive.getPoseEstimate()).forward(distance).addDisplacementMarker(() -> {
                currentTrajectory = null;
                pickUpCone();
            }).build();
        });
    }

    private void navigateToNearestJunction() {
        Pose2d pose = hardware.drive.getPoseEstimate();

        currentJunction = GameConstants.HIGH_JUNCTIONS[0];

        for (Vector2d junction : GameConstants.HIGH_JUNCTIONS) {

            double distance = pose.vec().distTo(junction);
            if (distance < pose.vec().distTo(currentJunction)) {
                currentJunction = junction;
            }
        }

        currentTrajectory = laneSystem.beginNavigatingTo(currentJunction, 0, () -> {
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
        hardware.drive.breakFollowing();
        currentTrajectory = laneSystem.beginNavigatingTo(startingPose.vec(), 0, () -> {
            boolean forward = distanceRight == 0;

            if (forward) {
                moveForward();
                return;
            }

            boolean left = distanceRight < 0;
            float distance = Math.abs(distanceRight);

            TrajectoryBuilder builder = hardware.drive.trajectoryBuilder(hardware.drive.getPoseEstimate());

            if (left) {
                builder.strafeLeft(distance);
            } else {
                builder.strafeRight(distance);
            }

            builder.addDisplacementMarker(() -> {
                currentTrajectory = null;
                moveForward();
            });

            currentTrajectory = builder.build();
        });
    }

    private void moveForward() {
        final float FORWARD_DISTANCE = 36;

        currentTrajectory = hardware.drive.trajectoryBuilder(hardware.drive.getPoseEstimate()).forward(FORWARD_DISTANCE).addDisplacementMarker(() -> {
            currentTrajectory = null;
        }).build();
    }

    private void pickUpCone() {
        clipperSystem.beginClip();
        try {
            Thread.currentThread().sleep(100);
        } catch (InterruptedException exc) {
        }
        navigateToNearestJunction();
    }


    enum AllianceMember {
        RED,
        BLUE
    }
}
