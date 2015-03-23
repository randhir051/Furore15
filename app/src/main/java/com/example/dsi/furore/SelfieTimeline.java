package com.example.dsi.furore;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.NavUtils;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

public class SelfieTimeline extends ActionBarActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int RESULT_LOAD_IMAGE = 2;
    int lastPosition = 0;
    private Toolbar toolbar;
    StaggeredGridView gridView;
    int drawables[] = {R.drawable.art, R.drawable.dance, R.drawable.game, R.drawable.art, R.drawable.dance, R.drawable.game,
            R.drawable.art, R.drawable.dance, R.drawable.game, R.drawable.art, R.drawable.dance, R.drawable.game};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_timeline);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Selfie!!");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initFloatingMenu();
        gridView = (StaggeredGridView) findViewById(R.id.grid_view);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View footer = inflater.inflate(R.layout.footer, null);
        gridView.addFooterView(footer, "potato", true);
        gridView.setAdapter(new GridViewAdapter());
        Log.d("footer count:", gridView.getFooterViewsCount() + "");

    }

    public class GridViewAdapter extends BaseAdapter {

        private void setAnimation(View viewToAnimate, int position) {
            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_in_bottom);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
            else {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_in_top);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }

        @Override
        public int getCount() {
            return drawables.length;
        }

        @Override
        public Object getItem(int position) {
            return drawables[position];
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            LayoutInflater inflater = getLayoutInflater();
            if (convertView == null) {
                convertView = inflater.inflate(R.layout.single_image_view, parent, false);
            }
            DynamicHeightImageView iv = (DynamicHeightImageView) convertView.findViewById(R.id.imageView);
            TextView textView = (TextView) convertView.findViewById(R.id.imageText);
            textView.setText("this is random text");
            iv.setImageResource(drawables[position]);
            setAnimation(convertView, position);

            return convertView;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selfie_timeline, menu);
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
        } else if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }
        return super.onOptionsItemSelected(item);
    }

    private void initFloatingMenu() {


        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.drawable.camera);

        final FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();


        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
// change the camera icon
        Drawable cameraDrawable = getResources().getDrawable(R.drawable.camera);
        ImageView cameraIcon = new ImageView(this);

        SubActionButton cameraButton = itemBuilder.setContentView(cameraIcon).build();
        cameraButton.setBackgroundDrawable(cameraDrawable);


        Drawable attachDrawable = getResources().getDrawable(R.drawable.camera);
        ImageView attachIcon = new ImageView(this);
        SubActionButton attachButton = itemBuilder.setContentView(attachIcon).build();
        attachButton.setBackgroundDrawable(attachDrawable);


        FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(cameraButton)
                .addSubActionView(attachButton)
                .attachTo(actionButton)

                .build();


        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //function to open camera and capture an image
                clickPicture();
            }
        });

        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //function to select an existing image
                selectPicture();
            }
        });

    }

    private void selectPicture() {
        Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, RESULT_LOAD_IMAGE);
    }

    private void clickPicture() {
        Intent picIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (picIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(picIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //add code for displaying picture and uploading it
        }
        if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK && null != data) {

            Uri selectedImage = data.getData();
            String[] filePathColumn = {MediaStore.Images.Media.DATA};

            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
            cursor.moveToFirst();

            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String picturePath = cursor.getString(columnIndex);
            cursor.close();

            Bitmap bm = BitmapFactory.decodeFile(picturePath);
//add function to display and send the data
        }
    }
}
