package com.erick.hotweather.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.erick.hotweather.R;

/**
 * Created by erickxhyang on 2018/10/2.
 */

public class CustomTitleBar extends RelativeLayout {

    /**
     * 标题栏的根布局
     */
    private RelativeLayout mRootRl;

    /**
     * 左边标题栏的文字
     */
    private TextView mTitleTv;

    /**
     * 标题栏的右边搜索按钮
     */
    private ImageView mSearchIv;

    /**
     * 标题栏的背景颜色
     */
    private int titleBackgroundColor;

    /**
     * 标题栏的显示的标题文字
     */
    private String titleText;

    /**
     * 标题栏的显示的标题文字颜色
     */
    private int titleTextColor;

    /**
     * 标题栏的显示的标题文字大小
     */
    private int titleTextSize;

    /**
     * 右边搜索按钮的资源图片
     */
    private int rightButtonImageId;

    /**
     * 是否显示右边保存按钮
     */
    private boolean showRightButton;

    /**
     * 搜索图标的点击事件
     */
    private TitleOnClickListener titleOnClickListener;

    /**
     * 监听标题点击接口
     */
    public interface TitleOnClickListener {
        /**
         * 标题的点击事件
         */
        void onTitleClick();

        /**
         * 搜索按钮的点击事件
         */
        void onSearchClick();
    }

    public CustomTitleBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        /*加载布局文件*/
        LayoutInflater.from(context).inflate(R.layout.title_bar, this, true);
        mRootRl = (RelativeLayout) findViewById(R.id.root_rl);
        mTitleTv = (TextView) findViewById(R.id.title_text);
        mSearchIv = (ImageView) findViewById(R.id.search_iv);

        /*获取属性值*/
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.CustomTitleBar);

        /*标题相关*/
        titleBackgroundColor = typedArray.getColor(R.styleable.CustomTitleBar_title_background, Color.WHITE);
        titleText = typedArray.getString(R.styleable.CustomTitleBar_title_text);
        titleTextColor = typedArray.getColor(R.styleable.CustomTitleBar_title_textColor, Color.parseColor("#7e7c7c"));
        titleTextSize = typedArray.getColor(R.styleable.CustomTitleBar_title_textSize, 20);

        /*右边搜索按钮相关*/
        rightButtonImageId = typedArray.getResourceId(R.styleable.CustomTitleBar_right_button_image, 0);
        showRightButton = typedArray.getBoolean(R.styleable.CustomTitleBar_show_right_button, true);

        /*设置值*/
        setTitleBackgroundColor(titleBackgroundColor);
        setTitleText(titleText);
        setTitleTextColor(titleTextColor);
        setTitleTextSize(titleTextSize);
        setRightButtonImageId(rightButtonImageId);
        setShowRightButton(showRightButton);

        mTitleTv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleOnClickListener != null) {
                    titleOnClickListener.onTitleClick();
                }
            }
        });
        mSearchIv.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titleOnClickListener != null) {
                    titleOnClickListener.onSearchClick();
                }
            }
        });
        typedArray.recycle();
    }

    public RelativeLayout getRootRl() {
        return mRootRl;
    }

    public TextView getTitleTv() {
        return mTitleTv;
    }

    public ImageView getSearchIv() {
        return mSearchIv;
    }

    public int getTitleBackgroundColor() {
        return titleBackgroundColor;
    }

    public String getTitleText() {
        return titleText;
    }

    public int getTitleTextColor() {
        return titleTextColor;
    }

    public int getTitleTextSize() {
        return titleTextSize;
    }

    public int getRightButtonImageId() {
        return rightButtonImageId;
    }

    public boolean isShowRightButton() {
        return showRightButton;
    }

    public void setTitleBackgroundColor(int titleBackgroundColor) {
        if (this.mRootRl != null) {
            this.mRootRl.setBackgroundColor(titleBackgroundColor);
        }
    }

    public void setTitleText(String titleText) {
        if (this.mTitleTv != null) {
            this.mTitleTv.setText(titleText);
        }
    }

    public void setTitleTextColor(int titleTextColor) {
        if (this.mTitleTv != null) {
            this.mTitleTv.setTextColor(titleTextColor);
        }
    }

    public void setTitleTextSize(int titleTextSize) {
        if (this.mTitleTv != null) {
            this.mTitleTv.setTextSize(titleTextSize);
        }
    }

    public void setRightButtonImageId(int rightButtonImageId) {
        if (this.mSearchIv != null) {
            this.mSearchIv.setBackgroundResource(rightButtonImageId);
        }
    }

    public void setShowRightButton(boolean showRightButton) {
        if (this.mSearchIv != null) {
            this.mSearchIv.setVisibility(showRightButton ? VISIBLE : INVISIBLE);
        }
    }

    public void setTitleOnClickListener(TitleOnClickListener titleOnClickListener) {
        this.titleOnClickListener = titleOnClickListener;
    }

}
