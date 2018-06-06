package com.bactrack.backtrack_mobile.bactrackandroidsdkdemo;




import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TextInputEditText;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.DatePicker;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;


import com.jjoe64.graphview.DefaultLabelFormatter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.stacktips.view.CalendarListener;
import com.stacktips.view.CustomCalendarView;
import com.stacktips.view.DayDecorator;
import com.stacktips.view.DayView;
import com.stacktips.view.utils.CalendarUtils;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.nio.channels.FileChannel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Locale;
import java.util.Random;


import BACtrackAPI.API.BACtrackAPI;
import BACtrackAPI.API.BACtrackAPICallbacks;
import BACtrackAPI.Constants.BACTrackDeviceType;
import BACtrackAPI.Exceptions.BluetoothLENotSupportedException;
import BACtrackAPI.Exceptions.BluetoothNotEnabledException;
import BACtrackAPI.Exceptions.LocationServicesNotEnabledException;
import BACtrackAPI.Mobile.Constants.Errors;

import static com.bactrack.backtrack_mobile.bactrackandroidsdkdemo.DBHandler.DATABASE_NAME;


public class FirstScreen extends AppCompatActivity implements MultiSpinner.OnMultipleItemsSelectedListener{

    private static final byte PERMISSIONS_FOR_SCAN = 100;
    SharedPreferences sharedpreference;
    private CustomCallback callback;



    private static String TAG = "FirstScreen";

    private TextView statusMessageTextView;
    private TextView batteryLevelTextView;

    private BACtrackAPI mAPI;
    private String currentFirmware;
    private Button serialNumberButton;
    private Button useCountButton;
    private Context mContext;



    Button button;

    private DBHandler db;
    private String currentDateTime;
    private String currentMonday;
    private SimpleDateFormat sdf;
    private SimpleDateFormat sdf_graph;



    private LinearLayout layout_FabBeer;
    private LinearLayout layoutFabWine;
    private LinearLayout layoutFabDrink;
    private LinearLayout layoutFabShot;
    private LinearLayout screen;


    //boolean flag to know if main FAB is in open or closed state.
    private boolean fabExpanded = false;
    private FloatingActionButton fab;
    private FloatingActionButton beer;
    private FloatingActionButton wine;
    private FloatingActionButton drink;
    private FloatingActionButton shot;


    private int beerCount;
    private int wineCount;
    private int drinkCount;
    private int shotCount;
    private int bacCount;


    private int isBeer;
    private int isWine;
    private int isDrink;
    private int isShot;

    private String food;
    private String symptom;

    private ImageView count;
    private ImageView cal;
    private ImageView money;
    private TextView weekly_goal;
    private TextView countText;
    private TextView calText;
    private TextView moneyText;
    private TextView bacText;

    private CircleDisplay cd;
    private double units;
    private double unitsW;
    private int max;
    String[] items;

    private boolean submit=false;

    private int maxB;
    private int maxW;
    private int maxD;
    private int maxS;
    private float unidades;
    private int calorias;
    private int dinero;




    private GraphView graph;
    private LineGraphSeries<DataPoint> series;
    private DatePickerDialog.OnDateSetListener myDatePicker;
    private Calendar myCalendar;
    private Calendar currentCalendar;
    private List decorators;

    private CustomCalendarView cale;
    private Button test;
    private TextInputEditText editText;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first_screen);

        sharedpreference= PreferenceManager.getDefaultSharedPreferences(this.getBaseContext());

        Boolean isFirstRun = getSharedPreferences("PREFERENCE", MODE_PRIVATE)
                .getBoolean("isFirstRun", true);

        if (isFirstRun) {
            //show sign up activity
            startActivity(new Intent(FirstScreen.this, MainActivity.class));
            //Toast.makeText(FirstScreen.this, "Run only once", Toast.LENGTH_LONG)
              //      .show();
        }
        getSharedPreferences("PREFERENCE", MODE_PRIVATE).edit()
                .putBoolean("isFirstRun", false).commit();

        ImageView icon=(ImageView)findViewById(R.id.icon);
        ImageButton delete=(ImageButton)findViewById(R.id.delete);
        ImageButton send=(ImageButton)findViewById(R.id.send);

        test=(Button)findViewById(R.id.connect_nearest_button_id);


        TextView change=(TextView)findViewById(R.id.goalito);
        count=(ImageView)findViewById(R.id.imageView);
        cal=(ImageView)findViewById(R.id.imageView2);
        money=(ImageView)findViewById(R.id.imageView3);
        weekly_goal=(TextView)findViewById(R.id.textView10);
        countText=(TextView)findViewById(R.id.textView2);
        calText=(TextView)findViewById(R.id.textView3);
        moneyText=(TextView)findViewById(R.id.textView4);
        bacText=(TextView)findViewById(R.id.textView8);

        fab= (FloatingActionButton)findViewById(R.id.fab);
        beer= (FloatingActionButton)findViewById(R.id.fabBeer);
        wine= (FloatingActionButton)findViewById(R.id.fabwine);
        drink= (FloatingActionButton)findViewById(R.id.fabdrink);
        shot= (FloatingActionButton)findViewById(R.id.fabshot);
        layout_FabBeer = (LinearLayout) this.findViewById(R.id.layoutBeer);
        layoutFabWine = (LinearLayout) this.findViewById(R.id.layoutFabWine);
        layoutFabDrink = (LinearLayout) this.findViewById(R.id.layoutFabDrink);
        layoutFabShot = (LinearLayout) this.findViewById(R.id.layoutFabShot);

        screen= (LinearLayout)this.findViewById(R.id.tab3);

        beerCount=0;
        wineCount=0;
        drinkCount=0;
        shotCount=0;
        bacCount=0;

        isBeer=0;
        isWine=0;
        isDrink=0;
        isShot=0;

        maxB=0;
        maxW=0;
        maxD=0;
        maxS=0;

        unidades=0.0f;
        calorias=0;
        dinero=0;

        food="-";
        symptom="-";



        db = new DBHandler(this);

        //******************  TAB 3  **********************************************


        this.statusMessageTextView = (TextView)this.findViewById(R.id.status_message_text_view_id);
        this.setStatus(R.string.TEXT_DISCONNECTED);




        String apiKey = "8a550de3b6e741bab8c1c6f023b893";

        try {
            mAPI = new BACtrackAPI(this, mCallbacks, apiKey);
            mContext = this;
        } catch (BluetoothLENotSupportedException e) {
            e.printStackTrace();
            this.setStatus(R.string.TEXT_ERR_BLE_NOT_SUPPORTED);
        } catch (BluetoothNotEnabledException e) {
            e.printStackTrace();
            this.setStatus(R.string.TEXT_ERR_BT_NOT_ENABLED);
        } catch (LocationServicesNotEnabledException e) {
            e.printStackTrace();
            this.setStatus(R.string.TEXT_ERR_LOCATIONS_NOT_ENABLED);
        }

        //Ir a la main clickando el icono de la app (DropIt)

        icon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                Intent i1= new Intent(getApplicationContext(), FirstScreen.class);
                startActivity(i1);
            }
        });
