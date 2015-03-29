package com.example.dsi.furore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends ActionBarActivity {

    public ViewPager mPager;
    //ImageView back;
    EventTypeFragment type = new EventTypeFragment();
    EventDetails details = new EventDetails();
    EventListFragment list = new EventListFragment();
    JSONparser jsonParserGet = new JSONparser();
    SharedPreferences prefs;

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
            return "Events";
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
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Furore");

        //back = (ImageView) findViewById(R.id.back_button_image);

        prefs = getSharedPreferences(Utility.PREFS,0);
        setSupportActionBar(toolbar);

        NavigationDrawerFragment drawerFragment = (NavigationDrawerFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp((DrawerLayout) findViewById(R.id.DrawerLayout), toolbar);

        mPager = (NonSwipeableViewPager) findViewById(R.id.hello);
        mPager.setAdapter(new myPagerAdapter(getSupportFragmentManager()));

        mPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if(position!=0){
               //    back.setVisibility(View.VISIBLE);
                }
                else {
                //    back.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                mPager.setCurrentItem(mPager.getCurrentItem()-1);
//            }
//        });

        //drawerFragment.mDrawerToggle.setDrawerIndicatorEnabled(false);
        if(prefs.getBoolean("isFirstLogin",true)){
            new GetData().execute();
            prefs.edit().putBoolean("isFirstLogin",false).apply();
        }
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


    class GetData extends AsyncTask<String, String, JSONObject> {

        JSONObject json;

        @Override
        protected JSONObject doInBackground(String... args) {
            try {

//                List<NameValuePair> params = new ArrayList<NameValuePair>();
//                params.clear();
//                params.add(new BasicNameValuePair("location", get.getString("location","Vellore")));
//                params.add(new BasicNameValuePair("user_id", get.getString("user_id","")));

                json = jsonParserGet.makeHttpRequest(
                        Utility.RETRIEVE_EVENTS, "GET", null);
                Log.d("Retrieve events attempt",
                        json.toString());
                return json;
            } catch (Exception ex) {
                ex.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(JSONObject result) {
            if (result != null) {
               setData(result);
            }
            else{
                Toast.makeText(getApplicationContext(), "Sorry, unable to process request", Toast.LENGTH_LONG).show();
            }
        }

    }

    private void setData(JSONObject result) {

       String id, name, cordinator, category, rules, timing, fee, cash;


        DBEventDetails put = new DBEventDetails(this);
        put.open();
        try {
            JSONObject forSize = result.getJSONObject("0");
            int size = forSize.getInt("size");
            for(int i=1;i<=size;i++){
                JSONObject c = result.getJSONObject(i+"");
                id = c.getString("id");
                name = c.getString("event_name");
                cordinator = c.getString("co_ordinator_name");
                category = c.getString("cat");
                rules = c.getString("rules") + "\nContact: " + c.getString("contact");
                timing = c.getString("time");
                fee = c.getString("fee");
                cash = c.getString("cash");
                put.createEntry(id,name,cordinator,category,rules,timing,fee,cash);
            }
        }catch (JSONException ex){
            ex.printStackTrace();
        }
        put.close();
    }


//    private void setData(JSONObject result) {
//        String id, name, cordinator, category, rules, timing, fee, cash;
//        try {
//            JSONArray dataArray = result.getJSONArray("events");
//            for (int i = 0; i < dataArray.length(); i++) {
//                JSONObject c = dataArray.getJSONObject(i);
//
//                gid = c.getString("group_id");
//                boolean flag = true; //determines weather or not to insert a group
//                for(int j=0;j<availableGroups.size();j++){
//                    if(gid.equals(availableGroups.get(j).id)){
//                        flag = false;
//                    }
//                }
//                if(flag){
//                    gname = c.getString("group_name");
//                    gtag = c.getString("group_tagline");
//                    gdes = c.getString("group_description");
//                    gnou = c.getInt("group_nou");
//                    gimg = c.getString("group_image_uri");
//                    Group gr = new Group(gid, gname, gtag, gdes, gnou,gimg);
//                    availableGroups.add(gr);
//                    adapter.notifyItemInserted(availableGroups.size());
//                }
//                else{
//                    Toast.makeText(getActivity(),"Nothing new...",Toast.LENGTH_SHORT).show();
//                }
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//    }
}
