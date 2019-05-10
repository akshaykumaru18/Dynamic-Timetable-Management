package com.example.akshay.timetable888;

import android.content.Context;
import android.content.Intent;
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
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class StudentLayout extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView listView;
    private Toolbar supportActionBar;

    //--------------------------Firebase--------------------------//
    FirebaseDatabase mDatabase;
    DatabaseReference mDtabaseReference;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    //-------------------------------------------------------------------------//

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_layout);
        setupUIViews();
        initToolbar();
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
        listView = (ListView) findViewById(R.id.lvMain);

        //-----------------------firebase----------------------------//
        mDatabase = FirebaseDatabase.getInstance();
        mDtabaseReference = mDatabase.getReference().child("users");
        mFirebaseAuth = FirebaseAuth.getInstance();

    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Timetable");
    }



    private void setupListView() {
        String[] title = getResources().getStringArray(R.array.Student_Main);
        String[] description = getResources().getStringArray(R.array.Student_Description);

        StudentLayout.SimpleAdapter simpleAdapter = new StudentLayout.SimpleAdapter(this, title, description);
        listView.setAdapter(simpleAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {

                    case 0 : {
                        Intent weekActivity = new Intent(StudentLayout.this,SelectSemActivity.class);
                        startActivity(weekActivity);
                        break;
                    }

                }
            }
        });
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
            case R.id.logout:
                Intent intent = new Intent(StudentLayout.this,Authentication.class);
                startActivity(intent);
                finish();
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
