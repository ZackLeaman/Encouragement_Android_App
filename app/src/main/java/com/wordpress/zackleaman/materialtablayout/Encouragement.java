package com.wordpress.zackleaman.materialtablayout;

/**
 * Created by Zack on 7/22/2016.
 */
public class Encouragement {
    private String date = "";
    private String name = null;
    private String phoneNumber = "";
    private String message = "";

    public Encouragement(String date, String name, String phoneNumber, String message){
        this.date = date;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.message = message;
    }

    @Override
    public String toString() {
        if(name != null && name != "" && name != "null") {
            return phoneNumber + " on " + date + "\n" + message;
        }else{
            return phoneNumber + " on " + date + "\n" + message;
        }
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
