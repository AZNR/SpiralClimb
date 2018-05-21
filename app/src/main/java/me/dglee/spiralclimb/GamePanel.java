package me.dglee.spiralclimb;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.MotionEventCompat;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewDebug;

/**
 * Created by DavidLee on 2018-05-12.
 */

public class GamePanel extends SurfaceView implements
        SurfaceHolder.Callback {
    private MainThread thread;
    private Climber mainClimber;
    private Spiral spiral;
    int x; // width
    int y; // height
    float cellLength;
    float floorPos;
    float ceilPos;
    float floorToCeil;
    float xTouch;
    float yTouch;
    static float MIN_FLING_DISTANCE;


    public GamePanel (Context context) {
        super(context);

        getHolder().addCallback(this);

        thread = new MainThread(getHolder(), this);
        mainClimber = new Climber();
        spiral = new Spiral();
        setFocusable(true);
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        this.x = width;
        this.y = height;
        this.cellLength = x / (spiral.NUM_OBSTACLE + 1);
        this.floorPos = y / (spiral.NUM_OBSTACLE + 1) * spiral.NUM_OBSTACLE;
        this.ceilPos = x / (spiral.NUM_OBSTACLE + 1) * (spiral.NUM_OBSTACLE - 4);
        this.floorToCeil = floorPos - ceilPos;
        this.MIN_FLING_DISTANCE = x / 12;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        thread = new MainThread(getHolder(), this);

        thread.setRunning(true);
        thread.start();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.setRunning(false);
                thread.join();
                retry = false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                xTouch = event.getX();
                yTouch = event.getY();
                return true;
            case MotionEvent.ACTION_UP:
                if (!spiral.isGameRunning) {
                    spiral.reset();
                    mainClimber = new Climber();
                }
                if (event.getX() - xTouch > MIN_FLING_DISTANCE) {
                    if (mainClimber.dash())
                        spiral.speedUp(mainClimber.dashTime);
                }
                else if (event.getY() - yTouch > MIN_FLING_DISTANCE)
                    mainClimber.down();
                else
                    mainClimber.jump();
            default:
                return super.onTouchEvent(event);
        }
    }

    public void update() {
        for (int i = 0; i < spiral.speed; i++) {
            if (spiral.isGameRunning) {
                spiral.update();
                mainClimber.update();
            }

            if (checkCollision()) {
                spiral.isGameRunning = false;
            }
        }
    }

    private boolean checkCollision() {
        Obstacle currObstacle;
        Item currItem;
        float shift = cellLength * (spiral.count % spiral.UPDATES_PER_DEL) /
                spiral.UPDATES_PER_DEL;
        for (int i = 1; i < spiral.obstacles.size() - 1; i++) {
            currObstacle = spiral.obstacles.get(i);
            switch (currObstacle.getClass().getName()) {
                case "me.dglee.spiralclimb.StaticSpike":
                case "me.dglee.spiralclimb.MovingSpike":
                    float spikeHeightRel = currObstacle instanceof StaticSpike ?
                            ((StaticSpike) currObstacle).SPIKE_HEIGHT_RELATIVE :
                            ((MovingSpike) currObstacle).SPIKE_HEIGHT_RELATIVE;
                    float spikeHeight = floorToCeil * spikeHeightRel;
                    float r1x = cellLength * (i + 1) - shift;
                    float r1y = ceilPos + floorToCeil * currObstacle.yPosRelative;
                    float r2x = cellLength * (i + 2) - shift;
                    float r2y = ceilPos + floorToCeil * currObstacle.yPosRelative + spikeHeight;
                    float cx = x / 2;
                    float cy = ceilPos + floorToCeil * mainClimber.yPosRel;
                    float radius = floorToCeil * mainClimber.radiusRel;

                    if (currObstacle.checkCollision(r1x, r1y, r2x, r2y, cx, cy, radius))
                        return true;
                default:
                    if (currObstacle.checkCollision())
                        return true;
            }

            currItem = spiral.items.get(i);
            switch(currItem.getClass().getName()) {
                case "me.dglee.spiralclimb.Coin":
                    if (currItem.checkCollision(x / 2, ceilPos + floorToCeil * mainClimber.yPosRel,
                            cellLength * (i + 1.5f) - shift,
                            ceilPos + floorToCeil * currItem.yPosRel, floorToCeil *
                                    (((Coin)currItem).COIN_RADIUS_REL + mainClimber.radiusRel))) {
                        currItem.consume(mainClimber);
                        spiral.items.remove(i);
                        spiral.items.add(i, new EmptyItem());
                    }

            }
        }
        return false;
    }

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);

        // draw Spiral + obstacles
        Paint p = new Paint();
        p.setColor(Color.GRAY);
        canvas.drawRect(cellLength, ceilPos, cellLength * spiral.NUM_OBSTACLE,
                floorPos, p);

        float shift = cellLength * (spiral.count % spiral.UPDATES_PER_DEL) /
                spiral.UPDATES_PER_DEL;
        Obstacle c;
        Item currItem;
        for (int i = 0; i < spiral.obstacles.size(); i++) {
            c = spiral.obstacles.get(i);
            switch (c.getClass().getName()) {
                case "me.dglee.spiralclimb.StaticSpike":
                case "me.dglee.spiralclimb.MovingSpike":
                    p.setColor(Color.BLUE);
                    float spikeHeightRel = (c instanceof StaticSpike) ?
                            ((StaticSpike)c).SPIKE_HEIGHT_RELATIVE :
                            ((MovingSpike)c).SPIKE_HEIGHT_RELATIVE;
                    float spikeHeight = floorToCeil * spikeHeightRel;
                    canvas.drawRect(cellLength * (i + 1) - shift,
                            ceilPos + floorToCeil * c.yPosRelative,
                            cellLength * (i + 2) - shift, ceilPos +
                                    floorToCeil * c.yPosRelative + spikeHeight, p);

            }

            currItem = spiral.items.get(i);
            switch (currItem.getClass().getName()) {
                case "me.dglee.spiralclimb.Coin":
                    p.setColor(Color.YELLOW);
                    canvas.drawCircle(cellLength * (i + 1.5f) - shift,
                            ceilPos + floorToCeil * currItem.yPosRel,
                            floorToCeil * ((Coin)currItem).COIN_RADIUS_REL, p);
            }
        }

        // draw climber
        p.setColor(Color.BLACK);
        canvas.drawCircle(x / 2, ceilPos + floorToCeil * mainClimber.yPosRel,
                floorToCeil * mainClimber.radiusRel, p);

        p.setColor(Color.WHITE);
        canvas.drawRect(0, 0, cellLength, y, p);
        canvas.drawRect(cellLength * spiral.NUM_OBSTACLE, 0, x, y, p);

        // draw score
        p.setColor(Color.BLACK);
        p.setTextSize(cellLength);
        p.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(Integer.toString(mainClimber.score), x / 2,
                cellLength * 4, p);

        // check if game is over
        if (!spiral.isGameRunning) {
            p.setTextSize(cellLength * 1.5f);
            p.setColor(Color.RED);
            canvas.drawText("GAME OVER", x / 2, y / 2, p);
        }
    }
}
