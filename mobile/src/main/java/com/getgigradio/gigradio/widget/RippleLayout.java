package com.getgigradio.gigradio.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Region;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AccelerateInterpolator;

public class RippleLayout extends View {

    private float mDownX;
    private float mDownY;

    private float mRadius;
//    private int alpha;

    private Paint mPaint;

    public RippleLayout(final Context context) {
        super(context);
        init();
    }

    public RippleLayout(final Context context, final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RippleLayout(final Context context, final AttributeSet attrs, final int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setColor(Color.BLACK);
        mPaint.setAlpha(50);
    }

    @Override
    public boolean onTouchEvent(@NonNull final MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
            mDownX = event.getX();
            mDownY = event.getY();

            ObjectAnimator animator = ObjectAnimator.ofFloat(this, "radius", 0, getWidth() * 6.0f);
            animator.setInterpolator(new AccelerateInterpolator());
            animator.setDuration(2000);
            animator.start();

            ObjectAnimator animator1 = ObjectAnimator.ofInt(this, "alpha", 175, 0);
            animator1.setDuration(800);
            animator1.start();
            return false;
        }
        return false;
    }

    public void setRadius(final float radius) {
        mRadius = radius;
//        if (mRadius > 0) {
//            RadialGradient radialGradient = new RadialGradient(
//                    mDownX,
//                    mDownY,
//                    mRadius * 3,
//                    Color.BLACK,
//                    Color.TRANSPARENT,
//                    Shader.TileMode.MIRROR
//            );
//            mPaint.setShader(radialGradient);
//        }
        invalidate();
    }

    public void setAlpha(int alpha) {
//        this.alpha = alpha;
        mPaint.setAlpha(alpha);
    }

    private Path mPath = new Path();
    private Path mPath2 = new Path();

    @Override
    protected void onDraw(@NonNull final Canvas canvas) {
        super.onDraw(canvas);

        mPath2.reset();
        mPath2.addCircle(mDownX, mDownY, mRadius, Path.Direction.CW);

        canvas.clipPath(mPath2);

        mPath.reset();
        mPath.addCircle(mDownX, mDownY, mRadius / 3, Path.Direction.CW);

        canvas.clipPath(mPath, Region.Op.DIFFERENCE);

        canvas.drawCircle(mDownX, mDownY, mRadius, mPaint);
    }
}