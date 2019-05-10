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
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import static com.example.akshay.timetable888.DaySchedule.simpleAdapter;

public class Req_or_Swap_Sub extends DialogFragment{

    EditText select_sub;
    String selected_sub;
   int selected_sub_toRemove;

    Spinner spinner;
    String selected_slot;

    AddOrSwapSubjectListener addOrSwapSubjectListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.req_or_swap_sub,null);

        spinner = (Spinner)view.findViewById(R.id.spinner_slots);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(),android.R.layout.simple_spinner_item,
                getResources().getStringArray(R.array.slots));
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        builder.setView(view)
                .setTitle("Select")
                .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                          @Override
                          public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                              selected_sub_toRemove = Integer.parseInt(spinner.getItemAtPosition(position).toString());
                              addOrSwapSubjectListener.remove_selected_sub(selected_sub_toRemove);
                              Toast.makeText(getActivity(),"working",Toast.LENGTH_LONG).show();
                          }

                          @Override
                          public void onNothingSelected(AdapterView<?> parent) {

                          }
                      });

                    }
                })
                .setPositiveButton("add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //Toast.makeText(getContext(),"Clicked",Toast.LENGTH_SHORT).show();
                        selected_sub = select_sub.getText().toString();

                        if(addOrSwapSubjectListener != null){
                            addOrSwapSubjectListener.apply_selected_sub(selected_sub,selected_slot);
                            simpleAdapter.notifyDataSetChanged();
                        }
                        else{

                        }

                    }
                });
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selected_slot = spinner.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        select_sub = (EditText)view.findViewById(R.id.subjectName);

        return builder.create();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try {
            addOrSwapSubjectListener = (AddOrSwapSubjectListener)context;
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public interface AddOrSwapSubjectListener{
        void apply_selected_sub(String SubjectName,String SlotTimings);
        void remove_selected_sub(int SlotTimings);
    }
}







