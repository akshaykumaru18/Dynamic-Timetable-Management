package com.example.akshay.timetable888;

import android.content.Context;
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
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akshay.timetable888.Utils.LetterImageView;
import com.example.akshay.timetable888.modules.AddNewSubject;
import com.example.akshay.timetable888.modules.AddSubject;
import com.example.akshay.timetable888.modules.Faculty;
import com.example.akshay.timetable888.modules.Faculty_Schedule;
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

public class DaySchedule extends AppCompatActivity implements Req_or_Swap_Sub.AddOrSwapSubjectListener , Add_subject.ADD_SUBJECT {

    /* -----------------------------------firebase---------------------------- */
    FirebaseDatabase mDatabase;
    DatabaseReference mDtabaseReference;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    public static ListView listView;
   public static SimpleAdapter simpleAdapter;
    private Toolbar toolbar;

   /*Day Array List*/
    public static ArrayList<String> Monday;
    public static ArrayList<String> Tuesday;
    public static ArrayList<String> Wednesday;
    public static ArrayList<String> Thursday;
    public static ArrayList<String> Friday;
    public static ArrayList<String> Saturday;

    /*Day Slots Array List*/
    public static ArrayList<String> MondayTimings;
    public static ArrayList<String> TuesdayTimings;
    public static ArrayList<String> WednesdayTimings;
    public static ArrayList<String> ThursdayTimings;
    public static ArrayList<String> FridayTimings;
    public static ArrayList<String> SaturdayTimings;

    private ArrayList<String> PreferredDay;
    private ArrayList<String> PreferredTime;

    String selected_day;
    String selected_sem;
    int delete_position;

