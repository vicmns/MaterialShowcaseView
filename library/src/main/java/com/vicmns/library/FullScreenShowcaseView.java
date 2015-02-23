package com.vicmns.library;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.widget.TextView;

/**
 * Created by vicmns on 2/4/2015.
 */
public class FullScreenShowcaseView extends ShowcaseView {

    public FullScreenShowcaseView(Context context) {
        this(context, null);
    }

    public FullScreenShowcaseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public FullScreenShowcaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FullScreenShowcaseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initShowcaseView() {
        initViews();
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        mMainLayout = (ViewGroup) inflater.inflate(R.layout.showcase_fullscreen_layout, this, false);
        mTitleTextView = (TextView) mMainLayout.findViewById(R.id.showcase_title);
        mDescriptionTextView = (TextView) mMainLayout.findViewById(R.id.showcase_description);
        mShowcaseButton =  mMainLayout.findViewById(R.id.showcase_button);

        addView(mMainLayout);
    }

    public static class Builder {
        final FullScreenShowcaseView mShowcaseView;
        private boolean isAttached;
        private final Activity mActivity;

        public Builder(Activity activity) {
            mActivity = activity;
            mShowcaseView = new FullScreenShowcaseView(activity);
        }

        public Builder attachToView(ViewGroup parentView) {
            parentView.addView(mShowcaseView);
            mShowcaseView.setVisibility(View.INVISIBLE);
            isAttached = true;
            return this;
        }

        public Builder setOnClickListener(OnClickListener onClickListener) {
            mShowcaseView.overrideButtonClick(onClickListener);
            return this;
        }

        public Builder setShowCaseEventListener(OnShowcaseEventListener onShowcaseEventListener) {
            mShowcaseView.setonShowcaseEventListener(onShowcaseEventListener);
            return this;
        }

        public Builder setShowcaseTitle(String showcaseTitle) {
            mShowcaseView.setTitleText(showcaseTitle);
            return this;
        }

        public Builder setShowcaseDescription(String showcaseDescription) {
            mShowcaseView.setDescriptionText(showcaseDescription);
            return this;
        }

        public Builder doNotAttach() {
            isAttached = true;
            return this;
        }

        public Builder hideButton() {
            mShowcaseView.setShowcaseButtonVisibility(View.GONE);
            return this;
        }

        public FullScreenShowcaseView build() {
            if(!isAttached) {
                attachShowCaseToDecorView(mActivity, mShowcaseView);
            }

            return mShowcaseView;
        }
    }

    private static void attachShowCaseToDecorView(Activity activity, View showcaseView) {
        ((ViewGroup) activity.getWindow().getDecorView()).addView(showcaseView);
        showcaseView.setVisibility(View.INVISIBLE);
    }

    public void clear() {
        ((ViewManager) this.getParent()).removeView(this);
    }

    public void setStatusBarColor(int color){
        Activity parent = (Activity)getContext();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(parent.getWindow() != null)
                parent.getWindow().setStatusBarColor(color);
        }
    }

    public int getStatusBarColor() {
        Activity parent = (Activity)getContext();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(parent.getWindow() != null)
                return parent.getWindow().getStatusBarColor();
        }

        return getResources().getColor(android.R.color.black);
    }

    public void setNavigationBarColor(int color) {
        Activity parent = (Activity)getContext();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(parent.getWindow() != null)
                parent.getWindow().setNavigationBarColor(color);
        }
    }

    public int getNavigationBarColor() {
        Activity parent = (Activity)getContext();
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(parent.getWindow() != null)
                return parent.getWindow().getNavigationBarColor();
        }

        return getResources().getColor(android.R.color.black);
    }
}
