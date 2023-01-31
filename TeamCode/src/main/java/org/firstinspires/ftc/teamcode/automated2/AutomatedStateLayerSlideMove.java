package org.firstinspires.ftc.teamcode.automated2;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FrameInfo;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.Layer;
import org.firstinspires.ftc.teamcode.LayerInitInfo;
import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.arm.ClipperSystem;
import org.firstinspires.ftc.teamcode.arm.SimpleArmSystem;
import org.firstinspires.ftc.teamcode.arm.TimedClipperSystem;
import org.firstinspires.ftc.teamcode.automated.SleeveDetector;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

public class AutomatedStateLayerSlideMove extends Layer {

    /* Same as automated state layer, but moves arm at the same time as the drivetrain */
    private static final double RETURN_TIME = 8;
    private final TrajectoryRepository trajectoryRepository;
    private final Object buildSync = new Object();
    private Telemetry telemetry;
    private Hardware hardware;
    private double currentTime = 0.0;
    private AutomatedState lastMovementState = null;
    private AutomatedState lastState = null;
    private AutomatedState currentState = null;
    private SimpleArmSystem simpleArmSystem;
    private TimedClipperSystem clipperSystem;
    private SleeveDetector sleeveDetector;
    private Runnable lastCallback = null;
    private Thread buildThread;
    private boolean built = false;
    private boolean doneMovingSlide = false;

    public AutomatedStateLayerSlideMove(TrajectoryRepository trajectoryRepository) {
        super();

        this.trajectoryRepository = trajectoryRepository;
    }

    @Override
    public void init(LayerInitInfo initInfo) {
        super.init(initInfo);

        telemetry = initInfo.telemetry;
        hardware = initInfo.hardware;

        int viewId = initInfo.hardwareMap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", initInfo.hardwareMap.appContext.getPackageName());
        sleeveDetector = new SleeveDetector(viewId, hardware.webcamName);
        simpleArmSystem = new SimpleArmSystem(hardware.slideArmMotor);
        clipperSystem = new TimedClipperSystem(hardware.leftServo, hardware.rightServo);

        buildThread = new Thread(() -> {
            trajectoryRepository.build(hardware.drive);
            synchronized (buildSync) {
                built = true;
            }
        });
        buildThread.start();
    }

    @Override
    public void beforeStartLoop(FrameInfo frameInfo) {
        super.beforeStartLoop(frameInfo);

        sleeveDetector.update();

        synchronized (buildSync) {
            telemetry.addData("Trajectories built", built);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

        synchronized (buildSync) {
            if (!built) {
                throw new RuntimeException("Wait for the trajectories to be built before starting the game");
            }
            try {
                buildThread.join(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        hardware.drive.setPoseEstimate(trajectoryRepository.getInitialPose());
        sleeveDetector.onGameStart();

        moveToState(AutomatedState.CONE_CLIPPING);
        clipperSystem.beginClip(() -> {
            moveToState(AutomatedState.INITIAL_NAVIGATION);
            hardware.drive.followTrajectorySequenceAsync(trajectoryRepository.initialNavigation);
        });
    }

    private boolean initialBeganSlide = false;

    @Override
    public void tick(FrameInfo frameInfo) {
        super.tick(frameInfo);

        simpleArmSystem.update();
        clipperSystem.update();

        currentTime = frameInfo.time;

        telemetry.addLine("Current state is: " + currentState.name());

        switch (currentState) {
            case INITIAL_NAVIGATION:
                if (!initialBeganSlide && hardware.drive.getPoseEstimate().vec().distTo(trajectoryRepository.initialNavigation.end().vec()) < 10) {
                    beginSlideUp();
                    initialBeganSlide = false;
                }
            case JUNCTION_MOVE:
                if (!hardware.drive.isBusy() && doneMovingSlide) {
                    beginInchingForward(() -> {
                        beginSlideDown();
                    });
                    doneMovingSlide = false;
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
            case CONE_RELEASING:
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
        clipperSystem.beginClip(() -> {
            afterConeClip();
        });
    }

    private void beginRelease() {
        moveToState(AutomatedState.CONE_RELEASING);
        clipperSystem.beginRelease(() -> {
            afterConeRelease();
        });
    }

    private void afterConeClip() {
        moveToState(AutomatedState.SLIDE_UP_PAST_CONE);
        simpleArmSystem.liftPastCone(() -> {
            beginInchingBackward(() -> {
                moveToState(AutomatedState.JUNCTION_MOVE);
                hardware.drive.followTrajectorySequenceAsync(trajectoryRepository.junctionMove);
                beginSlideUp();
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

    private void beginSlideUp() {
//        moveToState(AutomatedState.MAIN_SLIDE_UP);
        simpleArmSystem.setRaised(true, () -> {
            doneMovingSlide = true;
        });
    }

    private void beginSlideDown() {
        moveToState(AutomatedState.MAIN_SLIDE_DOWN);
        simpleArmSystem.setRaised(false, () -> {
            beginRelease();
        });
    }

    private boolean shouldReturnToPark() {
        return currentTime > (30 - RETURN_TIME);
    }


    @Override
    public void onEnd() {
        super.onEnd();

        PoseStorage.robotPose = hardware.drive.getPoseEstimate();
        hardware.drive.breakFollowing();

        clipperSystem.adjustInitialPosition();
    }
}
