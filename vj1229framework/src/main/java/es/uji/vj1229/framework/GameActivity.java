package es.uji.vj1229.framework;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

/**
 * <p>The abstract base class for the activity that will control the game.
 * It holds a single {@link GameView} and creates it with the
 * {@link IGameController} returned by {@link GameActivity#buildGameController()},
 * that must be overriden by the class extending GameActivity.</p>
 *
 * <p>This file is part of the framework adapted for VJ1229, Mobile Device Applications in
 * <a href = "https://www.uji.es">Universitat Jaume I</a> from the one in the book
 * "Beginning Android Games" of Mario Zechner and Robert Green</p>
 *
 * @author Juan Miguel Vilar Torres and Juan Carlos Amengual Argudo
 * @see <a href="https://www.apress.com/gp/book/9781430246770">Begining Android Games</a>
 */
public abstract class GameActivity extends Activity {
    GameView gameView;
    IGameController gameController;

    private final int orientation;

    /**
     *  Default constructor. Sets portrait orientation
     */
    public GameActivity() {
        this(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    /**
     * Constructor to control the orientation.
     *
     * @param orientation The desired orientation, the possible values come from ActivityInfo
     *
     */
    public GameActivity(int orientation) {
        this.orientation = orientation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        Window window = getWindow();

        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        setRequestedOrientation(orientation);

        gameController = buildGameController();
        gameView = new GameView(this, gameController);

        setContentView(gameView);
    }

    /**
     *
     * @return the {@link IGameController} that will be used in the {@link GameView}.
     */
    abstract protected IGameController buildGameController();

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
