package nl.wegmisbruikspotter.maps;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class Zoek extends AppCompatActivity {

    public static final String MY_JSON ="MY_JSON";
    private static final String JSON_URL = "https://wegmisbruikspotter.000webhostapp.com/m_zoek.php";
    //private static final String JSON_URL = "https://wegmisbruikspotter.000webhostapp.com/m_retreivespots.php";
    List<List<String>> twoDim = new ArrayList<List<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoek);

    }

    //Run when Zoek is pressed.
    public void Zoek(View view) {

        EditText kenteken_text = (EditText) findViewById(R.id.kenteken);
        String kenteken = kenteken_text.getText().toString();
        ((Globals) getApplication()).setkenteken(kenteken);

        //getJSON(JSON_URL);
        getJSON(JSON_URL);
        Integer size = twoDim.size();
        Log.v("Test1","laatste");

    }

    public void getJSON(String url) {
        class GetJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            private Context mContext;

            public GetJSON (Context context){
                mContext = context;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Zoek.this, "Please Wait...",null,true,true);

            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                String kenteken = ((Globals) getApplication()).getkenteken();
                BufferedReader bufferedReader = null;
                try {
                    //URL url = new URL(uri);
                    //HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    URL url = new URL(uri);
                    URLConnection con = url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    String data = URLEncoder.encode("kenteken", "UTF-8") + "=" +
                            URLEncoder.encode(kenteken, "UTF-8");



                    con.setDoOutput(true);
                    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());

                    wr.write(data);
                    wr.flush();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json);
                    }

                    return sb.toString().trim();

                }catch(Exception e){
                    return null;
                }

            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);


                //Context context = getApplicationContext();
                //int duration = Toast.LENGTH_SHORT;

                //Toast toast = Toast.makeText(context, s, duration);
                //toast.show();

                //textViewJSON.setText(s);
                try {

                    JSONArray json = new JSONArray(s);
                    Integer test =  json.length();

                    for (int i = 0; i < json.length()-1; i++) {

                        JSONObject e = json.getJSONObject(i);
                        String id = e.getString("id");
                        String date = e.getString("datum");
                        String ergernis = e.getString("ergernis");

                        String[] inputLines = { id,date,ergernis };

                        for (String line : inputLines) {
                            List<String> row = new ArrayList<String>();

                            Scanner a = new Scanner(line);
                            while (a.hasNext())
                                row.add(a.next());

                            twoDim.add(row);
                        }

                        String result = "";
                        for(int b = 0; b < twoDim.size(); b++){
                            for(int j = 0; j < twoDim.get(b).size(); j++){
                                result += twoDim.get(b).get(j);
                            }
                            // System.out.println();
                            result += "\n";


                        }
                        Log.v("Test1","array gezet");
                        loading.dismiss();


                        //Create layout to hold single spot
                        final LinearLayout Layout = new LinearLayout(mContext);

                        //Set layout properties
                        LinearLayout.LayoutParams lprams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        //Set margins
                        lprams.setMargins(16, 16, 16, 0);
                        //Change background
                        Layout.setBackgroundResource(android.R.drawable.dialog_holo_light_frame);


                        //Make layout horizontal
                        Layout.setOrientation(LinearLayout.VERTICAL);

                        //Apply defined parameters to layout
                        Layout.setLayoutParams(lprams);

                        //Set layout properties
                        LinearLayout.LayoutParams tprams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.MATCH_PARENT
                        );

                        // create two new textviews for Date and ergernis
                        final TextView TextViewDate = new TextView(mContext);
                        final TextView TextViewErgernis = new TextView(mContext);

                        //Make variable final
                        final int index = i;

                        //Set onclick listener for layout
                        Layout.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Log.i("TAG", "The index is " + index);
                            }
                        });

                        // set properties of Date TextView
                        TextViewDate.setText(date);
                        TextViewDate.setTextSize(18);
                        TextViewDate.setTypeface(null, Typeface.BOLD);
                        TextViewDate.setPadding(10,10,0,0);
                        TextViewDate.setLayoutParams(tprams);

                        // set properties of Ergernis TextView
                        TextViewErgernis.setText(ergernis);
                        //TextViewErgernis.setTextSize(18);
                        //TextViewErgernis.setTypeface(null, Typeface.BOLD);
                        TextViewErgernis.setPadding(10,10,0,10);
                        TextViewErgernis.setLayoutParams(tprams);

                        LinearLayout l = (LinearLayout) findViewById(R.id.linearlayout);

                        // add the textview to the linearlayout
                        l.addView(Layout);
                        Layout.addView(TextViewDate);
                        Layout.addView(TextViewErgernis);

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }
        GetJSON gj = new GetJSON(this);
        gj.execute(url);






    }



}
