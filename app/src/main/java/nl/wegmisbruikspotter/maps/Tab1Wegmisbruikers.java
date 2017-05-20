package nl.wegmisbruikspotter.maps;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import java.util.ArrayList;
import java.util.Calendar;

public class Tab1Wegmisbruikers extends Fragment {

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
        // Inflate the layout for this fragment
        return v;


    }

}