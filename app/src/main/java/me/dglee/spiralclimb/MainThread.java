package me.dglee.spiralclimb;

import android.graphics.Canvas;
import android.view.SurfaceHolder;

/**
 * Created by DavidLee on 2018-05-12.
 */

public class MainThread extends Thread {
    public static final int MAX_FPS = 30;
    public double averageFPS;
    private SurfaceHolder surface;
    private GamePanel game;
    private boolean isRunning;
    public static Canvas canvas;

    public MainThread(SurfaceHolder surfaceHolder, GamePanel game) {
        super();
        this.surface = surfaceHolder;
        this.game = game;
    }

    public void setRunning(boolean running) {
        isRunning = running;
    }

    public void run() {
        long startTime;
        long timeMills = 1000/MAX_FPS;
        long waitTime;
        int frameCount = 0;
        long totalTime = 0;
        long targetTime = 1000/MAX_FPS;

        while (isRunning) {
            startTime = System.nanoTime();
            canvas = null;

            try {
                canvas = this.surface.lockCanvas();
                synchronized (surface) {
                    this.game.update();
                    this.game.draw(canvas);
                }
            }

            catch (Exception e) {
                e.printStackTrace();
            }

            finally {
                if (canvas != null) {
                    try {
                        surface.unlockCanvasAndPost(canvas);
                    }

                    catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            timeMills = (System.nanoTime() - startTime)/1000000;
            waitTime = targetTime - timeMills;
            try {
                if (waitTime > 0)
                    this.sleep(waitTime);
            }

            catch (Exception e) {
                e.printStackTrace();
            }

            totalTime += System.nanoTime() - startTime;
            frameCount++;
            if (frameCount == MAX_FPS) {
                averageFPS = 1000/((totalTime/frameCount)/1000000);
                frameCount = 0;
                totalTime = 0;
            }
            System.out.println(averageFPS);
        }
    }
}
