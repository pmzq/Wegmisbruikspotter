package nl.wegmisbruikspotter.maps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Spot extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private static final String JSON_URL = "http://www.wegmisbruikspotter.nl/m_retreivesinglespot.php";

    String kenteken;
    Bitmap b;
    ImageView img;
    String ergenis;
    String merk;
    String description;
    String lat;
    String lng;
    //String Spot_id = ((Globals) this.getApplication()).getspotid();
    String Image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainspot);

        Intent i = getIntent();
        //String id = i.getStringExtra("id");

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

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

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

    private void getJSON(String url) {
        class GetJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(Spot.this, "Please Wait...",null,true,true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];
                String spotid = ((Globals) getApplication()).getspotid();
                BufferedReader bufferedReader = null;
                try {
                    //URL url = new URL(uri);
                    //HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    URL url = new URL(uri);
                    URLConnection con = url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    String data = URLEncoder.encode("id", "UTF-8") + "=" +
                            URLEncoder.encode(spotid, "UTF-8");

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
                        Image = e.getString("Image");

                        //Set photo image when available
                        if (Image != "") {
                            img = (ImageView) findViewById(R.id.img_photo);
                            information info = new information();
                            info.execute(Image);
                        }


                        EditText kenteken_text = (EditText) findViewById(R.id.kenteken);
                        kenteken_text.setText(kenteken);

                        TextView ergernis_text = (TextView) findViewById(R.id.txtErgernis);
                        ergernis_text.setText(ergernis);

                        TextView description_text = (TextView) findViewById(R.id.txtDescription);
                        description_text.setText(description);

                        //Set merk image
                        String merk_low = merk_main.toLowerCase();
                        String merk = merk_low.replaceAll("\\s+","");
                        int merk_imageID = getResources().getIdentifier(merk, "drawable" , getPackageName());
                        ImageView imageView = (ImageView) findViewById(R.id.img_merk);
                        String test = "R.drawable." + merk;
                        imageView.setImageResource(merk_imageID);
                        
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);


    }

    public class information extends AsyncTask<String, String, String>
    {
        @Override
        protected String doInBackground(String... arg0) {

            String test = (String) arg0[0];

            try
            {
                URL url = new URL("http://www.wegmisbruikspotter.nl/images/"+test+".jpg");
                InputStream is = new BufferedInputStream(url.openStream());
                b = BitmapFactory.decodeStream(is);


            } catch(Exception e){}
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            img.setImageBitmap(b);

            //Set onclick listener for layout
            img.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    Context context = getApplicationContext();
                    Intent intent = new Intent(context, FullScreenImageActivity.class);
                    ((Globals) getApplication()).setImageUrl("http://www.wegmisbruikspotter.nl/images/"+Image+".jpg");
                    startActivity(intent);
                }
            });
        }
    }

}
