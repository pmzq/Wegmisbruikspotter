package nl.wegmisbruikspotter.maps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Spot extends AppCompatActivity {

    String kenteken;
    String ergenis;
    String merk;
    String description;
    String lat;
    String lng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spot);

        Intent i = getIntent();
        kenteken = i.getStringExtra("kenteken");
        ergenis = i.getStringExtra("ergenis");
        merk = i.getStringExtra("merk");
        description = i.getStringExtra("description");
        lat = i.getStringExtra("lat");
        lng = i.getStringExtra("lng");
    }
}
