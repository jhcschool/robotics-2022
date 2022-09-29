package org.firstinspires.ftc.teamcode;

import org.firstinspires.ftc.teamcode.game.JunctionHeight;

public class ArmSystem {

    JunctionHeight height = null;
    private Runnable endCallback;

    public void tick() {
        if (height == null) return;
    }

    public void signalBegin(JunctionHeight height, Runnable endCallback) {
        this.height = height;
        this.endCallback = endCallback;
    }

    private void onEnd() {
        height = null;
        endCallback.run();
    }
}
