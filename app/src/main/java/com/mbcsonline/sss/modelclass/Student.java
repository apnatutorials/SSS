package com.mbcsonline.sss.modelclass;

/**
 * Created by Angel on 11/18/2016.
 */
public class Student {
    private int id = -1 ;
    private String ame = "" ;
    private String dob = "" ;
    private int classId = -1 ;
    private String className = "" ;
    private String name = "" ;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getAme() {
        return ame;
    }

    public void setAme(String ame) {
        this.ame = ame;
        this.name = ame ;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public int getClassId() {
        return classId;
    }

    public void setClassId(int classId) {
        this.classId = classId;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
