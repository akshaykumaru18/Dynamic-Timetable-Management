package com.example.akshay.timetable888;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.akshay.timetable888.modules.DepartmentInfo;
import com.example.akshay.timetable888.modules.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import static android.text.TextUtils.isEmpty;

public class RegisterActivity extends AppCompatActivity {

    //---------------------------------firebase---------------------------------//
    FirebaseDatabase mDatabase;
    DatabaseReference mDtabaseReference;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    //---------------------------------------------------------------------------//

    EditText Emailid;
    EditText Name;
    EditText password;
    EditText confirmPassword;
    ProgressBar progressBar;

    Spinner selectDepartment;
    private String selectedDepartment;
    private ArrayList<String> departmentList;
    private ArrayAdapter<String> arrayAdapter;

    private String userName;
    private String userEmailid;
    private String passwd;
    private String confirmPasswd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setupUIViews();
        setupSpinner();
        setupFirebase();

    }
    public void setupUIViews(){
        Emailid = (EditText)findViewById(R.id.emailid);
        Name = (EditText)findViewById(R.id.name);
        password = (EditText)findViewById(R.id.password);
        confirmPassword = (EditText)findViewById(R.id.confirmPassword);

        progressBar = (ProgressBar)findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void progressBarVisible(){
        progressBar.setVisibility(View.VISIBLE);
    }
    public void progressBarInvisible(){
        progressBar.setVisibility(View.INVISIBLE);
    }

    public void setupSpinner(){
        selectDepartment = (Spinner)findViewById(R.id.SelectDepartment);
        departmentList = new ArrayList<String>();
        departmentList.add("AUTOMOBILE ENGINEERING");
        departmentList.add("CERAMICS");
        departmentList.add("CHEMICAL ENGINEERING");
        departmentList.add("CIVIL ENVIRONMENTAL ENGINEERING");
        departmentList.add("CIVIL ENGINEERING");
        departmentList.add("COMPUTER SCIENCE AND ENGINEERING");
        departmentList.add("ELECTRICAL AND ELECTRONICS ENGINEERING");
        departmentList.add("MECHANICAL WELDING AND SHEET METAL");
        departmentList.add("ELECTRONICS AND COMMUNICATION ENGG");
        departmentList.add("MACHINE TOOL TECHNOLOGY");
        departmentList.add("MECHANICAL ENGINEERING");
        departmentList.add("INSTRUMENTATION");
        departmentList.add("MECHATRONICS");
        departmentList.add("HEAT POWER ENGINEERING");
        departmentList.add("SCIENCE AND HUMANITY");
        arrayAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,departmentList);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        selectDepartment.setAdapter(arrayAdapter);

        selectDepartment.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedDepartment = departmentList.get(position);
                Toast.makeText(getApplicationContext(),selectedDepartment,Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    public void setupFirebase(){
//----------------------------------firebase-----------------------------------//
        mDatabase = FirebaseDatabase.getInstance();
        mDtabaseReference = mDatabase.getReference().child("users");
        mFirebaseAuth = FirebaseAuth.getInstance();
        setupFirebaseAuth();
    }


    public void registerNewUser(View view) {

        userName = Name.getText().toString();
        userEmailid = Emailid.getText().toString();
        passwd = password.getText().toString();
        confirmPasswd = confirmPassword.getText().toString();
        if(!isEmpty(userName) && !isEmpty(userEmailid) && !isEmpty(passwd) && !isEmpty(confirmPasswd)){

            if(userName.length() < 3){
                Name.setError("User Name must be Atleast 4char");
            }
            else{
                Name.setError(null);
                }
                if(userEmailid.contains("@")){
                    Emailid.setError(null);
                }
                else{
                Emailid.setError("Enter Valid Email-Id");
                }
            if(passwdCheck(passwd,confirmPasswd)){
                if(passwd.length() < 6){
                    password.setError("Minimum 6chars");
                }
                else{
                    password.setError(null);
                    regNewUser(userEmailid,passwd,"hod");
                }

            }else{
                Toast.makeText(getApplicationContext(),"Password Not Matched",Toast.LENGTH_SHORT).show();
            }

        }else{

            Name.setError("Required");
            Emailid.setError("Required");
            password.setError("Required");
            confirmPassword.setError("Required");
        }
    }


    public void regNewUser(final String userEmailid, String passwd, final String userRole){
        progressBarVisible();


        FirebaseAuth.getInstance().createUserWithEmailAndPassword(userEmailid,passwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
            if(task.isSuccessful()){

                    if(userRole.equalsIgnoreCase("hod")){
                        /* To create New Department*/
                        DepartmentInfo departmentInfo = new DepartmentInfo();
                        departmentInfo.setDepartmentName(selectedDepartment);
                        departmentInfo.setHodName(userName);

                        FirebaseDatabase.getInstance().getReference()

                                .child(selectedDepartment)
                                .setValue(departmentInfo);
                        User user = new User();
                        user.setHodName(userName);
                        user.setPhoneNumber("18181818181");
                        user.setSecurity_level("2");
                        user.setProfile_image("");
                        user.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
                        user.setEmailID(userEmailid);
                        user.setHodDepartment(selectedDepartment);

                        addTOFaculty(userName,userEmailid);
                        FirebaseDatabase.getInstance().getReference()
                                 .child("users")
                                 .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                         @Override
                         public void onComplete(@NonNull Task<Void> task) {
                             Log.i("User Id Registered ", FirebaseAuth.getInstance().getUid());
                             progressBarInvisible();
                             sendVerificationEmail();
                             FirebaseAuth.getInstance().signOut();
                             Intent signInIntent = new Intent(RegisterActivity.this, Authentication.class);
                                startActivity(signInIntent);
                            }
                            }).addOnFailureListener(new OnFailureListener() {
                             @Override
                             public void onFailure(@NonNull Exception e) {
                        }
                        });


                    }
            }
            else{
                progressBarInvisible();
            }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                FirebaseAuth.getInstance().signOut();
            Toast.makeText(getApplicationContext(),"Failed : " + e.getMessage() ,Toast.LENGTH_SHORT).show();
            progressBarInvisible();
            }
        });

    }


    private void addTOFaculty(String userName, String userEmailid){

        User user1 = new User();
        user1.setLecturerName(userName);
        user1.setPhoneNumber("18181818181");
        user1.setSecurity_level("2");
        user1.setProfile_image("");
        user1.setUser_id(FirebaseAuth.getInstance().getCurrentUser().getUid());
        user1.setEmailID(userEmailid);
        user1.setHodDepartment(selectedDepartment);
        FirebaseDatabase.getInstance().getReference()
                .child(selectedDepartment)
                .child("Faculty")
                .child("Lecturers")
                .child(userName)
                // .child("HOD")
                //.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(user1).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
            }
        });



    }

    //------------------------------------Sending Email For Authentication----------------------------//

    private void sendVerificationEmail(){

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful()){
                        Toast.makeText(getApplicationContext(),"Verification Email Sent",Toast.LENGTH_LONG).show();
                        Log.d("Email Status ", "Email Sent");
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(getApplicationContext(),"Email Id not Registered",Toast.LENGTH_LONG).show();
                    Log.d("Email Status ", "Email Not Sent");
                }
            });
        }
    }

    public Boolean passwdCheck(String password,String confirmPassword){
       return password.equals(confirmPassword);
    }

    //------------------------Firebase AuthstateListner setup---------------------------------//

    public void setupFirebaseAuth() {
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    Log.i("User Id ", mFirebaseAuth.getUid());
                } else {
                    Log.i("User Id ", "Not Signed In");
                }
            }
        };
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        FirebaseAuth.getInstance().removeAuthStateListener(mAuthStateListener);
    }
}

