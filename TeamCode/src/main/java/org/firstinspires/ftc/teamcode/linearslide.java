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

package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;


/**
 * Ramping Power
 * CB: Gaurav
 */

@TeleOp(name="Motor Run/Counts", group="Internal")

public class linearslide extends LinearOpMode {

    // Declare OpMode members.
    private DcMotor mtr;
    private double power = 0;
    private double epsilon = 0.1;
    private double numSteps = 10;
    private double count = 0;
    private double joystick;
    private double oldJoystick;

    @Override
    public void runOpMode() {
        // Wait for the game to start (driver presses PLAY)

        mtr = hardwareMap.dcMotor.get("a_motor");
        mtr.setDirection(DcMotor.Direction.FORWARD);

        //once ramping is completed, then it can brake.
        mtr.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mtr.setPower(power);
        mtr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        mtr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        //854 counts for getting on the ground

        waitForStart();

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {

            joystick = -gamepad1.left_stick_y * 0.1;


            //if(Math.abs(joystick-power)>epsilon)
             //   power += (joystick-power)/numSteps;

            mtr.setPower(joystick);


            telemetry.addData("Joystick", joystick);
            telemetry.addData("counts of motor",mtr.getCurrentPosition());

            if(gamepad1.b) {
                mtr.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
                mtr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
            }

            //telemetry.addData("power", power);

            //count++;

            //if(count % 5 == 0)
               // oldJoystick = joystick;

            //if(Math.abs(oldJoystick) < epsilon && Math.abs(joystick) < epsilon)
              //  power = 0;

            sleep(15);
            telemetry.update();
        }
    }
}
