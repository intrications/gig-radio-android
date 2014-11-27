package com.getgigradio.gigradio.util;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.widget.ImageButton;

public class ImageButtonUtils {

    public static void setImageButtonFaded(Context ctxt, boolean faded,
                                           ImageButton item, int iconResId) {

        Drawable originalIcon = ctxt.getResources().getDrawable(iconResId);
        Drawable icon = faded ?  convertDrawableToGrayScale(originalIcon) : originalIcon;
        item.setImageDrawable(icon);
    }

    public static Drawable convertDrawableToGrayScale(Drawable drawable) {
        if (drawable == null)
            return null;

        Drawable res = drawable.mutate();
        res.setColorFilter(Color.GRAY, PorterDuff.Mode.SRC_IN);
        return res;
    }

    public static void setImageButtonHighlighted(Context ctxt, boolean highlighted,
                                           ImageButton item, int iconResId) {

        Drawable originalIcon = ctxt.getResources().getDrawable(iconResId);
//        Drawable icon = highlighted ?  convertDrawableToHighlighted(ctxt, originalIcon) : originalIcon;
        originalIcon.setAlpha(highlighted ? 255 : 200);
        item.setImageDrawable(originalIcon);
    }

    public static Drawable convertDrawableToHighlighted(Context ctxt, Drawable drawable) {
        if (drawable == null)
            return null;

        Drawable res = drawable.mutate();
//        res.setColorFilter(ctxt.getResources().getColor(R.color.holo_blue), PorterDuff.Mode.SRC_IN);
        res.setAlpha(0);
        return res;
    }
}
