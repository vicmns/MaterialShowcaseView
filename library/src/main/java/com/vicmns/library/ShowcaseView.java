package com.vicmns.library;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by vicmns on 2/4/2015.
 */
public abstract class ShowcaseView extends RelativeLayout {
    protected View mTargetView, mTargetViewIndicator;
    protected ViewGroup mMainLayout;
    protected TextView mTitleTextView, mDescriptionTextView;
    protected View mShowcaseButton;

    private OnShowcaseEventListener mOnShowcaseEventListener;
    private AnimatorFactory mAnimatorFactory;

    private String mTitleText, mDescriptionText;

    public ShowcaseView(Context context) {
        this(context, null);
    }

    public ShowcaseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ShowcaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, 0, 0);
    }

    public ShowcaseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
        initVariables();
        initShowcaseView();
    }

    private void initVariables() {
        mAnimatorFactory = new AnimatorFactory();
    }

    protected abstract void initShowcaseView();

    public abstract void clear();

    public View getTargetView() {
        return mTargetView;
    }

    public void setTargetView(View targetView) {
        this.mTargetView = targetView;
    }

    public String getTitleText() {
        return mTitleText;
    }

    public void setTitleText(String mTitleText) {
        this.mTitleText = mTitleText;
        if(mTitleTextView!= null) {
            mTitleTextView.setText(mTitleText);
        }
    }

    public String getDescriptionText() {
        return mDescriptionText;
    }

    public void setDescriptionText(String mDescriptionText) {
        this.mDescriptionText = mDescriptionText;
        if(mDescriptionTextView != null) {
            mDescriptionTextView.setText(mDescriptionText);
        }
    }

    public void setonShowcaseEventListener(com.vicmns.library.OnShowcaseEventListener onShowcaseEventListener) {
        mOnShowcaseEventListener = onShowcaseEventListener;
    }

    public void setShowcaseButtonVisibility(int visibility) {
        if(mShowcaseButton != null) {
            mShowcaseButton.setVisibility(visibility);
        }
    }

    public void overrideButtonClick(OnClickListener onClickListener) {
        if(mShowcaseButton != null) {
            if(onClickListener != null) {
                mShowcaseButton.setOnClickListener(onClickListener);
            } else {
                mShowcaseButton.setOnClickListener(hideOnClickListener);
            }
        }
    }

    private OnClickListener hideOnClickListener = new OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    public void show() {
        show(false);
    }

    public void show(boolean immediate) {
        if(immediate) {
            setVisibility(View.VISIBLE);
        } else {
            mAnimatorFactory.fadeInView(this, getResources().getInteger(android.R.integer.config_shortAnimTime));
        }
    }

    public void hide() {
        hide(true);
    }

    public void hide(boolean immediate) {
        if(immediate) {
            setVisibility(View.GONE);
        } else {
            mAnimatorFactory.fadeOutView(this, getResources().getInteger(android.R.integer.config_shortAnimTime));
        }
    }

    protected boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }
}
