package nl.wegmisbruikspotter.maps;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

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

public class Tab2Spotters extends Fragment {

    public Tab2Spotters() {
// Required empty public constructor
    }

    private static final String JSON_URL = "http://www.wegmisbruikspotter.nl/m_spotters.php";
    private AdView mAdView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ArrayList<String> years = new ArrayList<String>();
        int thisYear = Calendar.getInstance().get(Calendar.YEAR);
        for (int i = 2012; i <= thisYear; i++) {
            years.add(Integer.toString(i));
        }
        ArrayAdapter<String> jaar_adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(), android.R.layout.simple_spinner_item, years);
        //Log.v("test",Integer.toString(thisYear));



        final View v = inflater.inflate(R.layout.tab2_spotters, container, false);
        Spinner spinYear = (Spinner) v.findViewById(R.id.jaar);
        spinYear.setAdapter(jaar_adapter);
        String jaar = Integer.toString(thisYear);
        ((Globals) getActivity().getApplication()).setjaar(jaar);

        //Load adds.
        mAdView = (AdView) v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        getJSON(JSON_URL, v);

        int spinnerPosition = jaar_adapter.getPosition(Integer.toString(thisYear));
        spinYear.setSelection(spinnerPosition);

        final int iCurrentSelection = spinYear.getSelectedItemPosition();

        spinYear.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                if (iCurrentSelection != position){
                    Jaar(v);
                }
                //iCurrentSelection = position;
                //Log.v("test","testnow");
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
                // your code here
            }
        });

        // Inflate the layout for this fragment
        return v;
