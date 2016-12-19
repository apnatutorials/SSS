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
import android.widget.Toast;

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

public class RecoverPasswordActivity extends AppCompatActivity implements AsyncCommunicator {
    private static final String TAG = "RecoverPasswordActivity" ;
    CustomDialog dialog ;
    TextInputLayout recoverPassword_TilEmailLayout, recoverPassword_TilPhoneLayout ;
    TextInputEditText recoverPassword_TietEmail, recoverPassword_TietPhone ;
    AsyncTaskHandler task = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recover_password_activity);
        dialog = new CustomDialog(this);
        recoverPassword_TilEmailLayout = (TextInputLayout)findViewById(R.id.recoverPassword_TilEmailLayout);
        recoverPassword_TilPhoneLayout = (TextInputLayout)findViewById(R.id.recoverPassword_TilPhoneLayout);

        recoverPassword_TietEmail = (TextInputEditText)findViewById(R.id.recoverPassword_TietEmail) ;
        recoverPassword_TietPhone = (TextInputEditText)findViewById(R.id.recoverPassword_TietPhone) ;

        recoverPassword_TietEmail.addTextChangedListener(new MyTextWatcher(this, recoverPassword_TietEmail, recoverPassword_TilEmailLayout));
        recoverPassword_TietPhone.addTextChangedListener(new MyTextWatcher(this, recoverPassword_TietPhone, recoverPassword_TilPhoneLayout ));

    }

    public void recoverPassword_ClickHandler(View view) {
        switch (view.getId()) {
            case R.id.recoverPassword_TvLogin:
                Intent laIntent = new Intent(this, LoginActivity.class);
                startActivity(laIntent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.recoverPassword_TvRegister:
                Intent raIntent = new Intent(this, RegisterActivity.class);
                startActivity(raIntent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.recoverPassword_BtnRecoverPasswrdOk:
                if( FormValidator.isValidEmail( recoverPassword_TietEmail.getText().toString()) == false
                        && FormValidator.isValidPhone( recoverPassword_TietPhone.getText().toString()) == false ) {
                    recoverPassword_TilEmailLayout.setError(getString(R.string.recover_password_error_message));
                    recoverPassword_TietEmail.requestFocus();
                } else
                {
                    Account account = new Account() ;
                    account.setEmail(Base64.encode(recoverPassword_TietEmail.getText().toString()));

                    account.setPhone(Base64.encode(recoverPassword_TietPhone.getText().toString()));


                    ServerRequest request = new ServerRequest();
                    request.setCommand(Constant.ACTION_RECOVER_PASSWORD_OK);
                    request.setAccount(account);

                    String data = new Gson().toJson(request) ;
                    task = new AsyncTaskHandler(this);
                    task.execute(new String[]{Constant.RECOVER_PASSWORD_URL,data} );
                }
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

        ServerResponse response = new Gson().fromJson(result,ServerResponse.class );
        if(response.isSuccess() == false) {
            dialog.showMessage(getString(R.string.error_dialog_title) , Base64.decode(response.getMessage()));
        }
        else{
            dialog.showMessage(getString(R.string.info_dialog_title) ,getString( R.string.password_emailed_smsed_to_your_registered_email_phone));

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

    public void cancelTask(){
        if (task!=null  ) {
            task.cancel(true);
            task = null ;
        }

    }
}
