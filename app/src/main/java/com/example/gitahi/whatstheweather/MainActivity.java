package com.example.gitahi.whatstheweather;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    EditText editText;
    TextView resultsTextView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText= findViewById(R.id.editText);
        resultsTextView = findViewById(R.id.resultsTextView);
    }


    public void getWeather(View view) {


        DownloadTask task = new DownloadTask();
        task.execute("https://samples.openweathermap.org/data/2.5/weather?q="+ editText.getText().toString() +"&appid=b6907d289e10d714a6e88b30761fae22");
    }






    public class DownloadTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... urls) {
        String result = "";
        URL url;
        HttpURLConnection urlConnection = null;

        try {

            url = new URL(urls[0]);
            urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();

            while (data != -1) {
                char current = (char) data;
                result += current;
                data = reader.read();
            }

            return result;

        } catch(Exception e ) {
            e.printStackTrace();
            return null;
        }
    }

    // this is what happens after the above finishes running.
    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);

        // Log.i("JSON", s);

        try {
            // we get the results object.
            JSONObject jsonObject = new JSONObject(s);
            String results = jsonObject.getString("weather");

             Log.i("People Results", results);

            JSONArray arr = new JSONArray(results);

            for(int i = 0; i < arr.length(); i++) {
                JSONObject jsonPart = arr.getJSONObject(i);

                Log.i("Main", jsonPart.getString("main"));
                Log.i("Description", jsonPart.getString("description"));
            }

        } catch(Exception e) {
            e.printStackTrace();
        }

    }
}


}