// Inflate the layout for this fragment
        //return inflater.inflate(R.layout.tab2_spotters, container, false);
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

                String jaar = ((Globals) getActivity().getApplication()).getjaar();
                BufferedReader bufferedReader = null;

                try {

                    //URL url = new URL(uri);
                    //HttpURLConnection con = (HttpURLConnection) url.openConnection();
                    URL url = new URL(uri);
                    URLConnection con = url.openConnection();
                    StringBuilder sb = new StringBuilder();

                    String data = URLEncoder.encode("jaar", "UTF-8") + "=" +
                            URLEncoder.encode(jaar, "UTF-8");



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

                try {

                    JSONArray json = new JSONArray(s);
                    Integer result =  json.length();

                    //Find linear layout and remove all child views from possible previous search
                    LinearLayout l = (LinearLayout) rootView.findViewById(R.id.linearLayout);
                    l.removeAllViews();

                    if (result < 2) {
                        //Log.v("test","HUH");
                        //In case of no results
                        final TextView Message = new TextView(mContext);
                        Message.setGravity(Gravity.CENTER);

                        Message.setText("Geen spotters gevonden voor dit jaar.");
                        //Message.setLayoutParams(params);

                        // add the textview to the linearlayout
                        l.addView(Message);
                    }
                    Integer test = json.length();
                    //Log.v("test","test1");
                    for (int i = 0; i < json.length()-1; i++) {

                        //Make variable final
                        final int index = i;
                        final int rank = index +1;

                        JSONObject e = json.getJSONObject(i);
                        final String id = e.getString("id");
                        final String user = e.getString("user");
                        final String Spots = e.getString("spots");

                        //Get machine width and height
                        final View parentView= rootView.findViewById(R.id.mainContent);
                        int height=parentView.getHeight();
                        int width=parentView.getWidth();

                        //Create layout to hold one place
                        final LinearLayout Layout = new LinearLayout(mContext);

                        //Set layout properties
                        LinearLayout.LayoutParams lprams = new LinearLayout.LayoutParams(
                                LinearLayout.LayoutParams.MATCH_PARENT,
                                LinearLayout.LayoutParams.MATCH_PARENT
                        );
                        //Set margins
                        lprams.setMargins(16, 16, 16, 0);
                        //Change background
                        Layout.setBackgroundResource(R.drawable.bottom_border);
                        Layout.setClickable(true);
                        //Layout.setPadding(0, 10, 0, 0);

                        //Make layout vertical
                        Layout.setOrientation(LinearLayout.HORIZONTAL);

                        //Apply defined parameters to layout
                        Layout.setLayoutParams(lprams);

                        //Set onclick listener for layout
                        /*Layout.setOnClickListener(new View.OnClickListener() {
                            public void onClick(View v) {
                                //Log.v("TAG", "The index is " + index);
                                Context context = getApplicationContext();
                                Intent intent = new Intent(context, Zoek.class);
                                //String id = marker.getTitle();
                                ((Globals) getActivity().getApplication()).setkenteken(kenteken);
                                startActivity(intent);
                            }
                        });*/

                        //Set lnumber_ayout properties
                        LinearLayout.LayoutParams number_params = new LinearLayout.LayoutParams(
                                (int) (width * .20),LinearLayout.LayoutParams.WRAP_CONTENT

                        );

                        //Set users layout properties
                        LinearLayout.LayoutParams params_user = new LinearLayout.LayoutParams(
                                (int) (width * .60),LinearLayout.LayoutParams.WRAP_CONTENT

                        );

                        // create three new textviews for Date and ergernis
                        final TextView TextViewNumber = new TextView(mContext);
                        final TextView TextViewUser = new TextView(mContext);
                        final TextView TextViewSpots = new TextView(mContext);

                        // set properties of number TextView
                        TextViewNumber.setText(Integer.toString(rank));
                        TextViewNumber.setTextSize(30);
                        TextViewNumber.setPadding(20, 0, 0, 5);
                        TextViewNumber.setTypeface(null, Typeface.BOLD);
                        TextViewNumber.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                        Log.v("test","test2");
                        //Set testNumber width to 15%
                        //TextViewNumber.getLayoutParams().width = (int) (width * .15);
                        TextViewNumber.setLayoutParams(number_params);

                        // set properties of Ergernis TextView
                        TextViewUser.setText(user);
                        TextViewUser.setPadding(25, 0, 0, 5);
                        TextViewUser.setTextSize(24);
                        TextViewUser.setLayoutParams(params_user);
                        //TextViewUser.getLayoutParams().height = 95;



                        //Set testuser width to 70%
                        //TextViewUser.getLayoutParams().width = (int) (width * .70);

                        //TextViewUser.setBackgroundResource(R.drawable.kentekenplaat);
                        TextViewUser.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);


                        // set properties of Date TextView
                        TextViewSpots.setText(Spots);
                        TextViewSpots.setTextSize(30);
                        TextViewSpots.setPadding(0, 0, 0, 5);
                        TextViewSpots.setTypeface(null, Typeface.BOLD);
                        TextViewSpots.setGravity(Gravity.CENTER_HORIZONTAL|Gravity.CENTER_VERTICAL);
                        //Set testspots width to 15%
                        //TextViewSpots.getLayoutParams().width = (int) (width * .15);
                        TextViewSpots.setLayoutParams(number_params);

                        // add the textview to the linearlayout
                        l.addView(Layout);
                        Layout.addView(TextViewNumber);
                        Layout.addView(TextViewUser);
                        Layout.addView(TextViewSpots);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }


        }

        GetJSON gj = new GetJSON(getActivity().getApplicationContext(),rootView);
        gj.execute(url);

    }

    //Run when jaar is chosen.
    public void Jaar(View view) {

        Spinner jaar_spinner = (Spinner) view.findViewById(R.id.jaar);
        String jaar = jaar_spinner.getSelectedItem().toString();
        ((Globals) getActivity().getApplication()).setjaar(jaar);
        //Log.v("test",jaar);
        //getJSON(JSON_URL);
        getJSON(JSON_URL,view);
        //Integer size = twoDim.size();
        //Log.v("Test1","laatste");

    }

}