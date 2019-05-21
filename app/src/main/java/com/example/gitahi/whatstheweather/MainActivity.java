package com.example.gitahi.whatstheweather;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

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

        try {
            DownloadTask task = new DownloadTask();

            String encodedCityName = URLEncoder.encode(editText.getText().toString(), "UTF-8");

            task.execute("https://samples.openweathermap.org/data/2.5/weather?q=" + encodedCityName + "&appid=b6907d289e10d714a6e88b30761fae22");

            InputMethodManager mgr = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            mgr.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Could Not Find Weather", Toast.LENGTH_SHORT).show();
        }
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

            Toast.makeText(getApplicationContext(), "Could Not Find Weather", Toast.LENGTH_SHORT).show();
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

            String message = "";

            for(int i = 0; i < arr.length(); i++) {
                JSONObject jsonPart = arr.getJSONObject(i);

                String main = jsonPart.getString("main");
                String description =jsonPart.getString("description");

                if (!main.equals("") && !description.equals("")) {
                    message += main + " : " + description + "\r\n";
                }
            }

            if (!message.equals("")) {
                resultsTextView.setText(message);
            } else {
                Toast.makeText(getApplicationContext(), "Could Not Find Weather", Toast.LENGTH_SHORT).show();
            }

        } catch(Exception e) {
            Toast.makeText(getApplicationContext(), "Could Not Find Weather", Toast.LENGTH_SHORT).show();
            e.printStackTrace();

        }

    }
}


}
