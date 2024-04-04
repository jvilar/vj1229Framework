package es.uji.vj1229.framework;

import android.graphics.Bitmap;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class AnimatedBMapTest {
    private static final int N_BITMAPS = 10;
    private static Bitmap[] bitmaps;

    @BeforeClass
    public static void setUp() {
        bitmaps = new Bitmap[N_BITMAPS];
        for (int i = 0 ; i < N_BITMAPS; i++){
            bitmaps[i] = Bitmap.createBitmap(1, 2, Bitmap.Config.ALPHA_8);
        }
    }

    private int bitmapIndex(Bitmap bitmap) {
        for (int i = 0; i < bitmaps.length; i++)
            if (bitmaps[i] == bitmap)
                return i;
        return -1;
    }

    @Test
    public void simpleLooping() {
        int FPS = 2;
        int totalTime = 2 * N_BITMAPS / FPS;
        float deltaTime = 1/(float)FPS;
        AnimatedBitmap animatedBitmap = new AnimatedBitmap(totalTime, bitmaps);

        int[] expected = new int [ (int)(totalTime * FPS)];
        int now = 0;
        for (int i = 0 ; i < expected.length ; i++) {
            expected[i] = now;
            if ( (i + 1) % FPS == 0 )
                now = (now + 1) % N_BITMAPS;
        }
        checkAnimation(deltaTime, animatedBitmap, expected);
    }

    @Test
    public void simpleNoLooping() {
        int FPS = 2;
        int totalTime = 2 * N_BITMAPS / FPS;
        float deltaTime = 1/(float)FPS;
        AnimatedBitmap animatedBitmap = new AnimatedBitmap(totalTime, bitmaps);

        int[] expected = new int [ (int)(totalTime * FPS)];
        int now = 0;
        for (int i = 0 ; i < expected.length ; i++) {
            expected[i] = now;
            if ( (i + 1) % FPS == 0 )
                now = Math.min(now + 1, N_BITMAPS - 1);
        }

        checkAnimation(deltaTime, animatedBitmap, expected);
    }



    private void checkAnimation(float deltaTime, AnimatedBitmap animatedBitmap, int[] expected) {
        for (int i = 0 ; i < expected.length ; i++) {
            int actual = bitmapIndex(animatedBitmap.getCurrentFrame());
            assertEquals(String.format("At iteration %d", i), expected[i], actual);
            animatedBitmap.onUpdate(deltaTime);
        }
    }

}
