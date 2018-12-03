package com.erick.hotweather.business.choosearea;

import com.erick.hotweather.base.BasePresenter;
import com.erick.hotweather.base.BaseView;

public interface ChooseAreaContract {

    interface IView extends BaseView<IPresenter> {
        void showLoading();

        void hideLoading();

        void showMessage(String msg);

    }

    interface IPresenter extends BasePresenter<IView> {

    }

}
