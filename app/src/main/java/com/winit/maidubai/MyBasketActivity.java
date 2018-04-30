package com.winit.maidubai;

import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.winit.maidubai.adapter.MyBasketListAdapter;
import com.winit.maidubai.businesslayer.CommonBL;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.dataaccesslayer.CartDA;
import com.winit.maidubai.dataaccesslayer.OrderDA;
import com.winit.maidubai.dataobject.CartListDO;
import com.winit.maidubai.dataobject.CartResponseDO;
import com.winit.maidubai.dataobject.CommonResponseDO;
import com.winit.maidubai.dataobject.ProductDO;
import com.winit.maidubai.dataobject.ResponseDO;
import com.winit.maidubai.utilities.NetworkUtil;
import com.winit.maidubai.utilities.StringUtils;
import com.winit.maidubai.webaccessLayer.ServiceMethods;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;

public class MyBasketActivity extends BaseActivity {
    private LinearLayout llMyBasket,llOrderDetailes,llShippingDetails,llSubTotal;
    private ListView lvBasketItem;
    private TextView tvTotal, tvShipping, tvPayable,tvTotalHeader, tvShippingHeader, tvPayableHeader;
    private Button btnPlaceOrder,btnBack;
    private double totalRate = 0.0 ;
   private  BigDecimal totalVatAmount=null;
    private int orderType;
    private MyBasketListAdapter myBasketListAdapter;
    private CartListDO tempCartDO;
    private ArrayList<CartListDO> arrCartListDOs;
    private TextView tvSubTotal,tvVatTotal;
    HashMap<String,Integer> hmMaxQty = new HashMap<String,Integer> ();
    boolean isMaxQtyExceeded =false;
    int orderqty;


