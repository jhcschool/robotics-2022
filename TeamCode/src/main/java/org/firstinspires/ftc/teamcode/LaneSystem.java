package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.MarkerCallback;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.acmerobotics.roadrunner.trajectory.TrajectoryBuilder;

import org.firstinspires.ftc.teamcode.drive.Drive;
import org.firstinspires.ftc.teamcode.drive.GrizzlyDrive;

public class LaneSystem {

    private final float[] laneCoordinates;
    private final Drive drive;
    private Vector2d[] nodes;

    public LaneSystem(Drive drive, float[] laneCoordinates) {
        this.drive = drive;
        this.laneCoordinates = laneCoordinates;

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
    public Trajectory beginNavigatingTo(Vector2d target, float relativeHeading, MarkerCallback endCallback) {

        Vector2d targetNode = nodes[0];

        for (int i = 0; i < nodes.length; i++) {
            if (nodes[i].distTo(target) < targetNode.distTo(target)) {
                targetNode = nodes[i];
            }
        }

        Pose2d currentPose = drive.getPoseEstimate();

        // might be wrong
        float absoluteHeading = (float) target.angleBetween(targetNode);

        TrajectoryBuilder trajectoryBuilder = drive.trajectoryBuilder(currentPose);

        trajectoryBuilder.lineToLinearHeading(new Pose2d(targetNode.getX(), currentPose.getY(), absoluteHeading + relativeHeading));
        trajectoryBuilder.lineToLinearHeading(new Pose2d(targetNode.getX(), targetNode.getY(), absoluteHeading + relativeHeading));

        if (endCallback != null)
            trajectoryBuilder.addDisplacementMarker(endCallback);

        return trajectoryBuilder.build();
    }

    public Trajectory getToLane(MarkerCallback endCallback) {

        Vector2d currentPosition = drive.getPoseEstimate().vec();

        boolean isX = true;
        float distanceToNearestLane = Float.MAX_VALUE;

        for (float laneCoordinate : laneCoordinates) {
            if (Math.abs(laneCoordinate - currentPosition.getX()) < Math.abs(distanceToNearestLane)) {
                distanceToNearestLane = (float) (laneCoordinate - currentPosition.getX());
                isX = true;
            }

            if (Math.abs(laneCoordinate - currentPosition.getY()) < Math.abs(distanceToNearestLane)) {
                distanceToNearestLane = (float) (laneCoordinate - currentPosition.getY());
                isX = false;
            }
        }

        Vector2d target;
        if (isX) {
            target = new Vector2d(currentPosition.getX() + distanceToNearestLane, currentPosition.getY());
        } else {
            target = new Vector2d(currentPosition.getX(), currentPosition.getY() + distanceToNearestLane);
        }

        TrajectoryBuilder builder = drive.trajectoryBuilder(drive.getPoseEstimate()).lineTo(target);

        if (endCallback != null)
            builder.addDisplacementMarker(endCallback);

        return builder.build();
    }
}
