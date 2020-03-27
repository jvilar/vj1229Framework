package es.uji.vj1229.framework;

import android.graphics.Bitmap;

/**
 * This class stores the frames corresponding to an animated bitmap.
 * Each frame is a {@link Bitmap}. The animation keeps looping around
 * the frames with a period indicated in the constructor.
 *
 * <p>This file is part of the framework adapted for VJ1229, Mobile Device Applications in
 * <a href = "https://www.uji.es">Universitat Jaume I</a> from the one in the book
 * "Beginning Android Games" of Mario Zechner and Robert Green</p>
 *
 * @author Juan Miguel Vilar Torres and Juan Carlos Amengual Argudo
 * @see <a href="https://www.apress.com/gp/book/9781430246770">Begining Android Games</a>
 */
public class AnimatedBitmap {
    private Bitmap[] frames;
    private float frameDuration;
    private float currentTime;
    private int currentIndex;

    /**
     * Constructor for the AnimatedBitmap
     *
     * @param totalTime the total time (in seconds) needed for the whole animation.
     * @param frames the frames.
     */
    public AnimatedBitmap(float totalTime, Bitmap ... frames) {
        this.frames = frames;
        frameDuration = totalTime / frames.length;
    }

    /**
     * Update the state of the animation so that {@link AnimatedBitmap#getCurrentFrame()}
     * returns the correct frame.
     *
     * @param deltaTime time elapsed (in seconds) since the last call.
     */
    public void update(float deltaTime) {
        currentTime += deltaTime;
        int n = (int)(currentTime / frameDuration);
        currentTime -= n * frameDuration;
        currentIndex = (currentIndex + n) % frames.length;
    }

    /**
     * @return The current frame according to the time elapsed.
     */
    public Bitmap getCurrentFrame() {
        return frames[currentIndex];
    }

    /**
     * Call {@link Bitmap#recycle()} on each of the frames.
     */
    public void recycle() {
        for (Bitmap frame: frames)
            frame.recycle();
    }
}