/*
        if(statusMessageTextView.getText().toString().substring(8,13).equals("Disco"))
            test.setText("Tap to Connect");
        else if (statusMessageTextView.getText().toString().substring(8,17).equals("Connected"))
            test.setText("Tap to Blow");
        Log.d("status",statusMessageTextView.getText().toString().substring(8,17));
*/
        //Change text when clicked
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                connectNearestClicked(view);
            }
        });


        //*************************************************************************


        //******************  ALL TABS  **********************************************
        //When Screen is clicked, it closes fab.
        //Collapses if main FAB was open already.
        //This gives FAB (Add) open/close behavior
        screen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded == true){
                    closeSubMenusFab();
                }
            }
        });

        //When main Fab (Add) is clicked, it expands if not expanded already.
        //Collapses if main FAB was open already.
        //This gives FAB (Add) open/close behavior
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fabExpanded == true){
                    closeSubMenusFab();
                } else {
                    openSubMenusFab();
                }
            }
        });

        //Only main FAB is visible in the beginning
        closeSubMenusFab();

        startAlarm();




        //******************  TAB 1  **********************************************

        //Get the Maximum units of alcohol depending if it's a man or woman using the app from SharedPreferences
        max = sharedpreference.getInt("max",0);
        weekly_goal.setText("Weekly goal: "+max+" std. alcohol units");

        change.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.goal, (ViewGroup) findViewById(R.id.linear_goal));

                final TextView tvUnits=(TextView)layout.findViewById(R.id.textView21);
                final TextView tvCalories=(TextView)layout.findViewById(R.id.textView14);
                final TextView tvMoney=(TextView)layout.findViewById(R.id.textView15);

                tvUnits.setText(unidades + " standard units");
                tvCalories.setText(calorias + " cal.");
                tvMoney.setText(dinero + " kr.");

                final NumberPicker npBeer = (NumberPicker)layout.findViewById(R.id.cerveza);
                npBeer.setMinValue(0);
                npBeer.setMaxValue(14);
                npBeer.setWrapSelectorWheel(false);
                npBeer.setValue(maxB);
                npBeer.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                        //Display the newly selected number from picker
                        maxB=newVal;
                        unidades= (float) (maxB+maxW*1.4+maxD*0.6+maxS*0.5);
                        calorias= maxB*39+maxW*160+maxD*106+maxS*60;
                        dinero= maxB*40+maxW*50+maxD*100+maxS*30;


                        tvUnits.setText(unidades + " standard units");
                        tvCalories.setText(calorias + " cal.");
                        tvMoney.setText(dinero + " kr.");
                    }
                });
                final NumberPicker npWine = (NumberPicker)layout.findViewById(R.id.vino);
                npWine.setMinValue(0);
                npWine.setMaxValue(14);
                npWine.setWrapSelectorWheel(false);
                npWine.setValue(maxW);
                npWine.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                        //Display the newly selected number from picker
                        maxW=newVal;
                        unidades= (float) (maxB+maxW*1.4+maxD*0.6+maxS*0.5);
                        calorias= maxB*39+maxW*160+maxD*106+maxS*60;
                        dinero= maxB*40+maxW*50+maxD*100+maxS*30;


                        tvUnits.setText(unidades + " standard units");
                        tvCalories.setText(calorias + " cal.");
                        tvMoney.setText(dinero + " kr.");
                    }
                });
                final NumberPicker npDrink = (NumberPicker)layout.findViewById(R.id.copas);
                npDrink.setMinValue(0);
                npDrink.setMaxValue(14);
                npDrink.setWrapSelectorWheel(false);
                npDrink.setValue(maxD);
                npDrink.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                        //Display the newly selected number from picker
                        maxD=newVal;
                        unidades= (float) (maxB+maxW*1.4+maxD*0.6+maxS*0.5);
                        calorias= maxB*39+maxW*160+maxD*106+maxS*60;
                        dinero= maxB*40+maxW*50+maxD*100+maxS*30;


                        tvUnits.setText(unidades + " standard units");
                        tvCalories.setText(calorias + " cal.");
                        tvMoney.setText(dinero + " kr.");
                    }
                });
                final NumberPicker npShot = (NumberPicker)layout.findViewById(R.id.chupitos);
                npShot.setMinValue(0);
                npShot.setMaxValue(14);
                npShot.setWrapSelectorWheel(false);
                npShot.setValue(maxS);
                npShot.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                        //Display the newly selected number from picker

                        maxS=newVal;
                        unidades= (float) (maxB+maxW*1.4+maxD*0.6+maxS*0.5);
                        calorias= maxB*39+maxW*160+maxD*106+maxS*60;
                        dinero= maxB*40+maxW*50+maxD*100+maxS*30;


                        tvUnits.setText(unidades + " standard units");
                        tvCalories.setText(calorias + " cal.");
                        tvMoney.setText(dinero + " kr.");
                    }
                });


                final Builder builder = new Builder(FirstScreen.this)
                        .setView(layout)
                        .setTitle("My weekly personal goal")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Send max. units
                                sharedpreference.edit().putInt("max",Math.round(unidades)).apply();
                                System.out.println("El maximo de unidades era: "+ unidades + " y lo paso como: "+ Math.round(unidades));
                                max = sharedpreference.getInt("max",0);
                                weekly_goal.setText("Weekly goal: "+max+" std. alcohol units");
                                cd.showValue((float) (unitsW*100/max), 100f, true);
                                if((float) (unitsW*100/max) >= 100)
                                    cd.setColor(Color.rgb(139,0,0));
                                else
                                    cd.setColor(Color.rgb(255,214,51));

                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog


                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();


            }
        });

        editText =(TextInputEditText)findViewById(R.id.editText);
        editText.setFocusable(false);
        editText.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {

                editText.setFocusableInTouchMode(true);

                return false;
            }
        });

        //Show Gauge
        cd = (CircleDisplay) findViewById(R.id.circleDisplay);
        cd.setFormatDigits(1);
        cd.setTouchEnabled(false);
        cd.setUnit("%");
        cd.setTextSize(16.0f);
        if((float) (unitsW*100/max) >= 100)
            cd.setColor(Color.rgb(139,0,0));
        else
            cd.setColor(Color.rgb(255,214,51));

        //cd.setCustomText(items); // sets a custom array of text




        items = new String[]{"Perfect - no hangover","Hangover", "Thirsty", "Tired", "Headache", "Dizziness/faintness",
                "Loss of appetite", "Stomach ache", "Nausea", "Heart racing"};

        //Send database
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportDB();
            }
        });

        //Delete the last drink drink
        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                db.deleteDrink(db.getAllDrinks().size());
                Toast.makeText(FirstScreen.this, "Last drink was deleted", Toast.LENGTH_LONG).show();
                updateTotals();
                cd.showValue((float) (unitsW*100/max), 100f, true);
                if((float) (unitsW*100/max) >= 100)
                    cd.setColor(Color.rgb(139,0,0));
                else
                    cd.setColor(Color.rgb(255,214,51));

            }
        });

        myCalendar = Calendar.getInstance();
        myDatePicker = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        //***************************************************************************

        //******************  TAB 2  **********************************************

        graph = (GraphView) findViewById(R.id.graph);
        series = new LineGraphSeries<>(generateData());;
        //cale= (CalendarView) findViewById(R.id.calendarView);

        //Initialize CustomCalendarView from layout
        cale = (CustomCalendarView) findViewById(R.id.calendar_view);


        //Initialize calendar with date
        currentCalendar = Calendar.getInstance(Locale.getDefault());

        //Show Monday as first date of week
        cale.setFirstDayOfWeek(Calendar.MONDAY);

        //Show/hide overflow days of a month
        cale.setShowOverflowDate(false);

        //call refreshCalendar to update calendar the view
        cale.refreshCalendar(currentCalendar);

        //Handling custom calendar events
        cale.setCalendarListener(new CalendarListener() {
            @Override
            public void onDateSelected(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                //Toast.makeText(FirstScreen.this, df.format(date), Toast.LENGTH_SHORT).show();
                openyourInfo(df.format(date));
            }

            @Override
            public void onMonthChanged(Date date) {
                SimpleDateFormat df = new SimpleDateFormat("MM-yyyy");
                //Toast.makeText(FirstScreen.this, df.format(date), Toast.LENGTH_SHORT).show();
            }
        });

        //adding calendar day decorators
        decorators = new ArrayList<>();
        decorators.add(new DisabledColorDecorator());
        cale.setDecorators(decorators);
        cale.refreshCalendar(currentCalendar);


        //***************************************************************************
    }

    protected void onStart()
    {
        super.onStart();

        final TextView tv = (TextView)findViewById(R.id.text);
        //tv.setText("this string is set dynamically from java code");

        //Tab 1
        TabHost th= (TabHost)findViewById(R.id.tabHost);
        th.setup();
        TabHost.TabSpec spec = th.newTabSpec("Tab one");
        spec.setContent(R.id.tab1);
        spec.setIndicator("Overview");
        th.addTab(spec);

        //Tab 2
        spec = th.newTabSpec("Tab two");
        spec.setContent(R.id.tab2);
        spec.setIndicator("My Progress");
        th.addTab(spec);

        //Tab 3
        spec = th.newTabSpec("Tab three");
        spec.setContent(R.id.tab3);
        spec.setIndicator("Breath & learn");
        th.addTab(spec);

        /*db.deleteDrink(32);
        db.deleteDrink(31);
        db.deleteDrink(30);
        db.deleteDrink(29);
        db.deleteDrink(28);
        db.deleteDrink(27);
        db.deleteDrink(26);
        db.deleteDrink(25);
        db.deleteDrink(24);
        db.deleteDrink(23);
        db.deleteDrink(22);
        db.deleteDrink(21);
        db.deleteDrink(20);
        db.deleteDrink(19);
        db.deleteDrink(18);
        db.deleteDrink(17);
        db.deleteDrink(16);*/
        //db.deleteDrink(15);

        updateTotals();
        generateData();
        callback = new CustomCallback() {
            public void onCallback() {

                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View layout = inflater.inflate(R.layout.goal, (ViewGroup) findViewById(R.id.linear_goal));

                final TextView tvUnits=(TextView)layout.findViewById(R.id.textView21);
                final TextView tvCalories=(TextView)layout.findViewById(R.id.textView14);
                final TextView tvMoney=(TextView)layout.findViewById(R.id.textView15);

                tvUnits.setText(unidades + " standard units");
                tvCalories.setText(calorias + " cal.");
                tvMoney.setText(dinero + " kr.");

                final NumberPicker npBeer = (NumberPicker)layout.findViewById(R.id.cerveza);
                npBeer.setMinValue(0);
                npBeer.setMaxValue(14);
                npBeer.setWrapSelectorWheel(false);
                npBeer.setValue(maxB);
                npBeer.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                        //Display the newly selected number from picker
                        maxB=newVal;
                        unidades= (float) (maxB+maxW*1.4+maxD*0.6+maxS*0.5);
                        calorias= maxB*39+maxW*160+maxD*106+maxS*60;
                        dinero= maxB*40+maxW*50+maxD*100+maxS*30;


                        tvUnits.setText(unidades + " standard units");
                        tvCalories.setText(calorias + " cal.");
                        tvMoney.setText(dinero + " kr.");
                    }
                });
                final NumberPicker npWine = (NumberPicker)layout.findViewById(R.id.vino);
                npWine.setMinValue(0);
                npWine.setMaxValue(14);
                npWine.setWrapSelectorWheel(false);
                npWine.setValue(maxW);
                npWine.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                        //Display the newly selected number from picker
                        maxW=newVal;
                        unidades= (float) (maxB+maxW*1.4+maxD*0.6+maxS*0.5);
                        calorias= maxB*39+maxW*160+maxD*106+maxS*60;
                        dinero= maxB*40+maxW*50+maxD*100+maxS*30;


                        tvUnits.setText(unidades + " standard units");
                        tvCalories.setText(calorias + " cal.");
                        tvMoney.setText(dinero + " kr.");
                    }
                });
                final NumberPicker npDrink = (NumberPicker)layout.findViewById(R.id.copas);
                npDrink.setMinValue(0);
                npDrink.setMaxValue(14);
                npDrink.setWrapSelectorWheel(false);
                npDrink.setValue(maxD);
                npDrink.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                        //Display the newly selected number from picker
                        maxD=newVal;
                        unidades= (float) (maxB+maxW*1.4+maxD*0.6+maxS*0.5);
                        calorias= maxB*39+maxW*160+maxD*106+maxS*60;
                        dinero= maxB*40+maxW*50+maxD*100+maxS*30;


                        tvUnits.setText(unidades + " standard units");
                        tvCalories.setText(calorias + " cal.");
                        tvMoney.setText(dinero + " kr.");
                    }
                });
                final NumberPicker npShot = (NumberPicker)layout.findViewById(R.id.chupitos);
                npShot.setMinValue(0);
                npShot.setMaxValue(14);
                npShot.setWrapSelectorWheel(false);
                npShot.setValue(maxS);
                npShot.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
                    @Override
                    public void onValueChange(NumberPicker picker, int oldVal, int newVal){
                        //Display the newly selected number from picker

                        maxS=newVal;
                        unidades= (float) (maxB+maxW*1.4+maxD*0.6+maxS*0.5);
                        calorias= maxB*39+maxW*160+maxD*106+maxS*60;
                        dinero= maxB*40+maxW*50+maxD*100+maxS*30;


                        tvUnits.setText(unidades + " standard units");
                        tvCalories.setText(calorias + " cal.");
                        tvMoney.setText(dinero + " kr.");
                    }
                });


                final Builder builder = new Builder(FirstScreen.this)
                        .setView(layout)
                        .setTitle("My weekly personal goal")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // Send max. units
                                sharedpreference.edit().putInt("max",Math.round(unidades)).apply();
                                System.out.println("El maximo de unidades era: "+ unidades + " y lo paso como: "+ Math.round(unidades));
                                max = sharedpreference.getInt("max",0);
                                weekly_goal.setText("Weekly goal: "+max+" std. alcohol units");
                                cd.showValue((float) (unitsW*100/max), 100f, true);
                                if((float) (unitsW*100/max) >= 100)
                                    cd.setColor(Color.rgb(139,0,0));
                                else
                                    cd.setColor(Color.rgb(255,214,51));

                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // User cancelled the dialog


                            }
                        });
                AlertDialog alertDialog = builder.create();
                alertDialog.show();

            }
        };

        cd.setCallback(callback);

        cd.showValue((float) (unitsW*100/max), 100f, true);
        if((float) (unitsW*100/max) >= 100)
            cd.setColor(Color.rgb(139,0,0));
        else
            cd.setColor(Color.rgb(255,214,51));


        th.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            @Override
            public void onTabChanged(String tabId) {
                if("Tab one".equals(tabId)) {
                    fab.show();
                    //Get the Maximum units of alcohol depending if it's a man or woman using the app from SharedPreferences
                    max = sharedpreference.getInt("max",0);
                    weekly_goal.setText("Weekly goal: "+max+" std. alcohol units");
                    //Update number of units, calories and money
                    updateTotals();
                    cd.showValue((float) (unitsW*100/max), 100f, true);
                    if((float) (unitsW*100/max) >= 100)
                        cd.setColor(Color.rgb(139,0,0));
                    else
                        cd.setColor(Color.rgb(255,214,51));

                }
                if("Tab two".equals(tabId)) {

                    closeSubMenusFab();
                    fab.hide();


                    // Making graph
                    makeTheGraph(series);
                    //Update calendar
                    decorators.add(new DisabledColorDecorator());
                    cale.setDecorators(decorators);
                    cale.refreshCalendar(currentCalendar);

                }
                if("Tab three".equals(tabId)) {

                    closeSubMenusFab();
                    fab.hide();

                }

            }});



        MultiSpinner multiSpinner = (MultiSpinner) findViewById(R.id.multi_spinner);
        multiSpinner.setItems(items);
        multiSpinner.setListener(this);



        FrameLayout touchInterceptor = (FrameLayout)findViewById(R.id.touchInterceptor);
        touchInterceptor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    if (editText.isFocused()) {
                        Rect outRect = new Rect();
                        editText.getGlobalVisibleRect(outRect);
                        if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                            editText.clearFocus();
                            InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            food = editText.getText().toString();
                            symptom = "-";
                            new DatePickerDialog(FirstScreen.this, myDatePicker, myCalendar
                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();
                        }
                    }
                }
                return false;
            }
        });


        //**************************** TAB 2 ***************************************************

        // Making graph
        makeTheGraph(series);



        //**************************************************************************************


        //Record a beer
        beer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                beerCount++;
                isBeer=1;
                Log.d("BEER","nº of beers: "+ beerCount);

                clickDrink();
                updateTotals();
                cd.showValue((float) (unitsW*100/max), 100f, true);
                if((float) (unitsW*100/max) >= 100)
                    cd.setColor(Color.rgb(139,0,0));
                else
                    cd.setColor(Color.rgb(255,214,51));

                isBeer=0;

            }
        });

        wine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                wineCount++;
                Log.d("wine","nº of wine: "+ wineCount);
                isWine=1;
                clickDrink();
                updateTotals();
                cd.showValue((float) (unitsW*100/max), 100f, true);
                if((float) (unitsW*100/max) >= 100)
                    cd.setColor(Color.rgb(139,0,0));
                else
                    cd.setColor(Color.rgb(255,214,51));

                isWine=0;
            }
        });
        drink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                drinkCount++;
                Log.d("drink","nº of drinks: "+ drinkCount);
                isDrink=1;
                clickDrink();
                updateTotals();
                cd.showValue((float) (unitsW*100/max), 100f, true);
                if((float) (unitsW*100/max) >= 100)
                    cd.setColor(Color.rgb(139,0,0));
                else
                    cd.setColor(Color.rgb(255,214,51));

                isDrink=0;
            }
        });
        shot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                shotCount++;
                Log.d("shot","nº of shots: "+ shotCount);
                isShot=1;
                clickDrink();
                updateTotals();
                cd.showValue((float) (unitsW*100/max), 100f, true);
                if((float) (unitsW*100/max) >= 100)
                    cd.setColor(Color.rgb(139,0,0));
                else
                    cd.setColor(Color.rgb(255,214,51));

                isShot=0;
            }
        });




    }



    //******************  TAB 1  **********************************************

    public void show() {

        TextView txtView = (TextView)findViewById(R.id.textView9);

        //Toggle
        /*
        if (txtView .getVisibility() == View.VISIBLE)
            txtView.setVisibility(View.INVISIBLE);
        else
            txtView.setVisibility(View.VISIBLE);
        */
        //If you want it only one time
        txtView.setVisibility(View.VISIBLE);

    }

