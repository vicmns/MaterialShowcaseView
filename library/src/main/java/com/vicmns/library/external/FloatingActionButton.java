package com.vicmns.library.external;
/*
 * Copyright 2014, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Outline;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewOutlineProvider;
import android.widget.Checkable;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.vicmns.library.R;

/**
 * A Floating Action Button is a {@link android.widget.Checkable} view distinguished by a circled
 * icon floating above the UI, with special motion behaviors.
 */
public class FloatingActionButton extends FrameLayout implements Checkable {
    private static final int NORMAL_SIZE = 0;
    private static final int SMALL_SIZE = 1;

    private int mFabSize, mShadowSize;

    private ImageView mImageView;
    private Drawable mImageViewDrawable;

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a compound button changes.
     */
    public static interface OnCheckedChangeListener {

        /**
         * Called when the checked state of a FAB has changed.
         *
         * @param fabView   The FAB view whose state has changed.
         * @param isChecked The new checked state of buttonView.
         */
        void onCheckedChanged(FloatingActionButton fabView, boolean isChecked);
    }

    /**
     * An array of states.
     */
    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    private static final String TAG = "FloatingActionButton";

    // A boolean that tells if the FAB is checked or not.
    private boolean mChecked;

    // A listener to communicate that the FAB has changed it's state
    private OnCheckedChangeListener mOnCheckedChangeListener;

    public FloatingActionButton(Context context) {
        this(context, null, 0, 0);
    }

