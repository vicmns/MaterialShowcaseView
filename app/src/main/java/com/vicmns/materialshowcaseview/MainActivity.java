package com.vicmns.materialshowcaseview;

import android.content.res.Configuration;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.vicmns.library.FullScreenShowcaseView;
import com.vicmns.library.ShowcasePager;
import com.vicmns.library.ShowcaseView;
import com.vicmns.library.SingleShowcaseView;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    private SingleShowcaseView singleShowcaseView, singleShowcaseView2, singleShowcaseView3;
    private ShowcasePager showcasePager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FullScreenShowcaseView fullScreenShowcaseView =
                new FullScreenShowcaseView.Builder(this).setShowcaseTitle("Welcome")
                        .setShowcaseDescription("This is a simple showcase. To continue swipe left or just tap the arrow button!")
                        .doNotAttach().build();

        singleShowcaseView = new SingleShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.viewToTarget))
                .setShowcaseTitle("Simple TextView")
                .setShowcaseDescription("This is a simple and boring TextView")
                .doNotAttach().doNotAutoManageBarColors().doNotCalculateMargins().build();

        singleShowcaseView2 = new SingleShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.viewToTarget2))
                .setShowcaseTitle("Doge")
                .setShowcaseDescription("WOW! so much showcase, much android")
                .doNotAttach().doNotAutoManageBarColors().doNotCalculateMargins().build();

        singleShowcaseView3 = new SingleShowcaseView.Builder(this)
                .setTarget(findViewById(R.id.viewToTarget3))
                .setShowcaseTitle("A button")
                .setShowcaseDescription("No, you can't touch it  ( ͡° ͜ʖ ͡°)")
                .doNotAttach().doNotAutoManageBarColors().doNotCalculateMargins().build();

        List<ShowcaseView> showcaseViewList = new ArrayList<>();
        showcaseViewList.add(fullScreenShowcaseView);
        showcaseViewList.add(singleShowcaseView);
        showcaseViewList.add(singleShowcaseView2);
        showcaseViewList.add(singleShowcaseView3);

        showcasePager = new ShowcasePager(this);
        showcasePager.showShowcaseList(showcaseViewList);
        showcasePager.onOrientationChange();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.show_showcase1:
                singleShowcaseView.show();
                return true;
            case R.id.show_showcase2:
                singleShowcaseView2.show();
                return true;
            case R.id.show_showcase3:
                singleShowcaseView3.show();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        showcasePager.onOrientationChange();
        super.onConfigurationChanged(newConfig);
    }
}
