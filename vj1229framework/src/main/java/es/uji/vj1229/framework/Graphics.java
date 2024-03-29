package es.uji.vj1229.framework;

import static android.graphics.Bitmap.Config.ARGB_8888;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

/**
 * <p>A thin wrapper around a {@link Bitmap} to ease drawing with a {@link Canvas}
 * on it.</p>
 *
 * <p>The class keeps a {@link Bitmap}, a {@link Canvas}, and a {@link Paint} and
 * offers a simplified interface for drawing on the {@link Bitmap}.</p>
 *
 * <p>This file is part of the framework adapted for VJ1229, Mobile Device Applications in
 * <a href = "https://www.uji.es">Universitat Jaume I</a> from the one in the book
 * "Beginning Android Games" of Mario Zechner and Robert Green</p>
 *
 * @author Juan Miguel Vilar Torres and Juan Carlos Amengual Argudo
 * @see <a href="https://www.apress.com/gp/book/9781430246770">Begining Android Games</a>
 */
public class Graphics {
    private final Bitmap frameBuffer;
    private final Canvas canvas;
    private final Paint paint;
    private int textColor;

    /**
     * The constructor.
     *
     * @param width width of the bitmap to be created.
     * @param height height of the bitmap to be created.
     */
    public Graphics(int width, int height) {
        this.frameBuffer = Bitmap.createBitmap(width, height, ARGB_8888);
        canvas = new Canvas(frameBuffer);
        paint = new Paint();
        setTextAlign(Paint.Align.LEFT);
        setTextSize(20);
        setTypeface(Typeface.DEFAULT_BOLD);
        setTextColor(0xffe0e0e0);
    }

    /**
     *
     * @return The {@link Bitmap} in its current state
     */
    public Bitmap getFrameBuffer() {
        return frameBuffer;
    }

    /**
     * Clear the {@link Bitmap} to a uniform color.
     *
     * @param color the color un RGB format (24 bits).
     */
    public void clear(int color) {
        canvas.drawRGB((color & 0xff0000) >> 16, (color & 0xff00) >> 8, color & 0xff);
    }

    /**
     * Draw a line from {@code (x1, y1)} to {@code (x2, y2)} with the given width and color
     * @param x1 the x coordinate of the first point of the line
     * @param y1 the y coordinate of the first point of the line
     * @param x2 the x coordinate of the second point of the line
     * @param y2 the y coordinate of the second point of the line
     * @param width the width of the line
     * @param color the color in ARGB format (32 bits)
     */
    public void drawLine(float x1, float y1, float x2, float y2, float width, int color) {
        paint.setColor(color);
        paint.setStrokeWidth(width);
        canvas.drawLine(x1, y1, x2, y2, paint);
    }

