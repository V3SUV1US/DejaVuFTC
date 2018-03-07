package org.firstinspires.ftc.teamcode.ImTrying;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import static org.firstinspires.ftc.teamcode.called.RobotValues.ARM_JEWEL_UP;
import static org.firstinspires.ftc.teamcode.called.RobotValues.SCALE_FACTOR;

/**
 * Created by vali on 2/26/2018.
 */

/**
 * Created by vali on 2/26/2018.
 */

/**
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When an selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive TeleopHdrive for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studios to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this opmode to the Driver Station OpMode list
 */

@TeleOp(name="TeleopHdrive", group="Linear Opmode")
//@Disabled

public class TeleopHdrive extends LinearOpMode {

    // Declare OpMode members.

    //TODO: assign servo values; they are not 0.93
    public double CLAW_RIGHT1_OUT = 0.7;
    public double CLAW_RIGHT2_OUT = 0.67;
    public double CLAW_LEFT1_OUT = 0.28;
    public double CLAW_LEFT2_OUT = 0.26;

    public double CLAW_RIGHT1_IN = 0.5;
    public double CLAW_RIGHT2_IN = 0.47;
    public double CLAW_LEFT1_IN = 0.48;
    public double CLAW_LEFT2_IN = 0.47;

    private double LeftPow = 0;
    private double RightPow = 0;
    private double ClawPow = 0;
    private double StrafePow = 0;

    private DcMotor LeftDrive = null;
    private DcMotor RightDrive = null;
    private DcMotor StrafeDrive = null;
    private DcMotor ClawMotor = null;
    public Servo ClawR1, ClawL1, ClawR2, ClawL2;
    public ColorSensor sensorColor;

    //public float hsv[] = {0F, 0F, 0F};

    @Override
    public void runOpMode() {
        telemetry.addData("Say", "Hello Driver");    //
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        RightDrive  = hardwareMap.get(DcMotor.class, "right_drive");
        LeftDrive  = hardwareMap.get(DcMotor.class, "left_drive");
        ClawMotor  = hardwareMap.get(DcMotor.class, "claw_motor");
        StrafeDrive  = hardwareMap.get(DcMotor.class, "strafe_drive");

        ClawR1 = hardwareMap.get(Servo.class, "claw_right1");
        ClawR2 = hardwareMap.get(Servo.class, "claw_right2");
        ClawL1 = hardwareMap.get(Servo.class, "claw_left1");
        ClawL2 = hardwareMap.get(Servo.class, "claw_left2");

        ClawR1.setPosition(CLAW_RIGHT1_OUT);
        ClawR2.setPosition(CLAW_RIGHT2_OUT);
        ClawL1.setPosition(CLAW_LEFT1_OUT);
        ClawL2.setPosition(CLAW_LEFT2_OUT);

        //sensorColor = hardwareMap.get(ColorSensor.class, "sensor_color_distance");
        //Color.RGBToHSV((int) (sensorColor.red() * SCALE_FACTOR),
        //        (int) (sensorColor.green() * SCALE_FACTOR),
        //        (int) (sensorColor.blue() * SCALE_FACTOR),
        //        hsv);

        // Most robots need the motor on one side to be reversed to drive forward
        // Reverse the motor that runs backwards when connected directly to the battery
        LeftDrive.setDirection(DcMotor.Direction.FORWARD);
        RightDrive.setDirection(DcMotor.Direction.REVERSE);
        ClawMotor.setDirection(DcMotor.Direction.FORWARD);
        StrafeDrive.setDirection(DcMotor.Direction.REVERSE);

        //Set zero power behavior - might be useful
        RightDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LeftDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        ClawMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        StrafeDrive.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        //Set power for drive motors
        RightDrive.setPower(RightPow);
        LeftDrive.setPower(LeftPow);
        ClawMotor.setPower(ClawPow);
        StrafeDrive.setPower(StrafePow);

        //Set runmode for all motors
        RightDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        LeftDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        ClawMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        StrafeDrive.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);

        // Wait for the game to start (driver presses PLAY)
        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            //TODO: the whole holonomic drive thingy

            //StrafePow = gamepad1.left_stick_x;

            double drive = -gamepad1.left_stick_y;
            double turn  =  gamepad1.right_stick_x;
            LeftPow = Range.clip(drive + turn, -1.0, 1.0) ;
            RightPow = Range.clip(drive - turn, -1.0, 1.0) ;

            //Set power for all motors
            RightDrive.setPower(RightPow);
            LeftDrive.setPower(LeftPow);
            ClawMotor.setPower(ClawPow);
            StrafeDrive.setPower(StrafePow);

            /*
            if(gamepad1.dpad_left)
                StrafePow += 0.3;
            if(gamepad1.dpad_right)
                StrafePow -= 0.3;
            */

            if(gamepad1.y) {
                ClawPow += 0.1;
            }
            if(gamepad1.a) {
                ClawPow -= 0.1;
            }

            ClawPow = Range.clip(ClawPow, -0.3, 0.3) ;
//            if(gamepad1.a) {
//                ClawPow = -1;
//                sleep(1000);
//                ClawPow = 0;
//            }

            if(gamepad1.x) {
                ClawR1.setPosition(CLAW_RIGHT1_IN);
                ClawR2.setPosition(CLAW_RIGHT2_IN);
                ClawL1.setPosition(CLAW_LEFT1_IN);
                ClawL2.setPosition(CLAW_LEFT2_IN);
            }
            if(gamepad1.b) {
                ClawR1.setPosition(CLAW_RIGHT1_OUT);
                ClawR2.setPosition(CLAW_RIGHT2_OUT);
                ClawL1.setPosition(CLAW_LEFT1_OUT);
                ClawL2.setPosition(CLAW_LEFT2_OUT);
            }

            telemetry.addData("Claw motor power:", ClawPow);
            telemetry.update();

            sleep(69);

            /*
            // Show the elapsed game time and wheel power.
            telemetry.addData("Status", "Run Time: " + runtime.toString());
            telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftPower, rightPower);
            telemetry.update();
            */
            /*
            telemetry.addData("Servo Right1:", CLAW_RIGHT1_OUT);
            telemetry.addData("Servo Right2:", CLAW_RIGHT2_OUT);
            telemetry.addData("Servo Left1:", CLAW_LEFT1_OUT);
            telemetry.addData("Servo Left2:", CLAW_LEFT2_OUT);
            telemetry.update();
            */
        }
    }
}


    /*
    //name DC motors
    public DcMotor mtrFL, mtrFR, mtrBL, mtrBR, mtrClawL, mtrClawR;

    //Power variables
    public double powFL = 0;
    public double powFR = 0;
    public double powBL = 0;
    public double powBR = 0;
    public double powClawL = 0;
    public double powClawR = 0;

    HardwareMap hwMap           =  null;
    */


