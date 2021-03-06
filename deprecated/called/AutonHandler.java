package org.firstinspires.ftc.teamcode.deprecated.called;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.RelicRecoveryVuMark;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;

import static java.lang.Thread.sleep;
import static org.firstinspires.ftc.teamcode.deprecated.called.RobotValues.ARM_JEWEL_DOWN;
import static org.firstinspires.ftc.teamcode.deprecated.called.RobotValues.ARM_JEWEL_UP;
import static org.firstinspires.ftc.teamcode.deprecated.called.RobotValues.COUNTS_PER_INCH;
import static org.firstinspires.ftc.teamcode.deprecated.called.RobotValues.COUNTS_TO_CRYPTO_FRONT;
import static org.firstinspires.ftc.teamcode.deprecated.called.RobotValues.COUNTS_TO_GET_TO_EDGE_OF_CRYPTO;
import static org.firstinspires.ftc.teamcode.deprecated.called.RobotValues.COUNTS_TO_PLACE_GLYPH;
import static org.firstinspires.ftc.teamcode.deprecated.called.RobotValues.COUNTS_TO_VUFORIA;
//import static org.firstinspires.ftc.teamcode.deprecated.called.RobotValues.COUNTS_TO_VUFORIA_FRONT;
import static org.firstinspires.ftc.teamcode.deprecated.called.RobotValues.COUNT_TO_CRYPTO;
import static org.firstinspires.ftc.teamcode.deprecated.called.RobotValues.DEGREES_TO_TURN_FOR_CRYPTO;
import static org.firstinspires.ftc.teamcode.deprecated.called.RobotValues.EXTRUDE_CLAW_POWER;
import static org.firstinspires.ftc.teamcode.deprecated.called.RobotValues.HITTER_JEWEL_MIDDLE;
import static org.firstinspires.ftc.teamcode.deprecated.called.RobotValues.HITTER_JEWEL_NORTH;
import static org.firstinspires.ftc.teamcode.deprecated.called.RobotValues.NEW_COUNTS_TO_CRYPTO_FRONT;
import static org.firstinspires.ftc.teamcode.deprecated.called.RobotValues.SPEED_TO_CRYPTO;
import static org.firstinspires.ftc.teamcode.deprecated.called.RobotValues.SPEED_TO_PLACE_GLYPH;
import static org.firstinspires.ftc.teamcode.deprecated.called.RobotValues.SPEED_TO_TURN;
import static org.firstinspires.ftc.teamcode.deprecated.called.RobotValues.SPEED_TO_VUFORIA;

/**
 * Created by gbhat on 11/9/2017.
 */

//TODO make ramp speed functions; reduces slippage
public class AutonHandler {
    HWRobot r = new HWRobot();

    boolean blueTeam = false;
    String vuf;
    String generalDirection;
    VuforiaLocalizer vuforia;
    VuforiaTrackables relicTrackables;
    VuforiaTrackable relicTemplate;


    public void autonInit(Telemetry atele, HardwareMap hwmap, LinearOpMode invoked) {
        r.getOpModeData(atele, hwmap, invoked);
        r.init("all");

        int cameraMonitorViewId = hwmap.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hwmap.appContext.getPackageName());

        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(
                cameraMonitorViewId
        );

        parameters.vuforiaLicenseKey = r.vuforiaKey;

        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;
        this.vuforia = ClassFactory.createVuforiaLocalizer(parameters);

