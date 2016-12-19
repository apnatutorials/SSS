package com.mbcsonline.sss.modelclass;

/**
 * Created by Angel on 12/15/2016.
 */
public class Bus {
    private int id =-1 ;
    private String busNumber = "" ;
    private String regNumber = "" ;
    private String regYear = "" ;
    private String licValidTill = "" ;
    private String condition = "" ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBusNumber() {
        return busNumber;
    }

    public void setBusNumber(String busNumber) {
        this.busNumber = busNumber;
    }

    public String getRegNumber() {
        return regNumber;
    }

    public void setRegNumber(String regNumber) {
        this.regNumber = regNumber;
    }

    public String getRegYear() {
        return regYear;
    }

    public void setRegYear(String regYear) {
        this.regYear = regYear;
    }

    public String getLicValidTill() {
        return licValidTill;
    }

    public void setLicValidTill(String licValidTill) {
        this.licValidTill = licValidTill;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }
}
