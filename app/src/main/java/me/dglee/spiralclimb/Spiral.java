package me.dglee.spiralclimb;

import java.util.ArrayList;

/**
 * Created by DavidLee on 2018-05-11.
 */

class Spiral {
    static final int BASE_SPEED = 1;
    static final int ANGLE = 35;
    static final int NUM_OBSTACLE = 10;
    static int UPDATES_PER_DEL = 5; // updates per refresh
    static int UPDATES_PER_ADD = 15;
    int speed; // speed at which game updates
    int count; // how many
    int speedUpDuration;
    boolean isGameRunning; // whether to update or not

    ArrayList<Obstacle> obstacles; // 5 obstacles active at a time
    ArrayList<Item> items;

    Spiral() {
        speed = BASE_SPEED;
        count = 0;
        obstacles = new ArrayList<Obstacle>();
        items = new ArrayList<Item>();
        for (int i = 0; i < NUM_OBSTACLE; i++) {
            obstacles.add(new EmptyObstacle());
            items.add(new EmptyItem());
        }
        isGameRunning = true;
        speedUpDuration = 0;
    }

    public void reset() {
        speed = BASE_SPEED;
        count = 0;
        obstacles.clear();
        items.clear();
        for (int i = 0 ; i < NUM_OBSTACLE; i++) {
            obstacles.add(new EmptyObstacle());
            items.add(new EmptyItem());
        }
        isGameRunning = true;
        speedUpDuration = 0;
    }

    private Obstacle generateObstacle() {
        double rand = Math.random();
        if (rand < .3)
            return new StaticSpike();
        else if (rand < .6)
            return new MovingSpike();
        else
            return new EmptyObstacle();
    }

    private Item generateItem() {
        double rand = Math.random();
        if (rand < .3)
            return new Coin();
        else
            return new EmptyItem();
    }

    public void update() { // 60 times per second
        count += BASE_SPEED;
        if (speedUpDuration == 0) {
            speed = BASE_SPEED;
        }
        if (count % UPDATES_PER_DEL == 0) {
            obstacles.remove(0);
            items.remove(0);
            if (count % UPDATES_PER_ADD == 0) {
                obstacles.add(generateObstacle());
                items.add(new EmptyItem());
            } else {
                obstacles.add(new EmptyObstacle());
                items.add(generateItem());
            }
        }

        for (Obstacle o : obstacles) {
            o.update();
        }
        speedUpDuration--;
    }

    public void speedUp(int duration) {
        speedUpDuration = duration;
        speed *= 2;
    }
}
