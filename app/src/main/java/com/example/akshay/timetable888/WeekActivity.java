package com.example.akshay.timetable888;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.akshay.timetable888.Utils.LetterImageView;

public class WeekActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private ListView dayslistView;

    public static SharedPreferences sharedPreferences;
    public static String SELECTED_DAY;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_week);



        if (VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            initToolbar();
            setupUIViews();
            setupListView();
        }else{
            initToolbar();
            setupUIViews();
            setupListView();
        }

    }

    private void setupUIViews() {

        if (VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.ToolbarWeek);
            dayslistView = (ListView) findViewById(R.id.lvWeek);
            sharedPreferences = getSharedPreferences("MY_DAY",MODE_PRIVATE);
        }else{
            dayslistView = (ListView) findViewById(R.id.lvWeek);
            sharedPreferences = getSharedPreferences("MY_DAY",MODE_PRIVATE);
        }

    }


    private void initToolbar() {
        setSupportActionBar(toolbar);
       // getSupportActionBar().setTitle("Week");
       // getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setupListView(){

        final String[] week = getResources().getStringArray(R.array.Week);

        WeekAdapter adapter = new WeekAdapter(this,R.layout.activity_week_single_item,week);
        dayslistView.setAdapter(adapter);
        dayslistView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                startActivity(new Intent(WeekActivity.this,DaySchedule.class));
                sharedPreferences.edit().putString(SELECTED_DAY,week[position]).apply();
                Toast.makeText(getApplicationContext(),"Clicked Day : " + week[position],Toast.LENGTH_SHORT ).show();
            }
        });

    }

    public class WeekAdapter extends ArrayAdapter{

        private int resource;
        private LayoutInflater layoutInflater;
        private String[] week = new String[]{};


        public WeekAdapter(Context context, int resource, String[] objects) {
            super(context, resource, objects);

            this.resource = resource;
            this.week = objects;
            layoutInflater = (LayoutInflater)context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

        }
        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder viewHolder;
            if (convertView == null){
                viewHolder =  new ViewHolder();
                convertView = layoutInflater.inflate(resource,null);
                viewHolder.ivLogo = (LetterImageView)convertView.findViewById(R.id.ivLetter);
                viewHolder.textView = (TextView)convertView.findViewById(R.id.tvWeek);
                convertView.setTag(viewHolder);
            }else{
                viewHolder = (ViewHolder)convertView.getTag();
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home :
                onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
