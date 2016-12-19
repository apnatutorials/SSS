package com.mbcsonline.sss;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.mbcsonline.sss.modelclass.Account;
import com.mbcsonline.sss.modelclass.Constant;
import com.mbcsonline.sss.modelclass.FormValidator;
import com.mbcsonline.sss.modelclass.MyTextWatcher;
import com.mbcsonline.sss.modelclass.ServerRequest;
import com.mbcsonline.sss.modelclass.ServerResponse;
import com.mbcsonline.sss.utils.AsyncCommunicator;
import com.mbcsonline.sss.utils.AsyncTaskHandler;
import com.mbcsonline.sss.utils.Base64;
import com.mbcsonline.sss.utils.CustomDialog;

public class RegisterActivity extends AppCompatActivity implements AsyncCommunicator  {
    String TAG = "RegisterActivity" ;
    CustomDialog dialog;
    AsyncTaskHandler task;
    TextInputLayout register_TilEmailLayout, register_TilPasswordLayout, register_TilConfirmPasswordLayout, register_TilNameLayout, register_TilPhoneLayout  ;
    TextInputEditText register_TietEmail, register_TietPassword, register_TietConfirmPassword, register_TietName, register_TietPhone  ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);
        dialog = new CustomDialog(this);

        register_TilEmailLayout = (TextInputLayout) findViewById(R.id.register_TilEmailLayout) ;
        register_TilPasswordLayout = (TextInputLayout) findViewById(R.id.register_TilPasswordLayout) ;
        register_TilConfirmPasswordLayout = (TextInputLayout) findViewById(R.id.register_TilConfirmPasswordLayout) ;
        register_TilNameLayout = (TextInputLayout) findViewById(R.id.register_TilNameLayout) ;
        register_TilPhoneLayout = (TextInputLayout) findViewById(R.id.register_TilPhoneLayout) ;

        register_TietEmail = (TextInputEditText) findViewById( R.id.register_TietEmail );
        register_TietPassword = (TextInputEditText) findViewById( R.id.register_TietPassword );
        register_TietConfirmPassword = (TextInputEditText) findViewById( R.id.register_TietConfirmPassword );
        register_TietName = (TextInputEditText) findViewById( R.id.register_TietName );
        register_TietPhone = (TextInputEditText) findViewById( R.id.register_TietPhone );

        register_TietEmail.addTextChangedListener(new MyTextWatcher(this, register_TietEmail, register_TilEmailLayout));
        register_TietPassword.addTextChangedListener(new MyTextWatcher(this, register_TietPassword, register_TilPasswordLayout));
        register_TietConfirmPassword.addTextChangedListener(new MyTextWatcher(this, register_TietConfirmPassword,register_TietPassword, register_TilConfirmPasswordLayout));

        register_TietName.addTextChangedListener(new MyTextWatcher(this, register_TietName, register_TilNameLayout));
        register_TietPhone.addTextChangedListener(new MyTextWatcher(this, register_TietPhone, register_TilPhoneLayout));

    }

    public void register_ClickHandler(View view){
        switch(view.getId()){
            case R.id.register_BtnRegisterOk:
                if (FormValidator.isValidEmail(register_TietEmail.getText().toString()) == false) {
                    register_TilEmailLayout.setError(getString(R.string.email_error_message));
                    register_TietEmail.requestFocus();
                } else if (FormValidator.isValidPassword(register_TietPassword.getText().toString()) == false) {
                    register_TilPasswordLayout.setError(getString(R.string.password_error_message));
                    register_TietPassword.requestFocus();
                }else if( FormValidator.isValidPassword(register_TietConfirmPassword.getText().toString()) == false ){
                    register_TietConfirmPassword.setError(getString(R.string.password_error_message));
                    register_TietConfirmPassword.requestFocus();
                }
                else if(FormValidator.isConfirmPasswordMatched(register_TietPassword.getText().toString(), register_TietConfirmPassword.getText().toString() )== false){
                    register_TietConfirmPassword.setError(getString(R.string.password_and_confirm_password_must_match) );
                    register_TietConfirmPassword.requestFocus();
                }
                else if(FormValidator.isValidName(register_TietName.getText().toString()) == false ){
                    register_TietName.setError(getString(R.string.enter_valid_name));
                    register_TietName.requestFocus();
                }
                else if(FormValidator.isValidPhone(register_TietPhone.getText().toString()) == false ){
                    register_TietPhone.setError(getString(R.string.enter_valid_phone));
                    register_TietPhone.requestFocus();
                }
                else {

                    Account account = new Account() ;
                    account.setEmail(Base64.encode(register_TietEmail.getText().toString()));
                    account.setPassword(Base64.encode(register_TietPassword.getText().toString()));
                    account.setAddress(Base64.encode(""));
                    account.setPhone(Base64.encode(register_TietPhone.getText().toString()));
                    account.setName(Base64.encode(register_TietName.getText().toString()));

                    ServerRequest request = new ServerRequest();
                    request.setCommand(Constant.ACTION_REGISTER_OK);
                    request.setAccount(account);

                    String data = new Gson().toJson(request) ;
                    task = new AsyncTaskHandler(this);
                    task.execute(new String[]{Constant.REGISTER_PARENT_URL,data} );

                }
                break;

            case R.id.register_TvLogin:
                Intent laIntent = new Intent(this, LoginActivity.class);
                startActivity(laIntent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.register_TvRecoverPassword:
                Intent rpIntent = new Intent(this, RecoverPasswordActivity.class);
                startActivity(rpIntent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onProgressUpdate(String progress) {

    }

    @Override
    public void onProcessResult(String result) {
        dialog.showMessage(getString(R.string.error_dialog_title) , "BALENDRSA");

        Log.v(TAG,result + "");
        ServerResponse response = new Gson().fromJson(result,ServerResponse.class );

        if(response.isSuccess() == false) {
            dialog.showMessage(getString(R.string.error_dialog_title) , Base64.decode(response.getMessage()));
        }
        else{
            String message = Base64.decode(response.getMessage());
            Log.v(TAG,message+ "");
            if(message.trim().equals(Constant.VERIFICATION)){
                Account account = null;
                 account = response.getAccount()[0];
                if(account != null &&  account.getStatus() == Account.STATUS_PENDING){
                    Constant.loginResponse = response ;
                    Intent cvIntent = new Intent(this, CodeValidatorActivity.class);
                    startActivity(cvIntent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
                else{
                    dialog.showMessage(getString(R.string.error_dialog_title) , getString( R.string.no_account_detail_found) );
                }

            }
            else if(message.trim().equals(Constant.LOGIN))
            {
                Intent cvIntent = new Intent(this, LoginActivity.class);
                startActivity(cvIntent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
            }

        }
    }

    @Override
    public String getStringById(int id) {
        return this.getString(id);
    }

    @Override
    public Context getContext() {
        return this;
    }
    @Override
    public void cancelTask(){
        if (task!=null  ) {
            task.cancel(true);
            task = null ;
        }

    }
}
