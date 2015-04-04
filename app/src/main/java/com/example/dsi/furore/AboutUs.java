package com.example.dsi.furore;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class AboutUs extends ActionBarActivity implements View.OnClickListener {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Developers");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        (findViewById(R.id.randhirmail)).setOnClickListener(this);
        (findViewById(R.id.darshanmail)).setOnClickListener(this);
        (findViewById(R.id.rohanmail)).setOnClickListener(this);
        (findViewById(R.id.dikshamail)).setOnClickListener(this);
        (findViewById(R.id.arjitmail)).setOnClickListener(this);
        (findViewById(R.id.abhishekmail)).setOnClickListener(this);
        (findViewById(R.id.antonymail)).setOnClickListener(this);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about_us, menu);
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
            return true;
        }
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        String email = null;
        switch (id) {
            case R.id.randhirmail:
                email = getResources().getString(R.string.randhirmail);
                break;
            case R.id.darshanmail:
                email = getResources().getString(R.string.darshanmail);
                break;
            case R.id.rohanmail:
                email = getResources().getString(R.string.rohanmail);
                break;
            case R.id.dikshamail:
                email = getResources().getString(R.string.dikshamail);
                break;
            case R.id.abhishekmail:
                email = getResources().getString(R.string.abhishekmail);
                break;
            case R.id.arjitmail:
                email = getResources().getString(R.string.arjitmail);
                break;
            case R.id.antonymail:
                email = getResources().getString(R.string.antonymail);
        }

        if (email != null) {
            try {
                Intent sendIntent = new Intent(Intent.ACTION_VIEW);
                sendIntent.setClassName("com.google.android.gm", "com.google.android.gm.ComposeActivityGmail");
                sendIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
                sendIntent.setData(Uri.parse(email));
                sendIntent.putExtra(Intent.EXTRA_SUBJECT, "Furore app");
                sendIntent.setType("plain/text");
                sendIntent.putExtra(Intent.EXTRA_TEXT, "");
                startActivity(sendIntent);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
