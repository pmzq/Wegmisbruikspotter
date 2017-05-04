package nl.wegmisbruikspotter.maps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import static nl.wegmisbruikspotter.maps.R.dimen.font_size;

public class MijnSpots extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String JSON_URL = "https://wegmisbruikspotter.000webhostapp.com/m_mijnspots.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mijn_spots);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        getJSON(JSON_URL);
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

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                loading = ProgressDialog.show(MijnSpots.this, "Please Wait...",null,true,true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                String facebookID = ((Globals) getApplication()).getfacebookID();
                BufferedReader bufferedReader = null;
                try {
                    //URL url = new URL(uri);
                    //HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    URL url = new URL(uri);
                    URLConnection con = url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    String data = URLEncoder.encode("facebookID", "UTF-8") + "=" +
                            URLEncoder.encode(facebookID, "UTF-8");

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

                    for (int i = 0; i < json.length(); i++) {
                        JSONObject e = json.getJSONObject(i);
                        String kenteken = e.getString("kenteken");
                        String ergernis = e.getString("ergernis");
                        String description = e.getString("description");
                        String merk_main = e.getString("merk");
                        String date = e.getString("datum");

                        //Make variable final
                        final int index = i;

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
                        Layout.setClickable(true);

                        //Make layout vertical
                        Layout.setOrientation(LinearLayout.VERTICAL);

                        //Apply defined parameters to layout
                        Layout.setLayoutParams(lprams);


                        //Set onclick listener for layout
                        Layout.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                Log.v("TAG", "The index is " + index);
                            }
                        });

                        //Set layout properties
                        LinearLayout.LayoutParams tprams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.MATCH_PARENT
                        );

                        //Set layout properties
                        LinearLayout.LayoutParams kentekenprams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.WRAP_CONTENT,
                                LinearLayout.LayoutParams.MATCH_PARENT
                        );
                        kentekenprams.height = 74;
                        kentekenprams.width = 300;
                        kentekenprams.setMargins(5,5,0,0);

                        // create two new textviews for Date and ergernis
                        final TextView TextViewDate = new TextView(mContext);
                        final TextView TextViewErgernis = new TextView(mContext);
                        final TextView TextViewKenteken = new TextView(mContext);


                        // set properties of Date TextView
                        TextViewDate.setText(date);
                        TextViewDate.setTextSize(12);
                        //TextViewDate.setTypeface(null, Typeface.BOLD);
                        TextViewDate.setPadding(10, 10, 0, 0);
                        TextViewDate.setLayoutParams(tprams);

                        //set properties of Kenteken TextView
                        TextViewKenteken.setText(kenteken);
                        TextViewKenteken.setGravity(Gravity.CENTER_HORIZONTAL | Gravity.CENTER_VERTICAL);
                        TextViewKenteken.setTypeface(null, Typeface.BOLD);
                        //TextViewKenteken.setPadding(10, 10, 0, 0);
                        TextViewKenteken.setLayoutParams(kentekenprams);
                        TextViewKenteken.setBackgroundResource(R.drawable.kentekenplaat);

                        // set properties of Ergernis TextView
                        TextViewErgernis.setText(ergernis);
                        //TextViewErgernis.setTextSize(18);
                        //TextViewErgernis.setTypeface(null, Typeface.BOLD);
                        TextViewErgernis.setPadding(10, 10, 0, 10);
                        TextViewErgernis.setLayoutParams(tprams);

                        //Find linear layout for placing results'
                        LinearLayout l = (LinearLayout) findViewById(R.id.linearlayout);

                        // add the textview to the linearlayout
                        l.addView(Layout);
                        Layout.addView(TextViewKenteken);
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
