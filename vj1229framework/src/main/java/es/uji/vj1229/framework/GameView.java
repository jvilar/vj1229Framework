package es.uji.vj1229.framework;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

/**
 * <p>The view for the {@link GameActivity}. It is an specialized {@link SurfaceView}.
 * </p>
 *
 * <p>It is in charge of calling
 * the {@link IEventProcessor#onUpdate} and the
 * {@link IBitmapProvider#onDrawingRequested()} methods of the
 * {@link IBitmapProvider} that receives in the constructor or is
 * set using {@link GameView#setBitmapProvider}
 * </p>
 * <p>This file is part of the framework adapted for VJ1229, Mobile Device Applications in
 * <a href = "https://www.uji.es">Universitat Jaume I</a> from the one in the book
 * "Beginning Android Games" of Mario Zechner and Robert Green</p>
 *
 * @author Juan Miguel Vilar Torres and Juan Carlos Amengual Argudo
 * @see <a href="https://www.apress.com/gp/book/9781430246770">Begining Android Games</a>
 */

public class GameView extends SurfaceView implements Runnable {
    public interface IBitmapProvider {
        /**
         * The method called by the {@link GameView} to notify the desired dimensions
         * of the bitmaps returned by {@link IBitmapProvider#onDrawingRequested()}.
         *
         * @param width the desired width of the bitmaps
         * @param height the desired height of the bitmaps
         */
        void onBitmapMeasuresAvailable(int width, int height);

        /**
         * The method called by the {@link GameView} to request the {@link Bitmap} to draw
         * in the screen
         * @return The desired {@link Bitmap}
         */
        Bitmap onDrawingRequested();
    }

    private IBitmapProvider bitmapProvider;
    private IEventProcessor eventProcessor;
    final SurfaceHolder holder;
    volatile boolean running;
    Thread renderThread;
    final TouchHandler touchHandler;

    public GameView(Context context) {
        this(context, null);
    }

    public GameView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, null, null);
    }

    /**
     * Constructor
     * @param context A context
     * @param bitmapProvider The object that provides the bitmaps
     * @param eventProcessor The object that will control the view
     */
    public GameView(Context context, IBitmapProvider bitmapProvider, IEventProcessor eventProcessor) {
        this(context, null, bitmapProvider, eventProcessor);
    }

    public GameView(Context context, AttributeSet attributeSet, IBitmapProvider bitmapProvider, IEventProcessor eventProcessor) {
        super(context, attributeSet);
        this.bitmapProvider = bitmapProvider;
        this.eventProcessor = eventProcessor;
        holder = getHolder();
        this.touchHandler = new TouchHandler(this);
        running = false;
    }


    /**
     * Sets the {@link IBitmapProvider}
     *
     * @param bitmapProvider the new {@link IBitmapProvider}
     */
    public void setBitmapProvider(IBitmapProvider bitmapProvider) {
        this.bitmapProvider = bitmapProvider;
    }

    /**
     * Sets the {@link IEventProcessor}
     *
     * @param eventProcessor the new  {@link IBitmapProvider}
     */
    public void setEventProcessor(IEventProcessor eventProcessor) {
        this.eventProcessor = eventProcessor;
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
     * refreshing the view as fast as possible by calling the {@link IEventProcessor} to process
     * the events and to get the bitmap to draw.
     *
     * If the bitmap returned by {@link IBitmapProvider#onDrawingRequested()} is null,
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

            eventProcessor.onUpdate(deltaTime, touchHandler.getTouchEvents());
            Bitmap frameBuffer = bitmapProvider.onDrawingRequested();
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
