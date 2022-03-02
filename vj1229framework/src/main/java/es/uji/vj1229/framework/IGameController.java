package es.uji.vj1229.framework;

import android.graphics.Bitmap;

import java.util.List;

import es.uji.vj1229.framework.TouchHandler.TouchEvent;

/**
 * <p>The interface for the controller of the {@link GameView}</p>
 * <p>This interface defines the methods that must be implemented by
 * the controllers that are used to refresh the {@link GameView} of the game.</p>
 * <p>This file is part of the framework adapted for VJ1229, Mobile Device Applications in
 * <a href = "https://www.uji.es">Universitat Jaume I</a> from the one in the book
 * "Beginning Android Games" of Mario Zechner and Robert Green</p>
 *
 * @author Juan Miguel Vilar Torres and Juan Carlos Amengual Argudo
 * @see <a href="https://www.apress.com/gp/book/9781430246770">Begining Android Games</a>
 */
public interface IGameController {
    /**
     * The method called by the {@link GameView} to notify the desired dimensions
     * of the bitmaps returned by {@link IGameController#onDrawingRequested()}.
     *
     * @param width the desired width of the bitmaps
     * @param height the desired height of the bitmaps
     */
    void onBitmapMeasuresAvailable(int width, int height);

    /**
     * The method called by the {@link GameView} prior to the call to
     * {@link IGameController#onDrawingRequested}. It informs the controller of the time passed
     * since the last call and the touch events that have been detected.
     * @param deltaTime the time (in seconds) since the last call
     * @param touchEvents the event processed since the last call
     */
    void onUpdate(float deltaTime, List<TouchEvent> touchEvents);

    /**
     * The method called by the {@link GameView} to request the {@link Bitmap} to draw
     * in the screen
     * @return The desired {@link Bitmap}
     */
    Bitmap onDrawingRequested();
}
