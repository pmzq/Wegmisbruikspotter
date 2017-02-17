package nl.wegmisbruikspotter.maps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

public class Spot extends AppCompatActivity {

    private static final String JSON_URL = "https://wegmisbruikspotter.000webhostapp.com/m_retreivesinglespot.php";

    String kenteken;
    String ergenis;
    String merk;
    String description;
    String lat;
    String lng;
    //String Spot_id = ((Globals) this.getApplication()).getspotid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot);

        Intent i = getIntent();
        //String id = i.getStringExtra("id");

        getJSON(JSON_URL);
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


                        //String id = e.getString("id");

                        //String[] point2 = point.split(",");
                        //double lat1 = Double.parseDouble(latitude);
                        //double lng1 = Double.parseDouble(longitude);

                        //Marker melbourne =
                        //Marker melbourne = mMap.addMarker(new MarkerOptions().title(e.getString("id")).position(new LatLng(lat1, lng1)).snippet(getResources().getString(R.string.Meer_info)));
                        //melbourne.showInfoWindow();

                        EditText kenteken_text = (EditText) findViewById(R.id.kenteken);
                        kenteken_text.setText(kenteken);

                        TextView ergernis_text = (TextView) findViewById(R.id.txtErgernis);
                        ergernis_text.setText(ergernis);

                        EditText description_text = (EditText) findViewById(R.id.txtDescription);
                        description_text.setText(description);

                        //Set merk image
                        String merk_low = merk_main.toLowerCase();
                        String merk = merk_low.replaceAll("\\s+","");
                        int merk_imageID = getResources().getIdentifier(merk, "drawable" , getPackageName());
                        ImageView imageView = (ImageView) findViewById(R.id.imgMerk);
                        //String test = "R.drawable." + merk;
                        imageView.setImageResource(merk_imageID);

                        Context context1 = getApplicationContext();
                        int duration1 = Toast.LENGTH_SHORT;

                        Toast toast1 = Toast.makeText(context1, ergernis, duration1);
                        toast1.show();
                        //Toast toast2 = Toast.makeText(context1, id, duration1);
                        //toast2.show();
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        GetJSON gj = new GetJSON();
        gj.execute(url);
    }
}
