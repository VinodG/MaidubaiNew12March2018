package com.winit.maidubai;

import android.content.Intent;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.Preference;
import com.winit.maidubai.utilities.LogUtils;

import java.util.Locale;

public class LanguageSelectionActivity extends BaseActivity {
    private LinearLayout llMain;
    private Button btnEnglish,btnArabic;
    private ImageView ivNext;
    private TextView tvSelectLang;
    String selectedLanguage="en";

    @Override
    public void initialise() {
        LogUtils.debug(LogUtils.LOG_TAG,"LanguageSelectionActivity Else");
        llMain = (LinearLayout) inflater.inflate(R.layout.language_sel_layout, null);
        llBody.addView(llMain, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        setTypeFaceNormal(llBody);
        toolbar.setVisibility(View.GONE);
        lockDrawer("LoginActivity");
        initialiseControls();

        selectedLanguage = preference.getStringFromPreference(Preference.LANGUAGE,"en");
        if(selectedLanguage.equalsIgnoreCase("en")){
            btnEnglish.setBackgroundResource(R.drawable.langbtn);
            btnArabic.setBackgroundResource(0);
            btnEnglish.setTextColor(getResources().getColor(R.color.white));
            btnArabic.setTextColor(getResources().getColor(R.color.brown3));
        }else{
            btnArabic.setBackgroundResource(R.drawable.langbtn);
            btnEnglish.setBackgroundResource(0);
            btnArabic.setTextColor(getResources().getColor(R.color.white));
            btnEnglish.setTextColor(getResources().getColor(R.color.brown3));
        }

    }

    @Override
    public void initialiseControls() {
        btnEnglish= (Button)llMain.findViewById(R.id.btnEnglish);
        btnArabic= (Button)llMain.findViewById(R.id.btnArabic);
        ivNext= (ImageView)llMain.findViewById(R.id.ivNext);
        tvSelectLang= (TextView)llMain.findViewById(R.id.tvSelectLang);
        btnEnglish.setTypeface(AppConstants.DinproBold);
        btnArabic.setTypeface(AppConstants.DinproBold);
        tvSelectLang.setTypeface(AppConstants.DinproBold);

        btnEnglish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnEnglish.setBackgroundResource(R.drawable.langbtn);
                btnArabic.setBackgroundResource(0);
                btnEnglish.setTextColor(getResources().getColor(R.color.white));
                btnArabic.setTextColor(getResources().getColor(R.color.brown3));

                selectedLanguage = "en";

            }
        });
        btnArabic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btnArabic.setBackgroundResource(R.drawable.langbtn);
                btnEnglish.setBackgroundResource(0);
                btnArabic.setTextColor(getResources().getColor(R.color.white));
                btnEnglish.setTextColor(getResources().getColor(R.color.brown3));

                selectedLanguage = "ar";
            }
        });

        ivNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setLanguage(selectedLanguage);
                Intent intent = new Intent(LanguageSelectionActivity.this,LoginActivity.class);
                startActivity(intent);
                //preference.saveBooleanInPreference(Preference.FIRST_TIME_DISPLAY,true);
                finish();
            }
        });
    }

    @Override
    public void loadData() {
    }

    private void setLanguage(String language) {

        preference.saveStringInPreference(Preference.LANGUAGE, language);

        Locale locale = new Locale(language);
        Locale.setDefault(locale);
        Configuration configuration = new Configuration();
        configuration.locale = locale;
        resources.updateConfiguration(configuration, getBaseContext().getResources().getDisplayMetrics());
    }
}
