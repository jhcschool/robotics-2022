package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.trajectory.MarkerCallback;

import org.firstinspires.ftc.teamcode.game.JunctionHeight;

public class ArmSystem {

    JunctionHeight height = null;

    public void tick() {
        if (height == null) return;
    }

    MarkerCallback endCallback;

    public void signalBegin(JunctionHeight height, MarkerCallback endCallback) {
        this.height = height;
        this.endCallback = endCallback;
    }
}
