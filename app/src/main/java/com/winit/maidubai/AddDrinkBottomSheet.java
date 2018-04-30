package com.winit.maidubai;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.maidubai.utilities.StringUtils;

/**
 * Created by Girish Velivela on 08-07-2016.
 */
public class AddDrinkBottomSheet extends BottomSheetDialogFragment implements View.OnClickListener {


    private int selectedLitres =100;
    private boolean selType = true;
    private EditText etSelQty;
    private double suggestedDrink, drinkAdded;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_drink_bootom_sheet, container, false);
        Bundle bundle = getArguments();
        suggestedDrink = bundle.getDouble("SuggestedDrink",0);
        drinkAdded = bundle.getDouble("AddedDrink",0);
        initialiseControls(view);
        return view;
    }

    public void initialiseControls(final View view) {

        ImageView ivSave = (ImageView) view.findViewById(R.id.ivSave);
        ImageView ivCancel = (ImageView) view.findViewById(R.id.ivCancel);
        //  final ImageView ivSelect = (ImageView) view.findViewById(R.id.ivSelect);
        final TextView tvSelect = (TextView) view.findViewById(R.id.ivSelect);
        // final ImageView ivCustom = (ImageView) view.findViewById(R.id.ivCustom);
        final TextView tvCustom = (TextView) view.findViewById(R.id.ivCustom);
        final TextView tvSelQty = (TextView) view.findViewById(R.id.tvSelQty);
        final TextView tvThundredMl = (TextView) view.findViewById(R.id.tvThundredMl);
        final LinearLayout llSelect = (LinearLayout) view.findViewById(R.id.llSelect);
        etSelQty = (EditText) view.findViewById(R.id.etSelQty);
        final TextView tvTwoThundredMl = (TextView) view.findViewById(R.id.tvTwoThundredMl);
        final TextView tvThreeThundredMl = (TextView) view.findViewById(R.id.tvThreeThundredMl);
        final TextView tvFiveThundredMl = (TextView) view.findViewById(R.id.tvFiveThundredMl);
        final TextView tvThousandThundredMl = (TextView) view.findViewById(R.id.tvThousandThundredMl);
        final TextView tvSelmesurey = (TextView) view.findViewById(R.id.tvSelmesure);
        final Context context = getActivity();
        ivSave.setOnClickListener(this);
        ivCancel.setOnClickListener(this);
        tvSelect.setOnClickListener(this);
        tvCustom.setOnClickListener(this);

        tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSelect.setBackgroundResource(R.drawable.select);
                tvCustom.setBackgroundResource(R.drawable.custom_unselect);
                selType = true;
                llSelect.setVisibility(View.VISIBLE);
                tvSelQty.setVisibility(View.VISIBLE);
                etSelQty.setVisibility(View.GONE);
                tvSelmesurey.setVisibility(View.GONE);
                tvSelect.setTextColor(context.getResources().getColor(R.color.white));
                tvCustom.setTextColor(context.getResources().getColor(R.color.drak_gray));
            }
        });

        tvCustom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tvSelect.setBackgroundResource(R.drawable.select_unselect);
                tvCustom.setBackgroundResource(R.drawable.custom);
                selType = false;
                llSelect.setVisibility(View.GONE);
                tvSelQty.setVisibility(View.GONE);
                etSelQty.setVisibility(View.VISIBLE);
                tvSelmesurey.setVisibility(View.VISIBLE);
                tvCustom.setTextColor(context.getResources().getColor(R.color.white));
                tvSelect.setTextColor(context.getResources().getColor(R.color.drak_gray));
            }
        });

        /******* following need change to setOnItemClickListener for Dyanmic*********/
        tvThundredMl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLitres = 100;
                tvSelQty.setText(context.getString(R.string.onehundredml));
                tvThundredMl.setTextSize(30);
                tvTwoThundredMl.setTextSize(17);
                tvThreeThundredMl.setTextSize(17);
                tvFiveThundredMl.setTextSize(17);
                tvThousandThundredMl.setTextSize(17);
            }
        });
        tvTwoThundredMl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLitres = 200;
                tvSelQty.setText(context.getString(R.string.twohundredml));
                tvThundredMl.setTextSize(17);
                tvTwoThundredMl.setTextSize(30);
                tvThreeThundredMl.setTextSize(17);
                tvFiveThundredMl.setTextSize(17);
                tvThousandThundredMl.setTextSize(17);
            }
        });
        tvThreeThundredMl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLitres = 330;
                tvSelQty.setText(context.getString(R.string.threethirtydml));
                tvThundredMl.setTextSize(17);
                tvTwoThundredMl.setTextSize(17);
                tvThreeThundredMl.setTextSize(30);
                tvFiveThundredMl.setTextSize(17);
                tvThousandThundredMl.setTextSize(17);
            }
        });
        tvFiveThundredMl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLitres = 500;
                tvSelQty.setText(context.getString(R.string.fivehundredml));
                tvThundredMl.setTextSize(17);
                tvTwoThundredMl.setTextSize(17);
                tvThreeThundredMl.setTextSize(17);
                tvFiveThundredMl.setTextSize(30);
                tvThousandThundredMl.setTextSize(17);
            }
        });
        tvThousandThundredMl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedLitres = 1000;
                tvSelQty.setText(context.getString(R.string.thousandml));
                tvThundredMl.setTextSize(17);
                tvTwoThundredMl.setTextSize(17);
                tvThreeThundredMl.setTextSize(17);
                tvFiveThundredMl.setTextSize(17);
                tvThousandThundredMl.setTextSize(30);
            }
        });
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.ivSave:
                Context context = getActivity();
                int addDrink = 0;
                if(selType)
                    addDrink = selectedLitres;
                else
                    addDrink = StringUtils.getInt(etSelQty.getText().toString().trim());
                if(addDrink != 0) {
//                    if(drinkAdded+addDrink <= suggestedDrink)
                        ((HydrationMeterActivity) context).addDrinkToMeter(addDrink);
                    /*else
                        ((HydrationMeterActivity)context).showCustomDialog(context,"",context.getString(R.string.more_drink_addding),context.getString(R.string.OK),"","");
*/
                }else
                    ((HydrationMeterActivity)context).showCustomDialog(context,"",context.getString(R.string.please_select_drink),context.getString(R.string.OK),"","");
                dismiss();
                break;
            case R.id.ivSelect:
                break;
            case R.id.ivCustom:
                break;
            case R.id.ivCancel:
                dismiss();
                break;
        }
    }
}
