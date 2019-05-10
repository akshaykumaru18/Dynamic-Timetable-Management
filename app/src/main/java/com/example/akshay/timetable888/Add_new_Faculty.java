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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

public class Add_new_Faculty extends DialogFragment {

    RelativeLayout relativeLayout;
    private EditText faculty_Name;
    EditText facultyContactNo;
    EditText facultyEmailId;
    Spinner departmentOfFaculty;
    CheckBox checkBox = null;

    String FacultyName;
    String faculty_contact_No;
    String faculty_Email_id;
    String department_of_faculty;

    AddNewFaculty addNewFaculty;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        final LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_new_faculty,null);

        ArrayList<String> departments = new ArrayList<String>();
        departments.add("COMPUTER SCIENCE AND ENGG");
        departments.add("ELECTRONICS AND COMMUNICATION ENGG");
        departments.add("MECHANICAL ENGG");
        departments.add("CIVIL ENGG");

        departmentOfFaculty = (Spinner)view.findViewById(R.id.faculty_department);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_dropdown_item,
                getResources().getStringArray(R.array.departments));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        departmentOfFaculty.setAdapter(adapter);


        builder.setView(view)
                .setTitle("Faculty")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        FacultyName = faculty_Name.getText().toString();
                        faculty_contact_No = facultyContactNo.getText().toString();
                        faculty_Email_id = facultyEmailId.getText().toString();
                        //department_of_faculty = departmentOfFaculty.getText().toString();
                        if(!FacultyName.isEmpty() && !faculty_contact_No.isEmpty() && !faculty_Email_id.isEmpty()){
                            if(faculty_contact_No.length() == 10){
                                if(faculty_Email_id.contains("@")){
                                    if(addNewFaculty != null){
                                        addNewFaculty.add_new_faculty(FacultyName,faculty_contact_No,faculty_Email_id,department_of_faculty);
                                    }
                                }else{
                                    Toast.makeText(getActivity(),"Invalid Email",Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(getActivity(),"Invalid number",Toast.LENGTH_SHORT).show();
                            }
                        }else{
                            Toast.makeText(getActivity(),"Error",Toast.LENGTH_SHORT).show();
                        }

                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        relativeLayout = (RelativeLayout)view.findViewById(R.id.relativeLayout);
        faculty_Name = (EditText)view.findViewById(R.id.faculty_name);
        facultyContactNo = (EditText) view.findViewById(R.id.faculty_phoneNumber);
        facultyEmailId = (EditText)view.findViewById(R.id.faculty_emailid);
        departmentOfFaculty = (Spinner) view.findViewById(R.id.faculty_department);

        departmentOfFaculty.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                department_of_faculty = departmentOfFaculty.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                departmentOfFaculty.setPrompt("required");
            }
        });


        return builder.create();

    }

    View.OnClickListener getOnclickDoSomething(final Button button){
        return new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                relativeLayout.addView(checkBox);
                Toast.makeText(getContext(),"Checked",Toast.LENGTH_LONG).show();
            }
        };
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            addNewFaculty = (AddNewFaculty)context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface AddNewFaculty{
        public void add_new_faculty(String facultyName,String facultyPhoneNo,String facultyEmailid, String faculty_department);
    }

}
