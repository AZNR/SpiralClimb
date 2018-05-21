package me.dglee.spiralclimb;

/**
 * Created by DavidLee on 2018-05-18.
 */

public class MovingSpike extends Obstacle {
    static final float SPIKE_HEIGHT_RELATIVE = 0.1f;
    float yVelRelative;

    MovingSpike() {
        yPosRelative = (float)Math.random();
        yVelRelative = Math.random() > 0.5 ? .02f : -.02f;
    }

    @Override
    public void update() {
        if (yPosRelative >= 1 - SPIKE_HEIGHT_RELATIVE) {
            yVelRelative = -.02f;
        } else if (yPosRelative <= 0) {
            yVelRelative = .02f;
        }

        // ensure range
        yPosRelative = Math.min(Math.max(yPosRelative + yVelRelative, 0),
                1 - SPIKE_HEIGHT_RELATIVE);
    }
}
