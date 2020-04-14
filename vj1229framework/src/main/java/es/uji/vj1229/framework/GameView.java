package es.uji.vj1229.framework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * <p>The view for the {@link GameActivity}. It is an specialized {@link SurfaceView}.
 * </p>
 *
 * <p>It is in charge of calling
 * the {@link IGameController#onUpdate} and the
 * {@link IGameController#onDrawingRequested()} methods of the
 * {@link IGameController} that receives in the constructor.
 * </p>
 * <p>This file is part of the framework adapted for VJ1229, Mobile Device Applications in
 * <a href = "https://www.uji.es">Universitat Jaume I</a> from the one in the book
 * "Beginning Android Games" of Mario Zechner and Robert Green</p>
 *
 * @author Juan Miguel Vilar Torres and Juan Carlos Amengual Argudo
 * @see <a href="https://www.apress.com/gp/book/9781430246770">Begining Android Games</a>
 */

public class GameView extends SurfaceView implements Runnable {
    IGameController gameController;
    SurfaceHolder holder;
    volatile boolean running;
    Thread renderThread;
    TouchHandler touchHandler;

    /**
     * Constructor
     * @param context A context
     * @param gameController The object that will control the view
     */
    public GameView(Context context, IGameController gameController) {
        super(context);
        this.gameController = gameController;
        holder = getHolder();
        this.touchHandler = new TouchHandler(this);
        running = false;
    }

    /**
     * Treatment of the onResume life cycle event
     */
    public void onResume() {
        running = true;
        renderThread = new Thread(this);
        renderThread.start();
    }

    /**
     * Treatment of the onPause life cycle event
     */
    public void onPause() {
        running = false;
        while (true) {
            try {
                renderThread.join();
                return;
            } catch (InterruptedException ignored) {}
        }
    }

    /**
     * Implementation of the {@link Runnable#run} method. The run process keeps
     * refreshing the view as fast as possible by calling the {@link IGameController} to process
     * the events and to get the bitmap to draw.
     *
     * If the bitmap returned by {@link IGameController#onDrawingRequested()} is null,
     * there is no redraw of the screen. This can reduce battery consumption.
     */
    @Override
    public void run() {
        Rect dstRect = new Rect();
        long startTime = System.nanoTime();

        while (running) {
            if (!holder.getSurface().isValid())
                continue;

            long now = System.nanoTime();
            float deltaTime = (now - startTime) / 1000_000_000f;
            startTime = now;

            gameController.onUpdate(deltaTime, touchHandler.getTouchEvents());
            Bitmap frameBuffer = gameController.onDrawingRequested();
            if (frameBuffer == null) { // No need to update, sleep 10 milliseconds
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ignored) { }
            } else {
                Canvas canvas = holder.lockCanvas();
                canvas.getClipBounds(dstRect);
                canvas.drawBitmap(frameBuffer, null, dstRect, null);
                holder.unlockCanvasAndPost(canvas);
            }
        }
    }
}
