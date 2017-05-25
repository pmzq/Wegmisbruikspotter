package nl.wegmisbruikspotter.maps;

import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.content.Context;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.SpannableString;
import android.text.method.ScrollingMovementMethod;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


public class AllSpots extends AppCompatActivity implements OnMapReadyCallback, NavigationView.OnNavigationItemSelectedListener {

    public GoogleMap mMap;
    public static final String MY_JSON ="MY_JSON";
    //private static final String JSON_URL = "https://wegmisbruikspotter.000webhostapp.com/m_retreivespots.php";
    private static final String JSON_URL = "http://www.wegmisbruikspotter.nl/m_retreivespots.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allspots);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

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

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Nederland
        LatLng Nederland = new LatLng(52.1883501, 5.0638998);
        //mMap.addMarker(new MarkerOptions().position(Nederland).title(""));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Nederland));
        mMap.animateCamera(CameraUpdateFactory.zoomTo( 8.0f ) );
        //Get JSON result of all spots
        getJSON(JSON_URL);

        /*//set on marker click listeners
        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener()
        {

            @Override
            public boolean onMarkerClick(Marker arg0) {
                Log.i("GoogleMapActivity", "onMarkerClick");
                Toast.makeText(getApplicationContext(),
                        "Marker Clicked: " + arg0.getTitle(), Toast.LENGTH_SHORT)
                        .show();
                return true;
            }

        });
        */

        //Start onlcik event handler for info window
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                //Load spot activity when info window is clicked
                Context context = getApplicationContext();
                Intent intent = new Intent(context ,Spot.class);
                String id = marker.getTitle();
                ((Globals) getApplication()).setspotid(id);
                startActivity(intent);
            }
        });

        // Setting a custom info window adapter for the google map
       /* googleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            // Use default InfoWindow frame
            @Override
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            // Defines the contents of the InfoWindow
            @Override
            public View getInfoContents(Marker arg0) {

                // Getting view from the layout file info_window_layout
                View v = getLayoutInflater().inflate(R.layout.infowindowlayout, null);

                // Getting the position from the marker
                LatLng latLng = arg0.getPosition();
                String title = arg0.getTitle();

                // Getting reference to the TextView to set latitude
                TextView tvKenteken = (TextView) v.findViewById(R.id.tv_kenteken);

                // Getting reference to the TextView to set longitude
                TextView tvMsg = (TextView) v.findViewById(R.id.tv_msg);

                // Setting the latitude
                tvKenteken.setText(title);

                String mystring=new String((getResources().getString(R.string.Meer_info)));
                SpannableString content = new SpannableString(mystring);
                content.setSpan(new UnderlineSpan(), 0, mystring.length(), 0);

                // Setting the longitude
                tvMsg.setText(content);

                // Returning the view containing InfoWindow contents
                return v;

            }
        });
        */

    }


    private void getJSON(String url) {
        class GetJSON extends AsyncTask<String, Void, String>{
            ProgressDialog loading;

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(AllSpots.this, "Please Wait...",null,true,true);
            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));

                    String json;
                    while((json = bufferedReader.readLine())!= null){
                        sb.append(json+"\n");
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

                //textViewJSON.setText(s);
                try {
                    JSONArray json = new JSONArray(s);

                    for (int i = 0; i < json.length(); i++) {
                        JSONObject e = json.getJSONObject(i);
                        String latitude = e.getString("latitude");
                        String longitude = e.getString("longitude");
                        //String id = e.getString("id");

                        //String[] point2 = point.split(",");
                        double lat1 = Double.parseDouble(latitude);
                        double lng1 = Double.parseDouble(longitude);

                        //Marker melbourne =
                        Marker melbourne = mMap.addMarker(new MarkerOptions().title(e.getString("id")).position(new LatLng(lat1, lng1)).snippet(getResources().getString(R.string.Meer_info)));
                        melbourne.showInfoWindow();
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
