package com.example.akshay.timetable888;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;



public class Add_subject extends DialogFragment  {


    /* -----------------------------------firebase---------------------------- */
    FirebaseDatabase mDatabase;
    DatabaseReference mDtabaseReference;
    FirebaseAuth mFirebaseAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;

    String subjectName;
    String subjectCode;
    String numberOfunits;
    String subjectFaculty;
    String selected_slot;

    ADD_SUBJECT add_subject;
    Spinner spinner;
    Spinner faculty_spinner;
    Spinner subject_spinner;


    ArrayAdapter<String> adapter1;
    ArrayAdapter<String> adapter2;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_subject,null);

        spinner = (Spinner)view.findViewById(R.id.spinner3);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.slots));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
//-----------------------------------------------------------------------------------------------------
        faculty_spinner = (Spinner)view.findViewById(R.id.faculty_Spinner);
        adapter1 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,
               MainActivity.facultyList);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        faculty_spinner.setAdapter(adapter1);

        subject_spinner = (Spinner)view.findViewById(R.id.subjectSpinner);
        adapter2 = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,
                DaySchedule.subject_List);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        subject_spinner.setAdapter(adapter2);

        builder.setView(view)
                .setTitle("ADD SUBJECT")
                .setPositiveButton("ADD", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                            if(add_subject != null){
                                add_subject.add_Subject(subjectName,subjectFaculty,selected_slot);
                            }



                    }
                }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_slot = spinner.getSelectedItem().toString();

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        faculty_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                subjectFaculty = faculty_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        subject_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               // Toast.makeText(getActivity(),"Working",Toast.LENGTH_SHORT).show();
                subjectName =  subject_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

      //  subject_faculty = (EditText)view.findViewById(R.id.faculty);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            add_subject = (ADD_SUBJECT)context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface ADD_SUBJECT{
        public void add_Subject(String Subject_name,String Subject_faculty,
                                String Selected_slot);

    }

}
