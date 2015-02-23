package com.vicmns.library;

import android.animation.Animator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.WindowManager;

import java.util.List;

/**
 * Created by vicmns on 1/29/2015.
 */
public class ShowcasePager implements ViewPager.OnPageChangeListener {
    private Context mContext;
    private ViewPager mShowcasePager;
    private ShowcasePagerAdapter mShowcasePagerAdapter;
    private AnimatorFactory mAnimatorFactory;
    private int cStatusBarColor, cNavigationBarColor;

    public ShowcasePager(FragmentActivity activity) {
        mContext = activity;
        LayoutInflater inflater = LayoutInflater.from(activity);
        mAnimatorFactory = new AnimatorFactory();
        ViewGroup parentView = ((ViewGroup) activity.getWindow().getDecorView());
        mShowcasePager = (ViewPager) inflater.inflate(R.layout.showcase_pager_layout, parentView, false);
        //mShowcasePager.setBackgroundColor(activity.getResources().getColor(android.R.color.holo_red_dark));
        mShowcasePagerAdapter = new ShowcasePagerAdapter(activity.getSupportFragmentManager());
        mShowcasePager.setAdapter(mShowcasePagerAdapter);
        mShowcasePager.setOnPageChangeListener(this);

        attachShowcasePager(activity, mShowcasePager);
    }

    private void initVars() {
        cStatusBarColor = getStatusBarColor();
        cNavigationBarColor = getNavigationBarColor();
    }

    private void setTransparentSystemBars(Activity activity){
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
            return;
        }

        int navControlFlags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            navControlFlags |= WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS | WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        }

        activity.getWindow().addFlags(navControlFlags);
    }

    private void attachShowcasePager(Activity activity, View showcasePager) {
        Point screenSize = getScreenSize();
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(screenSize.x, screenSize.y);
                ((ViewGroup) activity.getWindow().getDecorView()).addView(showcasePager, layoutParams);
    }

    private Point getScreenSize() {
        WindowManager wm = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        Point screenSize = new Point();
        display.getSize(screenSize);
        return screenSize;
    }

    public void showShowcaseList(List<ShowcaseView> showcaseViewList) {
        for(int i = 0; i < showcaseViewList.size() - 1; i++) {
            showcaseViewList.get(i)
                    .overrideButtonClick(changePagerPageListener(i + 1, showcaseViewList.size()));
        }

        showcaseViewList.get(showcaseViewList.size() - 1).overrideButtonClick(finishShowcase);

        mShowcasePagerAdapter.setShowcaseViewList(showcaseViewList);
        mShowcasePagerAdapter.notifyDataSetChanged();
    }

    private View.OnClickListener changePagerPageListener(final int page, final int numberOfPages) {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(page < numberOfPages)
                mShowcasePager.setCurrentItem(page, true);
            }
        };
    }

    private View.OnClickListener finishShowcase = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            hide(false);
        }
    };

    public void show() {
        show(false);
    }

    public void show(boolean immediate) {
        if(immediate) {
            mShowcasePager.setVisibility(View.VISIBLE);
        } else {
            mAnimatorFactory.fadeInView(mShowcasePager, mContext.getResources().getInteger(android.R.integer.config_shortAnimTime));
        }
    }

    public void hide() {
        hide(true);
    }

    public void hide(boolean immediate) {
        if(immediate) {
            mShowcasePager.setVisibility(View.GONE);
        } else {
            mAnimatorFactory.fadeOutView(mShowcasePager, mContext.getResources().getInteger(android.R.integer.config_shortAnimTime), new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    clearShowcase();
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
        }
    }

    private void clearShowcase() {
        ((ViewManager) mShowcasePager.getParent()).removeView(mShowcasePager);
        setNavigationBarColor(cNavigationBarColor);
        setStatusBarColor(cStatusBarColor);
    }

    public void setStatusBarColor(int color){
        Activity parent = (Activity) mContext;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(parent.getWindow() != null)
                parent.getWindow().setStatusBarColor(color);
        }
    }

    public int getStatusBarColor() {
        Activity parent = (Activity) mContext;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(parent.getWindow() != null)
                return parent.getWindow().getStatusBarColor();
        }

        return mContext.getResources().getColor(android.R.color.black);
    }

    public void setNavigationBarColor(int color) {
        Activity parent = (Activity) mContext;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(parent.getWindow() != null)
                parent.getWindow().setNavigationBarColor(color);
        }
    }

    public int getNavigationBarColor() {
        Activity parent = (Activity) mContext;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if(parent.getWindow() != null)
                return parent.getWindow().getNavigationBarColor();
        }

        return mContext.getResources().getColor(android.R.color.black);
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        mShowcasePagerAdapter.refreshShowCaseBarColors(position);
    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    public void onOrientationChange() {
        if(mShowcasePager != null) {
            mShowcasePager.setPageMargin(0);
        }
    }
}
