package com.winit.maidubai;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.dataobject.ProductDO;
import com.winit.maidubai.utilities.StringUtils;

import java.text.DecimalFormat;

public class OrderQuantityBottomSheet extends BottomSheetDialogFragment {
    private RadioGroup radioGroupQty;
    LinearLayout llTotal;
    private RadioButton btnRadioQty;
    private TextView tvDone, tvTotal, tvQty, tvPrice,tvSelectQty,tvAED,tvMinQty;
    private EditText etBtlCount;
    private int btlCount,position;
    private ProductDO productDO;
    private boolean isPresent;
    private Bundle bundle;
    private boolean isRadioFalg = true,isEditFalg = true;
    private DecimalFormat amountFormat;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.order_quantity_bottom_sheet, container, false);
        initializeControls(view);
        return view;
    }

    private void initializeControls(final View view) {


        radioGroupQty = (RadioGroup) view.findViewById(R.id.radioGroupQty);
        tvDone = (TextView) view.findViewById(R.id.tvDone);
        tvTotal = (TextView) view.findViewById(R.id.tvTotal);
        tvSelectQty = (TextView) view.findViewById(R.id.tvSelectQty);
        tvQty = (TextView) view.findViewById(R.id.tvQty);
        tvPrice = (TextView) view.findViewById(R.id.tvPrice);
        tvAED = (TextView) view.findViewById(R.id.tvAED);
        tvMinQty = (TextView) view.findViewById(R.id.tvMinQty);
        etBtlCount =(EditText)view.findViewById(R.id.etBtlCount);
        llTotal=(LinearLayout)view.findViewById(R.id.llTotal);
        tvDone.setTypeface(AppConstants.DinproMedium);
        ((BaseActivity)getActivity()).setTypeFaceMedium(llTotal);

        bundle=this.getArguments();
        position=bundle.getInt("position");
        productDO = (ProductDO) bundle.getSerializable("item");
        tvQty.setText(String.valueOf(productDO.btlCount*0));
        tvPrice.setText(""+ productDO.price);
        this.amountFormat = ((BaseActivity) getContext()).amountFormat;
//        tvTotal.setText("" +  Math.round((float)((0* productDO.btlCount * productDO.price)*100.0))/100.0);
        tvTotal.setText("" +  Math.round((double)((0* productDO.btlCount * productDO.price)*100.0))/100.0);
        tvMinQty.setText(getResources().getString(R.string.min_order_qty)+" : "+productDO.minquantity);
        btnRadioQty= (RadioButton) view.findViewById(R.id.rad1);
        etBtlCount.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int iqty = StringUtils.getInt(s.toString());
                if (isEditFalg) {
                    isRadioFalg = false;
                    if (iqty != 0 && iqty <= 6)
                        ((RadioButton) radioGroupQty.getChildAt(2 * (iqty - 1))).setChecked(true);
                    else {
                        int checkedId = radioGroupQty.getCheckedRadioButtonId();
                        RadioButton btnRadioQty = (RadioButton) view.findViewById(checkedId);
                        if (btnRadioQty != null)
                            btnRadioQty.setChecked(false);
                        radioGroupQty.clearCheck();
                    }
                    tvQty.setText("" + iqty);
                    String qty = "" + Math.round((float) ((iqty * productDO.price) * 100.0)) / 100.0;
//                    String qty =  calculateAmount(iqty,  productDO.price ,productDO.VAT);
                    if (qty.length() > 5) {
                        tvTotal.setTextSize(20);
                        tvAED.setTextSize(20);
                    } else {
                        tvTotal.setTextSize(26);
                        tvAED.setTextSize(26);
                    }
                    productDO.VATAmount =calculateVATAmount(iqty*productDO.price,productDO.VAT)  ;
                    tvTotal.setText("" + Math.round((float) ((iqty * productDO.price) * 100.0)) / 100.0);
//                    tvTotal.setText(calculateAmount(iqty,  productDO.price ,productDO.VAT));
                    isRadioFalg = true;
                }
            }
        });

        radioGroupQty.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (isRadioFalg) {
                    isEditFalg = false;
                    btnRadioQty = (RadioButton) view.findViewById(checkedId);
                    if (btnRadioQty != null) {
                        btnRadioQty.setChecked(true);
                        String sqty = btnRadioQty.getText().toString();
                        int iqty = Integer.parseInt(sqty);
                        tvQty.setText("" + sqty);
                        etBtlCount.setText("" + sqty);
                        tvTotal.setText("" + Math.round((float) ((iqty * productDO.price) * 100.0)) / 100.0);
                        productDO.VATAmount =calculateVATAmount(iqty*productDO.price,productDO.VAT)  ;
//                        tvTotal.setText( calculateAmount(iqty,  productDO.price ,productDO.VAT));
                    }
                    isEditFalg = true;
                }
            }
        });
        tvDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Context context = getActivity();
                if (((BaseActivity) context).checkNetworkConnection()) {
                    if (!etBtlCount.getText().toString().equals(""))
                        btlCount = Integer.parseInt(etBtlCount.getText().toString());
                    else
                        btlCount = 0;

                    if (btlCount == 0) {
                        dismiss();
                    } else {
                        productDO.btlCount = btlCount;
                        ((DashBoardActivity) context).startVan(position, productDO);
                        dismiss();
                    }
                }else
                    dismiss();
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        dismiss();
    }
    private String calculateAmount(int qty , float amount , double vat)
    {
        String str = "" +  amountFormat.format (((qty * amount) * (100+vat)) /100);
        return str;
    }
    private double calculateVATAmount(double amount , int vatPercentage)
    {
        if (amount ==0)
        {
            return  0;
        }
        else
            return  (amount*vatPercentage)/(100+vatPercentage);

    }
}
