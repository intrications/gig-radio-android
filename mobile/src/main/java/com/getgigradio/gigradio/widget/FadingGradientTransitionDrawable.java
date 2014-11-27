package com.getgigradio.gigradio.widget;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.TransitionDrawable;

public class FadingGradientTransitionDrawable extends TransitionDrawable {

    GradientDrawable currentGradient;

    GradientDrawable newGradient;

    public FadingGradientTransitionDrawable(Drawable[] layers) {
        super(layers);
    }

    public void transitionTo() {
//        set
    }
}

//
//GradientDrawable shadow = new GradientDrawable(GradientDrawable.Orientation
//        .TOP_BOTTOM, new int[]{getResources().getColor(R.color.gradient1a),
//        getResources().getColor(R.color.gradient1b)});
//GradientDrawable shadow2 = new GradientDrawable(GradientDrawable.Orientation
//        .TOP_BOTTOM, new int[]{getResources().getColor(R.color.gradient2a),
//        getResources().getColor(R.color.gradient2b)});
//
//TransitionDrawable transitionDrawable = new TransitionDrawable(new Drawable[]{shadow,
//        shadow2});
//holdr.background.setImageDrawable(transitionDrawable);
//        transitionDrawable.startTransition(500);