    public FloatingActionButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0, 0);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    public FloatingActionButton(Context context, AttributeSet attrs, int defStyleAttr,
                                int defStyleRes) {
        super(context, attrs, defStyleAttr);
        initVariables();
        initView(context, attrs, defStyleAttr);
    }

    private void initVariables() {
        mShadowSize = (int) getResources().getDimension(R.dimen.fab_support_elevation);
    }

    private void initView(Context context,  AttributeSet attrs, int defStyle) {
        TypedArray a;
        Resources.Theme theme = context.getTheme();
        if (theme == null) {
            return;
        }

        a = theme.obtainStyledAttributes(attrs, R.styleable.FloatingActionButton, defStyle, 0);
        if (a == null) {
            return;
        }
        initAttrs(a);
        a.recycle();

        addImageView(context);
        setClickable(true);

        if(isLollipop()) {
            initLollipopBackground();
        }
    }

    private void initAttrs(TypedArray a) {
        if(a.getInt(R.styleable.FloatingActionButton_fabSize, NORMAL_SIZE) == SMALL_SIZE) {
            mFabSize = (int) getResources().getDimension(R.dimen.fab_size_small);
        } else {
            mFabSize = (int) getResources().getDimension(R.dimen.fab_size);
        }
        setImageViewDrawable(a.getDrawable(R.styleable.FloatingActionButton_fabCenterDrawable));
    }

    private void addImageView(Context context) {
        mImageView = new ImageView(context);
        LayoutParams layoutParams =
                new LayoutParams((int) getResources().getDimension(R.dimen.fab_icon_size),
                        (int) getResources().getDimension(R.dimen.fab_icon_size));
        layoutParams.gravity = Gravity.CENTER;

        mImageView.setDuplicateParentStateEnabled(true);
        mImageView.setLayoutParams(layoutParams);
        if(mImageViewDrawable != null) mImageView.setImageDrawable(mImageViewDrawable);

        addView(mImageView);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void initLollipopBackground() {
        // Set the outline provider for this view. The provider is given the outline which it can
        // then modify as needed. In this case we set the outline to be an oval fitting the height
        // and width.
        setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                outline.setOval(0, 0, getWidth(), getHeight());
            }
        });

        // Finally, enable clipping to the outline, using the provider we set above
        setClipToOutline(true);
    }

    private boolean isLollipop() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP;
    }

    public Drawable getImageViewDrawable() {
        return mImageViewDrawable;
    }

    public void setImageViewDrawable(Drawable mImageViewDrawable) {
        this.mImageViewDrawable = mImageViewDrawable;
    }

    public int getFabSize() {
        return mFabSize;
    }

    public void setFabSize(int mFabSize) {
        this.mFabSize = mFabSize;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = mFabSize;
        int height = mFabSize;
        if (!isLollipop()) {
            width += mShadowSize * 2;
            height += mShadowSize * 2;
        }
        setMeasuredDimension(width, height);

    }

    private void setMarginsWithoutShadow() {
        if (getLayoutParams() instanceof MarginLayoutParams) {
            MarginLayoutParams layoutParams = (MarginLayoutParams) getLayoutParams();
            int leftMargin = layoutParams.leftMargin - mShadowSize;
            int topMargin = layoutParams.topMargin - mShadowSize;
            int rightMargin = layoutParams.rightMargin - mShadowSize;
            int bottomMargin = layoutParams.bottomMargin - mShadowSize;
            layoutParams.setMargins(leftMargin, topMargin, rightMargin, bottomMargin);
            requestLayout();
        }
    }

    /**
     * Sets the checked/unchecked state of the FAB.
     * @param checked
     */
    public void setChecked(boolean checked) {
        // If trying to set the current state, ignore.
        if (checked == mChecked) {
            return;
        }
        mChecked = checked;

        // Now refresh the drawable state (so the icon changes)
        refreshDrawableState();

        if (mOnCheckedChangeListener != null) {
            mOnCheckedChangeListener.onCheckedChanged(this, checked);
        }
    }

    public ImageView getCenterImageView() {
        return mImageView;
    }

    /**
     * Register a callback to be invoked when the checked state of this button
     * changes.
     *
     * @param listener the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    @Override
    public boolean isChecked() {
        return mChecked;
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        // As we have changed size, we should invalidate the outline so that is the the
        // correct size
        if(isLollipop()) {
            invalidateOutline();
        } else {
            initPreLollipopBackground();
            setMarginsWithoutShadow();
        }
    }

    private void initPreLollipopBackground() {
        StateListDrawable drawable = new StateListDrawable();
        drawable.addState(new int[]{android.R.attr.state_pressed}, createDrawable(new int[]{android.R.attr.state_pressed}));
        drawable.addState(new int[]{}, createDrawable(new int[]{}));
        setBackground(drawable);
        /*
         * Right now the ViewCompat library does not nothing when setting the elevation, maye perhaps
         * in the near future this will be implemented.
         */
        //ViewCompat.setElevation(this, (int) getResources().getDimension(R.dimen.fab_elevation));
    }

    private Drawable createDrawable(int[] state) {
        Drawable shadowDrawable = getShadowDrawable();
        LayerDrawable layerDrawable;
        if(getBackground() instanceof  StateListDrawable) {
            StateListDrawable drawable = (StateListDrawable) getBackground();
            drawable.setState(state);
            Drawable cDrawable = drawable.getCurrent();
            cDrawable.setBounds(0, 0 , mFabSize, mFabSize);
            layerDrawable = new LayerDrawable(new Drawable[]{shadowDrawable, cDrawable});
        }
        else {
            layerDrawable = new LayerDrawable(new Drawable[]{shadowDrawable, getBackground()});
        }

        layerDrawable.setLayerInset(1, mShadowSize, mShadowSize, mShadowSize, mShadowSize);

        return layerDrawable;
    }

    private Drawable getShadowDrawable() {
        Drawable tempShadowDrawable;
        int shadowDrawableSize = mFabSize + 2 * mShadowSize;
        if(mFabSize > (int) getResources().getDimension(R.dimen.fab_size_small)) {
            tempShadowDrawable = getResources().getDrawable(R.drawable.shadow);
        } else {
            tempShadowDrawable = getResources().getDrawable(R.drawable.shadow_mini);
        }
        Bitmap b = ((BitmapDrawable)tempShadowDrawable).getBitmap();
        Bitmap bitmapResize = Bitmap.createScaledBitmap(b, shadowDrawableSize, shadowDrawableSize, false);
        BitmapDrawable bitmapDrawable = new BitmapDrawable(getResources(), bitmapResize);
        bitmapDrawable.setGravity(Gravity.CENTER);
        bitmapDrawable.setAntiAlias(true);
        return bitmapDrawable;
    }

    @Override
    protected int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState(extraSpace + 1);
        if (isChecked()) {
            mergeDrawableStates(drawableState, CHECKED_STATE_SET);
        }
        return drawableState;
    }

    public void setFabBackground(Drawable drawable) {
        if(isLollipop()) {
            setBackground(drawable);
            invalidateOutline();
        } else {
            StateListDrawable cDrawable = new StateListDrawable();
            cDrawable.addState(new int[]{android.R.attr.state_pressed}, createDrawable(new int[]{android.R.attr.state_pressed}, drawable));
            cDrawable.addState(new int[]{}, createDrawable(new int[]{}, drawable));
            setMarginsWithoutShadow();
            setBackground(cDrawable);
        }
    }

    public void setFabBackground(int resid) {
        setFabBackground(getResources().getDrawable(resid));
    }

    private Drawable createDrawable(int[] state, Drawable newDrawable) {
        Drawable shadowDrawable = getShadowDrawable();
        LayerDrawable layerDrawable;
        if(newDrawable instanceof  StateListDrawable) {
            StateListDrawable drawable = (StateListDrawable) newDrawable;
            drawable.setState(state);
            Drawable cDrawable = drawable.getCurrent();
            cDrawable.setBounds(0, 0 , mFabSize, mFabSize);
            layerDrawable = new LayerDrawable(new Drawable[]{shadowDrawable, cDrawable});
        }
        else {
            layerDrawable = new LayerDrawable(new Drawable[]{shadowDrawable, newDrawable});
        }

        layerDrawable.setLayerInset(1, mShadowSize, mShadowSize, mShadowSize, mShadowSize);

        return layerDrawable;
    }
}
