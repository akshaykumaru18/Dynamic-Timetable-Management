package com.example.akshay.timetable888;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akshay.timetable888.modules.Faculty;
import com.example.akshay.timetable888.modules.Notification;
import com.example.akshay.timetable888.modules.User;
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

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private Toolbar supportActionBar;

    public static ArrayList<String> facultyList;
    public static ArrayList<String> subjectList;

    public static String Current_Lecturer;
    public static String department_of_hod;

    //--------------------------Firebase--------------------------//
    FirebaseDatabase mDatabase;
    DatabaseReference mDtabaseReference;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    //-------------------------------------------------------------------------//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupUIViews();
        initToolbar();
        setupListView();
        setupFirebaseAuth();
        initFaculty();

    }
    //---------------------Firebase Setup--------------------//
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

    private void setupUIViews() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
           toolbar = (Toolbar) findViewById(R.id.ToolbarMain);

        }
        listView = (ListView) findViewById(R.id.lvMain);

        //-----------------------firebase----------------------------//
        mDatabase = FirebaseDatabase.getInstance();
        mDtabaseReference = mDatabase.getReference().child("users");
        mFirebaseAuth = FirebaseAuth.getInstance();

    }


    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Timetable");
    }

    private void setupListView() {
        String[] title = getResources().getStringArray(R.array.Main);
        String[] description = getResources().getStringArray(R.array.Description);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, title, description);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0 : {
                        Intent weekActivity = new Intent(MainActivity.this,SelectSemActivity.class);
                        startActivity(weekActivity);
                        break;
                    }
                    case 1:{
                        Intent facultyActivity = new Intent(MainActivity.this,SubjectActivity.class);
                        startActivity(facultyActivity);
                       // Toast.makeText(getApplicationContext(),"Section Under Construction",Toast.LENGTH_SHORT).show();
                        break;
                    }

                    case 2:{
                        Intent facultyActivity = new Intent(MainActivity.this,FacultyActivity.class);
                        startActivity(facultyActivity);
                       // Toast.makeText(getApplicationContext(),"Section Under Construction",Toast.LENGTH_SHORT).show();
                        break;
                    }
             /*
                    case 3 :{
                        Intent facultyTimetableActivity = new Intent(MainActivity.this,FacultySchedule.class);
                        startActivity(facultyTimetableActivity);

                    }
                    */

                }

            }
        });
    }


    public void initFaculty(){

        facultyList = new ArrayList<String>();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query1 = reference
                //.child(Current_Lecturer)
               // .child(department_of_hod)
                .child("Faculty")
                .child("Lecturers");
        //.orderByKey();
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Faculty faculty1 = singleSnapshot.getValue(Faculty.class);
                    if(faculty1 != null){
                      facultyList.add(faculty1.getLecturerName());
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Faculty List empty",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Error loading",Toast.LENGTH_SHORT).show();
            }
        });


        //---------------------------------------------------------------------------------------------------------------




        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
        Query query3 = reference2
                .child("users")
                .orderByKey();

        query3.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Faculty faculty1 = singleSnapshot.getValue(Faculty.class);
                    if(faculty1 != null){
                        if(faculty1.getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                          /*  Toast.makeText(getApplicationContext(),"True",Toast.LENGTH_SHORT).show(); */
                            Toast.makeText(getApplicationContext(),faculty1.getHodName(),Toast.LENGTH_SHORT).show();
                            Current_Lecturer = faculty1.getHodName();

                        }else{
                           // Toast.makeText(getApplicationContext(),"False",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Null Object Reference",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


//---------------------------------------------------------------------------------------------------------------------------------


        DatabaseReference reference3 = FirebaseDatabase.getInstance().getReference();
        Query query = reference3
                .child("users")
                .orderByKey();
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    User user = singleSnapshot.getValue(User.class);
                    if(user != null){
                        if(user.getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            department_of_hod = user.getHodDepartment();
                            Toast.makeText(getApplicationContext(),department_of_hod,Toast.LENGTH_SHORT).show();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        /*------------------------------------------------------------------------------------------------------------------*/
        /*-----------------------------------------------------------------------------------------------------------------------

        DatabaseReference reference18 = FirebaseDatabase.getInstance().getReference();
        Query query2 = reference18
                .child(MainActivity.department_of_hod)
                .child("subjects");
        // .child(getString(R.string.timetable_node))
        //.child(selected_sem)
        //  .child(selected_day)
        // .orderByKey()
        // .limitToFirst(100);

        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    AddNewSubject addNewSubject = singleSnapshot.getValue(AddNewSubject.class);
                    if(addNewSubject != null){
                        subject_List = new ArrayList<String>();
                        Toast.makeText(getApplicationContext(),addNewSubject.getSubject_Name(),Toast.LENGTH_SHORT).show();
                        subject_List.add(addNewSubject.getSubject_Name());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
            }
        });
*/


    }
    public class SimpleAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater layoutInflater;
        private TextView title, description;
        private String[] titleArray;
        private String[] descriptionArray;
        private ImageView imageView;

        public SimpleAdapter(Context context, String[] title, String[] description) {

            this.mContext = context;
            titleArray = title;
            descriptionArray = description;
            layoutInflater = LayoutInflater.from(context);

        }

        @Override
        public int getCount() {
            return titleArray.length;
        }

        @Override
        public Object getItem(int position) {
            return titleArray[position];
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.main_acitvity_single_item, null);
            }
            title = (TextView) convertView.findViewById(R.id.tvmain);
            description = (TextView) convertView.findViewById(R.id.tvDescription);
            imageView = (ImageView) convertView.findViewById(R.id.ivmain);

            title.setText(titleArray[position]);
            description.setText(descriptionArray[position]);

            if (titleArray[position].equalsIgnoreCase("Timetable")) {
                imageView.setImageResource(R.drawable.timetable);
            } else if (titleArray[position].equalsIgnoreCase("Faculty")) {
                imageView.setImageResource(R.drawable.faculty);
            } else {
                imageView.setImageResource(R.drawable.subject);
            }
            return convertView;
        }
    }

    //------------------ Checking authentication State ---------------------------//
    private void checkAuthenticationState() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user == null){
            Log.i("Status","Check Authentication State Navigating Back to LogIn");
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
      getMenuInflater().inflate(R.menu.logout,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.logout: {

                Notification removeNotifcation = new Notification();
                removeNotifcation.setFacultyName("Test");
                removeNotifcation.setFrom("Developer");
                removeNotifcation.setTo("Tester");
                removeNotifcation.setDate("1");
                removeNotifcation.setTime("1");
                removeNotifcation.setToken_id("");
                removeNotifcation.setCurrent_id("");

                FirebaseDatabase.getInstance().getReference()
                        .child("Notification")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .child("notification")
                        .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(removeNotifcation)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    mFirebaseAuth.signOut();
                                    Intent intent = new Intent(MainActivity.this,Authentication.class);
                                    startActivity(intent);
                                    finish();
                                }else{

                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                    }
                });

            }




        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if(mAuthStateListener != null){
            FirebaseAuth.getInstance().addAuthStateListener(mAuthStateListener);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkAuthenticationState();
    }


    @Override
    protected void onStop() {
        super.onStop();
        if(mAuthStateListener != null){
            FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
        }
    }


}
