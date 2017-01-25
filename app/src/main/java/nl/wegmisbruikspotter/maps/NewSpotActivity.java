package nl.wegmisbruikspotter.maps;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import android.content.Context;
import android.os.AsyncTask;
import android.widget.TextView;
import android.widget.Toast;

public class NewSpotActivity extends AsyncTask<String,String,String> {
    private TextView statusField, roleField;
    private Context context;
    private int byGetOrPost = 0;
    int duration = Toast.LENGTH_SHORT;

    //flag 0 means get and 1 means post.(By default it is get.)
    public NewSpotActivity(Context context, int flag) {
        this.context = context;
        //this.statusField = statusField;
        //this.roleField = roleField;
        byGetOrPost = flag;

    }

    protected void onPreExecute() {
    }

    @Override
    protected String doInBackground(String... arg0) {
        if (byGetOrPost == 0) { //means by Get Method

            try {
                String kenteken = (String) arg0[0];
                //String password = (String) arg0[1];
                String link = "http://wegmisbruikspotter.000webhostapp.com/spotnu.php?kenteken=" + kenteken;


                URL url = new URL(link);
                HttpClient client = new DefaultHttpClient();
                HttpGet request = new HttpGet();
                request.setURI(new URI(link));
                HttpResponse response = client.execute(request);
                BufferedReader in = new BufferedReader(new
                        InputStreamReader(response.getEntity().getContent()));

                StringBuffer sb = new StringBuffer("");
                String line = "";

                while ((line = in.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                in.close();
                //return sb.toString();
                return "test";
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
            }
        } else {
            try {
                String kenteken = (String) arg0[0];
                String ergernis = (String) arg0[1];
                String merk = (String) arg0[2];
                String description = (String) arg0[3];
                String lat = (String) arg0[4];
                String lng = (String) arg0[5];

                String link = "http://www.wegmisbruikspotter.nl/m_spotnu.php";
                String data = URLEncoder.encode("kenteken", "UTF-8") + "=" +
                        URLEncoder.encode(kenteken, "UTF-8");
                data += "&" + URLEncoder.encode("ergernis", "UTF-8") + "=" +
                        URLEncoder.encode(ergernis, "UTF-8");
                data += "&" + URLEncoder.encode("merk", "UTF-8") + "=" +
                        URLEncoder.encode(merk, "UTF-8");
                data += "&" + URLEncoder.encode("description", "UTF-8") + "=" +
                        URLEncoder.encode(description, "UTF-8");
                data += "&" + URLEncoder.encode("latitude", "UTF-8") + "=" +
                        URLEncoder.encode(lat, "UTF-8");
                data += "&" + URLEncoder.encode("longitude", "UTF-8") + "=" +
                        URLEncoder.encode(lng, "UTF-8");

                URL url = new URL(link);
                URLConnection conn = url.openConnection();

                conn.setDoOutput(true);
                OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());

                wr.write(data);
                wr.flush();

                BufferedReader reader = new BufferedReader(new
                        InputStreamReader(conn.getInputStream()));

                StringBuilder sb = new StringBuilder();
                String line = null;

                // Read Server Response
                while ((line = reader.readLine()) != null) {
                    sb.append(line);
                    break;
                }

                return sb.toString();
            } catch (Exception e) {
                return new String("Exception: " + e.getMessage());
                //return "Error";
            }
        }
    }

    @Override
    protected void onPostExecute(String result) {
        //this.statusField.setText("Login Successful");
        //this.roleField.setText(result);

        Toast toast = Toast.makeText(context, result, duration);
        toast.show();
    }
}