package ru.ach4god.wirelessandroidmouse.base;

import android.content.Context;

public abstract class PresentorBase<T extends IActivityView> implements IPresenter<T> {
    private T view;
    private Context context;

    @Override
    public Context getApplicationContext() {
        return this.context;
    }

    @Override
    public void attachView(T view, Context context) {
        this.view = view;
        this.context = context;
    }

    @Override
    public void deattachView() {
        this.view = null;
        this.context = null;
    }

    @Override
    public void destroy() {
        this.view = null;
        this.context = null;
    }

    public T getView() {
        return view;
    }

    protected boolean isViewAttached() {
        return view != null;
    }
}
