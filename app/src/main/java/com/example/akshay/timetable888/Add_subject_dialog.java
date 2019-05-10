package com.example.akshay.timetable888;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;

import static android.text.TextUtils.isEmpty;

public class Add_subject_dialog extends DialogFragment {

    EditText subjectName;
    EditText numberOfUnits;
    EditText subjectCode;
    Spinner semester_spinner;
    private String subject_Name;
    private String subject_code;
    private String number_of_units;
    private String semester;
    private ArrayList<String> semesterList;

    AddNewSubject addNewSubject;

    Uri pdfUri;
    FirebaseStorage storage;
    FirebaseDatabase database;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_subject_dialog,null);

        semesterList = new ArrayList<String>();
        semesterList.add("SEMESTER 1");
        semesterList.add("SEMESTER 2");
        semesterList.add("SEMESTER 3");
        semesterList.add("SEMESTER 4");
        semesterList.add("SEMESTER 5");
        semesterList.add("SEMESTER 6");



        semester_spinner = (Spinner)view.findViewById(R.id.semester_spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,
               semesterList);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        semester_spinner.setAdapter(adapter);

        semester_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                semester = semester_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        builder.setView(view)
                .setTitle("Add Subject")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        subject_Name = subjectName.getText().toString();
                        subject_code = subjectCode.getText().toString();
                        number_of_units = numberOfUnits.getText().toString();

                        if(!isEmpty(subject_Name) && !isEmpty(subject_code) && !isEmpty(number_of_units)){

                            if(isEmpty(subject_Name)){
                                Toast.makeText(getActivity(),"Subject Name Required",Toast.LENGTH_SHORT).show();
                            }
                            else{
                                if(isEmpty(subject_code)){
                                    Toast.makeText(getActivity(),"Subject Code Required",Toast.LENGTH_SHORT).show();
                                }
                                else{
                                    if(isEmpty(number_of_units)){
                                        Toast.makeText(getActivity(),"Number of units Required",Toast.LENGTH_SHORT).show();
                                    }
                                    else{
                                        if(addNewSubject != null){
                                            addNewSubject.Add_New_Subject(subject_Name,subject_code,number_of_units,semester,MainActivity.department_of_hod);
                                        }
                                    }
                                }
                            }

                        }
                        else if(subject_Name == null && subject_code == null && number_of_units == null){
                            Toast.makeText(getActivity(),"Invalid Details",Toast.LENGTH_SHORT).show();

                        }

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        subjectName = (EditText)view.findViewById(R.id.subjectName);
        subjectCode = (EditText)view.findViewById(R.id.subjectCode);
        numberOfUnits = (EditText)view.findViewById(R.id.numberOfUnits);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            addNewSubject = (AddNewSubject)context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface AddNewSubject{
        void Add_New_Subject(String subjectName, String subjectCode, String number_of_units, String semester,String department);
    }

}
