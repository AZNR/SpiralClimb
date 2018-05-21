package me.dglee.spiralclimb;


/**
 * Created by DavidLee on 2018-05-13.
 */

public class StaticSpike extends Obstacle {
    boolean onTop; // true: on ceiling, false: on ground
    static final float SPIKE_HEIGHT_RELATIVE = 0.1f; // out of 100

    StaticSpike(boolean onTop) {
        this.onTop = onTop;
    }

    StaticSpike() {
        onTop = Math.random() < .5 ? true : false;
        yPosRelative = onTop ? 0 : 1 - SPIKE_HEIGHT_RELATIVE;
    }

    public void update() {

    }
}
