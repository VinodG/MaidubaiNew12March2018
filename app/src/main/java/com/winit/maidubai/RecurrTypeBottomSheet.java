package com.winit.maidubai;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.winit.maidubai.utilities.StringUtils;

/**
 * Created by Girish Velivela on 08-07-2016.
 */
public class RecurrTypeBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {

    private String recurrType= "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.recurr_type_bootom_sheet, container, false);
        initialiseControls(view);
        return view;
    }

    public void initialiseControls(final View view) {

        ImageView ivSave = (ImageView) view.findViewById(R.id.ivSave);
        ImageView ivCancel = (ImageView) view.findViewById(R.id.ivCancel);
        final TextView tvThundredMl = (TextView) view.findViewById(R.id.tvThundredMl);
        final TextView tvTwoThundredMl = (TextView) view.findViewById(R.id.tvTwoThundredMl);
        final TextView tvThreeThundredMl = (TextView) view.findViewById(R.id.tvThreeThundredMl);
        final TextView tvFiveThundredMl = (TextView) view.findViewById(R.id.tvFiveThundredMl);
        ivSave.setOnClickListener(this);
        ivCancel.setOnClickListener(this);

        /******* following need change to setOnItemClickListener for Dyanmic*********/
        tvThundredMl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recurrType = "Once in a Week";
                tvThundredMl.setTextSize(30);
                tvTwoThundredMl.setTextSize(17);
                tvThreeThundredMl.setTextSize(17);
                tvFiveThundredMl.setTextSize(17);
            }
        });
        tvTwoThundredMl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recurrType = "Once in two Weeks";
                tvThundredMl.setTextSize(17);
                tvTwoThundredMl.setTextSize(30);
                tvThreeThundredMl.setTextSize(17);
                tvFiveThundredMl.setTextSize(17);
            }
        });
        tvThreeThundredMl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recurrType = "Once in 3 Weeks";
                tvThundredMl.setTextSize(17);
                tvTwoThundredMl.setTextSize(17);
                tvThreeThundredMl.setTextSize(30);
                tvFiveThundredMl.setTextSize(17);
            }
        });
        tvFiveThundredMl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recurrType = "Once in a Month";
                tvThundredMl.setTextSize(17);
                tvTwoThundredMl.setTextSize(17);
                tvThreeThundredMl.setTextSize(17);
                tvFiveThundredMl.setTextSize(30);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ivSave:
                Context context = getActivity();
                if (!StringUtils.isEmpty(recurrType)){
                    dismiss();
                    if(context instanceof OrderDetailActivity)
                        ((OrderDetailActivity) context).selectedRcurrType(recurrType);
                    else if(context instanceof MyOrdersActivity)
                        ((MyOrdersActivity) context).selectedRcurrType(recurrType);
                    else
                        ((SelectAddressActivity) context).selectedRcurrType(recurrType);
                }else
                {
                    if(context instanceof OrderDetailActivity)
                        ((OrderDetailActivity) context).showCustomDialog(context, "", context.getString(R.string.please_Recurr_type), context.getString(R.string.OK), "", "");
                    else if(context instanceof MyOrdersActivity)
                        ((MyOrdersActivity) context).showCustomDialog(context, "", context.getString(R.string.please_Recurr_type), context.getString(R.string.OK), "", "");
                    else
                        ((SelectAddressActivity) context).showCustomDialog(context, "", context.getString(R.string.please_Recurr_type), context.getString(R.string.OK), "", "");
                }

                break;
            case R.id.ivCancel:
                dismiss();
                break;
        }
    }
}
