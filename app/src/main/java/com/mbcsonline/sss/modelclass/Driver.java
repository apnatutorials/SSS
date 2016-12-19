package com.mbcsonline.sss.modelclass;

/**
 * Created by Angel on 12/15/2016.
 */
public class Driver {
    private int id = -1 ;
    private String name = "" ;
    private String email = "" ;
    private String dlIssueDate = "" ;
    private String dlUrlPic = "" ;

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

    public String getDlIssueDate() {
        return dlIssueDate;
    }

    public void setDlIssueDate(String dlIssueDate) {
        this.dlIssueDate = dlIssueDate;
    }

    public String getDlUrlPic() {
        return dlUrlPic;
    }

    public void setDlUrlPic(String dlUrlPic) {
        this.dlUrlPic = dlUrlPic;
    }
}
