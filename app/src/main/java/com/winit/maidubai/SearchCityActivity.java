package com.winit.maidubai;

import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.winit.maidubai.adapter.CityNameAdapter;
import com.winit.maidubai.common.Preference;

import java.io.Serializable;
import java.util.ArrayList;

public class SearchCityActivity extends BaseActivity {
   private LinearLayout lSearchCityActivity,llNoArea;
    private ListView lvSelectCity;
    private ArrayList<String> arrCityNames;
    private ValueFilter valueFilter;
    CityNameAdapter adapter;
    ArrayList<City> arrCities;
    private EditText etSearch;
    //final  String temp[] = {"38","40", "42", "28","36","33","29","31","34", "37", "40"};

    @Override
    public void initialise() {
        lSearchCityActivity= (LinearLayout) inflater.inflate(R.layout.activity_search_city,null);
        lSearchCityActivity.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT));

        llBody.addView(lSearchCityActivity);
        setTypeFaceNormal(lSearchCityActivity);
        tvTitle.setVisibility(View.VISIBLE);
        tvTitle.setText(getString(R.string.select_city));
        tvCancel.setVisibility(View.VISIBLE);
        setStatusBarColor();
        initialiseControls();
        loadData();
    }

    private class ValueFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();

            if (constraint != null && constraint.length() > 0) {
                ArrayList<String> filterList = new ArrayList<>();
                ArrayList<City> filterCityList = new ArrayList<>();
                for (int i = 0; i < arrCityNames.size(); i++) {
                    if ( (arrCityNames.get(i).toUpperCase() )
                            .contains(constraint.toString().toUpperCase())) {
                        filterCityList.add(arrCities.get(i));
                    }
                }
                results.count = filterCityList.size();
                results.values = filterCityList;
            } else {
                results.count = arrCities.size();
                results.values = arrCities;
            }
            return results;

        }

        @Override
        protected void publishResults(CharSequence constraint,
                                      FilterResults results) {
            ArrayList<City> arrSearchCityNames = (ArrayList<City>) results.values;
            if(arrSearchCityNames != null && arrSearchCityNames.size() >0) {
                lvSelectCity.setVisibility(View.VISIBLE);
                llNoArea.setVisibility(View.GONE);
                adapter.refresh(arrSearchCityNames);
            }else{
                lvSelectCity.setVisibility(View.GONE);
                llNoArea.setVisibility(View.VISIBLE);
            }
        }
    }

    @Override
    public void initialiseControls() {

        lvSelectCity=(ListView)findViewById(R.id.lvSelectCity);
        llNoArea = (LinearLayout)findViewById(R.id.llNoArea);
        etSearch   =(EditText)findViewById(R.id.etSearch);
        llNoArea.setVisibility(View.GONE);
        adapter=new CityNameAdapter(this,null);
        lvSelectCity.setAdapter(adapter);
        valueFilter = new ValueFilter();

        etSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                if (cs != null && cs.length() > 0) {
                    valueFilter.filter(cs);
                }else
                    adapter.refresh(arrCities);
            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
            }

            @Override
            public void afterTextChanged(Editable arg0) {
            }
        });

        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        lvSelectCity.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City cityName = (City)view.getTag(R.string.city_name);
                preference.saveStringInPreference(Preference.CityName,cityName.cityName);
                Intent intent = getIntent();
                intent.putExtra("Latitude",cityName.lat);
                intent.putExtra("Longitude",cityName.longitude);
                intent.putExtra("cityName", cityName.cityName);
                setResult(RESULT_OK,intent );
                finish();
            }
        });

    }

    public class City implements Serializable{
        public String cityName;
        public double lat;
        public double longitude;
    }

    @Override
    public void loadData() {
        String[] cityNames={"Dubai","Abu Dhabi","Sharjah","Al Ain","Ajman","Ras al-Khaimah","Fujairah","Umm al-Quwain","Khor'fakkan","Dibb Al Fujairah"};
        double[] latits = {25.0747167,24.3869146,25.3175322,24.193241,25.4036675,25.7262519,25.2881734,25.5099649,25.3472852,25.5799656};
        double[] longs = {54.9472237,54.279026,55.37043,55.6064367,55.4636674,55.8278473,55.8887785,55.5952646,56.31840,56.216333};
        arrCityNames = new ArrayList<>();
        arrCities = new ArrayList<>();
        for(int i=0; i<cityNames.length;i++){
            City city = new City();
            city.cityName = cityNames[i];
            city.lat = latits[i];
            city.longitude =  longs[i];
            arrCityNames.add(cityNames[i]);
            arrCities.add(city);
        }
        adapter.refresh(arrCities);
    }
}
