package org.firstinspires.ftc.teamcode.input;

import java.util.Map;

public class InputState {

    private Map<String, ActionState> actionStates;
    private Map<String, Double> axisStates;

    public ActionState getAction(String mapping) {
        return actionStates.get(mapping);
    }

    public double getAxis(String mapping) {
        return axisStates.get(mapping);
    }

    void registerAction(String mapping, ActionState state) {
        actionStates.put(mapping, state);
    }

    void registerAxis(String mapping, double value) {
        axisStates.put(mapping, value);
    }

    void clear() {
        actionStates.clear();
        axisStates.clear();
    }
}
