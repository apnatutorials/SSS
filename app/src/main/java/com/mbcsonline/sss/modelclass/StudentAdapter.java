package com.mbcsonline.sss.modelclass;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.mbcsonline.sss.R;
import com.mbcsonline.sss.utils.Base64;

/**
 * Created by Angel on 12/9/2016.
 */
public class StudentAdapter extends ArrayAdapter<Student> {
    Context context ;
    public StudentAdapter(Context context, int resource, Student[] students) {
        super(context, android.R.layout.simple_list_item_1, students);
        this.context = context ;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Student student = getItem(position);
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.student_row,parent,false);
        }
        TextView homePage_tvStudent = (TextView) convertView.findViewById(R.id.homePage_tvStudent) ;
        homePage_tvStudent.setText(Base64.decode( student.getAme() ));

        if(position % 2 == 0){
            convertView.setBackgroundColor(context.getColor(R.color.colorBeige));
        }
        else{
            convertView.setBackgroundColor(context.getColor(R.color.colorCream));
        }

        return convertView;
    }
}
