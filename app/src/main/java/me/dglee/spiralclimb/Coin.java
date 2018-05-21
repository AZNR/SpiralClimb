package me.dglee.spiralclimb;

/**
 * Created by DavidLee on 2018-05-20.
 */

public class Coin extends Item {
    static final int COIN_VALUE = 100;
    static final float COIN_RADIUS_REL = 0.04f;

    Coin() {
        yPosRel = (float)Math.random() * (1 - COIN_RADIUS_REL);
    }


    public void consume(Climber c) {
        c.score += COIN_VALUE;
    }
}
