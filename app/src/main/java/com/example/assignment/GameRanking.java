package com.example.assignment;



import android.app.Activity;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GameRanking extends AppCompatActivity {
    private static String urltext = "http://192.168.0.101/api/ranking_api.php";
    ListView lvRanking;
    String[] ranking;
    SQLiteDatabase db;
    String sql;
    Cursor cursor = null;
    ArrayList<String> List = new ArrayList<>();
    ArrayAdapter<String> listAdapter;
    FetchPageTask task  = null;
    String[] name;
    int[] duration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_ranking);

        lvRanking = findViewById(R.id.lv_ranking);
        initialDB(); // create database and table.
        if (task == null || task.getStatus().equals(AsyncTask.Status.FINISHED)) {
            task = new FetchPageTask();
            task.execute(urltext);// pass url to task
        }
    }

    private class FetchPageTask extends AsyncTask<String, Integer, String>{
        @Override
        protected String doInBackground(String... values) {
            InputStream inputStream = null;
            String result = "";
            URL url = null;
            try{
                url = new URL(values[0]);
                HttpURLConnection con = (HttpURLConnection)
                        url.openConnection();
                con.setRequestMethod("GET"); // get data in url
                con.connect(); // connect to url

                inputStream = con.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while((line = bufferedReader.readLine()) !=null) {
                    result += line;

                }
                Log.d("doInBackground", "get data complete");
                inputStream.close();
                Log.d("doInBackground", result);
            }catch (Exception e) {
                result = e.getMessage();
            }

            return result; // pass result string
        }

        protected void onPostExecute(String result){
            try {
                db = SQLiteDatabase.openDatabase(
                        "/data/data/com.example.assignment/rankingDB" ,
                        null,
                        SQLiteDatabase.OPEN_READWRITE);
                JSONArray jsonArray = new JSONArray(result);
                ranking = new String[jsonArray.length()];
                name = new String[jsonArray.length()];
                duration = new int[jsonArray.length()];
                for (int i = 0; i < jsonArray.length(); i++) { // get all jsonarray value
                    name[i] = jsonArray.getJSONObject(i).getString("Name"); // get name column data in jsonArray
                    duration[i] = jsonArray.getJSONObject(i).getInt("Duration");// get duration column data in jsonArray
                    db.execSQL("insert into ranking values('" + name[i] + "', '" + duration[i] + "')"); // insert each column data to datbase
                }

                cursor = db.rawQuery("select * from ranking order by duration asc", null); // get data with database and sort by duration time.
                ArrayList<String> List = new ArrayList<>();
                for (int i = 0; i < cursor.getCount(); i++) {
                    cursor.moveToPosition(i);
                    String Name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                    int Duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
                    List.add("Rank " + (i + 1) + ", " + Name + ", " + Duration + " sec"); // add String format to List
                    ListAdapter listAdapter = new ArrayAdapter<String>(GameRanking.this, android.R.layout.simple_list_item_1, List); // set listAdapter
                    lvRanking.setAdapter(listAdapter); // add List data to listview
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    public void initialDB(){
        try{
            db = SQLiteDatabase.openDatabase(
                    "/data/data/com.example.assignment/rankingDB" ,
                    null,
                    SQLiteDatabase.CREATE_IF_NECESSARY);
            //part b
            sql = "DROP TABLE IF EXISTS ranking;";
            db.execSQL(sql);
            sql = "CREATE TABLE ranking(Name text, duration int);";
            db.execSQL(sql);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /*public void onClickShow(View v){
        InputStream inputStream = null;
        String result = "";

        URL url;

        try {
            db = SQLiteDatabase.openDatabase(
                    "/data/data/com.example.assignment/rankingDB" ,
                    null,
                    SQLiteDatabase.OPEN_READWRITE);
            //Log.d("Go", urltext);
            url = new URL(urltext);
            //Log.d("Go", url.toString());
            HttpURLConnection con = (HttpURLConnection) url.openConnection();

            con.setRequestMethod("GET");
            con.connect();
            Log.d("Go", url.toString());

            inputStream = con.getInputStream();

            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = bufferedReader.readLine())!=null) {
                result += line;
            }

            //Log.d("Go", result);
            inputStream.close();

            JSONArray jsonArray = new JSONArray(result);
            ranking = new String[jsonArray.length()];
            for (int i=0; i<jsonArray.length();i++){
                String Name = jsonArray.getJSONObject(i).getString("Name");
                int Duration = jsonArray.getJSONObject(i).getInt("Duration");
                db.execSQL("insert into ranking values('" + Name + "', '" + Duration + "')");
            }

            cursor = db.rawQuery("select * from ranking order by duration asc", null);
            ArrayList<String> List = new ArrayList<>();
            for(int i =0 ;i< cursor.getCount(); i ++){
                cursor.moveToPosition(i);
                String name = cursor.getString(cursor.getColumnIndexOrThrow("Name"));
                int duration = cursor.getInt(cursor.getColumnIndexOrThrow("duration"));
                List.add("Rank " + (i+1) + ", " + name + ", " + duration + " sec");
                ListAdapter listAdapter = new ArrayAdapter<>(this,android.R.layout.simple_list_item_1, List);
                lvRanking.setAdapter(listAdapter);
            }
            con.disconnect();
        }catch (Exception e){
            Log.e("Go", "Exception catch " + e.getMessage());
        }
    }*/
}

