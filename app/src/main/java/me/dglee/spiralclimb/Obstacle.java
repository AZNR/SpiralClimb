package me.dglee.spiralclimb;

/**
 * Created by DavidLee on 2018-05-11.
 */

abstract class Obstacle {
    int spawnRate; // out of 100
    float yPosRelative; // 0: ceil, 1: floor

    abstract public void update();

    public boolean checkCollision() {
        return false;
    }

    public boolean checkCollision(float rect1x, float rect1y,  float rect2x, float rect2y,
                                  float cCenterx, float cCentery, float r) {
        float DeltaX = cCenterx - Math.max(rect1x, Math.min(cCenterx, rect2x));
        float DeltaY = cCentery - Math.max(rect1y, Math.min(cCentery, rect2y));
        return (DeltaX * DeltaX + DeltaY * DeltaY) < (r * r);
    }
}
