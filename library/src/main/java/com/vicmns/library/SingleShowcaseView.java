package com.vicmns.library;

import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewManager;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by vicmns on 1/19/2015.
 */
public class SingleShowcaseView extends ShowcaseView {
    private static final String TAG = "TargetShowcaseView";
    private Paint eraserPaint, basicPaint;
    private Bitmap bitmapBuffer;
    private Point screenSize;
    private boolean mRecalculateShowcase, isViewOnTop;
    private boolean manageBarColors = true;
    private boolean calculateMargins = true;

    private int cStatusBarColor, cNavigationBarColor;

    public SingleShowcaseView(Context context) {
        this(context, null);
    }

    public SingleShowcaseView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SingleShowcaseView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public SingleShowcaseView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void initShowcaseView() {
        cStatusBarColor = getStatusBarColor();
        cNavigationBarColor = getNavigationBarColor();

        initScreenSize();
        initShowcasePaint();
        setShowViewPadding();
        initViews();
        getViewTreeObserver().addOnGlobalLayoutListener(new UpdateOnGlobalLayout());
        mRecalculateShowcase = true;
    }

    private void initScreenSize() {
        WindowManager wm = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        screenSize = new Point();
        display.getSize(screenSize);
    }

    private void initShowcasePaint() {
        PorterDuffXfermode xfermode = new PorterDuffXfermode(PorterDuff.Mode.MULTIPLY);
        eraserPaint = new Paint();
        eraserPaint.setColor(0xFFFFFF);
        eraserPaint.setAlpha(0);
        eraserPaint.setXfermode(xfermode);
        eraserPaint.setAntiAlias(true);
        basicPaint = new Paint();
    }

    private void setShowViewPadding() {
        if(!calculateMargins) {
            int paddingTop = isLollipop() ? 0 : getStatusBarHeight();
            setPadding(0, paddingTop, 0, 0);
            return;
        }

        if(getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setPadding(0, getStatusBarHeight(), getNavigationBarSize(), 0);
        } else {
            setPadding(0, getStatusBarHeight(), 0, getNavigationBarSize());
        }
    }

    private void initViews() {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        initMainTextLayout(inflater);
        initTargetViewIndicator();
    }

    private void initMainTextLayout(LayoutInflater inflater) {
        mMainLayout = (ViewGroup) inflater.inflate(R.layout.showcase_text_layout, this, false);
        mTitleTextView = (TextView) mMainLayout.findViewById(R.id.text_view_title);
        mDescriptionTextView = (TextView) mMainLayout.findViewById(R.id.text_view_description);
        mShowcaseButton = (Button) mMainLayout.findViewById(R.id.button);

        addView(mMainLayout);
    }

    private void initTargetViewIndicator() {
        int indicatorSize = getResources().getDimensionPixelSize(R.dimen.target_indicator_size);
        mTargetViewIndicator = new View(getContext());
        mTargetViewIndicator.setBackground(getResources().getDrawable(R.drawable.triangle_drawable));
        LayoutParams targetViewIndicatorParams = new LayoutParams(indicatorSize, indicatorSize);
        addView(mTargetViewIndicator, targetViewIndicatorParams);
    }

