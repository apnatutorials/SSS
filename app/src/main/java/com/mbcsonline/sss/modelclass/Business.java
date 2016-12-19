package com.mbcsonline.sss.modelclass;

/**
 * Created by Angel on 11/11/2016.
 */
public class Business {
    public static final int STATUS_ENABLED = 1;
    public static final int STATUS_DISABLED = 2;

    int id = -1;
    String name = "";

    String email = "";
    String phone = "";
    String address = "";
    int status = 0;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