        relicTrackables = this.vuforia.loadTrackablesFromAsset("RelicVuMark");
        relicTemplate = relicTrackables.get(0);
        relicTemplate.setName("relicVuMarkTemplate"); // can help in debugging; otherwise not necessary
    }

    public void auton(String team, String area, Telemetry telemetry, HardwareMap hwMap, boolean a) {
        //TODO error due to this? might need seperate initialization of getOpModeData
        //r.getOpModeData(telemetry, hwMap); r.init("all");

        if(team.toLowerCase().equals("red")) {
            blueTeam = false;
            generalDirection = "fwd";
        }
        else if(team.toLowerCase().equals("blue")) {
            blueTeam = true;
            generalDirection = "bk";
        }
        telemetry.addData("got to", "before back/front loop");
        telemetry.update();

        if(area.toLowerCase().equals("back")) {
            //teamString = (blueTeam) ? "blue" : "red";
            //jewel(team, a);
            //vuf = r.getVuMark(a);
            if(a) {
                relicTrackables.activate();
                RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
                for(int i = 1; i < 5; i++) {
                    vuMark = RelicRecoveryVuMark.from(relicTemplate);
                    rsleep(200);
                }
                if(vuMark == RelicRecoveryVuMark.UNKNOWN) {
                    vuf = "UNKNOWN";
                }
                else if(vuMark == RelicRecoveryVuMark.LEFT) {
                    vuf = "LEFT";
                }
                else if(vuMark == RelicRecoveryVuMark.CENTER) {
                    vuf = "CENTER";
                }
                else if(vuMark == RelicRecoveryVuMark.RIGHT) {
                    vuf = "RIGHT";
                }
            }

            r.translate(generalDirection, SPEED_TO_VUFORIA, COUNTS_TO_VUFORIA, a);
            rsleep(500);
            //rsleep(500);
            r.translate(generalDirection, SPEED_TO_CRYPTO, COUNT_TO_CRYPTO, a);
            rsleep(500);
            r.rotate("cw", SPEED_TO_TURN, DEGREES_TO_TURN_FOR_CRYPTO, a);
            rsleep(500);
            r.moveForCrypto(vuf, a);
            rsleep(500);
            r.translate("fwd", SPEED_TO_PLACE_GLYPH, COUNTS_TO_PLACE_GLYPH,a);
            rsleep(500);
            extrudeGlyphStart();
            rsleep(600);
            r.translate("back", 0.2, 4.0, a);
            extrudeGlyphStop();
        }
        else if (area.toLowerCase().equals("backtest")) {
            if(a) {
                r.getVuMark(a);
                r.translate(generalDirection, SPEED_TO_VUFORIA, COUNTS_TO_VUFORIA, a);
                rsleep(500);
                //rsleep(500);
                r.translate(generalDirection, SPEED_TO_CRYPTO, COUNT_TO_CRYPTO, a);
                rsleep(500);
                r.rotate("cw", SPEED_TO_TURN, DEGREES_TO_TURN_FOR_CRYPTO, a);
                rsleep(500);
                r.moveForCrypto(a);
                rsleep(500);
                r.translate("fwd", SPEED_TO_PLACE_GLYPH, COUNTS_TO_PLACE_GLYPH,a);
                rsleep(500);
                extrudeGlyphStart();
                rsleep(600);
                r.translate("back", 0.2, 4.0, a);
                extrudeGlyphStop();
            }
        }

        else if (area.toLowerCase().equals("eff-back")) {
            //more efficient test code; doesnt translate left or right to complete going to crypto
            rsleep(5000);
            getVuMarkDataNotFromHWROBOT();

            telemetry.addData("passed:", "vuforia");
            telemetry.update();

            rsleep(500);
            //jewel(team, a);
            //rsleep(500);
            r.translate(generalDirection,SPEED_TO_CRYPTO,COUNTS_TO_GET_TO_EDGE_OF_CRYPTO, a);
            rsleep(500);
            r.adjustPosForCrypto(vuf,generalDirection,team,false,a);
            rsleep(500);
            //r.rotate("cw", SPEED_TO_TURN, DEGREES_TO_TURN_FOR_CRYPTO, a);
            r.rotate("cw", SPEED_TO_TURN, 90, a);
            rsleep(500);
            r.translate("fwd", SPEED_TO_PLACE_GLYPH, COUNTS_TO_PLACE_GLYPH,a);
            rsleep(500);
            extrudeGlyphStart();
            rsleep(600);
            r.translate("back", 0.2, 4.0, a);
            extrudeGlyphStop();
        }

        else if (area.toLowerCase().equals("front")) {
            //jewel(team, a);
            //vuf = r.getVuMark(a);
            if(a) {
                relicTrackables.activate();
                RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
                for(int i = 1; i < 5; i++) {
                    vuMark = RelicRecoveryVuMark.from(relicTemplate);
                    try {
                        sleep(200);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                if(vuMark == RelicRecoveryVuMark.UNKNOWN) {
                    vuf = "UNKNOWN";
                }
                else if(vuMark == RelicRecoveryVuMark.LEFT) {
                    vuf = "LEFT";
                }
                else if(vuMark == RelicRecoveryVuMark.CENTER) {
                    vuf = "CENTER";
                }
                else if(vuMark == RelicRecoveryVuMark.RIGHT) {
                    vuf = "RIGHT";
                }
            }

            r.translate(generalDirection, 0.2, COUNTS_TO_VUFORIA, a);
            rsleep(500);
            //rsleep(500);
            r.translate(generalDirection, 0.2, COUNTS_TO_CRYPTO_FRONT, a);
            rsleep(500);
            r.rotate("ccw",0.2,90,a);
            rsleep(500);
            r.translate("fwd", 0.2, 12*COUNTS_PER_INCH, a);

            //TODO not sure if it'll move 90degrees more or not move
            if(blueTeam) {
                r.rotate("ccw",0.2,170,a);
                r.init("imu");
                rsleep(2000);
                r.rotate("ccw",0.2,10,a);
            }
            else {
                r.rotate("cw",0.2,0.001,a);
            }


            //if perhaps we use strafing, use this code (insert after counts to crypto front
            /*
            r.translate("left", 0.2, 12 * COUNTS_PER_INCH, a);
            if (blueTeam) {
                r.rotate("ccw", 0.2, 179, a);
            }*/


            //String quickdir = (blueTeam) ? "ccw" : "cw";

            rsleep(500);


            r.moveForCrypto(vuf, a);

            rsleep(700);

            r.translate("fwd", SPEED_TO_PLACE_GLYPH, COUNTS_TO_PLACE_GLYPH, a);
            rsleep(500);
            extrudeGlyphStart();
            rsleep(600);
            r.translate("back", 0.2, 4.0, a);
            extrudeGlyphStop();
        }
        else if (area.toLowerCase().equals("fronttest")) {
            if(a) {
                r.getVuMark(a);
                r.translate(generalDirection, 0.2, COUNTS_TO_VUFORIA, a);
                rsleep(500);
                //rsleep(500);
                r.translate(generalDirection, 0.2, COUNTS_TO_CRYPTO_FRONT, a);
                rsleep(500);
                r.rotate("ccw",0.2,90,a);
                rsleep(500);
                r.translate("fwd", 0.2, 12*COUNTS_PER_INCH, a);

                //TODO not sure if it'll move 90degrees more or not move
                if(blueTeam) {
                    r.rotate("ccw",0.2,170,a);
                    r.init("imu");
                    rsleep(2000);
                    r.rotate("ccw",0.2,10,a);
                }
                else {
                    r.rotate("cw",0.2,0.001,a);
                }
                rsleep(500);
                r.moveForCrypto(a);

                rsleep(700);

                r.translate("fwd", SPEED_TO_PLACE_GLYPH, COUNTS_TO_PLACE_GLYPH, a);
                rsleep(500);
                extrudeGlyphStart();
                rsleep(600);
                r.translate("back", 0.2, 3.0, a);
                extrudeGlyphStop();

            }
        }

        else if (area.toLowerCase().equals("eff-front")) {
            //efficient auton
            rsleep(5000);
            getVuMarkDataNotFromHWROBOT();

            telemetry.addData("passed:", "vuforia");
            telemetry.update();

            //rsleep(500);
            //jewel(team, a);
            rsleep(500);
            r.translate(generalDirection,0.2,NEW_COUNTS_TO_CRYPTO_FRONT, a);
            rsleep(500);
            r.rotate("ccw",0.2, 90, a);
            rsleep(500);
            r.adjustPosForCrypto(vuf,generalDirection,team,true,a);
            rsleep(500);
            if(blueTeam) {
                //r.init("vuf");
                r.rotate("ccw", 0.2, 90, a);
            }
            else if (!blueTeam) {
                //r.init("vuf");
                r.rotate("cw", 0.2, 90, a);
            }
            rsleep(500);
            r.translate("fwd", SPEED_TO_PLACE_GLYPH, COUNTS_TO_PLACE_GLYPH, a);
            rsleep(500);
            extrudeGlyphStart();
            rsleep(500);
            r.translate("back", 0.2, 4.0, a);
            rsleep(500);
            extrudeGlyphStop();
        }

        else if (area.toLowerCase().equals("jewel")) {
            jewel(team, a);
        }
        else if (area.toLowerCase().equals("vuf strafe")) {
            r.moveForCrypto("LEFT", a);
            r.moveForCrypto("RIGHT", a);
        }
    }

    private void extrudeGlyphStart() {
        r.mtrClawL.setPower(EXTRUDE_CLAW_POWER);
        r.mtrClawR.setPower(EXTRUDE_CLAW_POWER);
        rsleep(1000);
    }

    private void extrudeGlyphStop() {
        r.mtrClawL.setPower(0);
        r.mtrClawR.setPower(0);
    }

    private void rsleep(long milliseconds) {
        try {
            sleep(milliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void jewel(String teamColor, boolean active) {
        r.srvJewelArm.setPosition(ARM_JEWEL_DOWN/2);
        r.srvJewelHitter.setPosition(HITTER_JEWEL_MIDDLE);
        rsleep(250);
        r.srvJewelArm.setPosition(ARM_JEWEL_DOWN);
        rsleep(500);
        r.knockOffJewel(teamColor, active);
        rsleep(500);
        r.srvJewelHitter.setPosition(HITTER_JEWEL_MIDDLE);
        rsleep(500);
        r.srvJewelArm.setPosition(ARM_JEWEL_UP/2);
        rsleep(500);
        r.srvJewelHitter.setPosition(HITTER_JEWEL_NORTH);
        rsleep(250);
        r.srvJewelArm.setPosition(ARM_JEWEL_UP);
    }

    private void getVuMarkDataNotFromHWROBOT() {
        relicTrackables.activate();
        RelicRecoveryVuMark vuMark = RelicRecoveryVuMark.from(relicTemplate);
        for(int i = 1; i < 5; i++) {
            vuMark = RelicRecoveryVuMark.from(relicTemplate);
            rsleep(200);
        }
        if(vuMark == RelicRecoveryVuMark.UNKNOWN) {
            vuf = "UNKNOWN";
        }
        else if(vuMark == RelicRecoveryVuMark.LEFT) {
            vuf = "LEFT";
        }
        else if(vuMark == RelicRecoveryVuMark.CENTER) {
            vuf = "CENTER";
        }
        else if(vuMark == RelicRecoveryVuMark.RIGHT) {
            vuf = "RIGHT";
        }
    }

/*
    public void translate(String dir, double speed, double inches, boolean active){ // Directional translation
        r.decideDirection(dir);

        if(active) {
            r.mtrChangeMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

            r.getNewPositions(inches);

            r.setDirection();

            r.mtrSetTargetPos(posFL,posFR,posBL,posBR);

            r.mtrChangeMode(DcMotor.RunMode.RUN_TO_POSITION);

            r.mtrSetSpeed(speed);

            while(active && r.mtrFL.isBusy() && r.mtrFR.isBusy() && r.mtrBL.isBusy() && r.mtrBR.isBusy()) {
                posOutOfFinalTelemetry(countTargets);
            }

            mtrSetSpeed(0);

            mtrChangeMode(DcMotor.RunMode.RUN_USING_ENCODER);
        }
    }*/


























}
