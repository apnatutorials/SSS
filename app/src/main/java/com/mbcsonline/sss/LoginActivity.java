package com.mbcsonline.sss;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.AsyncTask;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.util.Log;
import android.view.View;

import com.google.android.gcm.GCMRegistrar;
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

public class LoginActivity extends AppCompatActivity implements AsyncCommunicator {
    TextInputEditText login_TietEmail, login_TietPassword;
    TextInputLayout login_TilPasswordLayout, login_TilEmailLayout;
    CustomDialog dialog ;
    Controller aController;
    AsyncTask<Void, Void, Void> mRegisterTask;
    String myemail;
    AsyncTaskHandler task = null ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        dialog = new CustomDialog(this) ;

        login_TietEmail = (TextInputEditText) findViewById(R.id.login_TietEmail);
        login_TietPassword = (TextInputEditText) findViewById(R.id.login_TietPassword);
        login_TilPasswordLayout = (TextInputLayout) findViewById(R.id.login_TilPasswordLayout);
        login_TilEmailLayout = (TextInputLayout) findViewById(R.id.login_TilEmailLayout);

        login_TietEmail.addTextChangedListener(new MyTextWatcher(this, login_TietEmail, login_TilEmailLayout));
        login_TietPassword.addTextChangedListener(new MyTextWatcher(this, login_TietPassword, login_TilPasswordLayout));

        // --------Vivek:Uncomment to add notification------//

        // Get Global Controller Class object (see application tag in AndroidManifest.xml)
        aController = new Controller(this);

        // Check if Internet present
//        if (!aController.isConnectingToInternet()) {
//
//            // Internet Connection is not present
//            aController.showAlertDialog(LoginActivity.this,
//                    "Internet Connection Error",
//                    "Please connect to Internet connection", false);
//            // stop executing code by return
//            return;
//        }

        // // Getting name, email from intent
        // Intent i = getIntent();
        //
        // name = i.getStringExtra("name");
        // email = i.getStringExtra("email");

        // Make sure the device has the proper dependencies.
        GCMRegistrar.checkDevice(this);

        // Make sure the manifest permissions was properly set
        GCMRegistrar.checkManifest(this);

        // lblMessage = (TextView) findViewById(R.id.lblMessage);

        // Register custom Broadcast receiver to show messages on activity
        registerReceiver(mHandleMessageReceiver, new IntentFilter(
                Config.DISPLAY_MESSAGE_ACTION));

        // Get GCM registration id
        final String regId = GCMRegistrar.getRegistrationId(this);

