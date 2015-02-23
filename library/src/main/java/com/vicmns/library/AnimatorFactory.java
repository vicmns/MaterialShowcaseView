package com.vicmns.library;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;

/**
 * Created by vicmns on 1/28/2015.
 */
public class AnimatorFactory {

    private static final String ALPHA = "alpha";
    private static final float VISIBLE = 1f;
    private static final float INVISIBLE = 0f;

    private final AccelerateDecelerateInterpolator interpolator;

    public AnimatorFactory() {
        interpolator = new AccelerateDecelerateInterpolator();
    }

    public void fadeInView(final View target, long duration) {
        fadeInView(target, duration, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                target.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {

            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void fadeInView(View target, long duration, Animator.AnimatorListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, ALPHA, INVISIBLE, VISIBLE);
        animator.setDuration(duration);
        animator.addListener(listener);
        animator.start();
    }

    public void fadeOutView(final View target, long duration) {
        fadeOutView(target, duration, new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                target.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
    }

    public void fadeOutView(View target, long duration, Animator.AnimatorListener listener) {
        ObjectAnimator animator = ObjectAnimator.ofFloat(target, ALPHA, VISIBLE, INVISIBLE);
        animator.setDuration(duration);
        animator.addListener(listener);
        animator.start();
    }

}
