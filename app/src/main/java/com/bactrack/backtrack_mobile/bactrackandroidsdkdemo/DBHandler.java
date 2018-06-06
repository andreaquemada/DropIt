package com.bactrack.backtrack_mobile.bactrackandroidsdkdemo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class DBHandler extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    public static final String DATABASE_NAME = "DropItDB";
    // Drinks table name
    private static final String TABLE_DRINKS = "Drinks";
    //Body table name
    private static final String TABLE_BODY = "Body";

    // Shops Table Columns names
    private static final String KEY_ID = "ID";
    private static final String KEY_DATE = "Date";
    private static final String KEY_NUMBEERS = "Nº_beers";
    private static final String KEY_NUMWINES = "Nº_glasses_wine";
    private static final String KEY_NUMDRINKS = "Nº_drinks";
    private static final String KEY_NUMSHOTS = "Nº_shots";
    private static final String KEY_BAC = "BAC_percentage";

    // Body Table Columns names
    private static final String KEY_FOOD = "Food";
    private static final String KEY_SYMPTOM = "Symptom";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_DRINKS_TABLE = "CREATE TABLE " + TABLE_DRINKS + " (" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_DATE + " TEXT," +
                KEY_NUMBEERS + " INTEGER," +
                KEY_NUMWINES + " INTEGER," +
                KEY_NUMDRINKS + " INTEGER," +
                KEY_NUMSHOTS + " INTEGER,"+
                KEY_BAC + " REAL)";

        String CREATE_BODY_TABLE = "CREATE TABLE " + TABLE_BODY + " (" +
                KEY_ID + " INTEGER PRIMARY KEY," +
                KEY_DATE + " TEXT," +
                KEY_FOOD + " TEXT," +
                KEY_SYMPTOM + " TEXT)";

        db.execSQL(CREATE_DRINKS_TABLE);
        db.execSQL(CREATE_BODY_TABLE);
        Log.d("DB","Databases created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_DRINKS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_BODY);
// Creating tables again
        onCreate(db);
    }


    // Adding new drink
    public void addDrink(DrinkEvent drinkEvent) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, drinkEvent.getDateTime()); // Date of Drink
        values.put(KEY_NUMBEERS, drinkEvent.getNum_beer());
        values.put(KEY_NUMWINES, drinkEvent.getNum_wine());
        values.put(KEY_NUMDRINKS, drinkEvent.getNum_drink());
        values.put(KEY_NUMSHOTS, drinkEvent.getNum_shot());
        values.put(KEY_BAC, drinkEvent.getBac());
        // Inserting Row
        db.insert(TABLE_DRINKS, null, values);
        db.close(); // Closing database connection
    }

    // Adding new food/symptom
    public void addBody(BodyEvent bodyEvent) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_DATE, bodyEvent.getDateTime()); // Date of Drink
        values.put(KEY_FOOD, bodyEvent.getFood());
        values.put(KEY_SYMPTOM, bodyEvent.getSymptom());
        // Inserting Row
        db.insert(TABLE_BODY, null, values);
        db.close(); // Closing database connection
    }

    // Getting one drink
    public DrinkEvent getDrink(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_DRINKS, new String[] { KEY_ID,
                        KEY_DATE, KEY_NUMBEERS, KEY_NUMWINES,
                        KEY_NUMDRINKS, KEY_NUMSHOTS, KEY_BAC}, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        DrinkEvent one_drink = new DrinkEvent(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), Integer.parseInt(cursor.getString(2)),
                Integer.parseInt(cursor.getString(3)),Integer.parseInt(cursor.getString(4)),
                Integer.parseInt(cursor.getString(5)), Float.parseFloat(cursor.getString((6))));
