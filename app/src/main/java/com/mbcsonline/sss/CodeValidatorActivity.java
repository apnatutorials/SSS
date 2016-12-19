package com.mbcsonline.sss;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.gson.Gson;
import com.mbcsonline.sss.R;
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

public class CodeValidatorActivity extends AppCompatActivity implements AsyncCommunicator {
    CustomDialog dialog ;
    private static final int REQUEST_TYPE_REGERATE_CODE = 0 ;
    private static final int REQUEST_TYPE_FINSIH_OPERATION = 1 ;
    private int requestType = REQUEST_TYPE_FINSIH_OPERATION ;
    AsyncTaskHandler task = null ;
    TextInputLayout codeValidator_TilVerificationCodeLayout ;
    TextInputEditText codeValidator_TietVerificationCode ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.code_validator_activity);
        dialog = new CustomDialog(this) ;

        codeValidator_TilVerificationCodeLayout = (TextInputLayout) findViewById(R.id.codeValidator_TilVerificationCodeLayout);
        codeValidator_TietVerificationCode = (TextInputEditText) findViewById( R.id.codeValidator_TietVerificationCode );
        codeValidator_TietVerificationCode.addTextChangedListener(new MyTextWatcher(this, codeValidator_TietVerificationCode, codeValidator_TilVerificationCodeLayout));
    }

    public void codeValidator_ClickHandler(View view){
        switch (view.getId()){
            case R.id.codeValidator_finish:
                String code = codeValidator_TietVerificationCode.getText().toString() ;
                if(FormValidator.isValidVerificationCode(code) == true) {
                    ServerRequest request = new ServerRequest();
                    request.setCommand(Constant.ACTION_CODE_VALIDATOR_OK);
                    request.setAccountId(Constant.loginResponse.getAccount()[0].getId());
                    request.setVerificationCode( Integer.parseInt(code.toString()));

                    String data = new Gson().toJson(request);
                    AsyncTaskHandler task = new AsyncTaskHandler(this);
                    task.execute(new String[]{Constant.CODE_VALIDATOR_URL, data});
                }
                else{
                    codeValidator_TilVerificationCodeLayout.setError(getString(R.string.code_verification_message));
                    codeValidator_TietVerificationCode.requestFocus();
                }
                break;

            case R.id.codeValidator_BtnResendCode:

                    ServerRequest request = new ServerRequest();
                    request.setCommand(Constant.ACTION_RGENERATE_VERIFICATION_CODE_OK);
                    request.setAccountId(Constant.loginResponse.getAccount()[0].getId());


                    String data = new Gson().toJson(request);
                    task = new AsyncTaskHandler(this);
                    task.execute(new String[]{Constant.REGENERATE_VERIFICATION_CODE_URL, data});

                break;
        }
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
                // Move user to Login page
                // pass serverResponse
                Intent laIntent = new Intent(this, LoginActivity.class);
                startActivity(laIntent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
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
