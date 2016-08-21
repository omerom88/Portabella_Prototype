package ioio.examples.hello_service;

import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.View;
import android.widget.LinearLayout;

import java.util.LinkedList;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeUnit;

/**
 * Created by Tomer on 13/08/2016.
 */
public class ActivitySwipeDetector implements View.OnTouchListener {

    private static LinearLayout[] layouts = new LinearLayout[6];
    private SortedSet<MultiPointerTouch> multiPointerTouch;

    /**
     * This inner class represent a Pointer on the screen. each pointer has a unique id and contains
     * a list of layout that the user moved his finger on.
     */
    private class MultiPointerTouch implements Comparable<MultiPointerTouch> {
        private int id;
        private LinkedList<PointerTouch> pointerList;
        private VelocityTracker velocityTracker = null;
        private boolean isStrumming = false;

        public MultiPointerTouch(int id) {
            this.id = id;
            pointerList = new LinkedList<PointerTouch>();
        }

        public int getId() {
            return id;
        }

        public void setVelocityTracker(VelocityTracker velocityTracker) {
            this.velocityTracker = velocityTracker;
        }

        public void clearVelocityTracker() {
            velocityTracker.clear();
        }

        /**
         * add a new Strumming to the pointer, and start the strumming action (if not started yet).
         * if if LayoutId == -1, than the pointer makes a new strumming.
         * @param layout The layout that the event took place.
         * @param pressure the pressure of the pointer on the screen.
         * @param y The y coordinate of the pointer.
         */
        public void addStrumming(int layout, float pressure, float y) {
            if (pointerList.size() == 0) {
                if (velocityTracker == null) {
                    // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                    setVelocityTracker(VelocityTracker.obtain());
                } else {
                    // Reset the velocity tracker back to its initial state.
                    clearVelocityTracker();
                }
                pointerList.addLast(new PointerTouch(layout, velocityTracker.getXVelocity(), pressure, y));
            } else {
                if (pointerList.getLast().getLayoutId() != layout) {
                    velocityTracker.computeCurrentVelocity(500);
                    pointerList.addLast(new PointerTouch(layout, velocityTracker.getXVelocity(), pressure, y));
                    run();
                    if (!isStrumming) {
                        isStrumming = true;
                    }
                }
            }
        }

        public void run() {
            LinkedList<PointerTouch> list = this.getPointerList();
            while (list.size() > 1) {
                PointerTouch second = list.get(1);
                int firstLayoutId = list.getFirst().getLayoutId();
                int secondLayoutId = second.getLayoutId();
                if (firstLayoutId != -1) {
                    if (secondLayoutId == -1) {
                        this.removeStrumming();
                    } else if (firstLayoutId < secondLayoutId &&(((firstLayoutId + secondLayoutId) % 4 == 1)
                            || (firstLayoutId + 1 < secondLayoutId))) {
                        int start = getMeitarBorder(firstLayoutId);
                        int end = getMeitarBorder(secondLayoutId);
                        onLeftSwipe(second.getVelocity(), second.getPressure(), second.getY(), start, end);
                    } else if ((firstLayoutId + secondLayoutId) % 4 == 1 || firstLayoutId > secondLayoutId + 1) {
                        int start = getMeitarBorder(firstLayoutId);
                        int end = getMeitarBorder(secondLayoutId);
                        onRightSwipe(second.getVelocity(), second.getPressure(), second.getY(), start, end);
                    }
                }
                this.removeStrumming();
            }
            this.isStrumming = false;
        }

        private int getMeitarBorder(int layoutId) {
            int meitar = layoutId / 2;
            return layoutId % 2 == 1 ? ++meitar : meitar;
        }

        public void removeStrumming() {
            if (pointerList.size() > 1) {
                pointerList.removeFirst();
            }
        }

        @Override
        public boolean equals(Object o) {
            if (!(o instanceof MultiPointerTouch)) {
                return false;
            }
            // id must be the same for two MultiPointerTouch to be equal
            MultiPointerTouch mpt = (MultiPointerTouch) o;
            return this.id == mpt.getId();
        }

