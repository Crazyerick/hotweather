package com.erick.hotweather.base;

import android.os.Bundle;

public interface BasePresenter<V extends BaseView> {

    void onStart();

    void onDestroy();

    void onSaveInstanceState(Bundle outState);

    void attachView(V view);

    void detachView();

}
