package nl.wegmisbruikspotter.maps;


import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    String kenteken;
    String ergernis;
    String merk;
    String description;
    String imageURL;
    String latitude;
    String longitude;
    Double lat;
    Double lng;
    int duration = Toast.LENGTH_SHORT;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        Intent i = getIntent();
        kenteken = i.getStringExtra("kenteken");
        merk = i.getStringExtra("merk");
        ergernis = i.getStringExtra("ergernis");
        description = i.getStringExtra("description");


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

        // Add a marker in Sydney and move the camera
        LatLng Nederland = new LatLng(52.1883501, 5.0638998);
        // mMap.addMarker(new MarkerOptions().position(Nederland).title("Marker in Nederland"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(Nederland));
        mMap.animateCamera( CameraUpdateFactory.zoomTo( 7.0f ) );

        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng position) {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(position));

                lat = position.latitude;
                lng = position.longitude;
                /*
                Context context = getApplicationContext();
                CharSequence text = position.toString();
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

                Context context1 = getApplicationContext();
                CharSequence text1 = description;
                int duration1 = Toast.LENGTH_SHORT;

                Toast toast1 = Toast.makeText(context1, text1, duration1);
                toast1.show();
                */
            }
        });

    }

    /**
     * Called when the user clicks the Selecter positie button.
     */
    public void Select(View view) {

        //Save locations and return to mainactivity
        Intent intent = new Intent(this, MainActivity.class);
        ((Globals) this.getApplication()).setlatitude(lat.toString());
        ((Globals) this.getApplication()).setlongitude(lng.toString());
        startActivity(intent);

    }




}
