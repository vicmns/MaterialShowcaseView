package com.vicmns.library;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by vicmns on 1/29/2015.
 */
public class ShowcasePagerItemFragment extends Fragment {
    private static final String TAG = "ShowCasePagerItemFragment";
    private ShowcaseView mShowcaseView;

    public ShowcasePagerItemFragment() {
        Log.d(TAG, "Default Pager constructor called");
    }

    public ShowcasePagerItemFragment(ShowcaseView showcaseView) {
        Log.d(TAG, "Parameter Pager constructor called");
        mShowcaseView = showcaseView;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "OnCreateView!");
        return mShowcaseView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(mShowcaseView instanceof SingleShowcaseView) {
            ((SingleShowcaseView) mShowcaseView).show(true);
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();

        if(mShowcaseView != null) {
            mShowcaseView.clear();
        }
    }
}
