package com.bactrack.backtrack_mobile.bactrackandroidsdkdemo;

public class BodyEvent {
    private int id;
    private String datetime;
    private String food;
    private String symptom;


    public BodyEvent()
    {
    }
    public BodyEvent(int id, String datetime, String food,String symptom)
    {
        this.id=id;
        this.datetime=datetime;
        this.food=food;
        this.symptom=symptom;
    }
    public void setId(int id) {
        this.id = id;
    }
    public void setDateTime(String datetime) {
        this.datetime = datetime;
    }
    public void setFood(String food) {
        this.food = food;
    }
    public void setSymptom(String symptom) {
        this.symptom = symptom;
    }

    public int getId() {
        return id;
    }
    public String getDateTime() {
        return datetime;
    }
    public String getFood() {
        return food;
    }
    public String getSymptom(){
        return symptom;
    }

}
