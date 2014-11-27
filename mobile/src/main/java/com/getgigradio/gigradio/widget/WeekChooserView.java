package com.getgigradio.gigradio.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.getgigradio.gigradio.R;
import com.getgigradio.gigradio.util.ImageButtonUtils;

import net.danlew.android.joda.DateUtils;

import org.joda.time.DateTime;
import org.joda.time.DateTimeConstants;

public class WeekChooserView extends LinearLayout implements View.OnClickListener {

//    private static final DateTimeFormatter formatter = DateUtils.form

    private final ImageButton previousButton;
    private final ImageButton nextButton;
    private final TextView selectedWeekTextView;

    private DateTime selectedWeek = new DateTime();

    private static final DateTime thisWeek = DateTime.now();

    private OnWeekChosenListener listener;

    public WeekChooserView(Context context) {
        this(context, null);
    }

    public WeekChooserView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WeekChooserView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        LayoutInflater.from(context).inflate(R.layout.view_week_picker, this);
        previousButton = (ImageButton) findViewById(R.id.wp_previous_button);
        nextButton = (ImageButton) findViewById(R.id.wp_next_button);
        selectedWeekTextView = (TextView) findViewById(R.id.wp_selected_week_text_view);

        previousButton.setOnClickListener(this);
        nextButton.setOnClickListener(this);

        updateViews();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.wp_previous_button:
                selectedWeek = selectedWeek.minusWeeks(1);
                updateViews();
                if (listener != null) {
                    listener.onWeekChosen(selectedWeek);
                }
                break;
            case R.id.wp_next_button:
                selectedWeek = selectedWeek.plusWeeks(1);
                updateViews();
                if (listener != null) {
                    listener.onWeekChosen(selectedWeek);
                }
                break;
        }
    }

    private void updateViews() {

        ImageButtonUtils.setImageButtonFaded(getContext(), false, previousButton, R.drawable.ic_action_back_material_black);
        previousButton.setEnabled(true);

        if ((selectedWeek.weekOfWeekyear()).equals(thisWeek.weekOfWeekyear())) {
            selectedWeekTextView.setText("This week");
            ImageButtonUtils.setImageButtonFaded(getContext(), true, previousButton, R.drawable.ic_action_back_material_black);
            previousButton.setEnabled(false);
        } else if ((selectedWeek.weekOfWeekyear()).equals(thisWeek.plusWeeks(1).weekOfWeekyear())) {
            selectedWeekTextView.setText("Next week");
        } else {
            DateTime mondayOfSelectedWeek = new DateTime(selectedWeek).withDayOfWeek(DateTimeConstants.MONDAY);
            DateTime sundayOfSelectedWeek = mondayOfSelectedWeek.withDayOfWeek(DateTimeConstants.SUNDAY);
            selectedWeekTextView.setText(String.format("%s -\n%s",
                    DateUtils.formatDateTime(getContext(), mondayOfSelectedWeek, DateUtils.FORMAT_SHOW_DATE),
                    DateUtils.formatDateTime(getContext(), sundayOfSelectedWeek, DateUtils.FORMAT_SHOW_DATE)));
        }
    }

    public DateTime getSelectedWeek() {
        return selectedWeek;
    }

    public void setSelectedWeek(DateTime selectedDateTime) {
        // update to today if before today
        if (selectedDateTime.isBeforeNow()) {
            selectedDateTime = DateTime.now();
        }
        this.selectedWeek = selectedDateTime;
        updateViews();
    }

    public void setWeekChosenListener(OnWeekChosenListener listener) {
        this.listener = listener;
    }

    // TOOD save state

    public interface OnWeekChosenListener {

        void onWeekChosen(DateTime selectedWeek);

    }
}