    /**
     * Draw a rectangle with its upper left corner in {@code (x, y)}, the given width, height and color
     *
     * @param x the x coordinate of the upper left corner
     * @param y the y coordinate of the upper left corner
     * @param width the width of the rectangle
     * @param height the height of the rectangle
     * @param color the color in ARGB format (32 bits)
     */
    public void drawRect(float x, float y, float width, float height, int color) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(x, y, x + width - 1, y + height - 1, paint);
    }

    /**
     *
     * @return The width of the underlying {@link Bitmap}
     */
    public int getWidth() {
        return frameBuffer.getWidth();
    }

    /**
     *
     * @return The height of the underlying {@link Bitmap}
     */
    public int getHeight() {
        return frameBuffer.getHeight();
    }

    /**
     *
     * @return the {@link Paint} object used in drawing. Note that it is changed
     * every time a drawing function that receives an attribute (like color) is
     * called. This function is intended to be used to set those attributes that
     * are not covered by the parameters.
     */
    public Paint getPaint() { return paint; }

    /**
     * Draw a {@link Bitmap} over the underlying {@link Bitmap}
     * @param bitmap the {@link Bitmap} to draw
     * @param x the x coordinate of the upper left corner of {@code bitmap}
     * @param y the y coordinate of the upper left corner of {@code bitmap}
     */
    public void drawBitmap(Bitmap bitmap, float x, float y) {
        canvas.drawBitmap(bitmap, x, y, null);
    }

    private static final Rect src = new Rect(), dst = new Rect();

    /**
     * Draw a {@link Bitmap} over the underlying {@link Bitmap} clipping in the
     * horizontal axis.
     * @param bitmap the {@link Bitmap} to draw
     * @param x the x coordinate of the upper left corner to draw {@code bitmap}, unless it is
     *          smaller than {@code minX}
     * @param y the y coordinate of the upper left corner to draw {@code bitmap}
     * @param minX the minimum x coordinate of the image in the underlying bitmap
     * @param maxX the maximum x coordinate of the image in the underlying bitmap
     */
    public void drawBitmap(Bitmap bitmap, float x, float y, float minX, float maxX) {
        int leftDst = (int)Math.max(minX, x),
                rightDst = (int) Math.min(maxX, x + bitmap.getWidth() - 1),
                leftSrc = x >= minX ? 0 : (int) (minX - x),
                rightSrc = leftSrc + rightDst - leftDst
        ;
        src.set( leftSrc,0, rightSrc, bitmap.getHeight());
        dst.set(leftDst, (int)y, rightDst, (int)y + bitmap.getHeight());
        canvas.drawBitmap(bitmap, src, dst, null);
    }

    /**
     * Draw a {@link Bitmap} or a portion of it over the underlying {@link Bitmap}
     * @param bitmap the {@link Bitmap} to draw
     * @param src the portion of {@code src} to draw. If {@code null}, the whole bitmap
     *            is drawn
     * @param dst the rectangle in which the {@code src} will be scaled/translated to
     *            fit, it cannot be null
     */
    public void drawBitmap(Bitmap bitmap, Rect src, Rect dst) {
        canvas.drawBitmap(bitmap, src, dst, null);
    }

    /**
     * Draw a rectangular portion of a {@link Bitmap} over the underlying {@link Bitmap}.
     * @param bitmap the {@link Bitmap} containing the rectangle that will be drawn
     * @param x the x coordinate of the position in which {@code bitmap} will be drawn
     * @param y the x coordinate of the position in which {@code bitmap} will be drawn
     * @param xb the x coordinate of the rectangle from {@code bitmap} which will be drawn
     * @param yb the y coordinate of the rectangle from {@code bitmap} which will be drawn
     * @param width the width of the rectangle from {@code bitmap} which will be drawn
     * @param height the height of the rectangle from {@code bitmap} which will be drawn
     */
    public void drawBitmap(Bitmap bitmap, float x, float y, float xb, float yb, float width, float height) {
        int xi = (int) x;
        int yi = (int) y;
        int xbi = (int) xb;
        int ybi = (int) yb;

        int w = Math.min(frameBuffer.getWidth() - xi, (int)width);
        int h = Math.min(frameBuffer.getHeight() - yi, (int)height);
        src.set(xbi, ybi, xbi + w, ybi + h);
        dst.set(xi, yi, xi + w, yi + h);
        canvas.drawBitmap(bitmap, src, dst, null);
    }


    /**
     * Draw a {@link String} in the given coordinates. Note that the
     * color fixed using is set in the {@link Paint} every time this
     * function is called.
     * @param x the x coordinate of the origin of the text
     * @param y the y coordinate of the origin of the text
     * @param s the text
     */
    public void drawText(float x, float y, String s) {
        paint.setColor(textColor);
        canvas.drawText(s, x, y, paint);
    }

    /**
     * Sets the color used when drawing texts
     * @param textColor the new color
     */
    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    /**
     * Sets the alignment of the text
     * @param align the new alignment
     */
    public void setTextAlign(Paint.Align align) {
        paint.setTextAlign(align);
    }

    /**
     * Sets the size of the text
     * @param size the new size
     */
    public void setTextSize(int size) {
        paint.setTextSize(size);
    }

    /**
     * Sets the type face for the text
     * @param typeface the new typeface
     */
    public void setTypeface(Typeface typeface) {
        paint.setTypeface(typeface);
    }


    /**
     * Draw a circle with the given center, radius, and color
     * @param x the x coordinate of the center
     * @param y the y coordinate of the center
     * @param r the radius
     * @param color the color in ARGB format (32 bits)
     */
    public void drawCircle(float x, float y, float r, int color) {
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawCircle(x, y, r, paint);
    }

    /**
     * Draw a {@link Drawable} in the given position with the given dimensions
     * @param drawable the {@link Drawable} to draw
     * @param x the x coordinate of the upper left corner of {@code drawable}
     * @param y the y coordinate of the upper left corner of {@code drawable}
     * @param width the width of {@code drawable}
     * @param height the height of {@code drawable}
     */
    public void drawDrawable(Drawable drawable, float x, float y, float width, float height) {
        drawable.setBounds((int)x, (int)y, (int)(x + width), (int)(y+height));
        drawable.draw(canvas);
    }

    /**
     * Recycle the frameBuffer. The object can not be used after this.
     */
    public void recycle() {
        frameBuffer.recycle();
    }
}
