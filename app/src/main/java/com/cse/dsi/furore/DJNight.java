package com.cse.dsi.furore;

import android.media.Image;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.ScrollView;

import com.nirhart.parallaxscroll.views.ParallaxScrollView;

public class DJNight extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_djnight);
//        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
//        toolbar.setTitle("DJ Night");
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setHomeButtonEnabled(true);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_djnight, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
//        if (id == R.id.home){
//            NavUtils.navigateUpFromSameTask(this);
//        }

        return super.onOptionsItemSelected(item);
    }

}
