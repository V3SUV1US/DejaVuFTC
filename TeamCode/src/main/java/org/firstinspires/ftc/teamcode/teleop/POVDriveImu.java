/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.dependencies.Robot;

import static org.firstinspires.ftc.teamcode.dependencies.Constants.HD_COUNTS_PER_INCH;


/**
 * POV Drive mode with encoder driving compatibility w/o while loops
 */

@TeleOp(name="POV Drive Imu", group="Competition")
//@Disabled
public class POVDriveImu extends LinearOpMode {

    // Declare OpMode members.
    Robot r = new Robot(this);

    double[] g1 = new double[4];
    double[] g2 = new double[4];
    double[] g1Adjusted = new double[4];
    double[] g2Adjusted = new double[4];

    private static double DEADZONE = 0.35;

    private static double MODIFIER = 0.1;

    double powL = 0;
    double powR = 0;
    
    double powIntake = 0;
    double powIntakeMax = 1;
    double powIntakeMin = -1;

    double powLift = 0;
    double powLiftMax = 1;
    double powLiftMin = -1;

    double powRotate = 0;
    double powRotateOutwards = 0.5;
    double powRotateTowardsRobot = -0.5;

    double powFL, powFR, powBR, powBL;

    double powTelescope = 0;
    
    final double TRIGGER_DEADZONE = 0.3;

    boolean telescopingMax = false;
    boolean telescopingMin = false;

    //code spec:
    // powLift; 1 for raise, -1 for fall
    // powIntake; 1 for intake, -1 for expel
    // powRotate; 1 for outwards, -1 for inwards
    // powTelescope; 1 for extend, -1 for retract


    @Override
    public void runOpMode() {
        // Wait for the game to start (driver presses PLAY)
        r.start(hardwareMap, telemetry);
        r.init();

        for(int i = 0; i<4; i++) {
            r.driveMotors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
            r.driveMotors[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }

        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            setGamepads(MODIFIER);

            powFL = Range.clip(g1[1] + g1[2] + g1[0], -1, 1);
            powFR = Range.clip(g1[1] - g1[2] - g1[0], -1, 1);
            powBL = Range.clip(g1[1] + g1[2] - g1[0], -1, 1);
            powBR = Range.clip(g1[1] - g1[2] + g1[0], -1, 1);


            //Button handling
            //L/R Trigger, intake
            r.refreshAngle();
            telemetry.addData("angle",r.getHeading());

            int[] currentPos = new int[4];


            if(gamepad1.a) {
                for(int i = 0; i<4; i++) {
                    r.driveMotors[i].setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                    r.driveMotors[i].setMode(DcMotor.RunMode.RUN_USING_ENCODER);
                }
            }



            for(int i = 0; i<4; i++) {
                currentPos[i] = r.driveMotors[i].getCurrentPosition();
                telemetry.addData(r.driveMotors[i] + " Pos, In", "%4d : %4f", currentPos[i], currentPos[i]/HD_COUNTS_PER_INCH);
            }








            telemetry.update();

            setPowers(powFL, powFR, powBL, powBR);


            sleep(100);





            /*
            //TODO see if vertical motor can be floated due to surgical tubing 
            



            //TODO a "bug" is that if the next tick the current position is instantaneously less than or equal to 0,
            // both telescopingMax and Min will be true, therefore the motor will lock.
            // I want to test to see if this case happens, which most likely will, then we can changed the code by making
            // two different if/else clauses. Let's just try it out!
            // also good thing for eng nb
            // or, we can keep this as is annd make the second powIntake handling part an else if so only 1 runs
            if(r.armMotors[1].getCurrentPosition()>=(TELESCOPING_MAX_POSITION))
                telescopingMax = true;
            else if(r.armMotors[1].getCurrentPosition()<=0)
                telescopingMin = true;
            else {
                telescopingMax = false;
                telescopingMin = false;
            }


            //TODO check if hardware cycle will allow this to run correctly
            if(telescopingMax) {
                if(powTelescope > 0)
                    powTelescope = 0;
            }

            if(telescopingMin) {
                if(powTelescope < 0)
                    powTelescope = 0;
            }
            */









        }
    }

    private void intake() {
        if(gamepad1.right_trigger > TRIGGER_DEADZONE)
            powIntake = powIntakeMax;
        else if (gamepad1.left_trigger > TRIGGER_DEADZONE)
            powIntake = powIntakeMin;
        else
            powIntake = 0;
    }

    private void raise() {
        if(gamepad1.dpad_up)
            powLift = powLiftMax;
        else if (gamepad1.dpad_down)
            powLift = powLiftMin;
        else
            powLift = 0;
    }

    private void rotation() {
        if(gamepad1.x)
            powRotate = powRotateTowardsRobot;
        else if (gamepad1.b)
            powRotate = powRotateOutwards;
        else
            powRotate = 0;
    }

    private void setPowers(double powFL, double powFR, double powBL, double powBR) {
        r.driveMotors[0].setPower(powFL);
        r.driveMotors[1].setPower(powFR);
        r.driveMotors[2].setPower(powBR);
        r.driveMotors[3].setPower(powBL);

    }

    private void setGamepads(double modifier) {
        //left joystick x, y, right joystick x, y
//        g1[0] = gamepad1.left_stick_x * modifier;
//        g1[1] = -gamepad1.left_stick_y * modifier;
//        g1[2] = gamepad1.right_stick_x * modifier;
//        g1[3] = -gamepad1.right_stick_y * modifier;

        g1[0] = gamepad1.left_stick_x;
        g1[1] = -gamepad1.left_stick_y;
        g1[2] = gamepad1.right_stick_x;
        g1[3] = -gamepad1.right_stick_y;

        for(int i = 0; i < g1.length; i++) {
            g1[i] = (Math.abs(g1[i]) > DEADZONE ? g1[i] : 0);
            g1[i]*=modifier;
        }





//        g2[0] = gamepad2.left_stick_x * modifier;
//        g2[1] = -gamepad2.left_stick_y * modifier;
//        g2[2] = gamepad2.right_stick_x * modifier;
//        g2[3] = -gamepad2.right_stick_y * modifier;

        //TODO deadzones


        telemetry.addData("left stick x:", "%4f", gamepad1.left_stick_x);
        telemetry.addData("left stick y:", "%4f", gamepad1.left_stick_y);
        telemetry.addData("right stick x:", "%4f", gamepad1.right_stick_x);
        telemetry.addData("right stick y:", "%4f", gamepad1.right_stick_y);
    }


//    private void ramping() {
//
//    }
}