        @Override
        public int compareTo(MultiPointerTouch o) {
            if (id > o.getId())
                return 1;
            else if (id < o.getId())
                return -1;
            return 0;
        }

        public void addMovement(MotionEvent event) {
            if (velocityTracker == null) {
                // Retrieve a new VelocityTracker object to watch the velocity of a motion.
                setVelocityTracker(VelocityTracker.obtain());
            }
            velocityTracker.addMovement(event);
        }

        public LinkedList<PointerTouch> getPointerList() {
            return pointerList;
        }
    }

    public ActivitySwipeDetector(LinearLayout[] layouts){
        ActivitySwipeDetector.layouts = layouts;
        this.multiPointerTouch = new TreeSet<MultiPointerTouch>();
    }

    private static void onRightSwipe(float velocity, float pressure, float y, int start, int end) {
        for (int i = start - 1; i >= end; i--) {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CordManager.restartTask(i, pressure, velocity, y);
        }
    }

    private static void onLeftSwipe(float velocity, float pressure, float y, int start, int end) {
        for (int i = start; i < end; i++) {
            try {
                TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            CordManager.restartTask(i, pressure, velocity, y);
        }
    }

    public boolean onTouch(View v, MotionEvent event) {
        // get pointer ID
        int pointerId = event.getPointerId(event.getActionIndex());

        CordManager.pauseUnRunningTasks();
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN: {
                Log.e("ACTION_POINTER_DOWN", "ACTION_POINTER_DOWN");
                multiPointerTouch.add(new MultiPointerTouch(pointerId));
                MultiPointerTouch mpt = getPointer(pointerId);
                if (mpt != null && pointerId < event.getPointerCount()) {
                    float downX = event.getX(pointerId);
                    mpt.addStrumming(computeLayout(downX), event.getPressure(pointerId), event.getY(pointerId));
                }
                return true;
            }
            case MotionEvent.ACTION_MOVE: {
                Log.e("ACTION_MOVE", "ACTION_MOVE");
                for (int i = 0; i < event.getPointerCount(); i++) {
                    MultiPointerTouch mpt = getPointer(i);
                    if (mpt != null) {
                        float downX = event.getX(i);
                        mpt.addMovement(event);
                        mpt.addStrumming(computeLayout(downX), event.getPressure(i), event.getY(i));
                    } else {
                        multiPointerTouch.add(new MultiPointerTouch(i));
                    }
                }
                return true;
            }
            case MotionEvent.ACTION_POINTER_UP: {
                Log.e("ACTION_POINTER_UP", "ACTION_POINTER_UP");
                MultiPointerTouch mpt = getPointer(pointerId);
                if (mpt != null) {
                    mpt.addStrumming(-1, 0, 0);
                }
                return true;
            }

            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL: {
                Log.e("ACTION: ", "ACTION_CANCEL \\ ACTION_UP");
                for (MultiPointerTouch element : multiPointerTouch) {
                    element.addStrumming(-1, 0 , 0);
                }
                return true;
            }
        }
        return false;
    }

    private MultiPointerTouch getPointer(int id) {
        for (MultiPointerTouch element : multiPointerTouch) {
            if (element.getId() == id) {
                return element;
            }
        }
        return null;
    }

    private static int computeLayout(float x) {
        int meitar = getMeitarByPosition(x);
        int tempMeitar = meitar;
        meitar *= 2;
        if (x > getMiddleOfLayout(layouts[tempMeitar])) {
            meitar++;
        }
        return meitar;
    }

    private static int getMeitarByPosition(float x) {
        for (int i = 0; i < layouts.length; i++) {
            if (x >= layouts[i].getLeft() && x <= layouts[i].getRight()) {
                return i;
            }
        }
        if (x >= layouts[layouts.length - 1].getRight()) {
            return layouts.length - 1;
        } else
            return 0;
    }

    private static float getMiddleOfLayout(LinearLayout layout) {
        return (layout.getRight() + layout.getLeft()) / 2;
    }

}