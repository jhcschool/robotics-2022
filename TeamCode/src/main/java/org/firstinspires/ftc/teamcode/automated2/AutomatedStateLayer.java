package org.firstinspires.ftc.teamcode.automated2;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FrameInfo;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.Layer;
import org.firstinspires.ftc.teamcode.LayerInitInfo;
import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.arm.ClipperSystem;
import org.firstinspires.ftc.teamcode.arm.SimpleArmSystem;
import org.firstinspires.ftc.teamcode.automated.SleeveDetector;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

public class AutomatedStateLayer extends Layer {

    /* Very different from the standard automated layer. It uses hardcoded trajectories and a finite state machine.
       It isn't the best code, but I don't have much time left in this season. */

    private Telemetry telemetry;
    private Hardware hardware;

    private final TrajectoryRepository trajectoryRepository;

    private double currentTime = 0.0;
    private double stateBeginTime;
    private AutomatedState lastMovementState = null;
    private AutomatedState lastState = null;
    private AutomatedState currentState = null;

    private SimpleArmSystem simpleArmSystem;
    private ClipperSystem clipperSystem;
    private SleeveDetector sleeveDetector;
    private Runnable lastCallback = null;

    public AutomatedStateLayer(TrajectoryRepository trajectoryRepository) {
        super();

        this.trajectoryRepository = trajectoryRepository;
    }

    @Override
    public void init(LayerInitInfo initInfo) {
        super.init(initInfo);

        telemetry = initInfo.telemetry;
        hardware = initInfo.hardware;

        trajectoryRepository.build(hardware.drive);

        int viewId = initInfo.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", initInfo.hardwareMap.appContext.getPackageName());
        sleeveDetector = new SleeveDetector(viewId, hardware.webcamName);
        simpleArmSystem = new SimpleArmSystem(hardware.slideArmMotor);
        clipperSystem = new ClipperSystem(hardware.leftServo, hardware.rightServo);
    }

    @Override
    public void beforeStartLoop(FrameInfo frameInfo) {
        super.beforeStartLoop(frameInfo);

        sleeveDetector.update();
    }

    @Override
    public void onStart() {
        super.onStart();

        hardware.drive.setPoseEstimate(trajectoryRepository.getInitialPose());
        sleeveDetector.onGameStart();

        moveToState(AutomatedState.INITIAL_NAVIGATION);
        hardware.drive.followTrajectorySequenceAsync(trajectoryRepository.initialNavigation);
    }

    @Override
    public void tick(FrameInfo frameInfo) {
        super.tick(frameInfo);

        simpleArmSystem.update();
        clipperSystem.update();

        currentTime = frameInfo.time;

        switch (currentState) {
            case INITIAL_NAVIGATION:
            case JUNCTION_MOVE:
                if (!hardware.drive.isBusy()) {
                    beginSlideUp();
                }
                break;

            case CONE_STACK_MOVE:
                if (!hardware.drive.isBusy()) {
                    beginConeRetrieval();
                }
                break;

            case INCHING_FORWARD:
            case INCHING_BACKWARD:
                if (!hardware.drive.isBusy()) {
                    if (lastCallback != null) {
                        lastCallback.run();
                    }
                }
                break;

            case CONE_CLIPPING:
                if (doneClip()) {
                    afterConeClip();
                }
                break;

            case CONE_RELEASING:
                if (doneClip()) {
                    afterConeRelease();
                }
                break;

            case SLIDE_UP_FOR_CONE:
            case SLIDE_UP_PAST_CONE:
            case PARKING_LOCATION_MOVE:
            case MAIN_SLIDE_UP:
            case MAIN_SLIDE_DOWN:
            default:
                break;


        }
    }

    private void moveToState(AutomatedState automatedState) {
        lastState = currentState;
        currentState = automatedState;
        stateBeginTime = currentTime;

        switch (automatedState) {
            case INITIAL_NAVIGATION:
            case JUNCTION_MOVE:
            case CONE_STACK_MOVE:
            case PARKING_LOCATION_MOVE:
                lastMovementState = automatedState;
                break;

            default:
                break;
        }
    }

    private void beginInchingForward(Runnable runnable) {
        moveToState(AutomatedState.INCHING_FORWARD);
        hardware.drive.followTrajectorySequenceAsync(trajectoryRepository.inchForwardTrajectories.get(lastMovementState));
        lastCallback = runnable;
    }

    private void beginInchingBackward(Runnable runnable) {
        moveToState(AutomatedState.INCHING_BACKWARD);
        hardware.drive.followTrajectorySequenceAsync(trajectoryRepository.inchBackwardTrajectories.get(lastMovementState));
        lastCallback = runnable;
    }

    private void beginConeRetrieval() {
        moveToState(AutomatedState.SLIDE_UP_FOR_CONE);
        simpleArmSystem.liftToCone(() -> {
            beginInchingForward(() -> {
                beginClip();
            });
        });
    }

    private void beginClip() {
        moveToState(AutomatedState.CONE_CLIPPING);
        clipperSystem.beginClip();
    }

    private void beginRelease() {
        moveToState(AutomatedState.CONE_RELEASING);
        clipperSystem.beginRelease();
    }

    private void afterConeClip() {
        moveToState(AutomatedState.SLIDE_UP_PAST_CONE);
        simpleArmSystem.liftPastCone(() -> {
            beginInchingBackward(() -> {
                moveToState(AutomatedState.JUNCTION_MOVE);
                hardware.drive.followTrajectorySequenceAsync(trajectoryRepository.junctionMove);
            });
        });
    }

    private void afterConeRelease() {
        beginInchingBackward(() -> {
            if (shouldReturnToPark()) {
                moveToState(AutomatedState.PARKING_LOCATION_MOVE);
                TrajectorySequence returnTrajectory = trajectoryRepository.parkingLocationMove.get(sleeveDetector.getResult());
                hardware.drive.followTrajectorySequenceAsync(returnTrajectory);
            } else {
                moveToState(AutomatedState.CONE_STACK_MOVE);
                hardware.drive.followTrajectorySequenceAsync(trajectoryRepository.coneStackMove);
            }
        });
    }

    private static double CLIP_TIME = 0.1;
    private boolean doneClip() {
        return currentTime > (stateBeginTime + CLIP_TIME);
    }

    private void beginSlideUp() {
        moveToState(AutomatedState.MAIN_SLIDE_UP);
        simpleArmSystem.setRaised(true, () -> {
            beginInchingForward(() -> {
                beginSlideDown();
            });
        });
    }

    private void beginSlideDown() {
        moveToState(AutomatedState.MAIN_SLIDE_DOWN);
        simpleArmSystem.setRaised(false, () -> {
            moveToState(AutomatedState.CONE_RELEASING);
            beginRelease();
        });
    }


    private static final double RETURN_TIME = 8;
    private boolean shouldReturnToPark() {
        return currentTime > (30 - RETURN_TIME);
    }


    @Override
    public void onEnd() {
        super.onEnd();

        PoseStorage.robotPose = hardware.drive.getPoseEstimate();
        hardware.drive.breakFollowing();
    }
}
