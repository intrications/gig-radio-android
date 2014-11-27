package com.getgigradio.gigradio.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Build;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.ImageButton;

public class ExpandingCircleButton extends ImageButton {

    private int radius;
    private boolean expanding;
    private boolean contracting;
    private Paint buttonPaint;
    private Paint postPressButtonPaint;
    private float buttonScale = 1.0f;
    private float postPressButtonScale = 0.0f;
    private int postPressButtonAlpha;
    private ObjectAnimator buttonAnimator;
    private ObjectAnimator postPressRippleAnimator;
    private ObjectAnimator postPressRippleAlphaAnimator;

    // TODO Make sure accessibility works - on focus, etc.

    public ExpandingCircleButton(Context context) {
        super(context);
        initValues();
    }

    public ExpandingCircleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initValues();
    }

    public ExpandingCircleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initValues();
    }

    public ExpandingCircleButton(Context context, AttributeSet attrs, int defStyleAttr,
                                 int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initValues();
    }

    private void initValues() {
        buttonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonPaint.setStyle(Paint.Style.STROKE);
        buttonPaint.setColor(Color.WHITE);
        buttonPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                getResources().getDisplayMetrics()));

        postPressButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        postPressButtonPaint.setStyle(Paint.Style.STROKE);
        postPressButtonPaint.setColor(Color.WHITE);
        postPressButtonPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue
                        .COMPLEX_UNIT_DIP, 1,
                getResources().getDisplayMetrics()));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            setBackground(null);
        } else {
            setBackgroundDrawable(null);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        radius = (int) (w * 0.5f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Allow drawing outside bounds
        Rect newRect = canvas.getClipBounds();
        newRect.inset(-1000, -1000);
        canvas.clipRect(newRect, Region.Op.REPLACE);

        postPressButtonPaint.setAlpha(postPressButtonAlpha);

        canvas.drawCircle(getWidth() / 2, getWidth() / 2, radius * buttonScale, buttonPaint);
        if (postPressButtonScale > 0) {
            canvas.drawCircle(getWidth() / 2, getWidth() / 2, radius * postPressButtonScale,
                    postPressButtonPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getActionMasked() == MotionEvent.ACTION_DOWN && expanding == false) {
            expanding = true;
            if (buttonAnimator != null) {
                buttonAnimator.cancel();
            }
            buttonAnimator = ObjectAnimator.ofFloat(this, "buttonScale", buttonScale, 1.2f);
            buttonAnimator.setDuration(200);
            buttonAnimator.setInterpolator(new LinearInterpolator());
            buttonAnimator.addListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    super.onAnimationEnd(animation);
                    expanding = false;
                }
            });
            buttonAnimator.start();
        } else if (event.getActionMasked() == MotionEvent.ACTION_UP && contracting == false) {
            contractButton();
            showPostPressRipple();
//        } else if (event.getActionMasked() == MotionEvent.ACTION_CANCEL && contracting == false) {
//            contractButton();
        }
        return super.onTouchEvent(event);
    }

    private void showPostPressRipple() {
        if (postPressRippleAnimator != null) {
            postPressRippleAnimator.cancel();
            postPressRippleAlphaAnimator.cancel();
        }
        postPressRippleAnimator = ObjectAnimator.ofFloat(this, "postPressButtonScale",
                buttonScale, 1.7f);
        postPressRippleAnimator.setDuration(500);
        postPressRippleAnimator.setInterpolator(new LinearInterpolator());
        postPressRippleAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                postPressButtonScale = 0f;
            }
        });
        postPressRippleAnimator.start();
        postPressRippleAlphaAnimator = ObjectAnimator.ofInt(this, "postPressButtonAlpha", 255, 0);
        postPressRippleAlphaAnimator.setDuration(500);
        postPressRippleAlphaAnimator.setInterpolator(new LinearInterpolator());
        postPressRippleAlphaAnimator.start();
    }

    private void contractButton() {
        contracting = true;
        if (buttonAnimator != null) {
            buttonAnimator.cancel();
        }
        buttonAnimator = ObjectAnimator.ofFloat(this, "buttonScale", buttonScale, 1.0f);
        buttonAnimator.setDuration(200);
        buttonAnimator.setInterpolator(new LinearInterpolator());
        buttonAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                contracting = false;
            }
        });
        buttonAnimator.start();
    }

    protected void setButtonScale(float buttonScale) {
        this.buttonScale = buttonScale;
        invalidate();
    }

    public void setPostPressButtonScale(float postPressButtonScale) {
        this.postPressButtonScale = postPressButtonScale;
        invalidate();
    }

    public void setPostPressButtonAlpha(int alpha) {
        this.postPressButtonAlpha = alpha;
    }
}
