package com.winit.maidubai;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.winit.maidubai.adapter.SelectAreaAdapter;
import com.winit.maidubai.dataobject.AreaDO;
import com.winit.maidubai.utilities.StringUtils;

import java.util.ArrayList;

public class SelectAreaActivity extends BaseActivity {
    private LinearLayout llSelectAreaActivity,llNoArea;
    private ListView lvSelectArea;
    private ArrayList<AreaDO> arrAreaDOs;
    private TextView tvArea,tvNoArea;
    private EditText etSearch;
    private SelectAreaAdapter selectAreaAdapter;
    private String type;

    @Override
    public void initialise() {
        llSelectAreaActivity = (LinearLayout) inflater.inflate(R.layout.activity_search_city,null);
        llSelectAreaActivity.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(llSelectAreaActivity);
        setTypeFaceNormal(llSelectAreaActivity);
        tvTitle.setVisibility(View.VISIBLE);
        tvCancel.setVisibility(View.VISIBLE);
        setStatusBarColor();
        arrAreaDOs = (ArrayList)getIntent().getSerializableExtra("areas");
        type = getIntent().getStringExtra("type");
        initialiseControls();
        loadData();
        if(type.equalsIgnoreCase("Area")) {
            tvTitle.setText(getString(R.string.select_area));
            etSearch.setHint(getString(R.string.search_area));
        }else {
            tvTitle.setText(getString(R.string.select_subarea));
            etSearch.setHint(getString(R.string.search_sub_area));
        }

    }

    @Override
    public void initialiseControls() {

        lvSelectArea =(ListView)findViewById(R.id.lvSelectCity);
        llNoArea =(LinearLayout)findViewById(R.id.llNoArea);
        tvArea =(TextView)findViewById(R.id.tvArea);
        etSearch   =(EditText)findViewById(R.id.etSearch);
        tvNoArea =(TextView)findViewById(R.id.tvNoArea);
        selectAreaAdapter = new SelectAreaAdapter(this,null);
        lvSelectArea.setAdapter(selectAreaAdapter);
        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence charSequence, int arg1, int arg2, int arg3) {

            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
                String text = editable.toString().toUpperCase();
                if (StringUtils.isEmpty(text)) {
//                    tvArea.setVisibility(View.VISIBLE);
                    lvSelectArea.setVisibility(View.VISIBLE);
                    llNoArea.setVisibility(View.GONE);
                    selectAreaAdapter.refresh(arrAreaDOs);
                } else {
                    ArrayList<AreaDO> filter = new ArrayList<AreaDO>();
                    for (AreaDO areaDO : arrAreaDOs) {
                        if(areaDO.Name.toUpperCase().contains(text))
                            filter.add(areaDO);
                    }
                    if (filter.size() > 0) {
//                        tvArea.setVisibility(View.VISIBLE);
                        lvSelectArea.setVisibility(View.VISIBLE);
                        llNoArea.setVisibility(View.GONE);
                        selectAreaAdapter.refresh(filter);
                    } else {
                        tvArea.setVisibility(View.GONE);
                        lvSelectArea.setVisibility(View.GONE);
                        llNoArea.setVisibility(View.VISIBLE);
                        if(type.equalsIgnoreCase("Area"))
                            tvNoArea.setText(getString(R.string.no_area));
                        else
                            tvNoArea.setText(getString(R.string.no_subarea));
                    }
                }
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lvSelectArea.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent();
                intent.putExtra("type", type);
                intent.putExtra("Area", (AreaDO)view.getTag(R.string.area));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    public void loadData() {
        if(arrAreaDOs!=null && arrAreaDOs.size()>0) {
            selectAreaAdapter.refresh(arrAreaDOs);
            llNoArea.setVisibility(View.GONE);
        }else{
            etSearch.setEnabled(false);
            tvArea.setVisibility(View.GONE);
            lvSelectArea.setVisibility(View.GONE);
            llNoArea.setVisibility(View.VISIBLE);
            if(type.equalsIgnoreCase("Area"))
                tvNoArea.setText(getString(R.string.no_area));
            else
                tvNoArea.setText(getString(R.string.no_subarea));
        }
    }

}
