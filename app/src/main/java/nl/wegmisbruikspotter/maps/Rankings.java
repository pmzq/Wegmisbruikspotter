package nl.wegmisbruikspotter.maps;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class Rankings extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rankings);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        // Get the ViewPager and set it's PagerAdapter so that it can display items
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(new PagerAdapter(getSupportFragmentManager(),
                Rankings.this));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#CCCCCC"));
        tabLayout.setupWithViewPager(viewPager);

        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            switch (i){

                case 0:
                    tabLayout.getTabAt(i).setIcon(R.drawable.ic_thumb_up_black_24dp);
                    break;
                case 1:
                    tabLayout.getTabAt(i).setIcon(R.drawable.ic_thumb_down_black_24dp);
                    break;
                case 2:
                    tabLayout.getTabAt(i).setIcon(R.drawable.ic_directions_car_black_24dp);
                    break;

            }

        }
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.zoeken) {
            Context context = getApplicationContext();
            Intent intent = new Intent(context ,Zoek.class);
            startActivity(intent);
            //return true;

        } else if (id == R.id.SpotNu) {
            Context context = getApplicationContext();
            Intent intent = new Intent(context ,MainActivity.class);
            startActivity(intent);

        } else if (id == R.id.AllSpots) {
            Context context = getApplicationContext();
            Intent intent = new Intent(context ,AllSpots.class);
            startActivity(intent);
            //return true;

        } else if (id == R.id.MijnSpots) {
            Context context = getApplicationContext();
            Intent intent = new Intent(context ,MijnSpots.class);
            startActivity(intent);
            //return true;

        } else if (id == R.id.Rankings) {
            Context context = getApplicationContext();
            Intent intent = new Intent(context ,Rankings.class);
            startActivity(intent);
            //return true;

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}