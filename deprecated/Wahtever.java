package org.firstinspires.ftc.teamcode.deprecated;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.deprecated.called.HWRobot;

/**
 * Created by Kalie on 2/3/2018.
 */
@TeleOp(name="VUF Test", group="Idontcare")
//@Disabled
public class Wahtever extends LinearOpMode {
    // Declare OpMode memSers.
    HWRobot r = new HWRobot();
    String vuf = "";

    @Override
    public void runOpMode() {
        // Wait for the game to start (driver presses PLAY)
        r.getOpModeData(telemetry, hardwareMap);
        r.init("vuf");

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
            vuf = r.getVuMarkOld(true);
            telemetry.addData("vuf:", vuf);
            sleep(200);
        }
    }
}