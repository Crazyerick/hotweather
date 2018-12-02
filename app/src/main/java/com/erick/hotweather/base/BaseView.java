package com.erick.hotweather.base;

public interface BaseView<P extends BasePresenter> {

    void setPresenter(P presenter);

}
