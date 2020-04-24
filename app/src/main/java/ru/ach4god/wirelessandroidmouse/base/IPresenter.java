package ru.ach4god.wirelessandroidmouse.base;

import android.content.Context;

public interface IPresenter <V extends IActivityView>{
    void attachView(V view, Context context);
    Context getApplicationContext();
    void deattachView();
    void viewIsReady();
    void destroy();
}
