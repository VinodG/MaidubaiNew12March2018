package com.winit.maidubai;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.view.View;
import android.widget.TextView;
import android.widget.TimePicker;

import com.winit.maidubai.utilities.StringUtils;

import java.util.Calendar;


/**
 * Created by Jayasai on 6/20/2016.
 */
public class PrefferdTimeBtmSheetDlgFrgmnt extends BottomSheetDialogFragment {
    // Context context;

    TimePicker timePicker;
    int i = 0;
    TextView prefferdTimeFrom, prefferdTimeTo, done;
    int hour, min;
    private BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_HIDDEN) {
                dismiss();
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    };

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.prefferd_time_sheet_layout, null);
        dialog.setContentView(contentView);
        timePicker = (TimePicker) contentView.findViewById(R.id.time_picker);
        prefferdTimeFrom = (TextView) contentView.findViewById(R.id.tv_first_time);
        prefferdTimeTo = (TextView) contentView.findViewById(R.id.tv_second_time);
        done = (TextView) contentView.findViewById(R.id.tv_done);
        Calendar calendar = Calendar.getInstance();
        Bundle bundle = getArguments();
        String stratTime = bundle.getString("StratTime","");
        String endTime = bundle.getString("endTime","");
        int crrntHour = calendar.get(Calendar.HOUR_OF_DAY);
        int crrntMin = calendar.get(Calendar.MINUTE);
        timePicker.setCurrentHour(crrntHour);
        timePicker.setCurrentMinute(crrntMin);
        if(!StringUtils.isEmpty(stratTime))
            prefferdTimeFrom.setText(stratTime);
        else
            prefferdTimeFrom.setText("08 : 00 AM");

        if (crrntHour == 12)
            crrntHour += 1;

        if(!StringUtils.isEmpty(endTime))
            prefferdTimeTo.setText(endTime);
        else
            prefferdTimeTo.setText("08 :00 PM");
        prefferdTimeFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 1;
                timePicker.setVisibility(View.VISIBLE);
                prefferdTimeFrom.setBackgroundResource(R.drawable.border);
                // prefferdTimeFrom.setTextSize(18);
                prefferdTimeTo.setBackgroundResource(R.drawable.bordergrey);
            }
        });
        prefferdTimeTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                i = 2;
                timePicker.setVisibility(View.VISIBLE);
                hour = timePicker.getCurrentHour();
                min = timePicker.getCurrentMinute();
                prefferdTimeTo.setText(changeTimeFormate(hour, min));
                timePicker.setVisibility(View.VISIBLE);
                prefferdTimeFrom.setBackgroundResource(R.drawable.bordergrey);
                // prefferdTimeFrom.setTextSize(18);
                prefferdTimeTo.setBackgroundResource(R.drawable.border);
            }
        });
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder setPrefferTime = new StringBuilder().append(prefferdTimeFrom.getText().toString()).append("  to ").append(prefferdTimeTo.getText().toString());
                Context context = getActivity();
                if(context instanceof SettingsActivity)
                    ((SettingsActivity)context).setPrefferdTime(setPrefferTime.toString());
                dismiss();
            }
        });


        timePicker.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {

            @Override
            public void onTimeChanged(TimePicker view, int hourOfDay, int minute) {
                // StringBuilder s;

                if (i == 1) {
                    String s = changeTimeFormate(hourOfDay, minute);
                    prefferdTimeFrom.setText(s);
                } else if (i == 2) {
                    if (hour == hourOfDay) {
                        if (min > minute)
                            timePicker.setCurrentMinute(min);
                    } else {
                        if (hour > hourOfDay) {
                            timePicker.setCurrentHour(hour);
                        }
                    }
                    String s = changeTimeFormate(timePicker.getCurrentHour(), timePicker.getCurrentMinute());
                    prefferdTimeTo.setText(s);

                }

            }
        });

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) contentView.getParent()).getLayoutParams();
        CoordinatorLayout.Behavior behavior = params.getBehavior();

        if (behavior != null && behavior instanceof BottomSheetBehavior) {
            ((BottomSheetBehavior) behavior).setBottomSheetCallback(bottomSheetCallback);
        }
    }

    private String changeTimeFormate(int hour, int min) {
        StringBuilder s;
        if (hour == 0) {
            hour += 12;
            s = new StringBuilder().append(hour).append(" : ").append(min).append(" AM");
        } else if (hour == 12)
            s = new StringBuilder().append(hour).append(" : ").append(min).append(" PM");
        else if (hour <= 12) {
            s = new StringBuilder().append(hour).append(" : ").append(min).append(" AM");
        } else {
            s = new StringBuilder().append(hour - 12).append(" : ").append(min).append(" PM");
        }


        return s.toString();
    }
}

