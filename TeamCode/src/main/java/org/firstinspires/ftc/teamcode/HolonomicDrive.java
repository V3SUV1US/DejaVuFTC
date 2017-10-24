package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;

/**
 * Created by gbhat on 8/20/2017.
 */

@TeleOp(name="Holonomic Drive", group="DriveOPs")
//@Disabled
public class HolonomicDrive extends LinearOpMode{
    HWRobot robot = new HWRobot();
    private double powFL = 0;
    private double powFR = 0;
    private double powBL = 0;
    private double powBR = 0;
    private double ch1,ch2,ch3,ch4;
    private double g2ch1, g2ch2, g2ch3, g2ch4;
    double spdLinearUp = 0.4, spdLinearDown = -spdLinearUp;
    double clawMid = 0.5;
    double clawIncrement = 0.02;
    double clawPos;
    private boolean runSlow = false;
    private double slowLimit = 0.5;
    private double modifierValue;
    private double modifierValueDefault = 1;

    @Override
    public void runOpMode() {
        /*//robot.init(hardwareMap);
        mtrFL = hardwareMap.dcMotor.get("fl_drive");
        mtrFR = hardwareMap.dcMotor.get("fr_drive");
        mtrBL = hardwareMap.dcMotor.get("bl_drive");
        mtrBR = hardwareMap.dcMotor.get("br_drive");

        mtrFL.setDirection(DcMotor.Direction.REVERSE);
        mtrFR.setDirection(DcMotor.Direction.FORWARD);
        mtrBL.setDirection(DcMotor.Direction.REVERSE);
        mtrBR.setDirection(DcMotor.Direction.FORWARD);

        mtrFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mtrFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mtrBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        mtrBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
*/

        robot.init(hardwareMap, DcMotor.RunMode.RUN_WITHOUT_ENCODER, telemetry);
        prompt(telemetry, "Init", "HW initialized");
        waitForStart();

        while(opModeIsActive()) {
            ch1 = gamepad1.left_stick_x;
            ch2 = -gamepad1.left_stick_y;
            ch3 = gamepad1.right_stick_x;
            ch4 = -gamepad1.left_stick_y;

            g2ch1 = gamepad2.left_stick_x;
            g2ch2 = -gamepad1.left_stick_y;
            g2ch3 = gamepad1.right_stick_x;
            g2ch4 = -gamepad1.left_stick_y;

            powFL = ch2 + ch1 + ch3;
            powFR = ch2 - ch1 - ch3;
            powBL = ch2 - ch1 + ch3;
            powBR = ch2 + ch1 - ch3;

            if(gamepad1.left_trigger > 0.1) {
                runSlow = !runSlow;
            }

            if(runSlow) {
                runSlow = !runSlow;
                if(modifierValue == modifierValueDefault) {
                    modifierValue = slowLimit;
                }
                else if(modifierValue == slowLimit) {
                    modifierValue = modifierValueDefault;
                }
            }

            powFL *= modifierValue;
            powFR *= modifierValue;
            powBL *= modifierValue;
            powBR *= modifierValue;

            telemetry.addData("Speed Mod: ", modifierValue);


            if(gamepad2.a) {
                //robot.mtrLinear.setPower(spdLinearDown);
            }
            else if (gamepad2.y){
                //robot.mtrLinear.setPower(spdLinearUp);
            }
            else {
                //robot.mtrLinear.setPower(0.0);
            }

            if(gamepad2.b) {
                clawPos += clawIncrement;
            }
            else if (gamepad2.x) {
                clawPos -= clawIncrement;
            }

            clawPos = Range.clip(clawPos, -clawMid, clawMid);
            robot.srvL.setPosition(clawMid + clawPos);
            robot.srvR.setPosition(clawMid - clawPos);


            telemetry.update();
            robot.mtrFL.setPower(powFL);
            robot.mtrFR.setPower(powFR);
            robot.mtrBL.setPower(powBL);
            robot.mtrBR.setPower(powBR);










        }
    }

    private void prompt(Telemetry telemetry, String prefix, String cmd) {
        telemetry.addData(prefix, cmd);
        telemetry.update();
    }
}

