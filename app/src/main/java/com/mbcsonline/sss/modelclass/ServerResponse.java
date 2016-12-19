package com.mbcsonline.sss.modelclass;

/**
 * Created by Angel on 11/11/2016.
 */
public class ServerResponse {

    private String command = "";
    private boolean success = true;
    private String message = "";

    Account []account = null ;
    Student []students = null ;
   // Bus bus = null ;
   // Driver driver = null ;
    GPSData []gpsData = null ;

    Business []businesses = null ;

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Account[] getAccount() {
        return account;
    }

    public void setAccount(Account[] account) {
        this.account = account;
    }

    public Business[] getBusinesses() {
        return businesses;
    }

    public void setBusinesses(Business[] businesses) {
        this.businesses = businesses;
    }

    public Student[] getStudents() {
        return students;
    }

    public void setStudents(Student[] students) {
        this.students = students;
    }

//    public Bus getBus() {
//        return bus;
//    }
//
//    public void setBus(Bus bus) {
//        this.bus = bus;
//    }
//
//    public Driver getDriver() {
//        return driver;
//    }
//
//    public void setDriver(Driver driver) {
//        this.driver = driver;
//    }
//
    public GPSData[] getGpsData() {
        return gpsData;
    }

    public void setGpsData(GPSData[] gpsData) {
        this.gpsData = gpsData;
    }
}
