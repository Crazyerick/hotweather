package com.erick.hotweather.base;

import android.os.Bundle;

import java.lang.ref.WeakReference;

public abstract class AbstractPresenter<V extends BaseView, M extends BaseModel> implements BasePresenter<V> {

    protected WeakReference<V> viewRef;
    protected M model;

    public AbstractPresenter() {
        model = createModel();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
    }

    @Override
    public void onDestroy() {
        detachView();
        model.onDestroy();
    }

    @Override
    public void attachView(V v) {
        viewRef = new WeakReference<V>(v);
        viewRef.get().setPresenter(this);
    }

    @Override
    public void detachView() {
        if (viewRef != null) {
            viewRef.clear();
            viewRef = null;
        }
    }

    public void setModel(M m) {
        model = m;
    }

    protected V getView() {
        return viewRef == null ? null : viewRef.get();
    }

    protected boolean isViewAttached() {
        return viewRef != null && viewRef.get() != null;
    }

    protected abstract M createModel();

}