        // Check if regid already presents
        if (regId.equals("")) {

            // Register with GCM
            GCMRegistrar.register(this, Config.GOOGLE_SENDER_ID);

        } else {

            // Toast.makeText(getApplicationContext(),
            // "Please wait while loading latest stuff...",Toast.LENGTH_LONG).show();//
            // Vivek
            // Device is already registered on GCM Server
            if (GCMRegistrar.isRegisteredOnServer(this)) {

                // Skips registration.
                // Toast.makeText(getApplicationContext(),
                // "Already registered with GCM Server",
                // Toast.LENGTH_LONG).show();

            } else {

                // Try to register again, but not in the UI thread.
                // It's also necessary to cancel the thread onDestroy(),
                // hence the use of AsyncTask instead of a raw thread.

                final Context context = this;
                final String useremail = UserEmailFetcher.getEmail(this);
                myemail = useremail;
                mRegisterTask = new AsyncTask<Void, Void, Void>() {

                    @Override
                    protected Void doInBackground(Void... params) {

                        // Register on our server
                        // On server creates a new user
                        aController.register(context, "test", useremail, regId);//

                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void result) {
                        mRegisterTask = null;
                    }

                };

                // execute AsyncTask
                mRegisterTask.execute(null, null, null);
            }
        }

    }

    //FOR GCM : Vivek
    // Create a broadcast receiver to get message and show on screen
    private final BroadcastReceiver mHandleMessageReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String newMessage = intent.getExtras().getString(
                    Config.EXTRA_MESSAGE);

            // Waking up mobile if it is sleeping
            aController.acquireWakeLock(getApplicationContext());

            // Display message on the screen
            // lblMessage.append(newMessage + "\n");

            // Toast.makeText(getApplicationContext(), "Got Message: " +
            // newMessage, Toast.LENGTH_LONG).show();

            // Releasing wake lock
            aController.releaseWakeLock();
        }
    };

    @Override
    protected void onDestroy() {
        // Cancel AsyncTask
        if (mRegisterTask != null) {
            mRegisterTask.cancel(true);
        }
        try {
            // Unregister Broadcast Receiver
            // unregisterReceiver(mHandleMessageReceiver);

            // Clear internal resources.
            // GCMRegistrar.onDestroy(this);

        } catch (Exception e) {
            Log.e("UnRegister  Error", "> " + e.getMessage());
        }
        super.onDestroy();
    }

    /**
     * Method called when user click on login button
     *
     * @param view
     */
    public void login_ClickHandler(View view) {
        switch (view.getId()) {
            case R.id.login_BtnLoginOk:
                if (FormValidator.isValidEmail(login_TietEmail.getText().toString()) == false) {
                    login_TilEmailLayout.setError(getString(R.string.email_error_message));
                    login_TietEmail.requestFocus();
                } else if (FormValidator.isValidPassword(login_TietPassword.getText().toString()) == false) {
                    login_TilPasswordLayout.setError(getString(R.string.password_error_message));
                    login_TietPassword.requestFocus();
                } else {
                    Account account = new Account() ;
                    account.setEmail(Base64.encode(login_TietEmail.getText().toString()));
                    account.setPassword(Base64.encode(login_TietPassword.getText().toString()));

                    ServerRequest request = new ServerRequest();
                    request.setCommand(Constant.ACTION_LOGIN_OK);
                    request.setAccount(account);

                    String data = new Gson().toJson(request) ;
                    task = new AsyncTaskHandler(this);
                    task.execute(new String[]{Constant.LOGIN_URL,data} );
                }
                break;
            case R.id.login_TvRegister:
                Intent raIntent = new Intent(this, RegisterActivity.class);

                startActivity(raIntent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;
            case R.id.login_TvRecoverPassword:
                Intent rpIntent = new Intent(this, RecoverPasswordActivity.class);
                startActivity(rpIntent);
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                break;

        }

    }

    @Override
    public void onProgressUpdate(String progress) {

    }

    @Override
    public void onProcessResult(String result) {
        task =null ;
        ServerResponse response = new Gson().fromJson(result,ServerResponse.class );
        if(response.isSuccess() == false) {
            dialog.showMessage(getString(R.string.error_dialog_title) , Base64.decode(response.getMessage()));
        }
        else{

            Account account = response.getAccount()[0] ;
           // dialog.showMessage(R.string.info_dialog_title, "Student length : " + response.get);

            if(account != null && account.getId()>-1 && account.getType() == Account.TYPE_BUSINESS_PARENT ){
                Constant.loginResponse = response ;
                // Go to homepage
                // pass serverResponse
                if(account.getStatus() == Account.STATUS_PENDING){
                    Intent hpIntent = new Intent(this, CodeValidatorActivity.class);
                    startActivity(hpIntent);
                    overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                }
                else{

                    if(response.getStudents() != null && response.getStudents().length >0){
                        Intent hpIntent = new Intent(this, HomePageActivity.class);
                        startActivity(hpIntent);
                        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
                    }
                    else
                    {
                        dialog.showMessage( getString(R.string.error_dialog_title ) , getString( R.string.no_student_found));
                    }

                }

            }
            else{
                dialog.showMessage(getString(R.string.error_dialog_title) , getString(R.string.only_parent_can_access_this_app));
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

    @Override
    public void onBackPressed() {
        finish();
    }
}
