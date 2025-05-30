package com.example.assignment;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class GameActivity extends AppCompatActivity implements View.OnClickListener {
    private Button[] buttons = new Button[9];
    private Button btn_Continue;
    private TextView tvShowText;
    boolean PlayerActive,CPUActive;
    private int Count;
    SQLiteDatabase db;
    String sql;
    Cursor cursor = null;
    int[] gameState = {0,0,0,0,0,0,0,0,0};

    int[][] winningPositions = {
            {0,1,2},{3,4,5},{6,7,8}, //rows
            {0,3,6},{1,4,7},{2,5,8}, //columns
            {0,4,8},{2,4,6} //cross
    };
    Calendar calendar;
    SimpleDateFormat simpleDateFormat;
    String playDate;
    String playTime;
    private long startTime = 0;
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState)   {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);
        tvShowText = findViewById(R.id.tv_showtext);
        btn_Continue = findViewById(R.id.btn_continue);
        startTime = System.currentTimeMillis(); // get game start time
        calendar = Calendar.getInstance();
        simpleDateFormat = new SimpleDateFormat("hh:mm aa"); // get the Date when you play the game
        playTime = simpleDateFormat.format(calendar.getTime());
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd"); // get the Time when you play the game
        playDate = simpleDateFormat.format(calendar.getTime());

        for(int i = 0;i<buttons.length;i++){ // setting onClick function for 9 buttons.
            String buttonID = "btn_" + i;
            int resourceID = getResources().getIdentifier(buttonID,"id", getPackageName());
            buttons[i] = findViewById(resourceID);
            buttons[i].setOnClickListener(this);
            buttons[i].setClickable(true);
        }
        initialDB(); // create db and table
        Count = 0;
        PlayerActive = true; // player move
        CPUActive = false; // cpu move
    }

    public void initialDB(){
        try{
            db = SQLiteDatabase.openDatabase(
                            "/data/data/com.example.assignment/GamesLogDB",
                    null,
                    SQLiteDatabase.CREATE_IF_NECESSARY);
            sql = "CREATE TABLE IF NOT EXISTS GamesLog (" +   // create table if table is not exists
                    "gameID INTEGER PRIMARY KEY AUTOINCREMENT," +
                    " PlayDate varchar(50)," +
                    " PlayTime varchar(50)" +
                    ", duration int," +
                    "winningStatus text)";

            db.execSQL(sql);
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }


    @Override
    public void onClick(View v) {
        if(!((Button)v).getText().toString().equals("")){  // if button is not "", return
            return;
        }

        String buttonID = v.getResources().getResourceEntryName(v.getId());
        int gameStatePointer = Integer.parseInt(buttonID.substring(buttonID.length()-1,buttonID.length())); // get

        ((Button) v).setText("O"); // set Player Clicked position be O
        ((Button) v).setTextColor(Color.parseColor("#FFC34A"));
        gameState[gameStatePointer] = 1; // set position is player clicked.
        Count++; // count the turn
        CPUActive = true;  // cpu move when player click the button
        PlayerActive = false; // player not move when click the button



        if(checkWinner()){ // check if has  winner
            int duration = getDuration(); // get the play duration
            tvShowText.setText("You Win! Duration : " + duration + " sec!"); // set text to Player win
            btn_Continue.setVisibility(View.VISIBLE); // set continue button be visible
            for(int i = 0;i<buttons.length;i++){ // unable all button clickable
                buttonID = "btn_" + i;
                int resourceID = getResources().getIdentifier(buttonID,"id", getPackageName());
                buttons[i].setClickable(false);
            }
            try{
                playDate = simpleDateFormat.format(calendar.getTime()); // get play date
                db = SQLiteDatabase.openDatabase("/data/data/com.example.assignment/GamesLogDB",
                        null,
                        SQLiteDatabase.OPEN_READWRITE);
                    db.execSQL("insert into GamesLog(PlayDate,PlayTime,Duration,winningStatus) " + //insert player game status data into database
                            "values('" + playDate + "', '" + playTime + "', '" + duration + "', 'Win')");
                    Toast.makeText(this, "inserted data", Toast.LENGTH_SHORT).show();
            }
            catch(Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }else if(Count == 9){ // 9 rounds = draw
            int duration = getDuration(); // get the play duration
            tvShowText.setText("Draw! Duration: " + duration + " sec!"); // set text for draw
            btn_Continue.setVisibility(View.VISIBLE);// set continue button be visible
            for(int i = 0;i<buttons.length;i++){ // unable all button clickable
                buttonID = "btn_" + i;
                int resourceID = getResources().getIdentifier(buttonID,"id", getPackageName());
                buttons[i].setClickable(false);
            }
            try{
                db = SQLiteDatabase.openDatabase("/data/data/com.example.assignment/GamesLogDB",
                        null,
                        SQLiteDatabase.OPEN_READWRITE);
                db.execSQL("insert into GamesLog(PlayDate,PlayTime,Duration,winningStatus) " + // insert draw status in database
                        "values('" + playDate + "', '" + playTime + "', '" + duration + "','Draw')");
                Toast.makeText(this, "inserted data", Toast.LENGTH_SHORT).show();
            }
            catch(Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }else{
            Random random = new Random(); // random number
            do {
                int randomNumber = random.nextInt(8);
                if (buttons[randomNumber].getText() == "") { //if the target button is nothing
                    buttons[randomNumber].setText("X"); // let target button be x
                    buttons[randomNumber].setTextColor(Color.parseColor("#70FFEA"));
                    buttonID = buttons[randomNumber].getResources().getResourceEntryName(buttons[randomNumber].getId());
                    gameStatePointer = Integer.parseInt(buttonID.substring(buttonID.length() - 1, buttonID.length())); // let target button be CPU
                    gameState[gameStatePointer] = 2;
                    CPUActive = false;
                    PlayerActive = true;
                    Count++; // round +1
                } else {
                    CPUActive = true;
                    PlayerActive = false;
                }
            }
            while (CPUActive == true && Count <9); // until target button is avalibility

            if(checkWinner()) {
                int duration = getDuration();
                tvShowText.setText("You Lose! Duration: " + duration + " sec!");
                btn_Continue.setVisibility(View.VISIBLE);
                for (int i = 0; i < buttons.length; i++) {
                    buttonID = "btn_" + i;
                    int resourceID = getResources().getIdentifier(buttonID, "id", getPackageName());
                    buttons[i].setClickable(false);
                }
                try {
                    db = SQLiteDatabase.openDatabase("/data/data/com.example.assignment/GamesLogDB",
                            null,
                            SQLiteDatabase.OPEN_READWRITE);
                    db.execSQL("insert into GamesLog(PlayDate,PlayTime,Duration,winningStatus) " +
                            "values('" + playDate + "', '" + playTime + "', '" + duration + "','Lose')");
                    Toast.makeText(this, "inserted data", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }else if(Count == 9){
                int duration = getDuration();
                tvShowText.setText("Draw! Duration: " + duration + " sec!");
                btn_Continue.setVisibility(View.VISIBLE);
                for(int i = 0;i<buttons.length;i++){
                    buttonID = "btn_" + i;
                    int resourceID = getResources().getIdentifier(buttonID,"id", getPackageName());
                    buttons[i].setClickable(false);
                }
                try{
                    db = SQLiteDatabase.openDatabase("/data/data/com.example.assignment/GamesLogDB",
                            null,
                            SQLiteDatabase.OPEN_READWRITE);
                    db.execSQL("insert into GamesLog(PlayDate,PlayTime,Duration,winningStatus) " +
                            "values('" + playDate + "', '" + playTime + "', '" + duration + "','Draw')");
                    Toast.makeText(this, "inserted data", Toast.LENGTH_SHORT).show();
                }
                catch(Exception e){
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }


        btn_Continue.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                playAgain();
                tvShowText.setText("You O vs CPU X");
                btn_Continue.setVisibility(View.INVISIBLE);
            }
        });

    }

    public boolean checkWinner(){ // check winner function
        boolean winnerResult = false;
        for(int[]winningPosion: winningPositions){
            if (gameState[winningPosion[0]] == gameState[winningPosion[1]]&&
                    gameState[winningPosion[1]]== gameState[winningPosion[2]]&&
                        gameState[winningPosion[0]]!=0){
                winnerResult = true;
            }
        }
        return winnerResult;
    }



    public void playAgain(){ // reset game
        Count = 0;
        PlayerActive = true;
        for(int i = 0;i<buttons.length; i++){
            gameState[i] = 0;
            buttons[i].setText("");
        }
        for(int i = 0;i<buttons.length;i++){
            String buttonID = "btn_" + i;
            int resourceID = getResources().getIdentifier(buttonID,"id", getPackageName());
            buttons[i].setClickable(true);
        }
        startTime = System.currentTimeMillis();
    }

    public int getDuration(){ // get the duration time when player inside this page.
        long finishTime = System.currentTimeMillis();
        int Duration = (int)(finishTime - startTime) / 1000;

        return Duration;
    }
}