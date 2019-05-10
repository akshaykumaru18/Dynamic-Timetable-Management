package com.example.akshay.timetable888;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akshay.timetable888.Utils.LetterImageView;
import com.example.akshay.timetable888.modules.AddNewSubject;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class SubjectActivity extends AppCompatActivity implements Add_subject_dialog.AddNewSubject {


    //-------------------------------Firebase---------------------------------------------------------------
    FirebaseDatabase mDatabase;
    DatabaseReference mDtabaseReference;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    //-------------------------------------------------------------------------------------------------------------------

    private ListView subjectListView;
    private Toolbar subjectToolbar;
    public static ArrayList<String> subjects;
    public static ArrayList<String> semesters;
    public static ArrayList<String> departments;
    public static SharedPreferences subjectPreferences;
    public static String SUB_PREFERED = "SUB_PREFERED";
    public static String SUB_SEM = "SUB_SEM";
    public static String SUB_POSITION;

    int position2;
    SubjectAdapter subjectAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject);

        setupUIViews();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initToolbar();
        }

        setupListView();
        setUpFirebase();

    }
    private void setupUIViews() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            subjectToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.ToolbarSubject);
            subjectListView = (ListView)findViewById(R.id.lvSubject);
            registerForContextMenu(subjectListView);
        }
        subjectPreferences = getSharedPreferences("Subjects",MODE_PRIVATE);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initToolbar() {
        setSupportActionBar(subjectToolbar);
        getSupportActionBar().setTitle("Subjects");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //-------------------------------firebase---------------------------------//
    public void setUpFirebase(){
        mDatabase = FirebaseDatabase.getInstance();
        mDtabaseReference = mDatabase.getReference().child("users");
        mFirebaseAuth = FirebaseAuth.getInstance();
        setupFirebaseAuth();
    }

    //-----------------------------------------------------------------------------//
    private void setupListView(){

        subjects = new ArrayList<String>();
        subjects.add("Add subject");

        semesters = new ArrayList<String>();
        semesters.add("Add semester");

        departments = new ArrayList<String>();
        departments.add("Department");

        subjectAdapter = new SubjectAdapter(this,R.layout.subject_single_item, subjects , semesters,departments);
        subjectListView.setAdapter(subjectAdapter);
        registerForContextMenu(subjectListView);

        subjectListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                position2= position;
                subjectPreferences.edit().putString(SUB_PREFERED,subjects.get(position)).apply();
                subjectPreferences.edit().putString(SUB_SEM,semesters.get(position)).apply();
                subjectPreferences.edit().putInt(SUB_POSITION,position2);

            }
        });
        initialSubjects();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.lvSubject){

            menu.add("Delete");
        }

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {

         if(item.getTitle() == "Delete"){
           deleteSubject(position2);
        }
        return super.onContextItemSelected(item);
    }

    public void editSubject(){
        Toast.makeText(getApplicationContext(),"Edit Now",Toast.LENGTH_LONG).show();
        Intent intent = new Intent(SubjectActivity.this,SubjectDetailEditActivity.class);
        startActivity(intent);
    }

    public void deleteSubject(int SubjectPosition){
        String deleteName = subjects.get(SubjectPosition);
        subjects.remove(SubjectPosition);
        semesters.remove(SubjectPosition);
        subjectAdapter.notifyDataSetChanged();

        DatabaseReference removeSubject = FirebaseDatabase.getInstance()
                .getReference()
                .child(MainActivity.department_of_hod)
                .child("subjects")
               // .child(getString(R.string.sem1))
                .child(deleteName);
        removeSubject.removeValue();

    }

    @Override
    public void Add_New_Subject(final String subjectName, final String subjectCode, final String number_of_units,
                                final String semester, final String department) {

        AddNewSubject addSubject = new AddNewSubject();
        addSubject.setSubject_Name(subjectName);
        addSubject.setSubject_Code(subjectCode);
        addSubject.setNumber_of_Units(number_of_units);
        addSubject.setSemester(semester);
        addSubject.setDepartment(department);
        //--------------------------------------------------------------------------------------------------------------
        FirebaseDatabase.getInstance().getReference()
                .child(MainActivity.department_of_hod)
                .child("subjects")
                .child(subjectName)
                .setValue(addSubject)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                updateAdapter(subjectName,subjectCode,number_of_units,semester,department);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
            }
        });

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query1 = reference
                .child(MainActivity.department_of_hod)
                .child(getString(R.string.subjects))
                .child(MainActivity.department_of_hod)
                .equalTo(subjectName);
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    AddNewSubject addNewSubject = singleSnapshot.getValue(AddNewSubject.class);
                    if(addNewSubject != null){
                        updateAdapter(subjectName,subjectCode,number_of_units,semester,department);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Error Not Added",Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void updateAdapter(final String subjectName, String subjectCode, String number_of_units,
                              final String semester,String department) {

        if(subjects.get(0).equalsIgnoreCase("Add Subject")){
            subjects.set(0,subjectName);
            subjectAdapter.notifyDataSetChanged();
        }
        if (semesters.get(0).equalsIgnoreCase("Add Semester")){
            semesters.set(0,semester);
            subjectAdapter.notifyDataSetChanged();
        }
        if(departments.get(0).equalsIgnoreCase("Department")){
            departments.set(0,department);
            subjectAdapter.notifyDataSetChanged();
        }
        else{
            subjects.add(subjectName);
            semesters.add(semester);
            departments.add(department);
            subjectAdapter.notifyDataSetChanged();
        }
    }

    public void initialSubjects(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query1 = reference
                .child(MainActivity.department_of_hod)
                .child(getString(R.string.subjects))
                .orderByKey()
                .limitToFirst(100);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    AddNewSubject addSubject1 = singleSnapshot.getValue(AddNewSubject.class);
                    if (addSubject1 != null) {
                        updateAdapter(addSubject1.getSubject_Name(),addSubject1.getSubject_Code(),
                                addSubject1.getNumber_of_Units(),addSubject1.getSemester(),addSubject1.getDepartment());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class SubjectAdapter extends ArrayAdapter {

        private int resource;
        private LayoutInflater layoutInflater;
        private ArrayList<String> subjects;
        private ArrayList<String> semesters;
        private ArrayList<String> department;

        public SubjectAdapter(Context context, int resource, ArrayList<String> objects,
                              ArrayList<String> semesters,ArrayList<String> department) {
            super(context, resource, objects);

            this.resource = resource;
            this.subjects = objects;
            this.semesters = semesters;
            this.department = department;
            layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

          SubjectActivity.SubjectAdapter.ViewHolder viewHolder;

            if (convertView == null){
                viewHolder = new ViewHolder();
                convertView = layoutInflater.inflate(resource,null);
                viewHolder.ivLogo = (LetterImageView)convertView.findViewById(R.id.ivLetterSubject);
                viewHolder.subject = (TextView)convertView.findViewById(R.id.tvSubject);
                viewHolder.semester = (TextView)convertView.findViewById(R.id.tvSem);
                viewHolder.department = (TextView)convertView.findViewById(R.id.tvDept);
                convertView.setTag(viewHolder);
            }else{

                viewHolder = (ViewHolder) convertView.getTag();
            }
            viewHolder.ivLogo.setOval(true);
            viewHolder.ivLogo.setLetter(subjects.get(position).charAt(0));
            viewHolder.subject.setText(subjects.get(position));
            viewHolder.semester.setText(semesters.get(position));
            viewHolder.department.setText(department.get(position));

            return convertView;
        }

        class ViewHolder{
            private LetterImageView ivLogo;
            private TextView subject;
            private TextView semester;
            private TextView department;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_subject,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){

            case R.id.addSubject :
                Add_subject_dialog add_subject_dialog = new Add_subject_dialog();
                add_subject_dialog.show(getSupportFragmentManager(),null);
                break;

            case android.R.id.home :
                onBackPressed();

        }
        return super.onOptionsItemSelected(item);
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
}


