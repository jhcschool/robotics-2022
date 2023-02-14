package org.firstinspires.ftc.teamcode.automated3;

import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.FrameInfo;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.Layer;
import org.firstinspires.ftc.teamcode.LayerInitInfo;
import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.arm.TimedArmSystem;
import org.firstinspires.ftc.teamcode.arm.TimedClipperSystem;
import org.firstinspires.ftc.teamcode.automated.SleeveDetector;
import org.firstinspires.ftc.teamcode.trajectorysequence.TrajectorySequence;

public class FastAutomatedStateLayer extends Layer {

    // ALERT!!!!!: Very bad code I made because time is limited
    private static final double RETURN_TIME = 8;
    private final TrajectoryRepository trajectoryRepository;
    private final Object buildSync = new Object();
    private Telemetry telemetry;
    private Hardware hardware;
    private double currentTime = 0.0;
    private ElapsedTime timeSinceStateActivation = new ElapsedTime();
    private AutomatedState currentState = null;
    private TimedArmSystem armSystem;
    private TimedClipperSystem clipperSystem;
    private SleeveDetector sleeveDetector;
    private Thread buildThread;
    private boolean built = false;

    private boolean startedMovingSlide = false;
    private boolean doneMovingSlide = false;

    public FastAutomatedStateLayer(TrajectoryRepository trajectoryRepository) {
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
        armSystem = new TimedArmSystem(hardware.slideArmMotor);
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

    @Override
    public void tick(FrameInfo frameInfo) {
        super.tick(frameInfo);

        armSystem.update();
        clipperSystem.update();

        currentTime = frameInfo.time;

        telemetry.addLine("Current state is: " + currentState.name());

        switch (currentState) {
            case INITIAL_NAVIGATION:
                if (timeSinceStateActivation.seconds() > (trajectoryRepository.initialNavigationEstimatedTime - armSystem.mainWaitTime) && !startedMovingSlide) {
                    startedMovingSlide = true;
                    beginSlideUp();
                }
                if (!hardware.drive.isBusy() && doneMovingSlide) {
                    startedMovingSlide = false;
                    doneMovingSlide = false;
                    beginSlideDown();
                }
                break;
            case JUNCTION_MOVE:
                if (timeSinceStateActivation.seconds() > (trajectoryRepository.junctionMoveEstimatedTime - armSystem.mainWaitTime) && !startedMovingSlide) {
                    startedMovingSlide = true;
                    beginSlideUp();
                }
                if (!hardware.drive.isBusy() && doneMovingSlide) {
                    startedMovingSlide = false;
                    doneMovingSlide = false;
                    beginSlideDown();
                }
                break;

            case CONE_STACK_MOVE:
                if (timeSinceStateActivation.seconds() > (trajectoryRepository.coneStackMoveEstimatedTime - 0.6) && !startedMovingSlide) {
                    startedMovingSlide = true;
                    beginConeRetrieval();
                }

                if (!hardware.drive.isBusy() && doneMovingSlide) {
                    startedMovingSlide = false;
                    doneMovingSlide = false;
                    beginClip();
                }
                break;

            case CONE_CLIPPING:
            case CONE_RELEASING:
            case SLIDE_UP_PAST_CONE:
            case PARKING_LOCATION_MOVE:
            case MAIN_SLIDE_DOWN:
            default:
                break;


        }
    }

    private void moveToState(AutomatedState automatedState) {
        currentState = automatedState;
        timeSinceStateActivation.reset();
    }

    private void beginConeRetrieval() {
        armSystem.liftToCone(() -> {
            doneMovingSlide = true;
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
        armSystem.liftPastCone(() -> {
                moveToState(AutomatedState.JUNCTION_MOVE);
                hardware.drive.followTrajectorySequenceAsync(trajectoryRepository.junctionMove);
        });
    }

    private void afterConeRelease() {
            if (shouldReturnToPark()) {
                moveToState(AutomatedState.PARKING_LOCATION_MOVE);
                TrajectorySequence returnTrajectory = trajectoryRepository.parkingLocationMove.get(sleeveDetector.getResult());
                hardware.drive.followTrajectorySequenceAsync(returnTrajectory);
            } else {
                moveToState(AutomatedState.CONE_STACK_MOVE);
                hardware.drive.followTrajectorySequenceAsync(trajectoryRepository.coneStackMove);
            }
    }

    private void beginSlideUp() {
        armSystem.setRaised(true, () -> {
            doneMovingSlide = true;
        });
    }

    private void beginSlideDown() {
        moveToState(AutomatedState.MAIN_SLIDE_DOWN);
        armSystem.setRaised(false, () -> {
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
