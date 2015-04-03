package com.example.dsi.furore;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dd.processbutton.FlatButton;
import com.rengwuxian.materialedittext.MaterialEditText;

public class uploadPreview extends ActionBarActivity {

    Toolbar toolbar;
    TextView name;
    ImageView preview;
    MaterialEditText desc;
    FlatButton upload;
    String path;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_preview);
        toolbar = (Toolbar) findViewById(R.id.app_bar1);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitle("Upload...");
        Intent in = getIntent();
        path = in.getStringExtra("path");
        name = (TextView) findViewById(R.id.pro_name);
        preview = (ImageView) findViewById(R.id.previewIV);
        desc = (MaterialEditText) findViewById(R.id.description_et);
        upload = (FlatButton) findViewById(R.id.upload_btn);
        preview.setImageBitmap(BitmapFactory.decodeFile(path));
//        name.setText(Facebook.NAME);

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String description = desc.getText().toString();
                if (description.length() > 80) {
                    Toast.makeText(uploadPreview.this, "Description is more than 80 characters!", Toast.LENGTH_SHORT).show();
                } else {
                    Intent in = new Intent(uploadPreview.this, uploadImage.class);
                    //change fb id
                    in.putExtra("fb_id", "Darshan007");
                    in.putExtra("path", path);
                    in.putExtra("desc", description);
                    startService(in);
                    Toast.makeText(uploadPreview.this, "Your selfie will be uploaded", Toast.LENGTH_SHORT).show();
                    uploadPreview.this.finish();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_upload_preview, menu);
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

        return super.onOptionsItemSelected(item);
    }
}
