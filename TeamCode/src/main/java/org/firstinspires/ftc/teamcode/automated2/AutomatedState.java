package org.firstinspires.ftc.teamcode.automated2;

public enum AutomatedState {
    // Trajectory states
    INITIAL_NAVIGATION,
    CONE_STACK_MOVE,
    JUNCTION_MOVE,
    PARKING_LOCATION_MOVE,

    INCHING_FORWARD,
    INCHING_BACKWARD,

    SLIDE_UP_FOR_CONE,
    CONE_CLIPPING,
    SLIDE_UP_PAST_CONE,

    MAIN_SLIDE_UP,
    MAIN_SLIDE_DOWN,
    CONE_RELEASING,
}
