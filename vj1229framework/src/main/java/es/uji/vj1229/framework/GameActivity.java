package es.uji.vj1229.framework;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * <p>The abstract base class for the activity that will control the game.
 * The class that inherits from it must create a {@link GameView}. This can
 * be done as part of a layout or as the single {@link android.view.View} of the activity.
 * To ease the creation, two auxiliary methods are provided: {@link #portraitFullScreenOnCreate()}
 * and {@link #landscapeFullScreenOnCreate()}. If they are used, that's enough.
 * If the {@GameView} is created otherwise, it is necessary to override {@link #getGameView()}.
 *
 * <p>This file is part of the framework adapted for VJ1229, Mobile Device Applications in
 * <a href = "https://www.uji.es">Universitat Jaume I</a> from the one in the book
 * "Beginning Android Games" of Mario Zechner and Robert Green</p>
 *
 * @author Juan Miguel Vilar Torres and Juan Carlos Amengual Argudo
 * @see <a href="https://www.apress.com/gp/book/9781430246770">Begining Android Games</a>
 */
public abstract class GameActivity extends Activity implements GameView.IBitmapProvider {
    private GameView gameView = null;

    /**
     * <p>If this method is called inside {@link #onCreate(Bundle)}, it creates a full screen
     * {@link GameView} in portrait orientation and designates this class as
     * the {@link es.uji.vj1229.framework.GameView.IBitmapProvider}. It also activates the
     * flag to keep the screen on.</p>
     */
    protected void portraitFullScreenOnCreate() {
        fullScreenOnCreate(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * <p>If this method is called inside {@link #onCreate(Bundle)}, it creates a full screen
     * {@link GameView} in landscape orientation and designates this class as
     * the {@link es.uji.vj1229.framework.GameView.IBitmapProvider}. It also activates the
     * flag to keep the screen on.</p>
     */
    protected void landscapeFullScreenOnCreate() {
        fullScreenOnCreate(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
    }

    /**
     * Base method for the implementation of {@link #portraitFullScreenOnCreate()} and
     * {@link #landscapeFullScreenOnCreate()}
     *
     * @param orientation the desired orientation from {@link ActivityInfo}
     */
    protected void fullScreenOnCreate(int orientation) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(orientation);

        gameView = new GameView(this, this, buildGameController());
        gameView.addOnLayoutChangeListener((view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> onBitmapMeasuresAvailable(right - left, bottom - top));
        setContentView(gameView);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (gameView == null) {
            gameView = getGameView();
            if (gameView == null)
                throw new IllegalStateException("The method getGameView returns null and none of the optional OnCreate methods has been used. Have you overridden getGameView?");
            gameView.setGameController(buildGameController());
            gameView.setBitmapProvider(this);
            gameView.addOnLayoutChangeListener((view, left, top, right, bottom, oldLeft, oldTop, oldRight, oldBottom) -> onBitmapMeasuresAvailable(right - left, bottom - top));
        }
    }

    /**
     *
     * @return the {@link IGameController} that will be used in the {@link GameView}.
     */
    abstract protected IGameController buildGameController();

    /**
     *
     * @return the {@link GameView} that was created by the activity
     */
    protected GameView getGameView() {
        return gameView;
    }

    /**
     * Transmit to the {@link GameView} the onResume event.
     */
    @Override
    protected void onResume() {
        super.onResume();
        gameView.onResume();
    }

    /**
     * Transmit to the {@link GameView} the onPause event.
     */
    @Override
    protected void onPause() {
        super.onPause();
        gameView.onPause();
    }
}
