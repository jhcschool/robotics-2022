package org.firstinspires.ftc.teamcode.drive;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.localization.Localizer;
import com.qualcomm.robotcore.hardware.DcMotorEx;

import org.firstinspires.ftc.teamcode.util.Encoder;


// Alternate non-dead-wheel localizer mode that doesn't use the IMU.
public class WheelEncoderLocalizer implements Localizer {
    private Pose2d poseEstimate = new Pose2d(0, 0, 0);
    private Pose2d lastPoseEstimate = new Pose2d(0, 0, 0);

    private Encoder frontLeftEncoder, rearLeftEncoder, frontRightEncoder, rearRightEncoder;

    public WheelEncoderLocalizer(DcMotorEx frontLeftEncoder, DcMotorEx rearLeftEncoder, DcMotorEx rearRightEncoder, DcMotorEx frontRightEncoder) {
        this.frontLeftEncoder = new Encoder(frontLeftEncoder);
        this.rearLeftEncoder = new Encoder(rearLeftEncoder);
        this.rearRightEncoder = new Encoder(rearRightEncoder);
        this.frontRightEncoder = new Encoder(frontRightEncoder);

        this.frontRightEncoder.setDirection(Encoder.Direction.REVERSE);
        this.rearRightEncoder.setDirection(Encoder.Direction.REVERSE);
    }

    @NonNull
    @Override
    public Pose2d getPoseEstimate() {
        return poseEstimate;
    }

    @Override
    public void setPoseEstimate(@NonNull Pose2d pose2d) {
        poseEstimate = pose2d;
    }

    @Nullable
    @Override
    public Pose2d getPoseVelocity() {
        return poseEstimate.minus(lastPoseEstimate);
    }

    double lastFl = 0, lastFr = 0, lastRl = 0, lastRr = 0;

    @Override
    public void update() {
        lastPoseEstimate = poseEstimate;

        double fl = DriveConstants.encoderTicksToInches(frontLeftEncoder.getCurrentPosition());
        double fr = DriveConstants.encoderTicksToInches(frontRightEncoder.getCurrentPosition());
        double rl = DriveConstants.encoderTicksToInches(rearLeftEncoder.getCurrentPosition());
        double rr = DriveConstants.encoderTicksToInches(rearRightEncoder.getCurrentPosition());

        double dfl = fl - lastFl;
        double dfr = fr - lastFr;
        double drl = rl - lastRl;
        double drr = rr - lastRr;
        double dx = (dfl + dfr + drl + drr) / 4;
        double dy = (dfl - dfr + drl - drr) / 4;
        double dtheta = (dfl - dfr - drl + drr) / (4 * DriveConstants.TRACK_WIDTH);

        lastFl = fl;
        lastFr = fr;
        lastRl = rl;
        lastRr = rr;

        poseEstimate = poseEstimate.plus(new Pose2d(dx, dy, dtheta));
    }
}
