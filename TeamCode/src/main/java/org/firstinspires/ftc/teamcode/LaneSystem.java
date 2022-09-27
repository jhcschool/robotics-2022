package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.MarkerCallback;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;

import org.firstinspires.ftc.teamcode.drive.StandardMecanumDrive;

public class LaneSystem {

    public LaneSystem(StandardMecanumDrive drive, float[] laneCoordinates) {
        this.drive = drive;

        int i = 0;
        for (float laneCoordinate : laneCoordinates) {
            for (float laneCoordinate2 : laneCoordinates) {
                Vector2d node = new Vector2d(laneCoordinate, laneCoordinate2);
                nodes[i] = node;
                i++;
            }
        }
    }

    // returns end distance from target
    public float beginNavigatingTo(Vector2d target, float relativeHeading, MarkerCallback endCallback) {
        this.target = target;

        Vector2d closestNode = nodes[0];
        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].distTo(target) < closestNode.distTo(target)) {
                closestNode = nodes[i];
            }
        }
        targetNode = closestNode;

        Pose2d currentPose = drive.getPoseEstimate();

        // might be wrong
        float absoluteHeading = (float) target.angleBetween(targetNode);

        trajectoryBuilder = drive.trajectoryBuilder(currentPose);

        trajectoryBuilder.lineToLinearHeading(new Pose2d(targetNode.getX(), currentPose.getY(), absoluteHeading + relativeHeading));
        trajectoryBuilder.lineToLinearHeading(new Pose2d(targetNode.getX(), targetNode.getY(), absoluteHeading + relativeHeading));

        trajectoryBuilder.addDisplacementMarker(() -> {
            trajectory = null;
        });

        if (endCallback != null)
        trajectoryBuilder.addDisplacementMarker(endCallback);

        trajectory = trajectoryBuilder.build();

        float endDistance = (float) target.distTo(targetNode);

        return endDistance;
    }

    public void stopNavigation() {
        trajectory = null;
    }

    public void tick() {
        if (trajectory != null) {
            drive.followTrajectoryAsync(trajectory);
        }
    }

    private Vector2d[] nodes;

    Vector2d targetNode;
    Vector2d target;

    Trajectory trajectory;
    TrajectoryBuilder trajectoryBuilder;

    private StandardMecanumDrive drive;
}