    @Override
    public void initialise() {
        llMyBasket = (LinearLayout) inflater.inflate(R.layout.my_basket, null);
        llBody.addView(llMyBasket,LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        setTypeFaceMedium(llMyBasket);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(R.string.my_basket);
        tvCancel.setVisibility(View.GONE);
        setStatusBarColor();
        lockDrawer("MyBasketActivity");
        tvCancel.setText(R.string.clear);
        if(getIntent().hasExtra("order_from")) {
            orderType = getIntent().getIntExtra("order_from",0);
        }
        // llBack.setVisibility(View.VISIBLE);
        initialiseControls();
        loadData();
    }

    @Override
    public void initialiseControls() {
        lvBasketItem = (ListView) llMyBasket.findViewById(R.id.lvBasketItem);
        btnBack = (Button) llMyBasket.findViewById(R.id.btnBack);
        btnPlaceOrder = (Button) llMyBasket.findViewById(R.id.btnPlaceOrder);
        tvTotal = (TextView) llMyBasket.findViewById(R.id.tvTotal);
        llOrderDetailes  = (LinearLayout)findViewById(R.id.llOrderDetailes);
        llSubTotal  = (LinearLayout)findViewById(R.id.llSubTotal);
        llShippingDetails  = (LinearLayout)findViewById(R.id.llShippingDetails);
        tvShipping = (TextView) llMyBasket.findViewById(R.id.tvShipping);
        tvPayable = (TextView) llMyBasket.findViewById(R.id.tvPayable);
        tvTotalHeader = (TextView) llMyBasket.findViewById(R.id.tvTotalHeader);
        tvShippingHeader = (TextView) llMyBasket.findViewById(R.id.tvShippingHeader);
        tvPayableHeader = (TextView) llMyBasket.findViewById(R.id.tvPayableHeader);
        tvSubTotal = (TextView) llMyBasket.findViewById(R.id.tvSubTotal);
        tvVatTotal = (TextView) llMyBasket.findViewById(R.id.tvVatTotal);
        AppConstants.MYBASKET = 2;

        tvTotalHeader.setTypeface(AppConstants.DinproRegular);
        tvShippingHeader.setTypeface(AppConstants.DinproRegular);
        tvPayableHeader.setTypeface(AppConstants.DinproBold);

        llShippingDetails.setVisibility(View.GONE);
        llSubTotal.setVisibility(View.GONE);

        tvTotal.setTypeface(AppConstants.DinproMedium);
        tvVatTotal.setTypeface(AppConstants.DinproMedium);
        tvSubTotal.setTypeface(AppConstants.DinproMedium);
        tvPayable.setTypeface(AppConstants.DinproMedium);
        tvShipping.setTypeface(AppConstants.DinproMedium);
        myBasketListAdapter = new MyBasketListAdapter(this, null);
        lvBasketItem.setAdapter(myBasketListAdapter);

//        for(int i=0;i<AppConstants.items.size();i++){
//            totalRate+=(AppConstants.items.get(i).price)*(AppConstants.items.get(i).btlCount);
//        }

        btnPlaceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              /*  ArrayList<ProductDO> minproductDOs = new CartDA(MyBasketActivity.this).getProductMinimumOrderLimit();
                if(minproductDOs != null && minproductDOs.size()>0)
                {
                    String msg = *//*"ProductName\tMin.Qnty\n"*//*"";
                    for(ProductDO productDO : minproductDOs)
                    {
                        if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar") && !StringUtils.isEmpty(productDO.AltDescription))
                        {
                            msg = msg+getString(R.string.minquantity)+" "+productDO.AltDescription+" : "+productDO.btlCount+"\n";
                        }
                        else
                        {
                            msg = msg+productDO.name+getString(R.string.minquantity)+" : "+productDO.btlCount+"\n";
                        }
                    }
                    showCustomDialog(MyBasketActivity.this,getString(R.string.alert),msg,getString(R.string.OK),"","");
                }
                else
                {
                    Intent intent = new Intent(MyBasketActivity.this, ManageAddressActivity.class);
                    intent.putExtra("ActivityType",AppConstants.SELECT_ADDRESS);
                    startActivityForResult(intent, AppConstants.destoryActivity);
                }*/
                if(checkNetworkConnection()){
//                    if(isMaxQtyExceeded) {
                    if(orderqty > AppConstants.MAX_ORDER_QTY) {
                        showAlert("Order quantity is exceeded its maximum quantity.... ", null);
                    }else {
                        new CommonBL(MyBasketActivity.this, MyBasketActivity.this).validateMinOrderQty(preference.getStringFromPreference(Preference.SESSION_ID, ""));
                    }
                }

            }
        });
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if (AppConstants.items != null && AppConstants.items.size() > 0) {
                Resources resources = getResources();
                showCustomDialog(MyBasketActivity.this, "", resources.getString(R.string.DO_You_want_to_clear_Order), resources.getString(R.string.OK), resources.getString(R.string.cancel), "Confirm");
            }