//////////////////////////////////////////////////////////////////////

    //Adds a drink to the DB and Updates the numbers on the main screen
    private void clickDrink()
    {

        // Inserting Drink
        Log.d("Insert: ", "Inserting ..");


        Log.d("date24","Date in 24h fromat: "+currentDateTime);
        //Adding one drink
        db.addDrink(new DrinkEvent(0,currentDateTime, isBeer, isWine, isDrink, isShot, -1));
        Log.d("Add: ", "Drink added");

    }

    private void updateTotals()
    {
        //Calculating the Current Time
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
        currentDateTime = sdf.format(new Date());


        String am_start="08:00:00";
        String am_end="07:59:59";
        String startDate="-";
        String endDate="-";
        String startWeek="-";
        String endWeek="-";
        Calendar c;

        if(currentDateTime.substring(11,18).compareTo("08:00:00")>=0 && currentDateTime.substring(11,18).compareTo("23:59:59")<=0)
        {
            if(currentDateTime.substring(11,18).compareTo("08:00:00")==0)
                db.addDrink(new DrinkEvent(0,currentDateTime, -1, -1, -1, -1, 100));
            ////////// TODAY /////////////
            //Calculating startDate
            startDate= currentDateTime.substring(0, 11) + am_start;
            Log.d("startdate","Start Date: "+ startDate);

            //Calculating endDate
            c=Calendar.getInstance();
            try {
                c.setTime(sdf.parse(currentDateTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.add(Calendar.DATE, 1); //same with c.add(Calendar.DAY_OF_MONTH, 1);
            Date resultDate=new Date(c.getTimeInMillis());
            String datePlusOne = sdf.format(resultDate);
            endDate = datePlusOne.substring(0, 11) + am_end;
            Log.d("enddate","End Date: "+ endDate);

        }
        else if(currentDateTime.substring(11,18).compareTo("00:00:00")>=0 && currentDateTime.substring(11,18).compareTo("07:59:59")<=0)
        {

            ////////// TODAY /////////////
            //Calculating startDate
            c=Calendar.getInstance();
            try {
                c.setTime(sdf.parse(currentDateTime));
            } catch (ParseException e) {
                e.printStackTrace();
            }
            c.add(Calendar.DATE, -1);
            Date resultDate=new Date(c.getTimeInMillis());
            String dateMinusOne = sdf.format(resultDate);
            startDate = dateMinusOne.substring(0, 11) + am_start;
            Log.d("startdate","Start Date: "+ startDate);

            //Calculating endDate
            endDate = currentDateTime.substring(0, 11) + am_end;
            Log.d("enddate","End Date: "+ endDate);
        }

        ////////// WEEK ////////////

        //Calculating startWeek
        c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Date resultDateWeek=new Date(c.getTimeInMillis());
        String dateMonday = sdf.format(resultDateWeek);
        startWeek  = dateMonday.substring(0, 11) + am_start;
        Log.d("startdate","Start Monday: "+ startWeek);

        //Calculating endWeek
        c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dateMonday));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 7);
        Date resultWeekDate=new Date(c.getTimeInMillis());
        String datePlusSeven = sdf.format(resultWeekDate);
        endWeek = datePlusSeven.substring(0, 11) + am_end;
        Log.d("enddate","End Monday: "+ endWeek);



        //Calculating units / calories / money / last BAC
        List<Integer> drinksCount = db.getNumDrinks(startDate,endDate);
        units= drinksCount.get(0)+drinksCount.get(1)*1.4+drinksCount.get(2)*0.6+
                drinksCount.get(3)*0.5;
        int calories= drinksCount.get(0)*39+drinksCount.get(1)*160+drinksCount.get(2)*106+
                drinksCount.get(3)*60;
        int money= drinksCount.get(0)*40+drinksCount.get(1)*50+drinksCount.get(2)*100+
                drinksCount.get(3)*30;
        String bac;
        if(db.getLastBac()==100)
            bac="-";
        else
            bac=String.valueOf(db.getLastBac());

        //Calculating weekly units
        List<Integer> drinksWeekCount = db.getNumDrinks(startWeek,endWeek);
        unitsW= drinksWeekCount.get(0)+drinksWeekCount.get(1)*1.4+drinksWeekCount.get(2)*0.6+
                drinksWeekCount.get(3)*0.5;

        //Setting the previous values in the UI
        countText.setText(String.format("%.2f", units)+ " units");
        calText.setText(String.valueOf(calories)+ " cal.");
        moneyText.setText(String.valueOf(money)+ " kr.");
        bacText.setText(bac+ " %");


        // Reading all drinks
        Log.d("Reading: ", "Reading all drinks..");
        List<DrinkEvent> drinks = db.getAllDrinks();

        for (DrinkEvent drinkEvent : drinks) {
            String log = "Id: " + drinkEvent.getId() + " ,DateTime: " + drinkEvent.getDateTime() + " ,Beer: " + drinkEvent.getNum_beer()
                    + " ,Wine: " + drinkEvent.getNum_wine()+ " ,Drink: " + drinkEvent.getNum_drink()+ " ,Shot: " + drinkEvent.getNum_shot()+ " ,BAC: " + bac;

            // Writing drinks  to log
            Log.d("DRINK ENTRY: : ", log);

        }

        // Reading all food/symptoms
        Log.d("Reading: ", "Reading all food/symptoms..");
        List<BodyEvent> bodies = db.getAllBody();

        for (BodyEvent bodyEvent : bodies) {
            String log = "Id: " + bodyEvent.getId() + " ,DateTime: " + bodyEvent.getDateTime() + " ,Food: " + bodyEvent.getFood() + " ,Symptoms: " + bodyEvent.getSymptom();

            // Writing drinks  to log
            Log.d("BODY ENTRY: : ", log);

        }

    }


    private void clickBody(String currentDateTime,String food, String symptom)
    {

        // Inserting Body
        Log.d("Insert: ", "Inserting food/symptom..");
        //Adding one body
        db.addBody(new BodyEvent(0,currentDateTime, food, symptom));
        Log.d("Add: ", "Food/symptom added");

    }

    public void exportDB()
    {
        boolean toast=false;
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        File sd = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        File data = Environment.getDataDirectory();
        FileChannel source=null;
        FileChannel destination=null;
        String currentDBPath = data + "/"+ "com.bactrack.backtrack_mobile.bactrackandroidsdkdemo" +"/databases/"+DATABASE_NAME;
        String backupDBPath = DATABASE_NAME;
        File currentDB = new File(data,currentDBPath);
        File backupDB = new File(sd, backupDBPath);
        try {
            source = new FileInputStream(currentDB).getChannel();
            destination = new FileOutputStream(backupDB).getChannel();
            destination.transferFrom(source, 0, source.size());
            source.close();
            destination.close();
            toast=true;
            Toast.makeText(FirstScreen.this, "Please hold a sec ...", Toast.LENGTH_LONG).show();

        } catch(IOException e) {
            e.printStackTrace();
        }
        if(!toast)
        {
            Toast.makeText(FirstScreen.this, "Please go to Settings /Apps /App Permissions/Storage, search for DropIt and allow storage from the app.", Toast.LENGTH_LONG).show();
        }

        Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("*/*");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL,
                new String[] { "s161632@student.dtu.dk" });

        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,
                "DataDropIt_"
                        + sharedpreference.getString("alias",null)+"_"
                        +sharedpreference.getString("age",null)+"_"
                        +max+"_"
                        +sharedpreference.getString("wanto",null));

        emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(backupDB));
        FirstScreen.this.startActivityForResult(Intent.createChooser(emailIntent, "Now send your data, mange tak!"),4);


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i("SS", "onActivityResult: " + requestCode + ", " + resultCode + ", "
                + (data != null ? data.toString() : "empty intent"));
        if(requestCode == 4) {
            if(resultCode == RESULT_OK) {
                //Email sent - that's how it should be
            } else {
                //Now it does this

            }
        }
        //finish(); // to end your activity when toast is shown
    }

    public DataPoint[] generateData()
    {
        Log.d("Calculate: ", "Calculating dates..");
        List<DrinkEvent> drinks = db.getAllDrinks();
        List<String> dates = new ArrayList<>();
        List<Integer> beers = new ArrayList<>();
        List<String> datesSort = new ArrayList<>();
        List<Integer> beersSort = new ArrayList<>();



        for (DrinkEvent drinkEvent : drinks) {
            String date=drinkEvent.getDateTime().substring(0,10);
            dates.add(date);
            beers.add(drinkEvent.getNum_beer()+drinkEvent.getNum_wine()+ drinkEvent.getNum_drink()+drinkEvent.getNum_shot());
        }
        if(beers.size()!=0 || dates.size()!=0)
        {
            int counter=0;
            beersSort.add(beers.get(0));
            datesSort.add(dates.get(0));

            for(int i=1;i<dates.size();i++)
            {

                if(dates.get(i).equals(datesSort.get(counter)))
                {
                    beersSort.set(counter,beersSort.get(counter)+beers.get(i));
                }
                else
                {
                    counter++;
                    datesSort.add(dates.get(i));
                    beersSort.add(beers.get(i));
                }
            }

            System.out.println("Dates: "+ datesSort);
            System.out.println("Drinks per day: "+ beersSort);
        }
        else
        {
            System.out.println("There is nothing in the database");
        }

        // generate Dates

        DataPoint[] dataPoints= new DataPoint[datesSort.size()];
        sdf_graph = new SimpleDateFormat("EE, dd MMM");
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
        int z;

        for(z=0;z<datesSort.size();z++) {
            Date d1= new Date();
            Log.d("For: ", "Hola estoy dentro del for");
            try {

                String proper=datesSort.get(z)+" 12:00:00";
                Log.d("Punto a mirar: ", "Esta es la fecha del punto: "+proper);
                d1= sdf.parse(proper);
                Log.d("Mi punto: ","Mi X: "+d1+", Mi Y: "+beersSort.get(z));

            } catch (ParseException e) {
                e.printStackTrace();
            }

            DataPoint punto=new DataPoint(d1, beersSort.get(z));
            dataPoints[z]=punto;
        }
        return dataPoints;

    }

    public Date[] generateBounds()
    {
        // set manual X bounds
        Calendar c1=Calendar.getInstance();
        Date[] bounds=new Date[2];
        Date window_start;
        Date window_end;

        c1.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        window_start= c1.getTime();
        c1.add(Calendar.DATE,7);
        window_end=c1.getTime();

        bounds[0]=window_start;
        bounds[1]=window_end;

        return bounds;
    }

    public void makeTheGraph(LineGraphSeries <DataPoint> seri)
    {

        seri.resetData(generateData());

        seri.setDrawDataPoints(true);
        seri.setDataPointsRadius(10);
        seri.setThickness(2);
        seri.setColor(Color.rgb(255,128,128));

        graph.addSeries(seri);

        graph.getGridLabelRenderer().setLabelFormatter(new DefaultLabelFormatter()
        {
            public String formatLabel(double value, boolean isValueX){
                if(isValueX)
                {
                    return sdf_graph.format(new Date((long)value));
                }
                else
                {
                    return super.formatLabel(value,isValueX);
                }

            }
        });
        // activate horizontal zooming and scrolling
        graph.getViewport().setScalable(true);


        //Set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(generateBounds()[0].getTime());
        graph.getViewport().setMaxX(generateBounds()[1].getTime());


        // set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(15);


        //Axis Titles
        graph.setTitle("No. drinks over time");
        graph.getGridLabelRenderer().setVerticalAxisTitle("No. drinks");

        //Number of vertical and horizontal points (labels)
        graph.getGridLabelRenderer().setNumVerticalLabels(6);
        graph.getGridLabelRenderer().setNumHorizontalLabels(3);

        graph.getGridLabelRenderer().setHumanRounding(false);

    }

    public void openyourInfo(String da)
    {
        sdf_graph = new SimpleDateFormat("EE, dd MMM");
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View layoutInfo = inflater.inflate(R.layout.your_info, (ViewGroup) findViewById(R.id.linear_info));
        final TextView tvDate=(TextView)layoutInfo.findViewById(R.id.textView24);
        final TextView tvFood=(TextView)layoutInfo.findViewById(R.id.textView25);
        final TextView tvSymptom=(TextView)layoutInfo.findViewById(R.id.textView32);
        final TextView tvBeers=(TextView)layoutInfo.findViewById(R.id.textView27);
        final TextView tvWines=(TextView)layoutInfo.findViewById(R.id.textView28);
        final TextView tvDrinks=(TextView)layoutInfo.findViewById(R.id.textView29);
        final TextView tvShots=(TextView)layoutInfo.findViewById(R.id.textView30);


        String proper=da+" 12:00:00";
        Date d5= new Date();
        try {
            d5=sdf.parse(proper);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        tvDate.setText(sdf_graph.format(new Date((long)d5.getTime())));


        String dame=sdf.format(d5).substring(0,10);
        //Calculating endDate
        Calendar c=Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dame+ " 12:00:00"));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 1); //same with c.add(Calendar.DAY_OF_MONTH, 1);
        Date resultDate=new Date(c.getTimeInMillis());
        String datePlusOne = sdf.format(resultDate);
        String dameMasUno = datePlusOne.substring(0, 10);
        Log.d("Search: ", "Searching food/symptoms...Dia actual: "+dame);
        List<BodyEvent> bodies = db.getAllBody();
        List<String> foods= new ArrayList<>();
        List<String> symptoms= new ArrayList<>();

        for (BodyEvent bodyEvent : bodies) {
            String date=bodyEvent.getDateTime().substring(0,10);
            if(date.equals(dame) && !bodyEvent.getFood().equals("-"))
            {
                foods.add(bodyEvent.getFood());
            }
            if(date.equals(dameMasUno) && !bodyEvent.getSymptom().equals("-"))
            {
                String elSintoma=bodyEvent.getSymptom();
                Log.d("el sintoma: ","The symptom: "+elSintoma);
                String sinCorchetes=elSintoma.substring(1, elSintoma.length()-1);
                Log.d("el sintoma: ","The symptom sin corchetes: "+sinCorchetes);
                symptoms.add(sinCorchetes);

            }
        }
        tvFood.setText(foods.toString().substring(1,foods.toString().length()-1));
        tvSymptom.setText(symptoms.toString().substring(1,symptoms.toString().length()-1));

        Log.d("Search: ", "Searching drinks..");
        List<DrinkEvent> drinks = db.getAllDrinks();
        int b=0;
        int w=0;
        int d=0;
        int s=0;

        for (DrinkEvent drinkEvent : drinks) {
            String date=drinkEvent.getDateTime();

            Log.d("Fecha ini y fecha fin: ","Fecha click: "+date+"\nFecha ini: "+dame+ " 08:00"+"\nFecha fin: "+dameMasUno+" 07:59");
            if(date.compareTo(dame +" 08:00")>=0 && date.compareTo(dameMasUno + " 07:59")<=0)
            {
                if(drinkEvent.getNum_beer()==1)
                    b++;
                else if (drinkEvent.getNum_wine()==1)
                    w++;
                else if(drinkEvent.getNum_drink()==1)
                    d++;
                else if(drinkEvent.getNum_shot()==1)
                    s++;
            }
        }
        tvBeers.setText(String.valueOf(b));
        tvWines.setText(String.valueOf(w));
        tvDrinks.setText(String.valueOf(d));
        tvShots.setText(String.valueOf(s));

        final Builder builder = new Builder(FirstScreen.this)
                .setView(layoutInfo)
                .setTitle("What happened?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // Send max. units


                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog


                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();


    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd HH:mm:ss"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.FRANCE);

        Log.d("Fecha: ","Fecha del me date time picker: "+sdf.format(myCalendar.getTime()));
        clickBody(sdf.format(myCalendar.getTime()),food,symptom);
    }


    private class DisabledColorDecorator implements DayDecorator {
        @Override
        public void decorate(DayView dayView) {
            if (CalendarUtils.isPastDay(dayView.getDate()) || CalendarUtils.isToday(myCalendar)) {
                Log.d("el day view", "El day view: "+dayView.getDate());
                sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
                String fecha=sdf.format(dayView.getDate()).substring(0,10);
                DataPoint[] sorted=generateData();

                for (int i=0;i<sorted.length;i++)
                {
                    String sortF=sdf.format(sorted[i].getX()).substring(0,10);
                    Log.d("question", "La fecha en question: "+fecha + ", La datesSort en i: "+ sortF+", El numero de drinks ese dia: "+sorted[i].getY());
                    if(fecha.equals(sortF) && sorted[i].getY()>4)
                    {

                        dayView.setBackgroundColor(getResources().getColor(R.color.DarkSalmon));
                        dayView.setTextColor(getResources().getColor(R.color.white));
                    }
                }

            }
        }
    }

    private void startAlarm() {
        AlarmManager manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;

        // SET TIME HERE
        Calendar calendar= Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY,21);
        calendar.set(Calendar.MINUTE,00);
        Log.d("Toast alarm: ", "Corriendo la funcion startAlarm");


        myIntent = new Intent(FirstScreen.this,AlarmNotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this,0,myIntent,0);

        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY,pendingIntent);


    }

    //closes FAB submenus
    private void closeSubMenusFab(){
        layout_FabBeer.setVisibility(View.INVISIBLE);
        layoutFabWine.setVisibility(View.INVISIBLE);
        layoutFabDrink.setVisibility(View.INVISIBLE);
        layoutFabShot.setVisibility(View.INVISIBLE);
        fab.setImageResource(R.drawable.plus);
        fabExpanded = false;
    }

    //Opens FAB submenus
    private void openSubMenusFab(){

        layout_FabBeer.setVisibility(View.VISIBLE);
        layoutFabWine.setVisibility(View.VISIBLE);
        layoutFabDrink.setVisibility(View.VISIBLE);
        layoutFabShot.setVisibility(View.VISIBLE);
        //Change settings icon to 'X' icon
        fab.setImageResource(R.drawable.cancel);
        fabExpanded = true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_FOR_SCAN: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    /**
                     * Only start scan if permissions granted.
                     */
                    mAPI.connectToNearestBreathalyzer();
                }
            }
        }
    }

    public void connectNearestClicked(View v) {
        if (mAPI != null) {
            setStatus(R.string.TEXT_CONNECTING);
            // Here, thisActivity is the current activity
            if (ContextCompat.checkSelfPermission(FirstScreen.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ContextCompat.checkSelfPermission(FirstScreen.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(FirstScreen.this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_FOR_SCAN);
            } else {
                /**
                 * Permission already granted, start scan.
                 */
                mAPI.connectToNearestBreathalyzer();
                mAPI.startCountdown();
            }

        }
    }

    public void disconnectClicked(View v) {
        if (mAPI != null) {
            mAPI.disconnect();
        }
    }

    public void getFirmwareVersionClicked(View v) {
        boolean result = false;
        if (mAPI != null) {
            result = mAPI.getFirmwareVersion();
        }
        if (!result)
            Log.e(TAG, "mAPI.getFirmwareVersion() failed");
        else
            Log.d(TAG, "Firmware version requested");
    }

    public void getSerialNumberClicked(View view) {
        boolean result = false;
        if (mAPI != null) {
            result = mAPI.getSerialNumber();
        }
        if (!result)
            Log.e(TAG, "mAPI.getSerialNumber() failed");
        else
            Log.d(TAG, "Serial Number requested");
    }

    public void requestUseCountClicked(View view) {
        boolean result = false;
        if (mAPI != null) {
            result = mAPI.getUseCount();
        }
        if (!result)
            Log.e(TAG, "mAPI.requestUseCount() failed");
        else
            Log.d(TAG, "Use count requested");
    }

    public void requestBatteryVoltageClicked(View view) {
        boolean result = false;
        if (mAPI != null) {
            result = mAPI.getBreathalyzerBatteryVoltage();
        }
        if (!result)
            Log.e(TAG, "mAPI.getBreathalyzerBatteryVoltage() failed");
        else
            Log.d(TAG, "Battery voltage requested");
    }


    public void startBlowProcessClicked(View v) {
        boolean result = false;
        if (mAPI != null) {
            result = mAPI.startCountdown();
        }
        if (!result)
            Log.e(TAG, "mAPI.startCountdown() failed");
        else
            Log.d(TAG, "Blow process start requested");
    }

    private void setStatus(int resourceId) {
        this.setStatus(this.getResources().getString(resourceId));
    }

    private void setStatus(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, message);
                if(message.equals("Disconnected"))
                    test.setText("Tap to connect");
                else if(message.equals("Connected!"))
                    test.setText("Tap to Blow");
                statusMessageTextView.setText(String.format("Status:\n%s", message));
            }
        });
    }

    private void setBatteryStatus(final String message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, message);
                //batteryLevelTextView.setText(String.format("\n%s", message));
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void selectedIndices(List<Integer> indices) {

    }
    @Override
    public void selectedStrings(List<String> strings) {
        food = "-";
        symptom = strings.toString();

        /*new SlideDateTimePicker.Builder(getSupportFragmentManager())
                .setListener(listener)
                .setInitialDate(new Date())
                .build()
                .show();*/
        new DatePickerDialog(FirstScreen.this, myDatePicker, myCalendar
                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                myCalendar.get(Calendar.DAY_OF_MONTH)).show();


        //Toast.makeText(this, strings.toString(), Toast.LENGTH_LONG).show();
        show();

    }



    private class APIKeyVerificationAlert extends AsyncTask<String, Void, String> {
    @Override
    protected String doInBackground(String... urls) {
        return urls[0];
    }

    @Override
    protected void onPostExecute(String result) {
        Builder apiApprovalAlert = new Builder(mContext);
        apiApprovalAlert.setTitle("API Approval Failed");
        apiApprovalAlert.setMessage(result);
        apiApprovalAlert.setPositiveButton(
                "Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mAPI.disconnect();
                        setStatus(R.string.TEXT_DISCONNECTED);
                        dialog.cancel();
                    }
                });

        apiApprovalAlert.create();
        apiApprovalAlert.show();
    }
}

    private final BACtrackAPICallbacks mCallbacks = new BACtrackAPICallbacks() {

        @Override
        public void BACtrackAPIKeyDeclined(String errorMessage) {
            FirstScreen.APIKeyVerificationAlert verify = new FirstScreen.APIKeyVerificationAlert();
            verify.execute(errorMessage);
        }

        @Override
        public void BACtrackAPIKeyAuthorized() {

        }

        @Override
        public void BACtrackConnected(BACTrackDeviceType bacTrackDeviceType) {
            setStatus(R.string.TEXT_CONNECTED);
        }

        @Override
        public void BACtrackDidConnect(String s) {
            setStatus(R.string.TEXT_DISCOVERING_SERVICES);
        }

        @Override
        public void BACtrackDisconnected() {
            setStatus(R.string.TEXT_DISCONNECTED);
            setBatteryStatus("");
            setCurrentFirmware(null);
        }
        @Override
        public void BACtrackConnectionTimeout() {

        }

        @Override
        public void BACtrackFoundBreathalyzer(BluetoothDevice bluetoothDevice) {
            Log.d(TAG, "Found breathalyzer : " + bluetoothDevice.getName());
        }

        @Override
        public void BACtrackCountdown(int currentCountdownCount) {
            setStatus(getString(R.string.TEXT_COUNTDOWN) + " " + currentCountdownCount);
        }

        @Override
        public void BACtrackStart() {
            setStatus(R.string.TEXT_BLOW_NOW);
        }

        @Override
        public void BACtrackBlow() {
            setStatus(R.string.TEXT_KEEP_BLOWING);
        }

        @Override
        public void BACtrackAnalyzing() {
            setStatus(R.string.TEXT_ANALYZING);
        }

        @Override
        public void BACtrackResults(float measuredBac) {
            if(measuredBac<0.05)
                setStatus(getString(R.string.TEXT_FINISHED) + " " + measuredBac + "\nYou are in a perfect state");
            else if(measuredBac>=0.05)
                setStatus(getString(R.string.TEXT_FINISHED) + " " + measuredBac + "\nReduced Inhibitions\n You cannot drive");
            else if (measuredBac>0.065 && measuredBac<=0.15)
                setStatus(getString(R.string.TEXT_FINISHED) + " " + measuredBac + "\nSlurred Speech");
            else if (measuredBac>0.15 && measuredBac<=0.25)
                setStatus(getString(R.string.TEXT_FINISHED) + " " + measuredBac + "\nEuphoria and motor imapirement");
            else if (measuredBac>0.25 && measuredBac<=0.35)
                setStatus(getString(R.string.TEXT_FINISHED) + " " + measuredBac + "\nConfusion");
            else if (measuredBac>0.35 && measuredBac<=0.45)
                setStatus(getString(R.string.TEXT_FINISHED) + " " + measuredBac + "\nStupor");
            else if (measuredBac>0.45 && measuredBac<=0.55)
                setStatus(getString(R.string.TEXT_FINISHED) + " " + measuredBac + "\nComa");
            else if (measuredBac>0.55 && measuredBac<=0.65)
                setStatus(getString(R.string.TEXT_FINISHED) + " " + measuredBac + "\nBreath stops and death");
            //Calculating the Current Time
            sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.FRANCE);
            currentDateTime = sdf.format(new Date());
            //Adding one drink
            db.addDrink(new DrinkEvent(0,currentDateTime, -1, -1, -1, -1, measuredBac));
            Log.d("Add: ", "BAC added");
        }

        @Override
        public void BACtrackFirmwareVersion(String version) {
            setStatus(getString(R.string.TEXT_FIRMWARE_VERSION) + " " + version);
            setCurrentFirmware(version);
        }

        @Override
        public void BACtrackSerial(String serialHex) {
            setStatus(getString(R.string.TEXT_SERIAL_NUMBER) + " " + serialHex);
        }

        @Override
        public void BACtrackUseCount(int useCount) {
            Log.d(TAG, "UseCount: " + useCount);
            setStatus(getString(R.string.TEXT_USE_COUNT) + " " + useCount);
        }

        @Override
        public void BACtrackBatteryVoltage(float voltage) {

        }

        @Override
        public void BACtrackBatteryLevel(int level) {
            setBatteryStatus(getString(R.string.TEXT_BATTERY_LEVEL) + " " + level);

        }

        @Override
        public void BACtrackError(int errorCode) {
            if (errorCode == Errors.ERROR_BLOW_ERROR)
                setStatus(R.string.TEXT_ERR_BLOW_ERROR);
        }
    };


    public void setCurrentFirmware(@Nullable String currentFirmware) {
        this.currentFirmware = currentFirmware;

        String[] firmwareSplit = new String[0];
        if (currentFirmware != null) {
            firmwareSplit = currentFirmware.split("\\s+");
        }
        if (firmwareSplit.length >= 1
                && Long.valueOf(firmwareSplit[0]) >= Long.valueOf("201510150003")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (serialNumberButton != null) {
                        serialNumberButton.setVisibility(View.VISIBLE);
                    }
                    if (useCountButton != null) {
                        useCountButton.setVisibility(View.VISIBLE);
                    }
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (serialNumberButton != null) {
                        serialNumberButton.setVisibility(View.GONE);
                    }
                    if (useCountButton != null) {
                        useCountButton.setVisibility(View.GONE);
                    }
                }
            });
        }
    }
}
