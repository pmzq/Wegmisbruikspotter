package nl.wegmisbruikspotter.maps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.Calendar;

import static com.facebook.FacebookSdk.getApplicationContext;

public class Tab1Wegmisbruikers extends Fragment {
    private static final String JSON_URL = "https://wegmisbruikspotter.000webhostapp.com/m_wegmisbruikers.php";

    public Tab1Wegmisbruikers() {
// Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    //@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2010; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> jaar_adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, years);
        Log.v("test",Integer.toString(thisYear));


        View v = inflater.inflate(R.layout.tab1_wegmisbruikers, container, false);
        Spinner spinYear = (Spinner) v.findViewById(R.id.jaar);
        spinYear.setAdapter(jaar_adapter);

        getJSON(JSON_URL, v);

        // Inflate the layout for this fragment
        return v;


    }

    public void getJSON(String url,View rootView) {


        class GetJSON extends AsyncTask<String, Void, String> {
            ProgressDialog loading;

            private Context mContext;
            View rootView;

            public GetJSON (Context context, View rootView){
                mContext = context;
                this.rootView=rootView;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                loading = ProgressDialog.show(getActivity(), "Please Wait...",null,true,true);

            }

            @Override
            protected String doInBackground(String... params) {

                String uri = params[0];

                String kenteken = ((Globals) getActivity().getApplication()).getkenteken();
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
                    LinearLayout l = (LinearLayout) rootView.findViewById(R.id.linearlayout);
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
                                ((Globals) getActivity().getApplication()).setspotid(id);
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

        GetJSON gj = new GetJSON(getActivity().getApplicationContext(),rootView);
        gj.execute(url);

    }

    //Run when Zoek is pressed.
    public void Jaar(View view) {

        Spinner jaar_spinner = (Spinner) view.findViewById(R.id.jaar);
        String jaar = jaar_spinner.getSelectedItem().toString();
        ((Globals) getActivity().getApplication()).setkenteken(jaar);

        //getJSON(JSON_URL);
        getJSON(JSON_URL,view);
        //Integer size = twoDim.size();
        //Log.v("Test1","laatste");

    }

}