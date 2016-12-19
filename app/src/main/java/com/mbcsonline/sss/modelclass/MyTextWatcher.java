package com.mbcsonline.sss.modelclass;

import android.content.Context;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;

import com.mbcsonline.sss.R;

public class MyTextWatcher implements TextWatcher {
    TextInputEditText view, matchingView;
    TextInputLayout layout;

    Context ctx;

    public MyTextWatcher(Context context, TextInputEditText view, TextInputLayout layout) {
        this.view = view;
        this.layout = layout;
        this.ctx = context;
    }
    public MyTextWatcher(Context context, TextInputEditText view, TextInputEditText matchingView , TextInputLayout layout) {
        this.view = view;
        this.layout = layout;
        this.ctx = context;
        this.matchingView = matchingView ;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {
        switch (this.view.getId()) {
            case R.id.login_TietEmail:
                if (FormValidator.isValidEmail(view.getText().toString()) == false) {
                    layout.setError(ctx.getString(R.string.email_error_message));
                    view.requestFocus();
                } else {
                    layout.setErrorEnabled(false);
                }
                break;

            case R.id.login_TietPassword:
                if (FormValidator.isValidPassword(view.getText().toString()) == false) {
                    layout.setError(ctx.getString(R.string.password_error_message));
                    view.requestFocus();
                } else {
                    layout.setErrorEnabled(false);
                }
                break;

            case R.id.recoverPassword_TietEmail:
                if (FormValidator.isValidEmail(view.getText().toString()) == false) {
                    layout.setError(ctx.getString(R.string.email_error_message));
                    view.requestFocus();
                } else {
                    layout.setErrorEnabled(false);
                }
                break;

            case R.id.recoverPassword_TietPhone:
                if (FormValidator.isValidEmail(view.getText().toString()) == false) {
                    layout.setError(ctx.getString(R.string.phone_error_message));
                    view.requestFocus();
                } else {
                    layout.setErrorEnabled(false);
                }
                break;

            case R.id.register_TietEmail:
                if (FormValidator.isValidEmail(view.getText().toString()) == false) {
                    layout.setError(ctx.getString(R.string.email_error_message));
                    view.requestFocus();
                } else {
                    layout.setErrorEnabled(false);
                }
                break;

            case R.id.register_TietPassword:
                if (FormValidator.isValidPassword(view.getText().toString()) == false) {
                    layout.setError(ctx.getString(R.string.password_error_message));
                    view.requestFocus();
                } else {
                    layout.setErrorEnabled(false);
                }
                break;
            case R.id.register_TietConfirmPassword:
                if (FormValidator.isValidPassword(view.getText().toString()) == false) {
                    layout.setError(ctx.getString(R.string.password_error_message));
                    view.requestFocus();
                }
                else if(  FormValidator.isConfirmPasswordMatched(view.getText().toString(),matchingView.getText().toString())==false ){
                    layout.setError(ctx.getString(R.string.password_and_confirm_password_must_match));
                    view.requestFocus();

                }
                else {
                    layout.setErrorEnabled(false);
                    view.setHintTextColor( ctx.getColor(R.color.colorPrimaryDark) );
                }
                break;

            case R.id.register_TietName:
                if (FormValidator.isValidName(view.getText().toString()) == false) {
                    layout.setError(ctx.getString(R.string.enter_valid_name));
                    view.requestFocus();
                } else {
                    layout.setErrorEnabled(false);
                }
                break;
            case R.id.register_TietPhone:
                if (FormValidator.isValidPhone(view.getText().toString()) == false) {
                    layout.setError(ctx.getString(R.string.enter_valid_phone));
                    view.requestFocus();
                } else {
                    layout.setErrorEnabled(false);
                }
                break;

            case R.id.codeValidator_TietVerificationCode:
                if (FormValidator.isValidVerificationCode(view.getText().toString()) == false) {
                    layout.setError(ctx.getString(R.string.enter_valid_phone));
                    view.requestFocus();
                } else {
                    layout.setErrorEnabled(false);
                }
                break;
        }
    }


}