    public static ArrayList<String> subject_List;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_day_schedule);

        setupUIViews();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initToolbar();
        }
        setupListView();
        setUpFirebase();
    }


    public void setUpFirebase(){

        /* Firebase initalization*/
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
                // ---------------- Check Weather Email is Verified Or not----------------------------//
                if(user != null){
                    if(user.isEmailVerified()){

                        Log.i("Status"," Loged In ");
                        Log.i("Verified User Id",FirebaseAuth.getInstance().getUid());
                    }
                    else{
                        // Toast.makeText(getApplicationContext(),"Check Your Mail",Toast.LENGTH_LONG).show();
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

    private void setupUIViews(){
        listView = (ListView) findViewById(R.id.lvDayDetails);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar = (Toolbar)findViewById(R.id.ToolbarDayDetails);
        }

    }


    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initToolbar() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(SelectSemActivity.sharedPreferences.getString(SelectSemActivity.SELECTED_SEM,null));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupListView(){

        Monday = new ArrayList<>();
        Monday.add("Add Subject");
        Tuesday = new ArrayList<>();
        Tuesday.add("Add Subject");
        Wednesday = new ArrayList<>();
        Wednesday.add("Add Subject");
        Thursday = new ArrayList<>();
        Thursday.add("Add Subject");
        Friday = new ArrayList<>();
        Friday.add("Add Subject");
        Saturday = new ArrayList<>();
        Saturday.add("Add Subject");

        MondayTimings = new ArrayList<>();
        MondayTimings.add("Select Your Slot");
        TuesdayTimings = new ArrayList<>();
        TuesdayTimings.add("Select Your Slot");
        WednesdayTimings = new ArrayList<>();
        WednesdayTimings.add("Select Your Slot");
        ThursdayTimings = new ArrayList<>();
        ThursdayTimings.add("Select Your Slot");
        FridayTimings = new ArrayList<>();
        FridayTimings.add("Select Your Slot");
        SaturdayTimings = new ArrayList<>();
        SaturdayTimings.add("Select Your Slot");


         selected_day = WeekActivity.sharedPreferences.getString(WeekActivity.SELECTED_DAY,null);

        if (selected_day.equalsIgnoreCase("Monday")){
            PreferredDay = Monday;
            PreferredTime = MondayTimings;
        } else if(selected_day.equalsIgnoreCase("Tuesday")){
            PreferredDay = Tuesday;
            PreferredTime = TuesdayTimings;
        } else if (selected_day.equalsIgnoreCase("Wednesday")) {
            PreferredDay = Wednesday;
            PreferredTime = WednesdayTimings;
        }else if (selected_day.equalsIgnoreCase("Thursday")) {
            PreferredDay = Thursday;
            PreferredTime = ThursdayTimings;
        }else if (selected_day.equalsIgnoreCase("Friday")) {
            PreferredDay = Friday;
            PreferredTime = FridayTimings;
        } else{
            PreferredDay = Saturday;
            PreferredTime = SaturdayTimings;
        }

        simpleAdapter = new SimpleAdapter(this,PreferredDay,PreferredTime);
        listView.setAdapter(simpleAdapter);
        initialTimetable();
        registerForContextMenu(listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                delete_position = position;
                Add_subject add_subject = new Add_subject();
                add_subject.show(getSupportFragmentManager(),"Add");
            }
        });


    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        if(v.getId() == R.id.lvDayDetails){
            menu.add("Edit");
            menu.add("Delete");
        }
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if(item.getTitle() == "Edit"){
            editSubject();

        }else if(item.getTitle() == "Delete"){
           deleteSubject(delete_position);
        }
        return super.onContextItemSelected(item);
    }

    public void editSubject(){
        Toast.makeText(getApplicationContext(),"Edit Now",Toast.LENGTH_LONG).show();
    }

    public void deleteSubject(int position){
       // Toast.makeText(getApplicationContext(),"Delete Now",Toast.LENGTH_LONG).show();
        selected_sem = SelectSemActivity.sharedPreferences.getString(SelectSemActivity.SELECTED_SEM,null);
        Toast.makeText(getApplicationContext(),PreferredDay.get(position),Toast.LENGTH_LONG).show();

        DatabaseReference removeSubject = FirebaseDatabase.getInstance()
                .getReference()
                .child(MainActivity.department_of_hod)
                .child(getString(R.string.timetable_node))
                .child(SelectSemActivity.SELECTED_YEAR)
                .child(selected_sem)
                .child(selected_day)
                .child(PreferredTime.get(position));
        removeSubject.removeValue();

        PreferredDay.remove(position);
        PreferredTime.remove(position);
        simpleAdapter.notifyDataSetChanged();
    }

    @Override
    public void apply_selected_sub(String SubjectName, String SlotTimings) {
        Log.i("Working","True");
        Toast.makeText(getApplicationContext(),SubjectName + " : " + SlotTimings,Toast.LENGTH_LONG).show();
        updateAdapter(SubjectName,SlotTimings);
        simpleAdapter.notifyDataSetChanged();
    }

    @Override
    public void remove_selected_sub(int SlotTimings) {

    }

    public void updateAdapter(String PreferredSubject,String PreferredSlot) {
        String selected_day = WeekActivity.sharedPreferences.getString(WeekActivity.SELECTED_DAY,null);
        if (selected_day.equalsIgnoreCase("Monday")){
            if(Monday.get(0).equalsIgnoreCase("Add Subject")){
                Monday.remove(0);
                MondayTimings.remove(0);
            }
            Monday.add(String.valueOf(PreferredSubject));
            MondayTimings.add(String.valueOf(PreferredSlot));
            PreferredDay = Monday;
            PreferredTime = MondayTimings;
            simpleAdapter.notifyDataSetChanged();
        } else if(selected_day.equalsIgnoreCase("Tuesday")){

            if(Tuesday.get(0).equalsIgnoreCase("Add Subject")){
                Tuesday.remove(0);
                TuesdayTimings.remove(0);
            }
            Tuesday.add(String.valueOf(PreferredSubject));
            TuesdayTimings.add(String.valueOf(PreferredSlot));
            PreferredDay = Tuesday;
            PreferredTime = TuesdayTimings;
            simpleAdapter.notifyDataSetChanged();
        } else if (selected_day.equalsIgnoreCase("Wednesday")) {
            if(Wednesday.get(0).equalsIgnoreCase("Add Subject")){
                Wednesday.remove(0);
                WednesdayTimings.remove(0);
            }
            Wednesday.add(String.valueOf(PreferredSubject));
            WednesdayTimings.add(String.valueOf(PreferredSlot));
            PreferredDay = Wednesday;
            PreferredTime = WednesdayTimings;
            simpleAdapter.notifyDataSetChanged();
        }else if (selected_day.equalsIgnoreCase("Thursday")) {
            if(Thursday.get(0).equalsIgnoreCase("Add Subject")){
                Thursday.remove(0);
                ThursdayTimings.remove(0);
            }

            Thursday.add(String.valueOf(PreferredSubject));
            ThursdayTimings.add(String.valueOf(PreferredSlot));
            PreferredDay = Thursday;
            PreferredTime = ThursdayTimings;
            simpleAdapter.notifyDataSetChanged();
        }else if (selected_day.equalsIgnoreCase("Friday")) {

            if(Friday.get(0).equalsIgnoreCase("Add Subject")){
                Friday.remove(0);
                FridayTimings.remove(0);
            }
            Friday.add(String.valueOf(PreferredSubject));
            FridayTimings.add(String.valueOf(PreferredSlot));
            PreferredDay = Friday;
            PreferredTime = FridayTimings;
            simpleAdapter.notifyDataSetChanged();
        } else{
            if(Saturday.get(0).equalsIgnoreCase("Add Subject")){
                Saturday.remove(0);
                SaturdayTimings.remove(0);
            }
            Saturday.add(String.valueOf(PreferredSubject));
            SaturdayTimings.add(String.valueOf(PreferredSlot));
            PreferredDay = Saturday;
            PreferredTime = SaturdayTimings;
            simpleAdapter.notifyDataSetChanged();
        }

    }

    @Override
    public void add_Subject(final String Subject_name,
                            final String Subject_faculty, final String Selected_slot) {

        selected_sem = SelectSemActivity.sharedPreferences.getString(SelectSemActivity.SELECTED_SEM,null);

        final AddSubject addSubject = new AddSubject();
        addSubject.setSubject_name(Subject_name);
        addSubject.setFaculty(Subject_faculty);
        addSubject.setSelectedSlot(Selected_slot);

//--------------------------------------------------------------------------------------------------------
        FirebaseDatabase.getInstance().getReference()
                .child(MainActivity.department_of_hod)
                .child("timetable")
                .child(selected_sem)
                .child(selected_day)
                .child(Subject_name)
                .setValue(addSubject)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Successful",Toast.LENGTH_SHORT).show();
                            updateAdapter(Subject_name,Selected_slot);
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
               // FirebaseAuth.getInstance().signOut();
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


        //-------------------------------------------------------------------------------------------------------
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query11 = reference
                .child(MainActivity.department_of_hod)
                .child("timetable")
                .child(selected_sem)
                .child(selected_day)
                //.orderByKey()
                .equalTo(Subject_name);

        query11.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    AddSubject addSubject1 = singleSnapshot.getValue(AddSubject.class);
                    assert addSubject1 != null;
                    if (addSubject1 != null) {
                        //Toast.makeText(getApplicationContext(),addSubject1.getFaculty(),Toast.LENGTH_LONG).show();
                       //updateAdapter(addSubject1.getsubject_name(),addSubject1.getSelectedSlot());
                        updateAdapter(Subject_name,Selected_slot);
                    }else{
                        Toast.makeText(getApplicationContext(),"Error Retreiving",Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

        /*TO add subject for faculty*/
        Faculty_Schedule faculty_schedule = new Faculty_Schedule();
        faculty_schedule.setSubject_Name(Subject_name);
        faculty_schedule.setSlot(Selected_slot);
        faculty_schedule.setDepartment(MainActivity.department_of_hod);
        faculty_schedule.setSemester(selected_sem);
        FirebaseDatabase.getInstance().getReference()
                .child(MainActivity.department_of_hod)
                .child("Faculty")
                .child("Lecturers")
                .child(Subject_faculty)
                .child("subject Handled")
                .child(selected_sem)
                .child(Subject_name)
                .setValue(faculty_schedule)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Done ",Toast.LENGTH_SHORT).show();
                        }

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(),e.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });

    }


    public void initialTimetable(){

        subject_List = new ArrayList<String>();

        final String selected_sem = SelectSemActivity.sharedPreferences.getString(SelectSemActivity.SELECTED_SEM,null);


        DatabaseReference reference12 = FirebaseDatabase.getInstance().getReference();
        Query query = reference12
                .child("users")
                .orderByKey();

        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    final Faculty faculty = singleSnapshot.getValue(Faculty.class);
                    if(faculty!= null){
                        if(faculty.getUser_id().equals(FirebaseAuth.getInstance().getCurrentUser().getUid())){
                            if(faculty.getSecurity_level().equals("2")){
                                hod();

                            }else if(faculty.getSecurity_level().equals("1")){
                              //  Toast.makeText(getApplicationContext(),"Error loading",Toast.LENGTH_LONG).show();
                               lecturer();

                            }else if(faculty.getSecurity_level().equals("0")){

                            }
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG);
            }
        });


    }

    private void hod(){
        final String selected_sem = SelectSemActivity.sharedPreferences.getString(SelectSemActivity.SELECTED_SEM,null);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query1 = reference
                .child(MainActivity.department_of_hod)
                .child(getString(R.string.timetable_node))
                .child(SelectSemActivity.SELECTED_YEAR)
                .child(selected_sem)
                .child(selected_day)
                .orderByValue()
                .limitToFirst(100);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    AddSubject addSubject1 = singleSnapshot.getValue(AddSubject.class);
                    assert addSubject1 != null;
                    if (addSubject1 != null) {
                        //  Toast.makeText(getApplicationContext(),addSubject1.getFaculty(),Toast.LENGTH_LONG).show();
                       updateAdapter(addSubject1.getsubject_name(),addSubject1.getSelectedSlot());
                        //Toast.makeText(getApplicationContext(),dataSnapshot.child("selectedSlot").getValue().toString(),Toast.LENGTH_LONG).show();

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

        /*-----------------------------------------------------------------------------------------------------------------------*/

        DatabaseReference reference2 = FirebaseDatabase.getInstance().getReference();
        Query query2 = reference2
                .child(MainActivity.department_of_hod)
                .child("subjects");

        query2.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    AddNewSubject addNewSubject = singleSnapshot.getValue(AddNewSubject.class);
                    if(addNewSubject != null){
                        // Toast.makeText(getApplicationContext(),addNewSubject.getSubject_Name(),Toast.LENGTH_SHORT).show();
                        subject_List.add(addNewSubject.getSubject_Name());
                    }
                    else{

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void lecturer(){
        final String selected_sem = SelectSemActivity.sharedPreferences.getString(SelectSemActivity.SELECTED_SEM,null);
        selected_day = WeekActivity.sharedPreferences.getString(WeekActivity.SELECTED_DAY,null);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        Query query1 = reference
                .child(LecturerActivity.CurrentFacultyHod)
                // .child(MainActivity.department_of_hod)
                .child(getString(R.string.timetable_node))
                .child(SelectSemActivity.SELECTED_YEAR)
                .child(selected_sem)
                .child(selected_day)
                .limitToFirst(100);

        query1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                for(DataSnapshot singleSnapshot : dataSnapshot.getChildren()){
                    AddSubject addSubject1 = singleSnapshot.getValue(AddSubject.class);
                    assert addSubject1 != null;
                    if (addSubject1 != null) {
                        //  Toast.makeText(getApplicationContext(),addSubject1.getFaculty(),Toast.LENGTH_LONG).show();
                        updateAdapter(addSubject1.getsubject_name(),addSubject1.getSelectedSlot());

                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(),databaseError.getMessage(),Toast.LENGTH_LONG).show();
            }
        });

    }



    public class SimpleAdapter extends BaseAdapter {

        private Context mContext;
        private LayoutInflater layoutInflater;
        private TextView subject,time;
        private String[] subjectArray;
        private String[] timeArray;
        private LetterImageView letterImageView;

        private ArrayList<String> subjectList;
        private ArrayList<String> timeList;


        public SimpleAdapter(Context context, String[] subjectArray, String[] timeArray) {

            mContext = context;
           this.subjectArray = subjectArray;
           this.timeArray = timeArray;
           layoutInflater = LayoutInflater.from(context);

        }

        public SimpleAdapter(Context context,ArrayList<String> subjectList,ArrayList<String> timeList){
           this.mContext = context;
            this.subjectList = subjectList;
            this.timeList = timeList;
            layoutInflater = LayoutInflater.from(context);
        }

        public SimpleAdapter(Context context, String preferredSubject, String preferredSlot) {
            mContext = context;
            subjectList.add(preferredSubject);
            timeList.add(preferredSlot);


            simpleAdapter.notifyDataSetChanged();


        }

        @Override
        public int getCount() {
           // return subjectArray.length;
            return subjectList.size();
        }

        @Override
        public Object getItem(int position) {
           // return subjectArray[position];
            return subjectList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if (convertView == null) {
                convertView = layoutInflater.inflate(R.layout.selected_day_detail, null);
            }
            subject = (TextView) convertView.findViewById(R.id.tvSubjectDaydetails);
            time = (TextView) convertView.findViewById(R.id.tvSubjectTimings);
            letterImageView = (LetterImageView) convertView.findViewById(R.id.ivDayDetails);
            subject.setText(subjectList.get(position));
            time.setText(timeList.get(position));
            letterImageView.setOval(true);

            letterImageView.setLetter(subjectList.get(position).charAt(0));
            return convertView;

        }

    }

    @Override
    public boolean onCreateOptionsMenu(final Menu menu) {
        /* Check for the Permission and the inflate the menu according to that*/
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
                            if(faculty.getSecurity_level().equals("0")){
                                break;
                            }else if(faculty.getSecurity_level().equals("1")){

                                break;
                            }else if(faculty.getSecurity_level().equals("2")){
                                MenuInflater inflater = getMenuInflater();
                                inflater.inflate(R.menu.add_subject,menu);

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

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {

        /* Check for the Permission and the inflate the menu according to that*/
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
                            if(faculty.getSecurity_level().equals("0")){
                                break;
                            }else if(faculty.getSecurity_level().equals("1")){
                                switch (item.getItemId()){

                                    case android.R.id.home :
                                        onBackPressed();
                                        break;
                                }
                                break;
                            }else if(faculty.getSecurity_level().equals("2")){

                                switch (item.getItemId()){

                                    case R.id.addSubject :
                                        Add_subject add_subject = new Add_subject();
                                        add_subject.show(getSupportFragmentManager(),"Add");
                                        break;

                                    case android.R.id.home :
                                        onBackPressed();
                                        break;
                                }

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

            }
        });
/*
            switch (item.getItemId()){

                case R.id.addSubject :
                    Add_subject add_subject = new Add_subject();
                    add_subject.show(getSupportFragmentManager(),"Add");
                    break;

                case android.R.id.home :
                    onBackPressed();
                    break;
            }
            */

        return super.onOptionsItemSelected(item);
    }

}
