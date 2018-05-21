package me.dglee.spiralclimb;

/**
 * Created by DavidLee on 2018-05-20.
 */

abstract class Item {
    static final int SPAWN_RATE = 0;
    float yPosRel;

    Item() {
        this.yPosRel = 0;
    }

    public void update() {

    }

    abstract void consume(Climber c);

    public boolean checkCollision(float cCenterX, float cCenterY,
                                  float iCenterX, float iCenterY, float radius) {
        float distanceBetweenTwoCircles = ((cCenterX - iCenterX) * (cCenterX - iCenterX)) +
                ((cCenterY - iCenterY) * (cCenterY - iCenterY));

        if (radius * radius > distanceBetweenTwoCircles)
        {
            return true;
        }
        return false;
    }
}
