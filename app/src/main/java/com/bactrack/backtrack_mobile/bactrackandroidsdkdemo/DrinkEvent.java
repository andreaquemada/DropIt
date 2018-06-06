package com.bactrack.backtrack_mobile.bactrackandroidsdkdemo;



public class DrinkEvent {

    private int id;
    private String datetime;
    private int num_beer;
    private int num_wine;
    private int num_drink;
    private int num_shot;
    private float bac;


    public DrinkEvent()
    {
    }
    public DrinkEvent(int id, String datetime, int num_beer,int num_wine,int num_drink,int num_shot, float bac)
    {
        this.id=id;
        this.datetime=datetime;
        this.num_beer=num_beer;
        this.num_wine=num_wine;
        this.num_drink=num_drink;
        this.num_shot=num_shot;
        this.bac=bac;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setDateTime(String datetime) {
        this.datetime = datetime;
    }
    public void setNum_beer(int num_beer) {
        this.num_beer = num_beer;
    }
    public void setNum_wine(int num_wine) {
        this.num_wine = num_wine;
    }
    public void setNum_drink(int num_drink) {
        this.num_drink = num_drink;
    }
    public void setNum_shot(int num_shot) {
        this.num_shot = num_shot;
    }
    public void setBac(float bac)
    {
        this.bac=bac;
    }

    public int getId() {
        return id;
    }
    public String getDateTime() {
        return datetime;
    }
    public int getNum_beer() {
        return num_beer;
    }
    public int getNum_wine() {
        return num_wine;
    }
    public int getNum_drink() {
        return num_drink;
    }
    public int getNum_shot() {
        return num_shot;
    }
    public float getBac()
    {
        return bac;
    }

}
