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
import com.example.akshay.timetable888.modules.Faculty;
import com.example.akshay.timetable888.modules.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import static com.example.akshay.timetable888.R.string.selected_faculty;


public class FacultyActivity extends AppCompatActivity implements Add_new_Faculty.AddNewFaculty {


    //-------------------------------Firebase---------------------------------------------------------------
    FirebaseDatabase mDatabase;
    DatabaseReference mDtabaseReference;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    private ListView listView;
    private Toolbar toolbar;

    public static SharedPreferences sharedPreferencess;
    public static String SELECTED_FACULTY;
    public static int SELECTED_FACULTY_position;

    static ArrayList<String> faculty_Names ;
    static ArrayList<String> faculty_phNo;
    static ArrayList<String> faculty_emailId;
    static ArrayList<String> faculty_Department;
    Faculty_Adapter faculty_adapter;

    String hodDept = MainActivity.department_of_hod;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_faculty);

        setupUIViews();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initToolbar();
        }
        setupListView();
        setUpFirebase();
        Toast.makeText(getApplicationContext(),MainActivity.department_of_hod,Toast.LENGTH_SHORT).show();
    }


    private void setupUIViews() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar = (Toolbar) findViewById(R.id.ToolbarFaculty);

        }
        listView = (ListView) findViewById(R.id.lvFaculty);
        sharedPreferencess = getSharedPreferences("myFaculty",MODE_PRIVATE);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Faculty");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    //-------------------------------firebase---------------------------------//
    public void setUpFirebase(){
        mDatabase = FirebaseDatabase.getInstance();
        mDtabaseReference = mDatabase.getReference().child("users");
        mFirebaseAuth = FirebaseAuth.getInstance();
        setupFirebaseAuth();
    }


    private void setupListView(){

         faculty_Names = new ArrayList<>();
         faculty_phNo = new ArrayList<>();
         faculty_emailId = new ArrayList<>();
         faculty_Department = new ArrayList<>();

        faculty_Names.add("Add Faculty Name");
        faculty_phNo.add("Phone Number");
        faculty_emailId.add("Email-Id");
        faculty_Department.add("Department");


         faculty_adapter = new Faculty_Adapter(this,R.layout.faculty_single_item,
                 faculty_Names,faculty_Department);
        listView.setAdapter(faculty_adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                SELECTED_FACULTY = String.valueOf(faculty_Names.get(position));
                SELECTED_FACULTY_position = position;
                sharedPreferencess.edit().putString(String.valueOf(selected_faculty),SELECTED_FACULTY).apply();
                Log.i("Faculty position", String.valueOf(SELECTED_FACULTY));
                startActivity(new Intent(FacultyActivity.this,FacultyDetails.class));
                sharedPreferencess.edit().putInt("position",SELECTED_FACULTY_position).apply();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String deleteName = faculty_Names.get(position);

                String facalty_of_dept = MainActivity.department_of_hod;
               // Toast.makeText(getApplicationContext(),deleteName,Toast.LENGTH_LONG).show();
                faculty_Names.remove(position);
                faculty_phNo.remove(position);
                faculty_emailId.remove(position);
                faculty_Department.remove(position);
                faculty_adapter.notifyDataSetChanged();

                DatabaseReference removeFaculty = FirebaseDatabase.getInstance()
                        .getReference()
                       // .child(deleteName)
                       // .child(MainActivity.department_of_hod)
                        .child("Faculty")
                        .child("Lecturers")
                        .child(deleteName);
                removeFaculty.removeValue().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
                    }
                });
                return true;
            }
        });
        initalFacultyList();
    }

    @Override
    public void add_new_faculty(final String facultyName, final String facultyPhoneNo,
                                final String facultyEmailid, final String faculty_department) {


        FirebaseAuth.getInstance().createUserWithEmailAndPassword(facultyEmailid,"password")
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {

                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        addToUser(facultyName,facultyPhoneNo,facultyEmailid,faculty_department);
                        Faculty faculty = new Faculty();
                        faculty.setLecturerName(facultyName);
                        faculty.setPhoneNumber(facultyPhoneNo);
                        faculty.setEmailID(facultyEmailid);
                        faculty.setDepartment(faculty_department);
                        faculty.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());

                        FirebaseDatabase.getInstance().getReference()
                                .child(MainActivity.department_of_hod)
                                .child("Faculty")
                                .child("Lecturers")
                                .child(facultyName)
                                .setValue(faculty)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                        FirebaseDatabase.getInstance().getReference()
                                .child(MainActivity.department_of_hod)
                                .child("Faculty")
                                .child("Lecturers")
                                .child(facultyName)
                                .setValue(faculty)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {

                            }
                        });

                        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                        Query query1 = reference
                                .child(MainActivity.department_of_hod)
                                .child("Faculty")
                                .child("Lecturers")
                                .orderByKey()
                                .equalTo(facultyName);
                        query1.addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                    Faculty faculty1 = singleSnapshot.getValue(Faculty.class);
                                    if(faculty1 != null){

                                        updateNewFaculty(faculty1.getLecturerName(),faculty1.getPhoneNumber(),
                                                faculty1.getEmailID(),faculty1.getDepartment());
                                        faculty_adapter.notifyDataSetChanged();
                                    }
                                }
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {
                                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
                            }
                        });

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    private void addToUser(String facultyName, String facultyPhoneNo, String facultyEmailid, String faculty_department) {
        User user = new User();
        user.setLecturerName(facultyName);
        user.setPhoneNumber(facultyPhoneNo);
        user.setSecurity_level("1");
        user.setProfile_image("");
        user.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
        user.setEmailID(facultyEmailid);

        FirebaseDatabase.getInstance().getReference()
                .child("users")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.i("User Id Registered ", FirebaseAuth.getInstance().getUid());
                sendVerificationEmail();
                //FirebaseAuth.getInstance().signOut();

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Verification Email Sent" ,Toast.LENGTH_LONG).show();
                        Log.d("Email Status ", "Email Sent");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_LONG).show();
                    Log.d("Email Status ", "Email Not Sent");
                }
            });
        }
    }

    public  void updateNewFaculty(String facultyName, String facultyPhoneNo, String facultyEmailid, String faculty_department){

       String initDemoFaculty = faculty_Names.get(0);

        if(initDemoFaculty.equalsIgnoreCase("Add Faculty Name")){
            faculty_Names.set(0,facultyName);
            faculty_phNo.set(0,facultyPhoneNo);
            faculty_emailId.set(0,facultyEmailid);
            faculty_Department.set(0,faculty_department);
            faculty_adapter.notifyDataSetChanged();
        }
        else{
            faculty_Names.add(facultyName);
            faculty_phNo.add(facultyPhoneNo);
            faculty_emailId.add(facultyEmailid);
            faculty_Department.add(faculty_department);
            faculty_adapter.notifyDataSetChanged();
        }

    }

    public  void initalFacultyList(){

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query1 = reference
                .child(MainActivity.department_of_hod)
                .child("Faculty")
                .child("Lecturers");
                //.orderByKey();
        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    Faculty faculty1 = singleSnapshot.getValue(Faculty.class);
                    if(faculty1 != null){
                        updateNewFaculty(faculty1.getLecturerName(),faculty1.getPhoneNumber(),
                                faculty1.getEmailID(),faculty1.getDepartment());
                    }else{
                        Toast.makeText(getApplicationContext(),"No Faculty Present",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),"Error loading : " + databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class Faculty_Adapter extends ArrayAdapter {

        private int resource;
        private LayoutInflater layoutInflater;
        private String[] faculty;

        private ArrayList<String> faculty_names;
        private ArrayList<String> faculty_departments;


        public Faculty_Adapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);

            this.resource = resource;
            this.faculty = objects;
            layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        }

        public Faculty_Adapter(FacultyActivity context, int resource, ArrayList<String> faculty_names,ArrayList<String> faculty_departments) {
            super(context,resource,faculty_names);
            this.faculty_names = faculty_names;
            this.faculty_departments = faculty_departments;
            this.resource = resource;
            layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            Faculty_Adapter.ViewHolder viewHolder;
            if (convertView == null){
                viewHolder =  new ViewHolder();
                convertView = layoutInflater.inflate(resource,null);
                viewHolder.ivLogo = (LetterImageView)convertView.findViewById(R.id.ivLetterFaculty);
                viewHolder.textView = (TextView)convertView.findViewById(R.id.tvFacultyName);
                viewHolder.departement = (TextView)convertView.findViewById(R.id.tvDept);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
            }
            viewHolder.ivLogo.setOval(true);
            viewHolder.ivLogo.setLetter(faculty_names.get(position).charAt(0));
            viewHolder.textView.setText(faculty_names.get(position));
            viewHolder.departement.setText(faculty_departments.get(position));

            return convertView;
        }

        class ViewHolder{
            private LetterImageView ivLogo;
            private TextView textView;
            private TextView departement;

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.add_faculty,menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){

            case android.R.id.home :
                onBackPressed();
                break;

            case R.id.add_faculty :
                 Add_new_Faculty add_new_faculty = new Add_new_Faculty();
                 add_new_faculty.show(getSupportFragmentManager(),"New Faculty");
               // Intent createFacultyIntent = new Intent(FacultyActivity.this, Add_new_Faculty.AddNewFaculty.class);
                //startActivity(createFacultyIntent);
                break;

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
