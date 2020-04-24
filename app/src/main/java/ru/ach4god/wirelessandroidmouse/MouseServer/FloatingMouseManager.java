package ru.ach4god.wirelessandroidmouse.MouseServer;

import android.app.Activity;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import ru.ach4god.wirelessandroidmouse.R;

import static android.content.Context.WINDOW_SERVICE;

public class FloatingMouseManager {

    private static FloatingMouseManager instance;
    private Activity mActivity;
    private WindowManager mWindowManager;
    private View mFloatView;
    private WindowManager.LayoutParams mFloatViewLayoutParams;
    private boolean mIsFloatViewShowing = false;

    private FloatingMouseManager() {
    }

    private FloatingMouseManager(Activity activity) {
        this.mActivity = activity;
    }

    /**
     * Create new instance or return already created instance
     * @param activity Activity for create inflater
     * @return FloatingMouseManager instance
     */
    public static FloatingMouseManager getFloatingMouseManager(Activity activity) {
        if (instance == null) {
            instance = new FloatingMouseManager(activity);
            instance.onCreate();
        }
        return instance;
    }

    public static void destroy() {
        instance = null;
    }

    /**
     * Initialize instance fields
     */
    private void onCreate() {
        LayoutInflater inflater = LayoutInflater.from(mActivity);
        mFloatView = inflater.inflate(R.layout.popup_mouse, null);

        mFloatViewLayoutParams = new WindowManager.LayoutParams();
        mFloatViewLayoutParams.format = PixelFormat.TRANSLUCENT;
        mFloatViewLayoutParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

        mFloatViewLayoutParams.type = Build.VERSION.SDK_INT >= Build.VERSION_CODES.O
                ? WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
                : WindowManager.LayoutParams.TYPE_TOAST;

        mFloatViewLayoutParams.gravity = Gravity.CENTER;
        mFloatViewLayoutParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        mFloatViewLayoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
    }

    /**
     * Show cursor
     * @return FloatingMouseManager instance
     */
    public FloatingMouseManager show() {
        if (!mIsFloatViewShowing) {
            mIsFloatViewShowing = true;
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!mActivity.isFinishing()) {
                        mWindowManager = (WindowManager) mActivity.getSystemService(WINDOW_SERVICE);
                        if (mWindowManager != null) {
                            mWindowManager.addView(mFloatView, mFloatViewLayoutParams);
                        }
                    }
                }
            });
        }
        return this;
    }

    /**
     * Hide cursor
     * @return FloatingMouseManager instance
     */
    public FloatingMouseManager hide() {
        if (mIsFloatViewShowing) {
            mIsFloatViewShowing = false;
            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (mWindowManager != null) {
                        mWindowManager.removeViewImmediate(mFloatView);
                    }
                }
            });
        }
        return this;
    }

    /**
     * Click on current cursor position
     * @return FloatingMouseManager instance
     */
    public FloatingMouseManager click() {

        return this;
    }

    /**
     * Move mouse cursor by delta
     * @param deltaX velocity on X coordinate
     * @param deltaY velocity on Y coordinate
     * @return FloatingMouseManager instance
     */
    public FloatingMouseManager move(int deltaX, int deltaY) {
        Log.d("deltaXY", deltaX + " | " + deltaY);
        WindowManager.LayoutParams prm = mFloatViewLayoutParams;
        if (mWindowManager != null) {
            prm.x += deltaX;
            prm.y += deltaY;
            mWindowManager.updateViewLayout(mFloatView, prm);
        }
        return this;
    }

}
