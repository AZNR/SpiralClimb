package me.dglee.spiralclimb;

import android.graphics.Canvas;
import android.graphics.Paint;

import static java.lang.Math.PI;

/**
 * Created by DavidLee on 2018-05-12.
 */

public class Climber {
    float yPosRel; // current height position
    float yVelRel;
    float yAccelRel;
    int score; // current score
    int jumpState; // whether jump is available
    int dashTimer; // how long to freeze Climber
    boolean canDash; // whether dash is available
    float radiusRel; // radius of Climber
    public static final int dashTime = 15;

    public static final float JUMP_VEL_REL = -0.2f; // 1 unit covers floor to ceil

    Climber() {
        radiusRel = 0.04f;
        yPosRel = 1 - radiusRel;
        yVelRel = 0;
        yAccelRel = 0.05f; //
        score = 0;
        jumpState = 0;
        dashTimer = 0;
    }

    public void update() {
        if (dashTimer == 0) {
            yVelRel += yAccelRel;
            yPosRel += yVelRel;
            if (yPosRel > 1 - radiusRel) {  // once on ground, reset
                yPosRel = 1 - radiusRel;
                yVelRel = 0;
            }

            if (yPosRel == 1 - radiusRel)
                jumpState = 0;
        }
        else { // in dash state, do not change
            dashTimer--;
        }
        score++;
    }

    public void jump() {
        if (jumpState == 0) {
            yVelRel = JUMP_VEL_REL;
            jumpState = 1;
            canDash = true;
        } else if (jumpState == 1) {
            if (yVelRel < 0)
                yVelRel = 0;
            yVelRel = JUMP_VEL_REL;
            jumpState = 2;
            canDash = true;
        }
    }

    public void down() {
        yVelRel = -JUMP_VEL_REL;
        jumpState = 2;
        canDash = false;
    }

    public boolean dash() {
        if (canDash && yPosRel > 0) {
            dashTimer = dashTime;
            jumpState = 1;
            canDash = false;
            return true;
        }
        return false;
    }
}
