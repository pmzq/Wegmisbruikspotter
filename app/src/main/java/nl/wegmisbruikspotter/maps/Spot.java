package nl.wegmisbruikspotter.maps;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

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

        EditText kenteken_text = (EditText) findViewById(R.id.kenteken);
        kenteken_text.setText(kenteken);
    }
}
