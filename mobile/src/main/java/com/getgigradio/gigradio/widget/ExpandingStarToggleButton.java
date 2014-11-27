package com.getgigradio.gigradio.widget;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.Region;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.animation.LinearInterpolator;
import android.widget.Checkable;
import android.widget.ImageButton;

public class ExpandingStarToggleButton extends ImageButton implements Checkable {

    private boolean isChecked;
    private boolean isBroadCasting;
    private OnCheckedChangeListener onCheckedChangeListener;
    private static final int[] CHECKED_STATE_SET = {
            android.R.attr.state_checked
    };

    private int radius;
    private boolean expanding;
    private boolean contracting;
    private Paint buttonPaint;
    private Paint buttonFillPaint;
    private Paint postPressButtonPaint;
    private float buttonScale = 1.0f;
    private float postPressButtonScale = 0.0f;
    private int postPressButtonAlpha;
    private ObjectAnimator buttonAnimator;
    private ObjectAnimator postPressRippleAnimator;
    private ObjectAnimator postPressRippleAlphaAnimator;

    // TODO Make sure accessibility works - on focus, etc.

    public ExpandingStarToggleButton(Context context) {
        super(context);
        initValues();
    }

    public ExpandingStarToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        initValues();
    }

    public ExpandingStarToggleButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initValues();
    }

    public ExpandingStarToggleButton(Context context, AttributeSet attrs, int defStyleAttr,
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

        buttonFillPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        buttonFillPaint.setStyle(Paint.Style.FILL);
        buttonFillPaint.setColor(Color.YELLOW);
        buttonFillPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2,
                getResources().getDisplayMetrics()));

        postPressButtonPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        postPressButtonPaint.setStyle(Paint.Style.STROKE);
        postPressButtonPaint.setColor(Color.WHITE);
        postPressButtonPaint.setStrokeWidth(TypedValue.applyDimension(TypedValue
                        .COMPLEX_UNIT_DIP, 2,
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

        radius = (int) (w * 0.25f);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // Allow drawing outside bounds
        Rect newRect = canvas.getClipBounds();
        newRect.inset(-1000, -1000);
        canvas.clipRect(newRect, Region.Op.REPLACE);

        postPressButtonPaint.setAlpha(postPressButtonAlpha);
        Path starPath = getStarPath(getWidth() / 2, getHeight() / 2, radius * buttonScale,
                radius * buttonScale * .5f, 5);
        if (isChecked()) {
            canvas.drawPath(starPath, buttonFillPaint);
        } else {
            canvas.drawPath(starPath, buttonPaint);
        }
        if (postPressButtonScale > 0) {
            canvas.drawPath(getStarPath(getWidth() / 2, getHeight() / 2,
                            radius * postPressButtonScale, radius * postPressButtonScale * .5f, 5),
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
                buttonScale, 1.9f);
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

    public Path getStarPath(float x, float y, float radius, float innerRadius, int numOfPt) {

        double section = 2.0 * Math.PI / numOfPt;

        double startPoint = Math.toRadians(-18);

        Path path = new Path();

        path.moveTo(
                (float) (x + radius * Math.cos(startPoint)),
                (float) (y + radius * Math.sin(startPoint)));
        path.lineTo(
                (float) (x + innerRadius * Math.cos(startPoint + section / 2.0)),
                (float) (y + innerRadius * Math.sin(startPoint + section / 2.0)));

        for (int i = 1; i < numOfPt; i++) {
            path.lineTo(
                    (float) (x + radius * Math.cos(startPoint + (section * i))),
                    (float) (y + radius * Math.sin(startPoint + (section * i))));
            path.lineTo(
                    (float) (x + innerRadius * Math.cos(startPoint + (section * i + section / 2.0f))),
                    (float) (y + innerRadius * Math.sin(startPoint + (section * i + section / 2.0f))));
        }

        path.close();
        return path;
    }

    @Override
    public boolean performClick() {
        toggle();
        return super.performClick();
    }

    @Override
    public boolean isChecked() {
        return isChecked;
    }

    @Override
    public void setChecked(boolean checked) {
        if (isChecked == checked) {
            return;
        }
        isChecked = checked;
        refreshDrawableState();
        if (isBroadCasting) {
            return;
        }
        isBroadCasting = true;
        if (onCheckedChangeListener != null) {
            onCheckedChangeListener.onCheckedChanged( this, isChecked );
        }
        isBroadCasting = false;
    }

    @Override
    public int[] onCreateDrawableState(int extraSpace) {
        final int[] drawableState = super.onCreateDrawableState( extraSpace + 1 );
        if (isChecked()) {
            mergeDrawableStates( drawableState, CHECKED_STATE_SET );
        }
        return drawableState;
    }

    public void setOnCheckedChangeListener(OnCheckedChangeListener onCheckedChangeListener) {
        this.onCheckedChangeListener = onCheckedChangeListener;
    }

    @Override
    public void toggle() {
        setChecked( !isChecked );
    }

    @Override
    public Parcelable onSaveInstanceState() {
        Bundle bundle = new Bundle();
        bundle.putParcelable( "instanceState", super.onSaveInstanceState() );
        bundle.putBoolean( "state", isChecked );
        return bundle;
    }

    @Override
    protected void onRestoreInstanceState(Parcelable state) {
        if (state instanceof Bundle) {
            Bundle outState = (Bundle) state;
            setChecked( outState.getBoolean( "state" ) );
            state = outState.getParcelable( "instanceState" );
        }
        super.onRestoreInstanceState( state );
    }

    public static interface OnCheckedChangeListener {

        void onCheckedChanged(Checkable buttonView, boolean isChecked);
    }
}
