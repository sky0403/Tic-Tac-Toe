package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class YourRecords extends AppCompatActivity implements View.OnClickListener{
    ListView lv_Record;
    Button btn_Chart;
    SQLiteDatabase db;
    String sql;
    Cursor cursor = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_records);

        lv_Record = findViewById(R.id.lv_Records);
        ShowList();
        btn_Chart = findViewById(R.id.btn_ShowChart);
        btn_Chart.setOnClickListener(this);

    }
    public void ShowList(){
        try{
            db = SQLiteDatabase.openDatabase(
                    "/data/data/com.example.assignment/GamesLogDB" ,
                    null,
                    SQLiteDatabase.OPEN_READWRITE);
            cursor = db.rawQuery("select * from GamesLog order by GameID desc",null);
            ArrayList<String> List = new ArrayList<>();
            if(cursor.getCount() == 0){
                Toast.makeText(this,"The Database was Empty .",Toast.LENGTH_LONG).show();
            }else{
                while(cursor.moveToNext()){
                    String playDate = cursor.getString(cursor.getColumnIndexOrThrow("PlayDate"));
                    String playTime = cursor.getString(cursor.getColumnIndexOrThrow("PlayTime"));
                    String winningStatus = cursor.getString(cursor.getColumnIndexOrThrow("winningStatus"));
                    int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
                    List.add(playDate + ", " + playTime + ", " + winningStatus + ", " + duration + " sec");

                    ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, List);
                    lv_Record.setAdapter(listAdapter);
                }
            }
        }
        catch(Exception e)
        {
            Toast.makeText(this, e.getMessage(),Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(this, RecordPieChart.class);
        startActivity(intent);
    }
}