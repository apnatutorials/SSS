package com.mbcsonline.sss.modelclass;

import android.text.TextUtils;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FormValidator {
    private static java.lang.String nameRegularExpression = "^[\\p{L} .'-]+$";

    public static boolean isValidEmail(String email){
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }
    public static boolean isValidPassword(String password){
       return  !TextUtils.isEmpty(password) && password.trim().length()==8 ;
    }

    public static boolean isValidPhone(String phone){
        return   !TextUtils.isEmpty( phone ) && android.util.Patterns.PHONE.matcher(phone).matches()  ;
    }

    public static boolean isValidName(String name){

        return   !TextUtils.isEmpty( name ) && Pattern.matches(nameRegularExpression, name) ;
    }

    public static boolean isConfirmPasswordMatched(String password, String confirmPassword){
        return  password.equals(confirmPassword) ;
    }

    public static boolean isValidVerificationCode(String code){
        return   !TextUtils.isEmpty( code ) && code.length()==4  ;
    }
}
