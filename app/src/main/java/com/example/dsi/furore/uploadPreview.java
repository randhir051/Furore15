package com.example.dsi.furore;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.dd.processbutton.FlatButton;
import com.github.johnpersano.supertoasts.SuperCardToast;
import com.github.johnpersano.supertoasts.SuperToast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.rengwuxian.materialedittext.MaterialEditText;

import de.hdodenhof.circleimageview.CircleImageView;

public class uploadPreview extends ActionBarActivity {

    Toolbar toolbar;
    TextView name;
    ImageView preview;
    CircleImageView dp;
    MaterialEditText desc;
    FlatButton upload;
    String path, id, user_name, user_img;
    SharedPreferences preferences;
    DisplayImageOptions options;
    public static ImageLoader imageLoader = ImageLoader.getInstance();


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

        preferences = getSharedPreferences(Utility.PREFS, MODE_APPEND);
        id = preferences.getString(FuroreApplication.USER_ID, "-1");
        user_name = preferences.getString(FuroreApplication.USER_NAME, "");
        user_img = preferences.getString(FuroreApplication.USER_IMAGE, "");
        name.setText(user_name);
        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String description = desc.getText().toString();
                if (description.length() > 80) {
//                    Toast.makeText(uploadPreview.this, "Description is more than 80 characters!", Toast.LENGTH_SHORT).show();
                    callToast("Description is more than 80 characters!", uploadPreview.this);

                } else if (description.length() <= 0) {
                    callToast("please add a description!", uploadPreview.this);
                } else {
                    Intent in = new Intent(uploadPreview.this, uploadImage.class);
                    //change fb id
                    in.putExtra("fb_id", id);
                    in.putExtra("path", path);
                    in.putExtra("desc", description);
                    startService(in);
//                    Toast.makeText(uploadPreview.this, "Your selfie will be uploaded", Toast.LENGTH_SHORT).show();
//                    callToast("Your selfie will be uploaded");
                    SuperToast superToast = new SuperToast(uploadPreview.this);
                    superToast.setDuration(SuperToast.Duration.LONG);
                    superToast.setBackground(SuperToast.Background.BLUE);
                    superToast.setText("Your selfie will be uploaded soon");
                    superToast.setAnimations(SuperToast.Animations.FLYIN);
                    superToast.show();
                    uploadPreview.this.finish();
                }
            }
        });
        {
            options = new DisplayImageOptions.Builder()
                    .cacheOnDisc(true).cacheInMemory(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
//                .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
//                .showImageOnFail(R.drawable.ic_error) // resource or drawable
                    .displayer(new FadeInBitmapDisplayer(700)).build();
        }
        dp = (CircleImageView) findViewById(R.id.dp);
        try {
            imageLoader.displayImage(user_img, dp, options);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void callToast(String msg, Activity act) {
        SuperCardToast superCardToast = new SuperCardToast(act);
        superCardToast.setText(msg);
        superCardToast.setDuration(SuperToast.Duration.LONG);
        superCardToast.setBackground(SuperToast.Background.BLUE);
        superCardToast.setTextColor(Color.WHITE);
        superCardToast.setSwipeToDismiss(true);
        superCardToast.setAnimations(SuperToast.Animations.FLYIN);
        superCardToast.show();
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
