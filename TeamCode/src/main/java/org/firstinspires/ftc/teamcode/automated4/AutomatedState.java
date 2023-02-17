package org.firstinspires.ftc.teamcode.automated4;

public enum AutomatedState {
    // Trajectory states
    INITIAL_NAVIGATION,
    CONE_STACK_MOVE,
    JUNCTION_MOVE,
    PARKING_LOCATION_MOVE,

    CONE_CLIPPING,
    SLIDE_UP_PAST_CONE,

    MAIN_SLIDE_DOWN,
    CONE_RELEASING,

    ERROR_REDUCTION,
}
