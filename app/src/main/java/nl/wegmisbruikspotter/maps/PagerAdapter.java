package nl.wegmisbruikspotter.maps;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

public class PagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                Tab1Wegmisbruikers tab1 = new Tab1Wegmisbruikers(); //Wegmisbruikers
                return tab1;
            case 1:
                Tab2Spotters tab2 = new Tab2Spotters(); //Spotters
                return tab2;
            case 2:
                Tab3Automerk tab3 = new Tab3Automerk(); //Auto merk
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}