package nl.wegmisbruikspotter.maps;

import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Base64;
import android.util.Log;
import android.util.Pair;
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
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import static android.R.attr.fragment;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    //Button btpic, btnup;
    private Uri fileUri;
    String picturePath;
    //Uri selectedImage;
    Bitmap photo;
    String ba;

    String kenteken;
    String ergernis;
    String merk;
    String description;
    String imageURL;
    String latitude;
    String longitude;
    Double lat;
    Double lng;
    Context context = this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //String facebookName = ((Globals) this.getApplication()).getfacebookName();

        //Context context = getApplicationContext();
        //int duration = Toast.LENGTH_SHORT;

        //Toast toast = Toast.makeText(context, facebookName, duration);
        //toast.show();

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
        //kenteken = i.getStringExtra("kenteken");
        kenteken =  ((Globals) this.getApplication()).getkenteken();
        EditText kenteken_text = (EditText) findViewById(R.id.kenteken);
        kenteken_text.setText(kenteken);

        merk = ((Globals) this.getApplication()).getmerk();
        Spinner merk_spinner = (Spinner) findViewById(R.id.Merk);
        //Set the merk_spinner to a previous selected value
        selectValue(merk_spinner, merk);

        //ergernis = i.getStringExtra("ergernis");
        ergernis = ((Globals) this.getApplication()).getergernis();
        Spinner ergernis_spinner = (Spinner) findViewById(R.id.ergernis);
        //Set the merk_spinner to a previous selected value
        selectValue(ergernis_spinner, ergernis);

        description = ((Globals) this.getApplication()).getdescription();
        EditText description_text = (EditText) findViewById(R.id.txtDescription);
        description_text.setText(description);
        printKeyHash();

    }

    private void printKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.i("KeyHash:",
                        Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("jk", "Exception(NameNotFoundException) : " + e);
        } catch (NoSuchAlgorithmException e) {
            Log.e("mkm", "Exception(NoSuchAlgorithmException) : " + e);
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

        if (id == R.id.AllSpots) {
            Context context = getApplicationContext();
            Intent intent = new Intent(context ,AllSpots.class);
            startActivity(intent);
            //return true;

        } else if (id == R.id.SpotNu) {
            Context context = getApplicationContext();
            Intent intent = new Intent(context ,MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    /**
     * Called when the user clicks the Selecteer locatie button
     */
    public void Selecteer(View view) {

            //Retreived filled in values
            Spinner ergernis_spinner = (Spinner) findViewById(R.id.ergernis);
            String ergernis = ergernis_spinner.getSelectedItem().toString();

            Spinner merk_spinner = (Spinner) findViewById(R.id.Merk);
            String merk = merk_spinner.getSelectedItem().toString();

            EditText kenteken_text = (EditText) findViewById(R.id.kenteken);
            String kenteken = kenteken_text.getText().toString();

            EditText description_text = (EditText) findViewById(R.id.txtDescription);
            String description = description_text.getText().toString();

            ((Globals) getApplication()).setergernis(ergernis);
            ((Globals) getApplication()).setmerk(merk);
            ((Globals) getApplication()).setkenteken(kenteken);
            ((Globals) getApplication()).setdescription(description);

            //Show maps acitvity to select position.
            Intent intent = new Intent(this, MapsActivity.class);
            startActivity(intent);

    }

    private static final int PICK_PHOTO_FOR_AVATAR = 0;
    //Used for adding an image
    public void pickImage(View View) {

        //First check if the location has already been selected else the image is lost
        String latitude = ((Globals) this.getApplication()).getlatitude();
        String longitude = ((Globals) this.getApplication()).getlongitude();

        if (latitude == null || longitude == null) {
            Context context = getApplicationContext();
            CharSequence text = "Selecteer eerst een locatie";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
        } else {
            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
            intent.setType("image/*");
            startActivityForResult(intent, PICK_PHOTO_FOR_AVATAR);
        }
    }

    //Function to get the result of the specified requestCode
    //0 = Retreive image and add it to foto imageView
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_PHOTO_FOR_AVATAR && resultCode == MainActivity.RESULT_OK) {
            if (data == null) {
                //Display an error
                return;
            }
            try {
                //Set selected image...
                InputStream inputStream = context.getContentResolver().openInputStream(data.getData());
                Bitmap newProfilePic = BitmapFactory.decodeStream(inputStream);
                ImageView foto_image = (ImageView) findViewById(R.id.foto);
                foto_image.setImageBitmap(newProfilePic);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }


    public void Spot(View view) {

        //Retreived filled in values
        Spinner ergernis_spinner = (Spinner) findViewById(R.id.ergernis);
        String ergernis = ergernis_spinner.getSelectedItem().toString();

        Spinner merk_spinner = (Spinner) findViewById(R.id.Merk);
        String merk = merk_spinner.getSelectedItem().toString();

        EditText kenteken_text = (EditText) findViewById(R.id.kenteken);
        String kenteken = kenteken_text.getText().toString();

        EditText description_text = (EditText) findViewById(R.id.txtDescription);
        String description = description_text.getText().toString();

        //Make variables globally available
        ((Globals) this.getApplication()).setkenteken(kenteken);
        ((Globals) this.getApplication()).setergernis(ergernis);
        ((Globals) this.getApplication()).setmerk(merk);
        ((Globals) this.getApplication()).setdescription(description);

        //Check if Ergenis has been selected
        if (ergernis.equals("")) {

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
            //Place final Spot on website.
            String latitude = ((Globals) this.getApplication()).getlatitude();
            String longitude = ((Globals) this.getApplication()).getlongitude();
            String facebookID = ((Globals) this.getApplication()).getfacebookID();
            new NewSpotActivity(this, 1).execute(kenteken, ergernis, merk, description, latitude, longitude,facebookID);
        }
    }

    //This module is used to load the correct value for a spinner.
    private void selectValue(Spinner spinner, Object value) {
        for (int i = 0; i < spinner.getCount(); i++) {
            if (spinner.getItemAtPosition(i).equals(value)) {
                spinner.setSelection(i);
                break;
            }
        }
    }

    private void upload() {
        // Image location URL
        Log.e("path", "----------------" + picturePath);

        // Image
        Bitmap bm = BitmapFactory.decodeFile(picturePath);
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 90, bao);
        byte[] ba = bao.toByteArray();
        //ba1 = Base64.encodeBytes(ba);

        Log.e("base64", "-----" + ba);

        // Upload image to server
        new uploadToServer().execute();

    }

    public class uploadToServer extends AsyncTask<Void, Void, String> {

        private ProgressDialog pd = new ProgressDialog(MainActivity.this);
        protected void onPreExecute() {
            super.onPreExecute();
            pd.setMessage("Wait image uploading!");
            pd.show();
        }

        @Override
        protected String doInBackground(Void... params) {

            //ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
            //nameValuePairs.add(new BasicNameValuePair("base64", ba));
            //nameValuePairs.add(new BasicNameValuePair("ImageName", System.currentTimeMillis() + ".jpg"));

            List<Pair<String, String>> params1 = new ArrayList<>();
            params1.add(new Pair<>("base64", ba));
            params1.add(new Pair<>("ImageName", System.currentTimeMillis() + ".jpg"));

            try {
                HttpClient httpclient = new DefaultHttpClient();
                HttpPost httppost = new HttpPost(URL);
                httppost.setEntity(new UrlEncodedFormEntity(params1));
                HttpResponse response = httpclient.execute(httppost);
                String st = EntityUtils.toString(response.getEntity());
                Log.v("log_tag", "In the try Loop" + st);

            } catch (Exception e) {
                Log.v("log_tag", "Error in http connection " + e.toString());
            }
            return "Success";

        }

        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            pd.hide();
            pd.dismiss();
        }
    }



}