// return shop
        return one_drink;
    }


    // Getting All Drinks
    public List<DrinkEvent> getAllDrinks() {
        List<DrinkEvent> drinkList = new ArrayList<DrinkEvent>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_DRINKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                DrinkEvent drinkEvent = new DrinkEvent();
                drinkEvent.setId(Integer.parseInt(cursor.getString(0)));
                drinkEvent.setDateTime(cursor.getString(1));
                drinkEvent.setNum_beer(Integer.parseInt(cursor.getString(2)));
                drinkEvent.setNum_wine(Integer.parseInt(cursor.getString(3)));
                drinkEvent.setNum_drink(Integer.parseInt(cursor.getString(4)));
                drinkEvent.setNum_shot(Integer.parseInt(cursor.getString(5)));
                drinkEvent.setBac(Float.parseFloat(cursor.getString(6)));
                // Adding contact to list
                drinkList.add(drinkEvent);
            } while (cursor.moveToNext());
        }

        // return contact list
        return drinkList;
    }

    // Getting All foods/symptoms
    public List<BodyEvent> getAllBody() {
        List<BodyEvent> bodyList = new ArrayList<>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_BODY;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                BodyEvent bodyEvent = new BodyEvent();
                bodyEvent.setId(Integer.parseInt(cursor.getString(0)));
                bodyEvent.setDateTime(cursor.getString(1));
                bodyEvent.setFood(cursor.getString(2));
                bodyEvent.setSymptom(cursor.getString(3));
                // Adding contact to list
                bodyList.add(bodyEvent);
            } while (cursor.moveToNext());
        }

        // return contact list
        return bodyList;
    }


    // Getting Number of Drinks
    public List<Integer> getNumDrinks(String starDate, String endDate) {
        List<Integer> numdrinkList = new ArrayList<Integer>();
        //List<DrinkEvent> drinkList = new ArrayList<DrinkEvent>();
        SQLiteDatabase db = this.getWritableDatabase();

        //Queries for number of drinks

        Cursor cursorB = db.query(TABLE_DRINKS, new String[] { KEY_ID,
                        KEY_DATE, KEY_NUMBEERS, KEY_NUMWINES,
                        KEY_NUMDRINKS, KEY_NUMSHOTS, KEY_BAC}, KEY_NUMBEERS + "=? AND "+ KEY_DATE +">? AND "+
                        KEY_DATE+"<?",
                new String[] { String.valueOf(1),starDate,endDate}, null, null, null, null);
        Cursor cursorW = db.query(TABLE_DRINKS, new String[] { KEY_ID,
                        KEY_DATE, KEY_NUMBEERS, KEY_NUMWINES,
                        KEY_NUMDRINKS, KEY_NUMSHOTS, KEY_BAC}, KEY_NUMWINES + "=? AND "+ KEY_DATE +">? AND "+
                        KEY_DATE+"<?",
                new String[] { String.valueOf(1),starDate, endDate
                }, null, null, null, null);
        Cursor cursorD = db.query(TABLE_DRINKS, new String[] { KEY_ID,
                        KEY_DATE, KEY_NUMBEERS, KEY_NUMWINES,
                        KEY_NUMDRINKS, KEY_NUMSHOTS, KEY_BAC}, KEY_NUMDRINKS + "=? AND "+ KEY_DATE +">? AND "+
                        KEY_DATE+"<?",
                new String[] { String.valueOf(1),starDate, endDate
                }, null, null, null, null);
        Cursor cursorS = db.query(TABLE_DRINKS, new String[] { KEY_ID,
                        KEY_DATE, KEY_NUMBEERS, KEY_NUMWINES,
                        KEY_NUMDRINKS, KEY_NUMSHOTS, KEY_BAC}, KEY_NUMSHOTS + "=? AND "+ KEY_DATE +">? AND "+
                        KEY_DATE+"<?",
                new String[] { String.valueOf(1),starDate, endDate
                }, null, null, null, null);
        Cursor cursorBAC = db.query(TABLE_DRINKS, new String[] { KEY_ID,
                        KEY_DATE, KEY_NUMBEERS, KEY_NUMWINES,
                        KEY_NUMDRINKS, KEY_NUMSHOTS, KEY_BAC}, KEY_BAC + "!=? AND "+ KEY_DATE +">? AND "+
                        KEY_DATE+"<?",
                new String[] { String.valueOf(-1),starDate, endDate
                }, null, null, null, null);

        int countB;
        int countW;
        int countD;
        int countS;
        int countBAC;

        countB=cursorB.getCount();
        countW=cursorW.getCount();
        countD=cursorD.getCount();
        countS=cursorS.getCount();
        countBAC=cursorBAC.getCount();
        numdrinkList.add(countB);
        numdrinkList.add(countW);
        numdrinkList.add(countD);
        numdrinkList.add(countS);
        numdrinkList.add(countBAC);

        // looping through all rows and adding to list
        /*if (cursor.moveToFirst()) {
            do {
                DrinkEvent drinkEvent = new DrinkEvent();
                drinkEvent.setId(Integer.parseInt(cursor.getString(0)));
                drinkEvent.setDateTime(cursor.getString(1));
                drinkEvent.setNum_beer(Integer.parseInt(cursor.getString(2)));
                drinkEvent.setNum_wine(Integer.parseInt(cursor.getString(3)));
                drinkEvent.setNum_drink(Integer.parseInt(cursor.getString(4)));
                drinkEvent.setNum_shot(Integer.parseInt(cursor.getString(5)));
                // Adding contact to list
                drinkList.add(drinkEvent);
            } while (cursor.moveToNext());
        }
        */
        // return contact list
        return numdrinkList;

    }

    public float getLastBac()
    {
        float lastBAC;

        SQLiteDatabase db = this.getWritableDatabase();

        //Queries for number of drinks

        Cursor cursorBAC = db.query(TABLE_DRINKS, new String[] { KEY_ID,
                        KEY_DATE, KEY_NUMBEERS, KEY_NUMWINES,
                        KEY_NUMDRINKS, KEY_NUMSHOTS, KEY_BAC}, KEY_BAC + "!=?",
                new String[] { String.valueOf(-1)}, null, null, KEY_ID+" DESC", String.valueOf(1));

        if (cursorBAC != null)
            cursorBAC.moveToFirst();

        if(!cursorBAC.moveToFirst() || cursorBAC.getCount() ==0)
            lastBAC=100f;
        else {
            lastBAC = Float.parseFloat(cursorBAC.getString(6));
            String log = "Id: " + Integer.parseInt(cursorBAC.getString(0)) + " ,DateTime: " + cursorBAC.getString(1) + " ,Beer: " + Integer.parseInt(cursorBAC.getString(2))
                    + " ,Wine: " + Integer.parseInt(cursorBAC.getString(3)) + " ,Drink: " + Integer.parseInt(cursorBAC.getString(4)) + " ,Shot: " + Integer.parseInt(cursorBAC.getString(5)) + " ,BAC: " + Float.parseFloat(cursorBAC.getString(6));

            // Writing drinks  to log
            Log.d("PRINTED BAC: : ", log);
        }
        return lastBAC;
    }

    // Updating a drink
    public int updateDrink(DrinkEvent drink) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_ID, drink.getId());
        values.put(KEY_DATE, drink.getDateTime());
        values.put(KEY_NUMBEERS, drink.getNum_beer());
        values.put(KEY_NUMWINES, drink.getNum_wine());
        values.put(KEY_NUMDRINKS, drink.getNum_drink());
        values.put(KEY_NUMSHOTS, drink.getNum_shot());
        values.put(KEY_BAC, drink.getBac());

    // updating row
        return db.update(TABLE_DRINKS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(drink.getId())});
    }


    // Deleting a drink
    public void deleteDrink(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DRINKS, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    // Deleting a food/symptom
    public void deleteBody(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BODY, KEY_ID + " = ?",
                new String[] { String.valueOf(id) });
        db.close();
    }

    // Deleting a DrinksTable
    public void deleteDrinksTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_DRINKS,null, null);
        db.close();
    }

    // Deleting a BodyTable
    public void deleteBodyTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_BODY,null, null);
        db.close();
    }

}
