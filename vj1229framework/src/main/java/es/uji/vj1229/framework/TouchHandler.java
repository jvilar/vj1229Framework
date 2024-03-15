package es.uji.vj1229.framework;

import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * <p>A {@link View.OnTouchListener} for processing touch events in the
 * {@link GameView}.</p>
 * <p>Its main use is to process the touch events in the {@link GameView} so that they are
 * passed on to the corresponding {@link IEventProcessor}.
 * </p>
 * <p>This file is part of the framework adapted for VJ1229, Mobile Device Applications in
 * <a href = "https://www.uji.es">Universitat Jaume I</a> from the one in the book
 * "Beginning Android Games" of Mario Zechner and Robert Green</p>
 *
 * @author Juan Miguel Vilar Torres and Juan Carlos Amengual Argudo
 * @see <a href="https://www.apress.com/gp/book/9781430246770">Begining Android Games</a>
 */
public class TouchHandler implements View.OnTouchListener {
    /**
     * The types of touch that can be detected.
     */
    public enum TouchType {
        /**
         * A finger is down
         */
        TOUCH_DOWN,
        /**
         * A finger is up
         */
        TOUCH_UP,
        /**
         * A finger has moved
         */
        TOUCH_DRAGGED
    }

    /**
     * The class for storing the events.
     */
    public static class TouchEvent {
        /**
         * The type of the event.
         */
        public TouchType type;
        /**
         * The coordinates of the event.
         */
        public int x, y;
        /**
         * The index of the finger.
         */
        public int pointer;
    }

    /**
     * Allow for a maximum of ten fingers.
     */
    public static final int MAX_TOUCH_POINTS = 10;

    private final boolean[] isTouched = new boolean[MAX_TOUCH_POINTS];
    private final int[] touchX = new int[MAX_TOUCH_POINTS];
    private final int[] touchY = new int[MAX_TOUCH_POINTS];
    private final int[] id = new int[MAX_TOUCH_POINTS];
    private final Pool<TouchEvent> touchEventPool;
    private final List<TouchEvent> touchEvents = new ArrayList<>();
    private final List<TouchEvent> touchEventsBuffer = new ArrayList<>();

    /**
     * Construct the handler and attach to the given {@link View}.
     * @param view the view to attach the handler to.
     */
    public TouchHandler(View view) {
        Pool.PoolObjectFactory<TouchEvent> factory = new Pool.PoolObjectFactory<TouchEvent>() {
            @Override
            public TouchEvent createObject() {
                return new TouchEvent();
            }
        };

        touchEventPool = new Pool<>(factory, 100);
        view.setOnTouchListener(this);
    }

    /**
     * Override of the {@link View.OnTouchListener#onTouch}
     * @param v the view.
     * @param event the event.
     * @return always {@code true} because the events are always processed.
     */
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        synchronized (this) {
            int action = event.getActionMasked();
            int pointerIndex = event.getActionIndex();
            int pointerCount = event.getPointerCount();

            int pointerId;
            switch (action) {
                case MotionEvent.ACTION_DOWN:
                case MotionEvent.ACTION_POINTER_DOWN:
                    pointerId = event.getPointerId(pointerIndex);
                    registerEvent(event, pointerIndex, pointerId, TouchType.TOUCH_DOWN);
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_POINTER_UP:
                    pointerId = event.getPointerId(pointerIndex);
                    registerEvent(event, pointerIndex, pointerId, TouchType.TOUCH_UP);
                    break;
                case MotionEvent.ACTION_MOVE:
                    for (int i = 0 ; i < pointerCount ; i++ ) {
                        pointerId = event.getPointerId(i);
                        registerEvent(event, i, pointerId, TouchType.TOUCH_DRAGGED);
                    }
                    break;
            }

            for (int i = pointerCount; i < MAX_TOUCH_POINTS; i++ ) {
                isTouched[i] = false;
                id[i] = -1;
            }
            return true;
        }
    }

    private void registerEvent(MotionEvent event, int i, int pointerId, TouchType type) {
        TouchEvent touchEvent = touchEventPool.newObject();
        touchEvent.type = type;
        touchEvent.pointer = pointerId;
        touchEvent.x = touchX[i] = (int)event.getX();
        touchEvent.y = touchY[i] = (int)event.getY();
        isTouched[i] = true;
        id[i] = pointerId;
        touchEventsBuffer.add(touchEvent);
    }

    /**
     * Return true if the given finger is down.
     * @param pointer the finger.
     * @return {@code true} if it is down.
     */
    public boolean isTouchDown(int pointer) {
        synchronized (this) {
            int index = getIndex(pointer);
            return index >= 0 && index < MAX_TOUCH_POINTS && isTouched[index];
        }
    }

    /**
     * Return the x coordinate of the position of the given finger.
     * @param pointer the finger.
     * @return the x coordinate of the position.
     */
    public int getTouchX(int pointer) {
        synchronized (this) {
            int index = getIndex(pointer);
            if (index < 0 || index >= MAX_TOUCH_POINTS)
                return 0;
            else
                return touchX[index];
        }
    }

    /**
     * Return the y coordinate of the position of the given finger.
     * @param pointer the finger.
     * @return the y coordinate of the position.
     */
    public int getTouchY(int pointer) {
        synchronized (this) {
            int index = getIndex(pointer);
            if (index < 0 || index >= MAX_TOUCH_POINTS)
                return 0;
            else
                return touchY[index];
        }
    }

    /**
     * The list of events that happened since the last call. The list is
     * cleared.
     * @return the list of events.
     */
    public List<TouchEvent> getTouchEvents() {
        synchronized (this) {
            for (TouchEvent touchEvent : touchEvents)
                touchEventPool.free(touchEvent);
            touchEvents.clear();
            touchEvents.addAll(touchEventsBuffer);
            touchEventsBuffer.clear();
            return touchEvents;
        }
    }

    private int getIndex(int pointerId) {
        for (int i = 0; i < MAX_TOUCH_POINTS; i++) {
            if (id[i] == pointerId)
                return i;
        }
        return -1;
    }
}
