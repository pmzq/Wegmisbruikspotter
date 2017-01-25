package nl.wegmisbruikspotter.maps;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    String kenteken;
    String ergernis;
    String merk;
    String description;
    String imageURL;
    String latitude;
    String longitude;
    Double lat;
    Double lng;

    /**
     * Called when the user clicks the spotnu button
     */
    public void Selecteer(View view) {

        Spinner ergernis_spinner = (Spinner) findViewById(R.id.ergernis);
        String ergernis = ergernis_spinner.getSelectedItem().toString();

        Spinner merk_spinner = (Spinner) findViewById(R.id.Merk);
        String merk = merk_spinner.getSelectedItem().toString();

        EditText kenteken_text = (EditText) findViewById(R.id.kenteken);
        String kenteken = kenteken_text.getText().toString();

        EditText description_text = (EditText) findViewById(R.id.description);
        String description = description_text.getText().toString();

        //Check if Ergenis has been selected
        if (ergernis.equals("Selecteer Ergernis")) {

            Context context = getApplicationContext();
            CharSequence text = "Selecteer een Ergernis";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            // Ergenis.setError( "Selecteer een " );

        } else if (kenteken.equals("")) {

            Context context = getApplicationContext();
            CharSequence text = "Vul het kenteken in";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        } else if (description.equals("")) {

            Context context = getApplicationContext();
            CharSequence text = "Vul een omschrijving in";
            int duration = Toast.LENGTH_SHORT;

            Toast toast = Toast.makeText(context, text, duration);
            toast.show();

        }else {
            //Send variables to next activity
            Intent intent = new Intent(this, MapsActivity.class);
            // set
            ((Globals) this.getApplication()).setkenteken(kenteken);
            ((Globals) this.getApplication()).setergernis(ergernis);
            ((Globals) this.getApplication()).setmerk(merk);
            ((Globals) this.getApplication()).setdescription(description);

            //intent.putExtra("kenteken", kenteken);
            //intent.putExtra("ergernis", ergernis);
            //intent.putExtra("merk", merk);
            //intent.putExtra("description", description);
            startActivity(intent);
        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //EditText kenteken_text = (EditText) findViewById(R.id.kenteken);
        //kenteken_text.requestFocus();

        /* FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        }); */

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Spinner spinner = (Spinner) findViewById(R.id.ergernis);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.Ergernis_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        Spinner Merk_spinner = (Spinner) findViewById(R.id.Merk);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> Merk = ArrayAdapter.createFromResource(this,
                R.array.Merk_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        Merk.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        Merk_spinner.setAdapter(Merk);

        //Retreive filled in fields when already filled in.
        Intent i = getIntent();
        kenteken = i.getStringExtra("kenteken");
        merk = i.getStringExtra("merk");
        ergernis = i.getStringExtra("ergernis");
        description = i.getStringExtra("description");

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

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void Spot(View view) {

        //Place final Spot on website.

        //latitude = lat.toString();
        //longitude = lng.toString();
        //new SigninActivity(this,1).execute(kenteken,ergernis,merk,description,latitude,longitude);
        Context context1 = getApplicationContext();
        int duration1 = Toast.LENGTH_SHORT;
        String latitude = ((Globals) this.getApplication()).getlatitude();

        Toast toast1 = Toast.makeText(context1, latitude, duration1);
        toast1.show();
    }
}

