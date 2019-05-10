package com.example.akshay.timetable888;

import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import java.util.ArrayList;
import java.util.Arrays;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.akshay.timetable888.FacultyActivity.SELECTED_FACULTY;
import static com.example.akshay.timetable888.FacultyActivity.faculty_Names;
import static com.example.akshay.timetable888.FacultyActivity.sharedPreferencess;
import static com.example.akshay.timetable888.R.string.selected_faculty;

public class FacultyDetails extends AppCompatActivity implements Add_new_Faculty.AddNewFaculty {

    private CircleImageView facultyImage;
    private android.support.v7.widget.Toolbar toolbar;


    private TextView facultyName;
    private TextView facultyPhoneNumber;
    private TextView facultyEmailid;
    private TextView facultyDepartment;

  //  private ArrayList<String> faculty_Name;
  //  private ArrayList<String> facultyContact;
   // private ArrayList<String> faculty_Emailid;
    //private ArrayList<String> faculty_department;


    private static String selected_faculty_name;
    private static String selected_faculty_phNo;
    private static String selected_faculty_emailId;
    private static String selected_faculty_department;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_details);

        setupUIViews();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initToolbar();
        }
        setupDetails();
    }

    private void setupUIViews() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.ToolbarFacultyDetails);
        }

        facultyImage = (CircleImageView)findViewById(R.id.FacultyPhoto);
        facultyName = (TextView)findViewById(R.id.facultyName);
        facultyPhoneNumber = (TextView)findViewById(R.id.tvFaculty_Ph_number);
        facultyEmailid = (TextView)findViewById(R.id.tvFacultyEmailid);
        facultyDepartment = (TextView)findViewById(R.id.tvFacultyDepartment);


    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Faculty");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupDetails(){

        //String Selected_faculty = sharedPreferencess.getString(String.valueOf(selected_faculty),"Null");
        //Integer.parseInt(Selected_faculty);
    //    Log.i("selected Faculty", Selected_faculty);
        int faculty_position = sharedPreferencess.getInt("position",0);
        facultyImage.setImageResource(R.drawable.lecturer);
        facultyName.setText(FacultyActivity.faculty_Names.get(faculty_position));
        facultyPhoneNumber.setText(FacultyActivity.faculty_phNo.get(faculty_position));
        facultyEmailid.setText(FacultyActivity.faculty_emailId.get(faculty_position));
        facultyDepartment.setText(FacultyActivity.faculty_Department.get(faculty_position));


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void add_new_faculty(String facultyName, String facultyPhoneNo, String facultyEmailid, String faculty_department) {

    }
}
