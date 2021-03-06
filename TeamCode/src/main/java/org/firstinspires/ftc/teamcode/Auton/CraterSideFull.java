package org.firstinspires.ftc.teamcode.Auton;
import com.disnodeteam.dogecv.CameraViewDisplay;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.vuforia.CameraDevice;

//import org.firstinspires.ftc.teamcode.DogeCVTesting.CustomGoldDetector;
import org.firstinspires.ftc.teamcode.dependencies.Enums;
import org.firstinspires.ftc.teamcode.dependencies.Enums.GoldPosition;
import org.firstinspires.ftc.teamcode.dependencies.Robot;

import static org.firstinspires.ftc.teamcode.dependencies.Constants.SERVO_UNLOCKED;


@Autonomous(name = "Crater Side Full", group = "Auton")
public class CraterSideFull extends LinearOpMode {

    Robot r = new Robot(this, Enums.OpModeType.AUTON);

    //constants that will probably be moved to the Constants class
    private double speed = 0.22;
    private int time = 50;

    @Override public void runOpMode() {
        r.start(hardwareMap, telemetry);
        r.init();

        CameraDevice.getInstance().setFlashTorchMode(false);


        telemetry.addData("Stat:" ,"Initialized");
        telemetry.update();

        waitForStart();

        //detach from the lander

        r.armMotors[0].setPower(-1);

        sleep(200);
        r.servoMotors[1].setPosition(0.35);
        sleep(500);

        //r.positionDrive(0,840,0.3 );

        r.armMotors[0].setPower(0.5);
        while(!isStopRequested() && r.armMotors[0].getCurrentPosition() <= 900) {}
        r.armMotors[0].setPower(0);

        sleep(100);

        //this is an enum
        //save the detected position of the gold mineral
        //GoldPosition x = r.getGoldPosition();
        GoldPosition x = GoldPosition.MIDDLE;

        //translate out of the hook

        r.translate(Enums.Direction.RIGHT, 4, 0.1);

        //todo go back 640 counts
        r.translate(Enums.Direction.BACK, 14.5,-0.15);

        //check if last motor hits pos or first motor hits position

        //the robot moves to knock out the detected mineral in its position
        //net backwards distance is 45 inches in every case
        telemetry.addData("Position:", x);
        telemetry.update();

        //translate 46 inches right
        //rotate ccw to 150°
        //translate 30 inches back
        //drop marker
        //translate 30 inches forward
        //rotate ccw to 180°
        //translate 46 inches left +/- distance to mineral
        //drive forward for a while

        r.translate(Enums.Direction.RIGHT, 41, speed);
        sleep(time);
        r.rotate("ccw", speed, 126);
        sleep(time);
        r.translate(Enums.Direction.LEFT, 3, speed);
        sleep(time);
        r.translate(Enums.Direction.BACK, 31, speed);
        sleep(time);
        r.positionDrive(2, 600,0.75);
        sleep(500);
        r.positionDrive(2, -600,0.75);
        sleep(time);
        r.translate(Enums.Direction.FWD, 31, speed);
        sleep(time);
        r.translate(Enums.Direction.RIGHT, 3, speed);
        sleep(time);
        r.rotate("cw", speed, -30);
        sleep(time);

        if (x==Enums.GoldPosition.MIDDLE) {
            telemetry.update();
            r.translate(Enums.Direction.LEFT,33+13, speed);
            r.translate(Enums.Direction.BACK,20, speed);
        }
        else if (x==Enums.GoldPosition.RIGHT) {
            r.translate(Enums.Direction.LEFT, 36+33, speed);
            sleep(time);
            r.translate(Enums.Direction.BACK,20, speed);
            sleep(time);
//            r.translate(Enums.Direction.RIGHT, 14, speed);
//            sleep(time);
        }else{
            r.translate(Enums.Direction.LEFT, 31, speed);
            sleep(time);
            r.translate(Enums.Direction.BACK,20, speed);
            sleep(time);
//            r.translate(Enums.Direction.LEFT, 14, speed);
//            sleep(time);

            telemetry.update();
        }
    }
}
