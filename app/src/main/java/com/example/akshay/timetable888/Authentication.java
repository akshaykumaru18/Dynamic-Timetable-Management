package com.example.akshay.timetable888;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.akshay.timetable888.modules.Faculty;
import com.example.akshay.timetable888.modules.Notification;
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

import static android.support.v4.app.NotificationCompat.Builder;
import static android.text.TextUtils.isEmpty;

public class Authentication extends AppCompatActivity {


    Builder notification;
    public static final int uniqueID = 108;

    EditText emailFeild;
    EditText passwdField;
    Button login;
    Button signUp;
  //  Spinner spinner;
    ProgressBar progressBar;
   private String selectedRole;
    //------------------------------------------Firebase-------------------------------//

    FirebaseDatabase mDatabase;
    DatabaseReference mDtabaseReference;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_authentication);

        setUpUI();
        setUpFirebase();

    }
    public void setUpUI(){

        emailFeild = (EditText)findViewById(R.id.emailField);
        passwdField = (EditText)findViewById(R.id.passwdField);
        login = (Button)findViewById(R.id.loginButton);
        signUp = (Button)findViewById(R.id.signupButton);
      //  spinner = (Spinner)findViewById(R.id.spinner);
        progressBar = (ProgressBar)findViewById(R.id.progressBar);
    }

    public void notificationCall(String user) {

        notification = (NotificationCompat.Builder) new Builder(this);
        notification.setDefaults(NotificationCompat.DEFAULT_ALL);
        notification.setSmallIcon(R.drawable.faculty);
        notification.setLargeIcon(BitmapFactory.decodeResource(getResources(),R.drawable.faculty));
        notification.setContentTitle("Loged in : " + user);
        notification.setContentText("Success");

        NotificationManager notificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(1,notification.build());
    }

    public void progressBarVisible(){
        progressBar.setVisibility(View.VISIBLE);
    }
    public void progressBarInvisible(){
        progressBar.setVisibility(View.INVISIBLE);
    }
/*
    public void setUpSpinner(){
        final ArrayList<String> user = new ArrayList<String >();
        user.add("HOD");
        user.add("LECTURER");
        user.add("STUDENT");


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item,user);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedRole = user.get(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
*/
    public void setUpFirebase(){
        //-------------------------------firebase---------------------------------//
        mDatabase = FirebaseDatabase.getInstance();
        mDtabaseReference = mDatabase.getReference().child("users");
        mFirebaseAuth = FirebaseAuth.getInstance();
        setupFirebaseAuth();
    }
//----------------------------------------------------------------------------------------------
    public void login(View view) {
        final String emailId = emailFeild.getText().toString();
        String password = passwdField.getText().toString();

        if(!isEmpty(emailId) && !isEmpty(password)){
            if(emailId.contains("@")){
                emailFeild.setError(null);
            }else{
                emailFeild.setError("Enter valid Email-Id");
            }
            if (password.length() < 6){
                passwdField.setError("Minimum Password Length is 6");
            }
            else{
                passwdField.setError(null);
                signInUser(emailId,password);
            }
        }
        else{
           // Toast.makeText(getApplicationContext(),"Fill the details and proceed",Toast.LENGTH_SHORT);
          //  Log.i("Status " , "In valid Parameters");
            emailFeild.setError("Required");
            passwdField.setError("Required");
        }

    }

    private void signInUser(final String emailId, String password){


        progressBarVisible();
        mFirebaseAuth.getInstance().signInWithEmailAndPassword(emailId,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    Notification notification = new Notification();
                    notification.setFrom("Akshay");
                    FirebaseDatabase.getInstance().getReference()
                            .child("Notification")
                            .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .child("notification")
                            .setValue(notification)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    });


                    Log.i("Email Id",emailId);
                    progressBarInvisible();
                    DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
                    Query query = reference
                            .child("users")
                            .orderByKey();
                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                                Faculty faculty = singleSnapshot.getValue(Faculty.class);
                                if(faculty != null){

                                    if(faculty.getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                                        if(faculty.getSecurity_level().equals("2")){
                                            Intent intent = new Intent(Authentication.this,MainActivity.class);
                                            startActivity(intent);
                                            notificationCall(faculty.getSecurity_level());
                                            finish();
                                            break;

                                        }else if(faculty.getSecurity_level().equals("1")){
                                            Intent intent = new Intent(Authentication.this,LecturerActivity.class);
                                            startActivity(intent);
                                            notificationCall(faculty.getSecurity_level());
                                            finish();
                                            break;
                                        }else if(faculty.getSecurity_level().equals("0")){
                                            Intent intent = new Intent(Authentication.this,StudentLayout.class);
                                            startActivity(intent);
                                            notificationCall(faculty.getSecurity_level());
                                            finish();
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


                   // if(FirebaseAuth.getInstance().getUid() == )
                    /*
                    Intent dashBoardIntent = new Intent(Authentication.this,MainActivity.class);
                    dashBoardIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
                    startActivity(dashBoardIntent);
                    notificationCall();
                    finish();
                    */
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Authentication.this, e.getMessage(), Toast.LENGTH_SHORT).show();
               // progressBar.setVisibility(View.INVISIBLE);
                progressBarInvisible();
            }
        });
    }

    public void signUp(View view) {
        Intent registerIntent = new Intent(Authentication.this,RegisterActivity.class);
        startActivity(registerIntent);
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
                // ---------------- Check Weather Email is Verified Or not----------------------------//
                if(user != null){
                    if(user.isEmailVerified()){

                        Log.i("Status"," Loged In ");
                        Log.i("Verified User Id",FirebaseAuth.getInstance().getUid());
                    }
                    else{
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(Authentication.this,Authentication.class));
                        Toast.makeText(getApplicationContext(),"Check Your Mail",Toast.LENGTH_LONG).show();

                    }
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
