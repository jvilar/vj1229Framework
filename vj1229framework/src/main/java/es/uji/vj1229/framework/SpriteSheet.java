package es.uji.vj1229.framework;

import android.graphics.Bitmap;
import android.graphics.Matrix;

/**
 * <p>Auxiliary class for managing sprite sheets.</p>
 * <p>The sprite sheet is a {@link Bitmap} that is assumed to have several rows and columns
 * of smaller images (sprites) contained in cells of a fixed width and height. Convenience
 * methods are provided to
 * recover either individual images or entire rows.</p>
 * <p>It is assumed that the first cell of the first row is in coordinates {@code (0,0)} of
 * the {@link Bitmap}. The different rows may have different number of columns.</p>
 * <p>This file is part of the framework adapted for VJ1229, Mobile Device Applications in
 * <a href = "https://www.uji.es">Universitat Jaume I</a> from the one in the book
 * "Beginning Android Games" of Mario Zechner and Robert Green</p>
 *
 * @author Juan Miguel Vilar Torres and Juan Carlos Amengual Argudo
 * @see <a href="https://www.apress.com/gp/book/9781430246770">Begining Android Games</a>
 */
public class SpriteSheet {
    private final Bitmap sheet;
    private final int cellHeight;
    private final int cellWidth;
    private final int rows;
    private final int columns;

    /**
     * Constructor.
     * @param sheet the {@link Bitmap} containing the sprites.
     * @param cellHeight the height of the cell of an individual sprite.
     * @param cellWidth the width of the cell of an individual sprite.
     */
    public SpriteSheet(Bitmap sheet, int cellHeight, int cellWidth) {
        this.sheet = sheet;
        this.cellHeight = cellHeight;
        this.cellWidth = cellWidth;

        rows = sheet.getHeight() / cellHeight;
        columns = sheet.getWidth() / cellWidth;
    }

    /**
     * Return the given number of columns from a row, scaled to the given width
     * and height.
     * @param row the row to get the sprites.
     * @param nColumns the number of columns to return.
     * @param width the desired width.
     * @param height the desired height.
     * @return An array containing the {@link Bitmap} of the sprites. These bitmaps are created
     * and they have to be recycled if needed.
     */
    public Bitmap[] getScaledRow(int row, int nColumns, int width, int height) {
        Bitmap[] bitmaps = new Bitmap[nColumns];
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.postScale((float)width/cellWidth, (float)height/cellHeight);

        for (int col = 0 ; col < nColumns ; col++) {
            Bitmap bitmap = Bitmap.createBitmap(sheet, col * cellWidth,
                    row * cellHeight, cellWidth, cellHeight,
                    scaleMatrix, true);
            bitmaps[col] = bitmap;
        }
        return bitmaps;
    }

    /**
     * Return a sprite from a given row and column.
     * @param row the row.
     * @param col the column.
     * @return A newly created {@link Bitmap} containing the desired sprite.
     */
    public Bitmap getSprite(int row, int col) {
        return Bitmap.createBitmap(sheet, col * cellWidth,
                row * cellHeight, cellWidth, cellHeight);
    }


    /**
     * Return a sprite from a given row and column scaled to the desired dimensions.
     * @param row the row.
     * @param col the column.
     * @param width the new width.
     * @param height the new height.
     * @return A newly created {@link Bitmap} containing the desired sprite.
     */

    public Bitmap getScaledSprite(int row, int col, int width, int height) {
        Matrix scaleMatrix = new Matrix();
        scaleMatrix.postScale((float)width/cellWidth, (float)height/cellHeight);

        return Bitmap.createBitmap(sheet, col * cellWidth,
                row * cellHeight, cellWidth, cellHeight,
                scaleMatrix, true);
    }
}
