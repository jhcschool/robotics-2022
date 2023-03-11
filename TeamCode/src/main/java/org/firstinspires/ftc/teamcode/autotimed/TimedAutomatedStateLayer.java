package org.firstinspires.ftc.teamcode.autotimed;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.CustomSleeve;
import org.firstinspires.ftc.teamcode.FrameInfo;
import org.firstinspires.ftc.teamcode.Hardware;
import org.firstinspires.ftc.teamcode.Layer;
import org.firstinspires.ftc.teamcode.LayerInitInfo;
import org.firstinspires.ftc.teamcode.PoseStorage;
import org.firstinspires.ftc.teamcode.arm.TimedArmSystem;
import org.firstinspires.ftc.teamcode.arm.TimedClipperSystem;
import org.firstinspires.ftc.teamcode.automated.SleeveDetector;

public class TimedAutomatedStateLayer extends Layer {

    private final TimedRepository timedRepository;
    private Telemetry telemetry;
    private Hardware hardware;
    private double currentTime = 0.0;
    private ElapsedTime timeSinceStateActivation = new ElapsedTime();
    private AutomatedState currentState = null;
    private TimedArmSystem armSystem;
    private TimedClipperSystem clipperSystem;
    private SleeveDetector sleeveDetector;
    private CustomSleeve sleeveResult = null;
    private boolean startedMovingSlide = false;
    private boolean doneMovingSlide = false;

    public TimedAutomatedStateLayer(TimedRepository timedRepository) {
        super();

        this.timedRepository = timedRepository;
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
        sleeveDetector.start();
    }

    @Override
    public void beforeStartLoop(FrameInfo frameInfo) {
        super.beforeStartLoop(frameInfo);

        sleeveDetector.update();
    }

    @Override
    public void onStart() {
        super.onStart();

        hardware.drive.setPoseEstimate(new Pose2d());
        sleeveDetector.onGameStart();

        moveToState(AutomatedState.CONE_CLIPPING);
        clipperSystem.beginClip(() -> {
            moveToState(AutomatedState.INITIAL_FORWARD);
        });

        sleeveResult = sleeveDetector.getResult();
    }

    @Override
    public void tick(FrameInfo frameInfo) {
        super.tick(frameInfo);

        armSystem.update();
        clipperSystem.update();

        currentTime = frameInfo.time;

        telemetry.addLine("Current state is: " + currentState.name());
        telemetry.addData("Sleeve result", sleeveResult.name());

        switch (currentState) {
            case INITIAL_FORWARD:
                if (timeSinceStateActivation.seconds() > (timedRepository.initialNavigationEstimatedTime - armSystem.mainWaitTime) && !startedMovingSlide) {
                    startedMovingSlide = true;
                    beginSlideUp();
                }
                if (timedRepository.initialForward(hardware.drive, timeSinceStateActivation) && doneMovingSlide) {
                    startedMovingSlide = false;
                    doneMovingSlide = false;
                    moveToState(AutomatedState.JUNCTION_STRAFE);
                }
                break;

            case JUNCTION_STRAFE:
                if (timedRepository.junctionStrafe(hardware.drive, timeSinceStateActivation)) {
                    beginSlideDown();
                }
                break;

            case JUNCTION_BACK:
                if (timedRepository.junctionBackward(hardware.drive, timeSinceStateActivation)) {
                    if (shouldReturnToPark()) {
                        moveToState(AutomatedState.PARKING_LOCATION_MOVE);
                    } else {
                        moveToState(AutomatedState.ROTATE_FACE_STACK);
                    }
                }
                break;

            case ROTATE_FACE_STACK:
                if (timedRepository.rotateFaceStack(hardware.drive, timeSinceStateActivation)) {
                    moveToState(AutomatedState.STACK_FORWARD);
                }
                break;

            case STACK_FORWARD:
                if (timeSinceStateActivation.seconds() > (timedRepository.coneStackMoveEstimatedTime - 0.6) && !startedMovingSlide) {
                    startedMovingSlide = true;
                    beginConeRetrieval();
                }

                if (timedRepository.stackForward(hardware.drive, timeSinceStateActivation) && doneMovingSlide) {
                    startedMovingSlide = false;
                    doneMovingSlide = false;
                    beginClip();
                }
                break;

            case STACK_BACK:
                if (timeSinceStateActivation.seconds() > (timedRepository.junctionMoveEstimatedTime - armSystem.mainWaitTime) && !startedMovingSlide) {
                    startedMovingSlide = true;
                    beginSlideUp();
                }
                if (timedRepository.stackBackward(hardware.drive, timeSinceStateActivation) && doneMovingSlide) {
                    startedMovingSlide = false;
                    doneMovingSlide = false;
                    beginSlideDown();
                }

            case ROTATE_ALIGN_JUNCTION:
                if (timedRepository.rotateAlignJunction(hardware.drive, timeSinceStateActivation)) {
                    moveToState(AutomatedState.JUNCTION_STRAFE);
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
        try {
            Thread.sleep(200);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
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
                moveToState(AutomatedState.STACK_BACK);
        });
    }

    private void afterConeRelease() {
        moveToState(AutomatedState.JUNCTION_BACK);
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

    private static int RETURN_TIME = 10;
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
