package me.dglee.spiralclimb;


/**
 * Created by DavidLee on 2018-05-14.
 */

public class EmptyObstacle extends Obstacle {
    public void update() {

    }

    public boolean checkCollision(int yPosTop, int yPosBottom) {
        return false;
    }

}
