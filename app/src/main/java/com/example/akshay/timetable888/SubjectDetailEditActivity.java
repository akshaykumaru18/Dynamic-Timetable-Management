package com.example.akshay.timetable888;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SubjectDetailEditActivity extends AppCompatActivity {

    EditText subjectName;
    EditText numberOfUnits;
    EditText subjectCode;
    Spinner semester_spinner;
    Spinner department_spinner;
    Button selectPdf;

    android.support.v7.widget.Toolbar updateToolbar;

    private String subject_Name;
    private String subject_code;
    private String number_of_units;
    private String semester;
    private String department;
    private ArrayList<String> semesterList;

    //-------------------------------Firebase---------------------------------------------------------------

    FirebaseDatabase mDatabase;
    DatabaseReference mDtabaseReference;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    //-------------------------------------------------------------------------------------------------------------------

    String subject_selected = SubjectActivity.subjectPreferences.getString(SubjectActivity.SUB_PREFERED,"null");
    String subject_sem = SubjectActivity.subjectPreferences.getString(SubjectActivity.SUB_SEM,"null");
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_edit_detail);
        setUpFirebase();
        setupUiViews();
        initToolbar();
        initialDetails();
    }

    public void setupUiViews(){
        subjectName = (EditText)findViewById(R.id.subjectName);
        subjectCode = (EditText)findViewById(R.id.subjectCode);
        numberOfUnits = (EditText)findViewById(R.id.numberOfUnits);
        selectPdf = (Button)findViewById(R.id.SelectPdf);
        updateToolbar = (android.support.v7.widget.Toolbar)findViewById(R.id.ToolbarEdit);

        semesterList = new ArrayList<String>();
        semesterList.add("SEMESTER 1");
        semesterList.add("SEMESTER 2");
        semesterList.add("SEMESTER 3");
        semesterList.add("SEMESTER 4");
        semesterList.add("SEMESTER 5");
        semesterList.add("SEMESTER 6");
        semester_spinner = (Spinner)findViewById(R.id.semester_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,
                semesterList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semester_spinner.setAdapter(adapter);

        semester_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semester = semester_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        department_spinner = (Spinner)findViewById(R.id.departmentSpinner);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.departments));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        department_spinner.setAdapter(adapter1);

        department_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                department = department_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void initialDetails(){

        int position = SubjectActivity.subjectPreferences.getInt(SubjectActivity.SUB_POSITION,0);
        Toast.makeText(getApplicationContext(),SubjectActivity.subjects.get(position),Toast.LENGTH_LONG).show();
        subjectName.setText(SubjectActivity.subjects.get(position));

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query1 = reference.child(getString(R.string.subjects))
                .child(getString(R.string.sem1))
                .child(subject_selected);
               // .equalTo(subject_selected);
               // .child(subject_selected);
              //  .orderByKey()
               // .limitToFirst(100);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
/*                    AddNewSubject addSubject1 = singleSnapshot.getValue(AddNewSubject.class);
                    if (addSubject1 != null) {
                        // Toast.makeText(getApplicationContext(),"Working",Toast.LENGTH_SHORT).show();
                     //   updateAdapter(addSubject1.getSubject_Name(),addSubject1.getSubject_Code(),
                       //         addSubject1.getNumber_of_Units(),addSubject1.getSemester(),addSubject1.getDepartment());
                        Toast.makeText(getApplicationContext(),addSubject1.getSubject_Name(),Toast.LENGTH_LONG).show();
                      //  subjectName.setText(addSubject1.getSubject_Name());
                        //subjectCode.setText(addSubject1.getSubject_Code());
                        //numberOfUnits.setText(addSubject1.getNumber_of_Units());
                       // subjectName.setText(addSubject1.getSubject_Name());
                       // subjectName.getEditableText();
                    }else{
                        Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_LONG).show();
                    }
                    */
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initToolbar() {
        setSupportActionBar(updateToolbar);
        getSupportActionBar().setTitle("Update");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //-------------------------------firebase---------------------------------//
    public void setUpFirebase(){
        mDatabase = FirebaseDatabase.getInstance();
        mDtabaseReference = mDatabase.getReference().child("users");
        mFirebaseAuth = FirebaseAuth.getInstance();
        setupFirebaseAuth();
    }

    //-----------------------------Firebase Setup-----------------------------------------------------------//

    public void setupFirebaseAuth(){
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.i("User Id " , firebaseAuth.getUid().toString());
                }else{

                    Log.i("User Id " , "Not Signed In");
                }

            }
        };
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(mAuthStateListener != null){
            FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();

        }
        return super.onOptionsItemSelected(item);
    }

    public void selectPdf(View view) {


    }

    public void saveUpdate(View view) {


    }
}
