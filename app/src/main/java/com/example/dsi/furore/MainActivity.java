package com.example.dsi.furore;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends ActionBarActivity {

    private Toolbar toolbar;
    public ViewPager mPager;

    EventTypeFragment type = new EventTypeFragment();
    EventDetails details = new EventDetails();
    EventListFragment list = new EventListFragment();

    @Override
    public void onBackPressed() {
        if (mPager.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            mPager.setCurrentItem(mPager.getCurrentItem() - 1, true);
        }
    }

    class myPagerAdapter extends FragmentPagerAdapter {

        public myPagerAdapter(FragmentManager fm) {
            super(fm);
        }


        @Override
        public CharSequence getPageTitle(int position) {
            if (position == 0) {
                return "Joined Group";
            } else {
                return "Explore";
            }
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0:
                    return type;
                case 1:
                    return list;
                case 2:
                    return details;
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Furore");

        setSupportActionBar(toolbar);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.DrawerLayout), toolbar);

        mPager = (NonSwipeableViewPager) findViewById(R.id.hello);
        mPager.setAdapter(new myPagerAdapter(getSupportFragmentManager()));

        //drawerFragment.mDrawerToggle.setDrawerIndicatorEnabled(false);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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
            Intent in = new Intent(MainActivity.this, SelfieTimeline.class);
            startActivity(in);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