    private int getStatusBarHeight() {
        int result = 0;
        int resourceId = getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    private int getNavigationBarSize() {
        int id = getResources().getIdentifier(getResources().getConfiguration().orientation
                        == Configuration.ORIENTATION_PORTRAIT ? "navigation_bar_height" : "navigation_bar_width",
                "dimen", "android");
        if (id > 0) {
            return getResources().getDimensionPixelSize(id);
        }

        return 0;
    }

    private void repositionTargetView() {
        if(mTargetView == null || mMainLayout == null)
            return;

        //TODO: Check if the target view is below or above the center of screen
        //Also check if for orientation change to change button position and text layout width
        int mainTextLayoutPosition[] = new int[2];
        int targetViewPosition[] = new int[2];

        mMainLayout.getLocationInWindow(mainTextLayoutPosition);
        mTargetView.getLocationInWindow(targetViewPosition);

        int showcaseLayoutY2Position = mainTextLayoutPosition[1] + mMainLayout.getHeight() +
                getResources().getDimensionPixelOffset(R.dimen.target_indicator_size);

        LayoutParams mainTextLayoutLayoutParams = (LayoutParams) mMainLayout.getLayoutParams();

        if((targetViewPosition[1] + mTargetView.getHeight() / 2) < (screenSize.y / 2)) {
            //The view is at the top part
            isViewOnTop = false;
            mainTextLayoutLayoutParams.addRule(ALIGN_PARENT_BOTTOM);
            addTargetViewIndicator(ALIGN_PARENT_BOTTOM);
        } else {
            //The view is at the middle or bottom part.
            isViewOnTop = true;
            mainTextLayoutLayoutParams.addRule(ALIGN_PARENT_TOP);
            addTargetViewIndicator(ALIGN_PARENT_TOP);
            if(showcaseLayoutY2Position > targetViewPosition[1]) {
                int heightDifference = showcaseLayoutY2Position - targetViewPosition[1];
                Log.d(TAG, "Main showcase layout is bigger for: " + heightDifference);
                int mainTextLayoutHeight = mMainLayout.getHeight();
                mMainLayout.setMinimumHeight(heightDifference);
                mainTextLayoutLayoutParams.height = mainTextLayoutHeight - heightDifference +
                        + getResources().getDimensionPixelOffset(R.dimen.target_indicator_size);
            } else {
                Log.d(TAG, "Main showcase layout is smaller ");
                mMainLayout.setMinimumHeight(getResources().getDimensionPixelOffset(R.dimen.showcase_text_layout_minimum_height));
                mainTextLayoutLayoutParams.height = LayoutParams.WRAP_CONTENT;
            }
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);

        if(!manageBarColors) {
            return;
        }
        if(visibility == VISIBLE) {
            setNavigationColors();
        } else {
            clearNavigationColors();
        }
    }

    public void setNavigationColors() {
        if(isViewOnTop) {
            setStatusBarColor(getResources().getColor(R.color.primaryColor));
            setNavigationBarColor(cNavigationBarColor);
        } else {
            setStatusBarColor(cStatusBarColor);
            setNavigationBarColor(getResources().getColor(R.color.primaryColor));
        }
    }

    private void clearNavigationColors() {
        setStatusBarColor(cStatusBarColor);
        setNavigationBarColor(cNavigationBarColor);
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

    private void addTargetViewIndicator(int position) {
        LayoutParams targetViewIndicatorParams = (LayoutParams) mTargetViewIndicator.getLayoutParams();

        if(position == ALIGN_PARENT_TOP) {
            mTargetViewIndicator.setRotation(180);
            targetViewIndicatorParams.addRule(BELOW, R.id.text_layout);
        } else {
            mTargetViewIndicator.setRotation(0);
            targetViewIndicatorParams.addRule(ABOVE, R.id.text_layout);
        }
        targetViewIndicatorParams.addRule(CENTER_HORIZONTAL);
        mTargetViewIndicator.setLayoutParams(targetViewIndicatorParams);
    }

    private void updateBitmap() {
        if (bitmapBuffer == null || haveBoundsChanged()) {
            if(bitmapBuffer != null)
                bitmapBuffer.recycle();
            bitmapBuffer = Bitmap.createBitmap(screenSize.x, screenSize.y, Bitmap.Config.ARGB_8888);
        }
    }

    private boolean haveBoundsChanged() {
        return getMeasuredWidth() != bitmapBuffer.getWidth() ||
                getMeasuredHeight() != bitmapBuffer.getHeight();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        if(mTargetView == null) {
            super.dispatchDraw(canvas);
            return;
        }

        drawTargetOnCanvas(canvas);

        super.dispatchDraw(canvas);
    }

    private void drawTargetOnCanvas(Canvas canvas) {
        bitmapBuffer.eraseColor(Color.argb(200, 30, 30, 30));
        drawShowcase();
        canvas.drawBitmap(bitmapBuffer, 0, 0, basicPaint);
    }

    private void drawShowcase() {
        Canvas bufferCanvas = new Canvas(bitmapBuffer);
        Rect targetViewRect;
        int[] location = new int[2];
        mTargetView.getLocationInWindow(location);
        targetViewRect = new Rect(0, location[1], getMeasuredWidth(), location[1] + mTargetView.getHeight());
        bufferCanvas.drawRect(targetViewRect, eraserPaint);
    }

    public boolean isManageBarColors() {
        return manageBarColors;
    }

    public void setManageBarColors(boolean manageBarColors) {
        this.manageBarColors = manageBarColors;
    }

    public boolean isCalculateMargins() {
        return calculateMargins;
    }

    public void setCalculateMargins(boolean calculateMargins) {
        this.calculateMargins = calculateMargins;
    }

    public static class Builder {
        final SingleShowcaseView mSingleShowcaseView;
        private boolean isAttached;
        private final Activity mActivity;

        public Builder(Activity activity) {
            mActivity = activity;
            mSingleShowcaseView = new SingleShowcaseView(activity);
        }

        public Builder attachToView(ViewGroup parentView) {
            parentView.addView(mSingleShowcaseView);
            mSingleShowcaseView.setVisibility(View.INVISIBLE);
            isAttached = true;
            return this;
        }

        public Builder setTarget(View targetView) {
            mSingleShowcaseView.setTargetView(targetView);
            return this;
        }

        public Builder setShowcaseTitle(String showcaseTitle) {
            mSingleShowcaseView.setTitleText(showcaseTitle);
            return this;
        }

        public Builder setShowcaseDescription(String showcaseDescription) {
            mSingleShowcaseView.setDescriptionText(showcaseDescription);
            return this;
        }

        public Builder setOnClickListener(OnClickListener onClickListener) {
            mSingleShowcaseView.overrideButtonClick(onClickListener);
            return this;
        }

        public Builder setShowCaseEventListener(com.vicmns.library.OnShowcaseEventListener onShowcaseEventListener) {
            mSingleShowcaseView.setonShowcaseEventListener(onShowcaseEventListener);
            return this;
        }

        public Builder doNotAttach() {
            isAttached = true;
            return this;
        }

        public Builder doNotAutoManageBarColors() {
            mSingleShowcaseView.setManageBarColors(false);
            return this;
        }

        public Builder doNotCalculateMargins() {
            mSingleShowcaseView.setCalculateMargins(false);
            return this;
        }

        public Builder hideButton() {
            mSingleShowcaseView.setShowcaseButtonVisibility(View.GONE);
            return this;
        }

        public SingleShowcaseView build() {
            if(!isAttached) {
                attachShowCaseToDecorView(mActivity, mSingleShowcaseView);
            }
            
            return mSingleShowcaseView;
        }
    }

    private static void attachShowCaseToDecorView(Activity activity, SingleShowcaseView singleShowcaseView) {
        ((ViewGroup) activity.getWindow().getDecorView()).addView(singleShowcaseView);
        singleShowcaseView.setVisibility(View.INVISIBLE);
    }

    public void clear() {
        ((ViewManager) this.getParent()).removeView(this);
    }

    @Override
    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mRecalculateShowcase = true;
    }

    private class UpdateOnGlobalLayout implements ViewTreeObserver.OnGlobalLayoutListener {

        @Override
        public void onGlobalLayout() {
            if(mRecalculateShowcase) {
                setShowViewPadding();
                updateBitmap();
                repositionTargetView();
                mRecalculateShowcase = false;
            }
        }
    }
}
