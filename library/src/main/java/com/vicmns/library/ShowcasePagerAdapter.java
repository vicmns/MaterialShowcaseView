package com.vicmns.library;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by vicmns on 1/29/2015.
 */
public class ShowcasePagerAdapter extends FragmentStatePagerAdapter {
    private List<ShowcaseView> mShowcaseViewList;

    public ShowcasePagerAdapter(FragmentManager fm) {
        super(fm);
        mShowcaseViewList = new ArrayList<>();
    }

    public void setShowcaseViewList(List<ShowcaseView> showcaseViewList) {
        mShowcaseViewList = showcaseViewList;
    }

    public void addShowCase(SingleShowcaseView singleShowcaseView) {
        mShowcaseViewList.add(singleShowcaseView);
    }

    @Override
    public Fragment getItem(int position) {
        return new ShowcasePagerItemFragment(mShowcaseViewList.get(position));
    }

    @Override
    public int getCount() {
        return mShowcaseViewList.size();
    }

    public void refreshShowCaseBarColors(int position) {
        View currentView = mShowcaseViewList.get(position);
        if(currentView instanceof SingleShowcaseView) {
            ((SingleShowcaseView) currentView).setNavigationColors();
        }
    }

    @Override
    public float getPageWidth(int position) {
        return 1;
    }
}
