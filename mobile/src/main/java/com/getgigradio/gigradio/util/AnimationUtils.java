package com.getgigradio.gigradio.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.view.View;

public class AnimationUtils {

    public static void animateToGone(final View view) {
        view.animate().alpha(0f).setDuration(200).setListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                view.setVisibility(View.GONE);
            }
        });
    }

    public static void animateToVisible(final View view) {
        view.setVisibility(View.VISIBLE);
        view.animate().alpha(1f).setDuration(200).setListener(null);
    }

}
