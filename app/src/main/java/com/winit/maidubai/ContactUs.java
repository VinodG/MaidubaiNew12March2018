package com.winit.maidubai;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.winit.maidubai.common.AppConstants;

public class ContactUs extends BaseActivity {

    LinearLayout llContactUs, llCustomerCall;
    ImageView ivCall;
    private TextView tvCustService, tvEmail, tvFax, tvOffice;
    private GoogleMap googleMap;

    @Override
    public void initialise() {
        llContactUs = (LinearLayout) inflater.inflate(R.layout.activity_contact_us, null);
        llContactUs.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(llContactUs);
        tvTitle.setVisibility(View.VISIBLE);
        setStatusBarColor();
        setTypeFaceNormal(llContactUs);
        tvTitle.setText(getResources().getString(R.string.contactusttl));
        ivMenu.setVisibility(View.VISIBLE);
        ivMenu.setImageResource(R.drawable.menu_white);
        tvCancel.setVisibility(View.INVISIBLE);
        tvCancel.setClickable(false);
        tvCancel.setText(getResources().getString(R.string.save));

        initialiseControls();
        llCustomerCall.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("MissingPermission")
            @Override
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(Intent.ACTION_CALL);
                    intent.setData(Uri.parse("tel:" + "80062438224"));
                    if (ActivityCompat.checkSelfPermission(ContactUs.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
                    startActivity(intent);
                }catch (android.content.ActivityNotFoundException ex){
                    ex.printStackTrace();
                }
            }
        });
    }

    @Override
    public void initialiseControls() {
        ivCall=(ImageView)findViewById(R.id.ivCall);
        llCustomerCall = (LinearLayout)findViewById(R.id.llCustomerCall);
        tvCustService = (TextView)findViewById(R.id.tvCustService);
        tvEmail = (TextView)findViewById(R.id.tvEmail);
        tvFax = (TextView)findViewById(R.id.tvFax);
        tvOffice = (TextView)findViewById(R.id.tvOffice);
        tvCustService.setTypeface(AppConstants.DinproBold);
        tvEmail.setTypeface(AppConstants.DinproBold);
        tvFax.setTypeface(AppConstants.DinproBold);
        tvOffice.setTypeface(AppConstants.DinproBold);
        setUpMapIfNeeded();
    }
    private void setUpMapIfNeeded() {
        if (googleMap == null) {
            SupportMapFragment mapFragment = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap)) ;
            mapFragment .getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap)
                {
                    LatLng latLng = new LatLng(17.4653, 78.3766);
                    googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                    googleMap.animateCamera(CameraUpdateFactory.zoomTo(10));;
                    MarkerOptions options = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
                    googleMap.addMarker(options);

                }
            });
        }
//        ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.googleMap)).getView().setVisibility(View.GONE);
    }
    private void setUpMarker(double latitude, double longtitude,String Address) {
        if(googleMap != null) {
            LatLng latLng = new LatLng(latitude, longtitude);
            MarkerOptions options = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.marker));
            googleMap.addMarker(options);
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(15));
        }
    }

    @Override
    public void loadData() {

    }
}
