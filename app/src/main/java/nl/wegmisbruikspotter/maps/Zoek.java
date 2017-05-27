package nl.wegmisbruikspotter.maps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
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

public class Zoek extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    public static final String MY_JSON ="MY_JSON";
    private static final String JSON_URL = "http://www.wegmisbruikspotter.nl/m_zoek.php";
    //private static final String JSON_URL = "https://wegmisbruikspotter.000webhostapp.com/m_retreivespots.php";
    List<List<String>> twoDim = new ArrayList<List<String>>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_zoek);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        String kenteken = ((Globals) getApplication()).getkenteken();

        //When kenteken is gevuld start serach immidiatly
        if(kenteken != "") {
            EditText kenteken_text = (EditText) findViewById(R.id.kenteken);
            kenteken_text.setText(kenteken);

            Zoek();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        //switch (item.getItemId()) {
        //   case R.id.action_settings:
        //       Context context = getApplicationContext();
        //       Intent intent = new Intent(context ,AllSpots.class);
        //      startActivity(intent);
        //return true;
        //        default:
        //        return super.onOptionsItemSelected(item);
        //  }


        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.zoeken) {
            Context context = getApplicationContext();
            Intent intent = new Intent(context ,Zoek.class);
            startActivity(intent);
            //return true;

        } else if (id == R.id.SpotNu) {
            Context context = getApplicationContext();
            Intent intent = new Intent(context ,MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.AllSpots) {
            Context context = getApplicationContext();
            Intent intent = new Intent(context ,AllSpots.class);
            startActivity(intent);
            //return true;

        } else if (id == R.id.MijnSpots) {
            Context context = getApplicationContext();
            Intent intent = new Intent(context ,MijnSpots.class);
            startActivity(intent);
            //return true;

        } else if (id == R.id.Rankings) {
            Context context = getApplicationContext();
            Intent intent = new Intent(context ,Rankings.class);
            startActivity(intent);
            //return true;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    //Run when Zoek is pressed.
    public void Zoek() {

        EditText kenteken_text = (EditText) findViewById(R.id.kenteken);
        String kenteken = kenteken_text.getText().toString();
        ((Globals) getApplication()).setkenteken(kenteken);

        //getJSON(JSON_URL);
        getJSON(JSON_URL);
        //Integer size = twoDim.size();
        //Log.v("Test1","laatste");

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
                loading.dismiss();

                //Context context = getApplicationContext();
                //int duration = Toast.LENGTH_SHORT;

                //Toast toast = Toast.makeText(context, s, duration);
                //toast.show();

                //textViewJSON.setText(s);
                try {

                    JSONArray json = new JSONArray(s);
                    Integer result =  json.length();
                    Log.e("test", result.toString());

                    //Find linear layout and remove all child views from possible previous search
                    LinearLayout l = (LinearLayout) findViewById(R.id.linearlayout);
                    l.removeAllViews();

                    if (result < 2) {
                        //Log.v("test","HUH");
                        //In case of no results
                        final TextView Message = new TextView(mContext);

                        /*Set layout properties
                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.WRAP_CONTENT
                        );
                        //params.setMargins(16,16,16,0);
                        //params.gravity = Gravity.CENTER_HORIZONTAL;
                        */
                        Message.setGravity(Gravity.CENTER);

                        Message.setText("Geen spots gevonden voor dit kenteken.");
                        //Message.setLayoutParams(params);

                        // add the textview to the linearlayout
                        l.addView(Message);
                    }
                    Integer test = json.length();
                    Log.v("test",test.toString());

                    for (int i = 0; i < json.length()-1; i++) {

                        //Make variable final
                        final int index = i;

                        JSONObject e = json.getJSONObject(i);
                        final String id = e.getString("id");
                        final String date = e.getString("datum");
                        final String ergernis = e.getString("ergernis");

                        Integer test1 = index;
                        String test2 = test1.toString();
                        Log.v("test",test2);

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
                            Layout.setBackgroundResource(R.drawable.bottom_border);
                            Layout.setClickable(true);
                            Layout.setPadding(0, 10, 0, 0);

                            //Make layout vertical
                            Layout.setOrientation(LinearLayout.VERTICAL);

                            //Apply defined parameters to layout
                            Layout.setLayoutParams(lprams);

                            //Set onclick listener for layout
                            Layout.setOnClickListener(new View.OnClickListener() {
                                public void onClick(View v) {
                                    //Log.v("TAG", "The index is " + index);
                                    Context context = getApplicationContext();
                                    Intent intent = new Intent(context, Spot.class);
                                    //String id = marker.getTitle();
                                    ((Globals) getApplication()).setspotid(id);
                                    startActivity(intent);
                                }
                            });

                            //Set layout properties
                            LinearLayout.LayoutParams tprams = new LinearLayout.LayoutParams(
                                    LinearLayout.LayoutParams.WRAP_CONTENT,
                                    LinearLayout.LayoutParams.MATCH_PARENT
                            );

                            // create two new textviews for Date and ergernis
                            final TextView TextViewDate = new TextView(mContext);
                            final TextView TextViewErgernis = new TextView(mContext);


                            // set properties of Date TextView
                            TextViewDate.setText(date);
                            TextViewDate.setTextSize(18);
                            TextViewDate.setTypeface(null, Typeface.BOLD);
                            TextViewDate.setPadding(10, 10, 0, 0);
                            TextViewDate.setLayoutParams(tprams);

                            // set properties of Ergernis TextView
                            TextViewErgernis.setText(ergernis);
                            TextViewErgernis.setPadding(10, 10, 0, 10);
                            TextViewErgernis.setLayoutParams(tprams);


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
