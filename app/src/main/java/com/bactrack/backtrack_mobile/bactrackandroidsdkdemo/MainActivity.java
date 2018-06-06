package com.bactrack.backtrack_mobile.bactrackandroidsdkdemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Rect;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;


import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static final byte PERMISSIONS_FOR_SCAN = 100;
    SharedPreferences sharedpreference;


    private static String TAG = "MainActivity";
    final String KEY_SAVED_RADIO_BUTTON_INDEX = "SAVED_RADIO_BUTTON_INDEX";
    final String KEY_SAVED_RADIO_BUTTON_INDEX_C = "SAVED_RADIO_BUTTON_INDEX";

    private Button toApp;
    boolean checked;
    boolean checkedC;
    RadioGroup radioGroup;
    RadioGroup radioC;
    EditText alias;
    EditText purpose;
    EditText age;
    LinearLayout layouti;

    private Intent i1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedpreference= PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());
        radioGroup = (RadioGroup)findViewById(R.id.radioGroupM);
        radioGroup.setOnCheckedChangeListener(radioGroupOnCheckedChangeListener);

        LoadPreferences();


        i1= new Intent(getApplicationContext(), FirstScreen.class);

        toApp=(Button)this.findViewById(R.id.go_to_app);
        alias=(EditText)findViewById(R.id.ali);
        purpose=(EditText)findViewById(R.id.ali2);
        age=(EditText)findViewById(R.id.ag);


        toApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sharedpreference.edit().putString("alias",alias.getText().toString()).apply();
                sharedpreference.edit().putString("wanto",purpose.getText().toString()).apply();
                sharedpreference.edit().putString("age",age.getText().toString()).apply();
                startActivity(i1);
                finish();
            }
        });

    }



    public void onRadioButtonClicked(View view) {
        // Is the button now checked?
        checked = ((RadioButton) view).isChecked();

        // Check which radio button was clicked
        switch(view.getId()) {
            case R.id.man:
                if (checked)
                    // Send Man max drinks
                    sharedpreference.edit().putInt("max",14).apply();


                break;
            case R.id.woman:
                if (checked)
                    // Send Woman max drinks
                    sharedpreference.edit().putInt("max",7).apply();

                break;
        }
    }

    OnCheckedChangeListener radioGroupOnCheckedChangeListener = new OnCheckedChangeListener(){

        @Override
        public void onCheckedChanged(RadioGroup group, int checkedId) {
            RadioButton checkedRadioButton = (RadioButton)radioGroup.findViewById(checkedId);
            int checkedIndex = radioGroup.indexOfChild(checkedRadioButton);
            SavePreferences(KEY_SAVED_RADIO_BUTTON_INDEX, checkedIndex);
        }

       };

    private void SavePreferences(String key, int value){
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt(key, value);
        editor.commit();
    }

    private void LoadPreferences(){
        SharedPreferences sharedPreferences = getSharedPreferences("MY_SHARED_PREF", MODE_PRIVATE);
        if (!sharedPreferences.getBoolean("firstTime", false)) {
            // <---- run your one time code here
            radioGroup.clearCheck();

            // mark first time has runned.
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("firstTime", true);
            editor.commit();
        }
        else{
            int savedRadioIndex = sharedPreferences.getInt(KEY_SAVED_RADIO_BUTTON_INDEX, 0);
            RadioButton savedCheckedRadioButton = (RadioButton)radioGroup.getChildAt(savedRadioIndex);
            savedCheckedRadioButton.setChecked(true);
        }

    }


}
