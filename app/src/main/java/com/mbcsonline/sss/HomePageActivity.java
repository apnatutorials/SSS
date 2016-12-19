package com.mbcsonline.sss;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.mbcsonline.sss.modelclass.Constant;
import com.mbcsonline.sss.modelclass.ServerRequest;
import com.mbcsonline.sss.modelclass.Student;
import com.mbcsonline.sss.modelclass.StudentAdapter;
import com.mbcsonline.sss.utils.AsyncCommunicator;
import com.mbcsonline.sss.utils.AsyncTaskHandler;
import com.mbcsonline.sss.utils.CustomDialog;

public class HomePageActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AsyncCommunicator {
    CustomDialog dialog = new CustomDialog(this);
    ListView homePage_lvStudent = null ;
    AsyncTask task = null ;
    StudentAdapter adapter = null ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page_activity);

        adapter = new StudentAdapter(this,-1,Constant.loginResponse.getStudents()) ;
        homePage_lvStudent = (ListView) findViewById(R.id.homePage_lvStudent);
        homePage_lvStudent.setAdapter(adapter);
        homePage_lvStudent.setOnItemClickListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
        Student student = adapter.getItem(position) ;
        //
       // studentId = Integer.parseInt( bundle.getString("studentId")) ;

        Toast.makeText(this, "studentId : " + student.getId() , Toast.LENGTH_SHORT).show();
        ServerRequest request = new ServerRequest();
        dialog = new CustomDialog(this);
        request.setCommand(Constant.ACTION_GET_GPS);
        request.setStudentId(student.getId());
        request.setDatetime("1970-01-01");
        String data = new Gson().toJson(request) ;
        task = new AsyncTaskHandler(this);
        task.execute(new String[]{Constant.GPS_URL,data} );
        //
        Intent raIntent = new Intent(this, MapsActivity.class);
        Bundle bundle = new Bundle();
        bundle.putString("studentId", student.getId()+"");
        raIntent.putExtras(bundle);
        startActivity(raIntent);
        overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    public void onProgressUpdate(String progress) {

    }

    @Override
    public void onProcessResult(String result) {

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
