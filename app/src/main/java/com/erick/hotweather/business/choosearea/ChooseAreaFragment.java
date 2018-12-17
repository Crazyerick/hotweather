package com.erick.hotweather.business.choosearea;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.erick.hotweather.R;
import com.erick.hotweather.base.BaseFragment;
import com.erick.hotweather.business.weather.WeatherActivity;

import java.util.ArrayList;
import java.util.List;

public class ChooseAreaFragment extends BaseFragment<ChooseAreaContract.IView, ChooseAreaContract.IPresenter> implements ChooseAreaContract.IView {

    private static final String TAG = "ChooseAreaFragment";

    private ProgressDialog mProgressDialog;

    private TextView titleText;

    private Button backButton;

    private ListView mListView;

    private ArrayAdapter<String> mAdapter;

    private List<String> dataList = new ArrayList<>();

    @Override
    protected ChooseAreaContract.IPresenter createPresenter() {
        return new ChooseAreaPresenter();
    }

    @Override
    protected int getLayout() {
        return R.layout.choose_area;
    }

    @Override
    protected void initUI(Bundle savedInstanceState) {
        titleText = getRootView().findViewById(R.id.title_text);
        backButton = getRootView().findViewById(R.id.back_button);
        mListView = getRootView().findViewById(R.id.list_view);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mPresenter.loadDatas(position);
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.handleBackClicked();
            }
        });
    }

    @Override
    protected void initParams() {

    }

    @Override
    protected void initData() {
        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, dataList);
        mListView.setAdapter(mAdapter);
    }

    /**
     * 显示进度对话框
     */
    @Override
    public void showLoading() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("正在加载...");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    /**
     * 关闭进度对话框
     */
    @Override
    public void hideLoading() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    @Override
    public void showMessage(String msg) {
        Toast.makeText(getContext(), msg, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setTitle(String text) {
        titleText.setText(text);
    }

    @Override
    public void showBackButton() {
        backButton.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideBackButton() {
        backButton.setVisibility(View.GONE);
    }

    @Override
    public void showDatas(List<String> datas) {
        dataList.clear();
        dataList.addAll(datas);
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void setSelection(int position) {
        mListView.setSelection(position);
    }

    @Override
    public void updateWeather(String weatherId) {
        if (getActivity() instanceof ChooseAreaActivity) {
            Intent intent = new Intent(getContext(), WeatherActivity.class);
            intent.putExtra("weather_id", weatherId);
            startActivity(intent);
            getActivity().finish();
        } else if (getActivity() instanceof WeatherActivity) {
            WeatherActivity activity = (WeatherActivity) getActivity();
            activity.drawerLayout.closeDrawers();
            activity.requestWeather(weatherId);
        }
    }

    @Override
    public void setPresenter(ChooseAreaContract.IPresenter presenter) {
        mPresenter = presenter;
    }

}
