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
import android.widget.EditText;

public class add_subject_details_dialog extends DialogFragment {

    EditText unitName;
    EditText unitContent;

    String unit_Name;
    String unit_content;

    Add_new_Syllabus add_new_syllabus;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.add_syllabus,null);

        builder.setView(view)
                .setTitle("Chapter")
                .setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        unit_Name = unitName.getText().toString();
                        unit_content = unitContent.getText().toString();

                        if(unit_Name == null){
                            unitName.setError("Required");
                        }else{
                            unitName.setError(null);
                        }

                        if(unit_content == null){
                            unitContent.setError("Required");
                        }
                        else{
                            unitContent.setError(null);
                        }

                        if(unit_Name == null && unit_content == null){
                            unitName.setError("Required");
                            unitContent.setError("Required");
                        }
                        else{

                            unitContent.setError(null);
                            if(add_new_syllabus != null){
                                add_new_syllabus.add_new_syllabus(unit_Name,unit_content);
                            }
                        }




                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        unitName = (EditText)view.findViewById(R.id.syllabus_subject);
        unitContent = (EditText)view.findViewById(R.id.subject_content);
        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            add_new_syllabus = (Add_new_Syllabus)context;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public interface Add_new_Syllabus{
        public void add_new_syllabus(String unitName, String unitContents);
    }
}