//            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MyBasketActivity.this, DashBoardActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
    }

    public void confirmToRemove(CartListDO cartListDO){
        tempCartDO = cartListDO;
        showCustomDialog(MyBasketActivity.this, "", getString(R.string.DO_You_want_to_remove_item), getString(R.string.OK), getString(R.string.cancel), "Remove Item");
    }

    @Override
    public void loadData() {

        showLoader("Loading data.....");
        new Thread(new Runnable() {
            @Override
            public void run() {
                arrCartListDOs = new CartDA(MyBasketActivity.this).getArrCartList();
                hmMaxQty = new OrderDA(MyBasketActivity.this).getMaxQuantityForEachProduct();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (NetworkUtil.isNetworkConnectionAvailable(MyBasketActivity.this)) {
                            String sessionId = preference.getStringFromPreference(Preference.SESSION_ID, "");
                            new CommonBL(MyBasketActivity.this, MyBasketActivity.this).getCartList(sessionId);
                        }else
                            hideLoader();
                        updatePrice();
                        if(arrCartListDOs != null && arrCartListDOs.size()>0) {
                            llOrderDetailes.setVisibility(View.VISIBLE);
                            myBasketListAdapter.refresh(arrCartListDOs);
                        }
                    }
                });
            }
        }).start();

    }

    public void updateProductCount(CartListDO cartListDO) {
        if (checkNetworkConnection()) {
            showLoader("Updating...");
            ArrayList<ProductDO> cartProduct = new ArrayList<>();
            ProductDO productDO = new ProductDO();
            productDO.code = cartListDO.productCode;
            productDO.btlCount = cartListDO.quantity;
            productDO.UOM = cartListDO.UOM;
            productDO.VAT = cartListDO.vatPerc;
            productDO.VATAmount = cartListDO.vatAmount;
            cartProduct.add(productDO);
            int id = preference.getIntFromPreference(Preference.CUSTOMER_ID, 0);
            String sessionId = preference.getStringFromPreference(Preference.SESSION_ID, "");
            new CommonBL(MyBasketActivity.this, MyBasketActivity.this).addToCart(cartProduct, cartListDO.shoppingCartId, sessionId, id);
        }
    }

    @Override
    public void onButtonYesClick(String from) {
        if(from.equalsIgnoreCase("Confirm")){
            llOrderDetailes.setVisibility(View.GONE);
            tvCancel.setVisibility(View.GONE);
        }else if(from.equalsIgnoreCase("Remove Item")){
            if(tempCartDO != null) {
                if(checkNetworkConnection())
                {
                    new CommonBL(MyBasketActivity.this,MyBasketActivity.this).removeFromCart(tempCartDO.sessionId,tempCartDO.shoppingCartId,tempCartDO.userId);
                }
                invisibleOrderList();
            }
        }
    }

    @Override
    public void onButtonNoClick(String from) {

    }

    @Override
    protected void onResume() {
        invisibleOrderList();
        super.onResume();
    }

    public void invisibleOrderList(){
        if(arrCartListDOs==null||arrCartListDOs.size()<=0) {
            llOrderDetailes.setVisibility(View.GONE);
            tvCancel.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode== AppConstants.destoryActivity && resultCode == RESULT_OK) {
            setResult(RESULT_OK);
            finish();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void updatePrice(){
      /*  if(arrCartListDOs != null) {
            totalRate = 0;
            for (int i = 0; i < arrCartListDOs.size(); i++) {
                totalRate += arrCartListDOs.get(i).totalPrice;
            }
            tvTotal.setText(getResources().getString(R.string.currency) + " " + decimalFormat.format(totalRate));
            tvShipping.setText(getResources().getString(R.string.currency) + " " + 0.0);
            tvPayable.setText(getResources().getString(R.string.currency) + " " + decimalFormat.format(totalRate));
        }*/
        isMaxQtyExceeded =false;
      double vatAmont = 0;
          orderqty = 0;;
        if(arrCartListDOs != null) {
            totalRate = 0;
            totalVatAmount = new BigDecimal("0.0");
            try {
                for (int i = 0; i < arrCartListDOs.size(); i++) {
//                arrCartListDOs.get(i).vatAmount = (arrCartListDOs.get(i).totalPrice*(100+arrCartListDOs.get(i).vatPerc))/100;
                    totalRate += arrCartListDOs.get(i).totalPrice;
//                    double vatAmt = (arrCartListDOs.get(i).unitPrice * arrCartListDOs.get(i).vatPerc *arrCartListDOs.get(i).quantity)/(  100+arrCartListDOs.get(i).vatPerc);
                    double vatAmt = (arrCartListDOs.get(i).unitPrice * arrCartListDOs.get(i).vatPerc *arrCartListDOs.get(i).quantity)/(  100 );
                    vatAmont =vatAmont+vatAmt;
                    arrCartListDOs.get(i).vatAmount = vatAmt;//(arrCartListDOs.get(i).unitPrice * arrCartListDOs.get(i).vatPerc )/(  100+arrCartListDOs.get(i).vatPerc);
//                    totalVatAmount = totalVatAmount.add(getVatAmount(arrCartListDOs.get(i).unitPrice+"",arrCartListDOs.get(i).vatPerc  ));
//                totalRate += calculateAmt(arrCartListDOs.get(i).quantity,  (float) arrCartListDOs.get(i).unitPrice ,arrCartListDOs.get(i).vatPerc);
                    orderqty =orderqty +  arrCartListDOs.get(i).quantity;
//  below code is for exceeding qty more than its maximum value
        // Integer maxQty = hmMaxQty.get(arrCartListDOs.get(i).productCode);
//                    if(maxQty !=null && arrCartListDOs.get(i).quantity > maxQty )
//                        isMaxQtyExceeded =    true ;
                }
                tvTotal.setText(getResources().getString(R.string.currency) + " " + amountFormat.format(totalRate));
                tvVatTotal.setText(getResources().getString(R.string.currency) + " " + amountFormat.format(vatAmont));
                tvSubTotal.setText(getResources().getString(R.string.currency) + " " + amountFormat.format(totalRate));
                tvShipping.setText(getResources().getString(R.string.currency) + " " + 0.0);
                tvPayable.setText(getResources().getString(R.string.currency) + " " + amountFormat.format(totalRate+vatAmont));
            }catch (Exception e)
            {
                e.printStackTrace();
            }

        }
    }

    @Override
    public void dataRetrieved(ResponseDO response) {
        hideLoader();
        if (response.method != null && response.method == ServiceMethods.WS_VALIDATE_MIN_ORDERS ) {
            if (response.data != null && response.data instanceof ArrayList) {
                ArrayList<ProductDO> minproductDOs = (ArrayList<ProductDO>)response.data;
                if(minproductDOs != null && minproductDOs.size()>0)
                {
                    String msg = "";
                    for(ProductDO productDO : minproductDOs)
                    {
                        if(preference.getStringFromPreference(Preference.LANGUAGE,"").equalsIgnoreCase("ar") && !StringUtils.isEmpty(productDO.AltDescription))
                        {
                            msg = msg+getString(R.string.minquantity)+" "+productDO.AltDescription+" : "+productDO.btlCount+"\n";
                        }
                        else
                        {
                            msg = msg+productDO.name+getString(R.string.minquantity)+" : "+productDO.btlCount+"\n";
                        }
                    }
                    showCustomDialog(MyBasketActivity.this,getString(R.string.alert),msg,getString(R.string.OK),"","");
                }
            }else{
                Intent intent = new Intent(MyBasketActivity.this, ManageAddressActivity.class);
                intent.putExtra("ActivityType",AppConstants.SELECT_ADDRESS);
                startActivityForResult(intent, AppConstants.destoryActivity);
            }
        }else if (response.method != null && (response.method == ServiceMethods.WS_CART_LIST || response.method == ServiceMethods.WS_ADD_TO_CART)) {
            if (response.data != null && response.data instanceof CartResponseDO) {
                arrCartListDOs = ((CartResponseDO)response.data).cartListDOs;
                new CartDA(MyBasketActivity.this).deleteProduct("");
                if(arrCartListDOs != null && arrCartListDOs.size() > 0) {
                    // needed not be wait untill data inserted into the database.
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            CartDA cartDA = new CartDA(MyBasketActivity.this);
                            cartDA.insertCartList(arrCartListDOs);
                            arrCartListDOs = new CartDA(MyBasketActivity.this).getArrCartList();

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    updatePrice();
                                    llOrderDetailes.setVisibility(View.VISIBLE);
                                    myBasketListAdapter.refresh(arrCartListDOs);
                                }
                            });
                        }
                    }).start();

                }
            }
        }else if(response.method!=null && response.method == ServiceMethods.WS_REMOVE_FROM_CART && response.data!=null && response.data instanceof CommonResponseDO){
            hideLoader();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    if(tempCartDO != null)
                    {
                        new CartDA(MyBasketActivity.this).deleteProduct(tempCartDO.productCode);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            arrCartListDOs.remove(tempCartDO);
                            updatePrice();
                            myBasketListAdapter.refresh(arrCartListDOs);
                            invisibleOrderList();
                        }
                    });
                }
            }).start();
        }

        super.dataRetrieved(response);
    }
    private BigDecimal getVatAmount(String  amount, int vat)
    {
        if(!TextUtils.isEmpty(amount)) {
            BigDecimal totalAmount  = new BigDecimal(amount);
            BigDecimal unitPer = totalAmount.divide(new BigDecimal(100+vat+""));
            return  unitPer.multiply(unitPer )  ;

        }
        else
            return  new BigDecimal("0.0");

    }
}
