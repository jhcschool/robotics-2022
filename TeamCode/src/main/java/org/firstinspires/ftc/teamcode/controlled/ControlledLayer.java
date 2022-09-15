package org.firstinspires.ftc.teamcode.controlled;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.FrameInfo;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.Layer;
import org.firstinspires.ftc.teamcode.LayerInitInfo;

@TeleOp(name="Controlled", group="Iterative Opmode")
public class ControlledLayer extends Layer {

        private ControlSystem controlSystem;
        private Hardware hardware;

        public ControlledLayer() {
            controlSystem = new ControlSystem();
        }

    @Override
    public void init(LayerInitInfo info) {
        super.init(info);
        hardware = info.hardware;
    }

    @Override
    public void tick(FrameInfo frameInfo) {
            controlSystem.tick(hardware.gamepad, hardware);
        }
}
