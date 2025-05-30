package com.example.assignment;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.Toast;

import java.util.ArrayList;

public class RecordPieChart extends AppCompatActivity {
    SQLiteDatabase db;
    String sql;
    Cursor cursor = null;
    String[] columns = {"gameID", "playDate", "playTime","duration","winningStatus"};
    String dataStrHeader = String.format("%4s %-12s %-9s %3s %-12s\n", "gameID", "playDate", "playTime","duration","winningStatus");
    String dataStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getSupportActionBar() != null)
            getSupportActionBar().hide();
        setContentView(new Panel(this));

    }

    class Panel extends View {
        int win,lose,draw,total;
        String title = "Your Winning Status";
        String items[] = {"Win","Lose","Draw"};

        int rColor[] = {Color.RED, Color.YELLOW, Color.GREEN};
        float iDegree = 0;
        public Panel(Context context) { super(context); }

        protected void onDraw(Canvas canvas){
            getData();
            int data[] = {win,lose,draw};
            super.onDraw(canvas);
            Paint paint = new Paint();
            paint.setStyle(Paint.Style.FILL);
            paint.setColor(Color.WHITE);
            canvas.drawPaint(paint);

            total = win+lose+draw;
            //Draw pie chart
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);

            for(int i = 0; i<data.length; i++){
                float drawDegree = data[i] * 360/total;
                Log.d("onDraw", drawDegree + "");

                paint.setColor(rColor[i]);
                RectF rectF = new RectF(50,100,getWidth()-50, getWidth()-50);
                canvas.drawArc(rectF,iDegree, drawDegree, true, paint);

                iDegree += drawDegree;
            }


            //Draw title
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(70);
            paint.setTypeface(Typeface.SERIF);

            canvas.drawText(title, 20,50,paint);

            paint.setTextSize(30);
            int vertSpace = getWidth() + 100;

            for(int i = items.length -1 ;i >= 0; i--){
                paint.setColor(rColor[i]);
                canvas.drawRect(getWidth()-200, vertSpace,
                        getWidth()-180, vertSpace+20,paint);

                paint.setColor(Color.BLACK);
                canvas.drawText(items[i], getWidth()-150, vertSpace +20, paint);

                vertSpace -= 40;
            }
        }
        public void getData(){
            try{
                db = SQLiteDatabase.openDatabase(
                        "/data/data/com.example.assignment/GamesLogDB" ,
                        null,
                        SQLiteDatabase.OPEN_READWRITE);
                cursor = db.rawQuery("select count(*) as Win from GamesLog where winningStatus = 'Win'",null);
                if(cursor != null&&cursor.getCount() != 0) {
                    while(cursor.moveToNext()) {
                        win = cursor.getInt(cursor.getColumnIndexOrThrow("Win"));
                    }
                }else{
                    win = 0;
                }
                cursor = db.rawQuery("select count(*) as Lose from GamesLog where winningStatus = 'Lose'",null);
                if(cursor != null&&cursor.getCount() != 0) {
                    while(cursor.moveToNext()) {
                        lose = cursor.getInt(cursor.getColumnIndexOrThrow("Lose"));
                    }
                }else{
                    lose = 0;
                }
                cursor = db.rawQuery("select count(*) as Draw from GamesLog where winningStatus = 'Draw'",null);
                if(cursor != null&&cursor.getCount() != 0) {
                    while(cursor.moveToNext()) {
                        draw = cursor.getInt(cursor.getColumnIndexOrThrow("Draw"));
                    }
                }else{
                    draw = 0;
                }
            }
            catch(Exception e)
            {
                Toast.makeText(RecordPieChart.this,e.getMessage(),Toast.LENGTH_LONG).show();
            }
        }
    }

}