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
import com.winit.maidubai.adapter.SelectEmiratesAdapter;
import com.winit.maidubai.dataobject.AreaDO;
import com.winit.maidubai.dataobject.Emirates;
import com.winit.maidubai.utilities.StringUtils;

import java.util.ArrayList;

public class SelectEmiratesActivity extends BaseActivity {
    private LinearLayout llSelectAreaActivity,llNoArea;
    private ListView lvSelectArea;
    private ArrayList<Emirates> arrEmirates;
    private TextView tvArea,tvNoArea;
    private EditText etSearch;
    private SelectEmiratesAdapter selectEmiratesAdapter;

    @Override
    public void initialise() {
        llSelectAreaActivity = (LinearLayout) inflater.inflate(R.layout.activity_search_city,null);
        llSelectAreaActivity.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
        llBody.addView(llSelectAreaActivity);
        setTypeFaceNormal(llSelectAreaActivity);
        tvTitle.setVisibility(View.VISIBLE);
        tvCancel.setVisibility(View.VISIBLE);
        setStatusBarColor();
        arrEmirates = (ArrayList)getIntent().getSerializableExtra("Emirates");
        initialiseControls();
        loadData();
        tvTitle.setText(getString(R.string.select_emirates));
        etSearch.setHint(getString(R.string.search_emairates));

    }

    @Override
    public void initialiseControls() {

        lvSelectArea =(ListView)findViewById(R.id.lvSelectCity);
        llNoArea =(LinearLayout)findViewById(R.id.llNoArea);
        tvArea =(TextView)findViewById(R.id.tvArea);
        etSearch   =(EditText)findViewById(R.id.etSearch);
        tvNoArea =(TextView)findViewById(R.id.tvNoArea);
        selectEmiratesAdapter = new SelectEmiratesAdapter(this,null);
        lvSelectArea.setAdapter(selectEmiratesAdapter);
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
                    selectEmiratesAdapter.refresh(arrEmirates);
                } else {
                    ArrayList<Emirates> filter = new ArrayList<>();
                    for (Emirates areaDO : arrEmirates) {
                        if(areaDO.Name.toUpperCase().contains(text))
                            filter.add(areaDO);
                    }
                    if (filter.size() > 0) {
                        lvSelectArea.setVisibility(View.VISIBLE);
                        llNoArea.setVisibility(View.GONE);
                        selectEmiratesAdapter.refresh(filter);
                    } else {
                        tvArea.setVisibility(View.GONE);
                        lvSelectArea.setVisibility(View.GONE);
                        llNoArea.setVisibility(View.VISIBLE);
                        tvNoArea.setText(getString(R.string.no_emirate));
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
                intent.putExtra("Emirate", (Emirates)view.getTag(R.string.area));
                setResult(RESULT_OK, intent);
                finish();
            }
        });

    }

    @Override
    public void loadData() {
        if(arrEmirates!=null && arrEmirates.size()>0) {
            selectEmiratesAdapter.refresh(arrEmirates);
            llNoArea.setVisibility(View.GONE);
        }else{
            etSearch.setEnabled(false);
            tvArea.setVisibility(View.GONE);
            lvSelectArea.setVisibility(View.GONE);
            llNoArea.setVisibility(View.VISIBLE);
            tvNoArea.setText(getString(R.string.no_emirate));
        }
    }

}
