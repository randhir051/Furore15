package com.cse.dsi.furore;

import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class Sponsors extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sponsors);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Social networks");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        TextView fb,insta, twi, you;

        fb = (TextView) findViewById(R.id.facebook);
        String linkText1 = "<a href='https://www.facebook.com/furore15'>Facebook</a>";
        fb.setText(Html.fromHtml(linkText1));
        fb.setMovementMethod(LinkMovementMethod.getInstance());

        twi = (TextView) findViewById(R.id.twitter);
        String linkText2 = "<a href='http://twitter.com/furore15'>Twitter</a>";
        twi.setText(Html.fromHtml(linkText2));
        twi.setMovementMethod(LinkMovementMethod.getInstance());

        insta = (TextView) findViewById(R.id.insta);
        String linkText3 = "<a href='https://instagram.com/furore15'>Instagram</a>";
        insta.setText(Html.fromHtml(linkText3));
        insta.setMovementMethod(LinkMovementMethod.getInstance());

        you = (TextView) findViewById(R.id.you);
        String linkText4 = "<a href='https://www.youtube.com/channel/UCkgRMRCTult6T7tkxwJ95nw'>YouTube</a>";
        you.setText(Html.fromHtml(linkText4));
        you.setMovementMethod(LinkMovementMethod.getInstance());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_sponsors, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }
}
