package com.example.akshay.timetable888;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akshay.timetable888.Utils.LetterImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SubjectSelectSem extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView semesterListView;
    private Toolbar supportActionBar;
    public static String SELECTED_SEM;
    public static SharedPreferences sharedPreferences;

    //--------------------------Firebase--------------------------//
    FirebaseDatabase mDatabase;
    DatabaseReference mDtabaseReference;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    //-------------------------------------------------------------------------//


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_select_sem);
        setupUIViews();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initToolbar();
        }
        setupFirebaseAuth();
        setupListView();
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
        semesterListView = (ListView) findViewById(R.id.lvMain);

        //-----------------------firebase----------------------------//
        mDatabase = FirebaseDatabase.getInstance();
        mDtabaseReference = mDatabase.getReference().child("users");
        mFirebaseAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("SEM",MODE_PRIVATE);

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initToolbar() {
        setSupportActionBar(toolbar);
//        getSupportActionBar().setTitle("Semester");
    }

    private void setupListView(){

        final String[] semesters = getResources().getStringArray(R.array.semester);

        SubjectSelectSem.SelectSemesterAdapter adapter = new SelectSemesterAdapter(this,R.layout.activity_week_single_item,semesters);
        semesterListView.setAdapter(adapter);
        semesterListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Toast.makeText(getApplicationContext(),semesters[position],Toast.LENGTH_LONG).show();

                startActivity(new Intent(SubjectSelectSem.this,SubjectActivity.class));
                sharedPreferences.edit().putString(SELECTED_SEM,semesters[position]).apply();

                Toast.makeText(getApplicationContext(),"Clicked Day : " + semesters[position],Toast.LENGTH_SHORT ).show();
            }
        });


    }

    public class SelectSemesterAdapter extends ArrayAdapter {

        private int resource;
        private LayoutInflater layoutInflater;
        private String[] week = new String[]{};


        public SelectSemesterAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);

            this.resource = resource;
            this.week = objects;
            layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            SubjectSelectSem.SelectSemesterAdapter.ViewHolder viewHolder;
            if (convertView == null){
                viewHolder =  new SelectSemesterAdapter.ViewHolder();
                convertView = layoutInflater.inflate(resource,null);
                viewHolder.ivLogo = (LetterImageView)convertView.findViewById(R.id.ivLetter);
                viewHolder.textView = (TextView)convertView.findViewById(R.id.tvWeek);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (SubjectSelectSem.SelectSemesterAdapter.ViewHolder) convertView.getTag();
            }
            viewHolder.ivLogo.setOval(true);
            viewHolder.ivLogo.setLetter(week[position].charAt(0));
            viewHolder.textView.setText(week[position]);

            return convertView;
        }

        class ViewHolder{
            private LetterImageView ivLogo;
            private TextView textView;
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
            case R.id.logout:
                Intent intent = new Intent(SubjectSelectSem.this,SubjectActivity.class);
                startActivity(intent);
                finish();
            case android.R.id.home:
                onBackPressed();
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
