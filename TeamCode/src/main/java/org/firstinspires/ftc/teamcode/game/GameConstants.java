package org.firstinspires.ftc.teamcode.game;

import com.acmerobotics.roadrunner.geometry.Vector2d;

public class GameConstants {

    public static final Vector2d[] HIGH_JUNCTIONS = {new Vector2d(0, 24), new Vector2d(24, 0), new Vector2d(0, -24), new Vector2d(-24, 0)};

    public static final Vector2d[] MID_JUNCTIONS = {new Vector2d(24, 24), new Vector2d(24, -24), new Vector2d(-24, -24), new Vector2d(-24, 24)};

    public static final Vector2d[] LOW_JUNCTIONS = {new Vector2d(24, 48), new Vector2d(48, 24), new Vector2d(48, -24), new Vector2d(24, -48),
            new Vector2d(-24, -48), new Vector2d(-48, -24), new Vector2d(-48, 24), new Vector2d(-24, 48)};
    
    public static final float[] LANE_COORDINATES = {-63, -45, -27, -9, 9, 27, 45, 63};
}
