package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE = 1234;
    private int trial = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickPlay(View v){
        Intent intent = new Intent(this, GameActivity.class); // open game activity page
        startActivityForResult(intent, REQUEST_CODE);
    }

    public void onClickRanking(View v){
        Intent intent = new Intent(this, GameRanking.class); // open game ranking page
        startActivity(intent);
    }

    public void onClickRecords(View v){
        Intent intent = new Intent(this, YourRecords.class); // open records page
        startActivity(intent);
    }

    public void onClickClose(View v ){
        finish();
    } // close app

}