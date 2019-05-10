package com.example.akshay.timetable888;

import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akshay.timetable888.Utils.LetterImageView;
import com.example.akshay.timetable888.modules.Faculty;
import com.example.akshay.timetable888.modules.Faculty_Schedule;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class FacultySchedule extends AppCompatActivity {


    //-------------------------------Firebase-------------------------------------------------------

    FirebaseDatabase mDatabase;
    DatabaseReference mDtabaseReference;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    //----------------------------------------------------------------------------------------------

    private Toolbar facultyScheduleToolbar;
    private ListView facultySchedule;
    public static ArrayList<String> subject;
    public static ArrayList<String> time;
    public static ArrayList<String> department;

    FacultyScheduleAdapter facultyScheduleAdapter;

    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty_schedule);
        setupUIViews();
        setUpFirebase();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initToolbar();
            setUpNavigationBar();
        }
        setupListView();

    }

    private void setupUIViews() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            facultyScheduleToolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.ToolbarFacultySchedule);
            facultySchedule = (ListView)findViewById(R.id.lvFacultySchedule);
            drawerLayout = (DrawerLayout)findViewById(R.id.drawerLayout);
            navigationView = (NavigationView)findViewById(R.id.navigationView);

        }


    }

    private void setUpNavigationBar(){
        actionBarDrawerToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {

                switch (menuItem.getItemId()){
                    case R.id.sem_1 :
                        initialSubjects("SEMESTER 1");
                        //initialSubjects(menuItem.getTitle().toString());
                        Toast.makeText(getApplicationContext(),menuItem.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getApplicationContext(),"Semester 1",Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.sem_2 :
                        initialSubjects("SEMESTER 2");
                        Toast.makeText(getApplicationContext(),menuItem.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.sem_3 :
                        initialSubjects("SEMESTER 3");
                        Toast.makeText(getApplicationContext(),menuItem.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.sem_4:
                        initialSubjects("SEMESTER 4");
                        Toast.makeText(getApplicationContext(),menuItem.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.sem_5 :
                        initialSubjects("SEMESTER 5");
                        Toast.makeText(getApplicationContext(),menuItem.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        break;

                    case R.id.sem_6 :
                        initialSubjects("SEMESTER 6");
                        Toast.makeText(getApplicationContext(),menuItem.getTitle().toString(),Toast.LENGTH_SHORT).show();
                        break;
                }
                return true;
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initToolbar() {
        setSupportActionBar(facultyScheduleToolbar);
        getSupportActionBar().setTitle("Your Schedule");
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
        subject = new ArrayList<String>();
        subject.add("Add subject");

        time = new ArrayList<String>();
        time.add("Time");

        department = new ArrayList<String>();
        department.add("Department");

         facultyScheduleAdapter = new FacultyScheduleAdapter(this,R.layout.faculty_schedule_single_item,
                subject,time,department);
        facultySchedule.setAdapter(facultyScheduleAdapter);

        initialSubjects("SEMESTER 1");

    }
    public void updateAdapter(final String subjectName, String slot, String department) {

        if(subject.get(0).equalsIgnoreCase("Add Subject")){
            subject.set(0,subjectName);
            facultyScheduleAdapter.notifyDataSetChanged();

        }
        if (time.get(0).equalsIgnoreCase("Time")){
            Toast.makeText(getApplicationContext(), slot, Toast.LENGTH_SHORT).show();
            time.set(0,slot);
            facultyScheduleAdapter.notifyDataSetChanged();
        }
        if(this.department.get(0).equalsIgnoreCase("Department")){
            this.department.set(0,department);
            facultyScheduleAdapter.notifyDataSetChanged();
        }
        else{
            subject.add(subjectName);
            time.add(slot);
            this.department.add(department);
            facultyScheduleAdapter.notifyDataSetChanged();
        }

        //String selected_day = WeekActivity.sharedPreferences.getString(WeekActivity.SELECTED_DAY,null);
    }

    public void initialSubjects(final String semester){

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
        Query query = reference2
                .child("users")
                .orderByKey();

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    final Faculty faculty = singleSnapshot.getValue(Faculty.class);
                    if(faculty != null){

                        if(faculty.getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            if(faculty.getSecurity_level().equals("0")){
                                break;
                            }else if(faculty.getSecurity_level().equals("1")){

                                /*For Lecturer Schedule */
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                Query query1 = reference
                                         //.child(LecturerActivity.CurrentFacultyHod)
                                        .child("COMPUTER SCIENCE AND ENGINEERING")
                                        .child("Faculty")
                                        .child("Lecturers")
                                        .child(LecturerActivity.CurrentFaculty)
                                        .child("subject Handled")
                                        .child(semester)
                                        .orderByValue();

                                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){

                                            Faculty_Schedule faculty_schedule = singleSnapshot.getValue(Faculty_Schedule.class);
                                            if (faculty_schedule != null) {
                                                updateAdapter(faculty_schedule.getSubject_Name(),faculty_schedule.getSlot(),
                                                        faculty_schedule.getDepartment());

                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(),"You don't have any active Schedule",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });
                                break;
                            }else if(faculty.getSecurity_level().equals("2")){
                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                                Query query1 = reference
                                        .child(MainActivity.department_of_hod)
                                        .child("Faculty")
                                        .child("Lecturers")
                                        .child(MainActivity.Current_Lecturer)
                                        .child("subject Handled")
                                        .child(semester)
                                        .orderByValue();
                                query1.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                                        for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                            Faculty_Schedule faculty_schedule = singleSnapshot.getValue(Faculty_Schedule.class);
                                            if (faculty_schedule != null) {
                                                Toast.makeText(getApplicationContext(),faculty_schedule.getSlot(),Toast.LENGTH_SHORT).show();
                                               updateAdapter(faculty_schedule.getSubject_Name(),faculty_schedule.getSlot(),
                                                        faculty_schedule.getDepartment());
                                            }
                                            else{
                                                Toast.makeText(getApplicationContext(),"You don't have any active Schedule",Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                    @Override
                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                        Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                                    }
                                });


                                break;
                            } else{
                                Toast.makeText(getApplicationContext(),"Access denied",Toast.LENGTH_SHORT).show();
                            }
                        }

                    }else{
                        Toast.makeText(getApplicationContext(),"Null object reference",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });


    }

    public static class FacultyScheduleAdapter extends ArrayAdapter {

        private int resource;
        private LayoutInflater layoutInflater;
        // private String[] week = new String[]{};
        private ArrayList<String> subject;
        private ArrayList<String> time;
        private ArrayList<String> department;

        public FacultyScheduleAdapter(Context context, int resource, ArrayList<String> objects,
                              ArrayList<String> time, ArrayList<String> department) {
            super(context, resource, objects);

            this.resource = resource;
            this.subject = objects;
            this.time = time;
            this.department = department;
            layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            FacultySchedule.FacultyScheduleAdapter.ViewHolder viewHolder;

            if (convertView == null){
                viewHolder = new FacultySchedule.FacultyScheduleAdapter.ViewHolder();
                convertView = layoutInflater.inflate(resource,null);
                viewHolder.ivLogo = (LetterImageView)convertView.findViewById(R.id.ivLetterSubject);
                viewHolder.subject = (TextView)convertView.findViewById(R.id.tvSubject);
                viewHolder.timeview = (TextView)convertView.findViewById(R.id.tvTime);
                viewHolder.department = (TextView)convertView.findViewById(R.id.tvDept);
                convertView.setTag(viewHolder);
            }else{

                viewHolder = (FacultySchedule.FacultyScheduleAdapter.ViewHolder) convertView.getTag();
            }
            viewHolder.ivLogo.setOval(true);
            viewHolder.ivLogo.setLetter(subject.get(position).charAt(0));
            viewHolder.subject.setText(subject.get(position));
            viewHolder.timeview.setText(time.get(position ));
            viewHolder.department.setText(department.get(position));

            return convertView;
        }

        class ViewHolder{
            private LetterImageView ivLogo;
            private TextView subject;
            private TextView timeview;
            private TextView department;
        }
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
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(actionBarDrawerToggle.onOptionsItemSelected(item)){
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
