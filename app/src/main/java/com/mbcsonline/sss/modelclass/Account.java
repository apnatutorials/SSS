package com.mbcsonline.sss.modelclass;

/**
 * Created by Angel on 11/11/2016.
 */
public class Account {
    public static final int TYPE_SUPER_ADMIN = 0 ;
    public static final int TYPE_BUSINESS_ADMIN = 1;
    public static final int TYPE_BUSINESS_USER = 2;
    public static final int TYPE_BUSINESS_PARENT = 3;
    public static final int TYPE_BUSINESS_DRIVER = 4;

    public static final int STATUS_PENDING = 0;
    public static final int STATUS_ENABLED = 1;
    public static final int STATUS_DISABLED = 2;

    private int id = -1 ;
    private String name = "" ;
    private String email ="" ;
    private String phone ="" ;
    private String address = "" ;
    private String password ="" ;
    private int status = STATUS_PENDING ;
    private int type = TYPE_BUSINESS_PARENT ;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
