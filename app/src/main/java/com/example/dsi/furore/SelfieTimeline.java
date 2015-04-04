package com.example.dsi.furore;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.etsy.android.grid.StaggeredGridView;
import com.etsy.android.grid.util.DynamicHeightImageView;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionButton;
import com.oguzdev.circularfloatingactionmenu.library.FloatingActionMenu;
import com.oguzdev.circularfloatingactionmenu.library.SubActionButton;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import fr.castorflex.android.circularprogressbar.CircularProgressBar;

public class SelfieTimeline extends ActionBarActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int RESULT_LOAD_IMAGE = 2;
    int lastPosition = 0;
    private Toolbar toolbar;
    public int number = 0;
    ArrayList<Float> lista = new ArrayList<>();
    public static ImageLoader imageLoader;

    Boolean done = false;
    StaggeredGridView gridView;
    CircularProgressBar cpb, main_cpb;
    TextView loadmore;
    View footer;
    GridViewAdapter mGridViewAdapter = null;
    static DisplayImageOptions defaultOptions;
    ImageLoaderConfiguration config;
    ArrayList<String> img_url_id = new ArrayList<>(), image_urls = new ArrayList<>(), ids = new ArrayList<>(), fb_ids = new ArrayList<>(), descs = new ArrayList<>(), likes = new ArrayList<>(), names = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_timeline);
        toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("Selfie!!");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        configUIL();
        main_cpb = (CircularProgressBar) findViewById(R.id.progress_circle);
        main_cpb.setVisibility(View.VISIBLE);
        new imageUrlLoader().execute();

        //for randomising the selfie image height
        lista.add((float) 0.9);
        lista.add((float) 1.05);
        lista.add((float) 0.95);
        lista.add((float) 1.1);

        initFloatingMenu();

        gridView = (StaggeredGridView) findViewById(R.id.grid_view);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        footer = inflater.inflate(R.layout.selfie_footer, null);
        gridView.addFooterView(footer, "potato", true);

        loadmore = (TextView) footer.findViewById(R.id.load_more_tv);
        cpb = (CircularProgressBar) footer.findViewById(R.id.pb_circle);
        footer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                add load more function
                loadmore.setVisibility(View.GONE);
                cpb.setVisibility(View.VISIBLE);
                number = number + 10;
                new imageUrlLoader().execute();
            }
        });
    }

    public void configUIL() {
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisc(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
//                .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
//                .showImageOnFail(R.drawable.ic_error) // resource or drawable
                .displayer(new SimpleBitmapDisplayer()).build();

        config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .discCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);

    }

    public class GridViewAdapter extends BaseAdapter {

        public GridViewAdapter() {
            imageLoader = ImageLoader.getInstance();
        }

        private void setAnimation(View viewToAnimate, int position) {
            // If the bound view wasn't previously displayed on screen, it's animated
            if (position > lastPosition) {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_in_bottom);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            } else {
                Animation animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.abc_slide_in_top);
                viewToAnimate.startAnimation(animation);
                lastPosition = position;
            }
        }

        class Holder {
            DynamicHeightImageView iv;
            ImageView ivLike;
            TextView textView, tvLikes;
            CardView cv;
            CircularProgressBar cpb_mini;

            Holder(View v) {
                iv = (DynamicHeightImageView) v.findViewById(R.id.imageView);
                textView = (TextView) v.findViewById(R.id.imageText);
                cv = (CardView) v.findViewById(R.id.card_view);
                ivLike = (ImageView) v.findViewById(R.id.ivLike);
                cpb_mini = (CircularProgressBar) v.findViewById(R.id.progress_circle_mini);
                tvLikes = (TextView) v.findViewById(R.id.tvLikes);
            }
        }

        @Override
        public int getCount() {
            return image_urls.size();
        }

        @Override
        public Object getItem(int position) {
            return image_urls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Holder mHolder = null;
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.single_image_view, parent, false);
                mHolder = new Holder(convertView);
                convertView.setTag(mHolder);
            } else {
                mHolder = (Holder) convertView.getTag();
            }
            mHolder.cv.setPreventCornerOverlap(false);
            mHolder.textView.setText(descs.get(position));
            mHolder.tvLikes.setText(likes.get(position));
            final Holder finalMHolder1 = mHolder;
            imageLoader.displayImage(image_urls.get(position)
                    , mHolder.iv, defaultOptions, new ImageLoadingListener() {
                @Override
                public void onLoadingStarted(String imageUri, View view) {
                    finalMHolder1.cpb_mini.setVisibility(View.VISIBLE);
                }

                @Override
                public void onLoadingFailed(String imageUri, View view, FailReason failReason) {

                }

                @Override
                public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                    finalMHolder1.cpb_mini.setVisibility(View.GONE);
                }

                @Override
                public void onLoadingCancelled(String imageUri, View view) {

                }
            });
            Log.d("raj", "image = " + image_urls.get(position) + " pos = " + position + " des = " + descs.get(position));
//            imageLoader.displayImage(image_urls.get(position), mHolder.iv, defaultOptions);

//            mHolder.iv.setBackgroundResource(R.drawable.round_corner);
//            mHolder.iv.setScaleType(ImageView.ScaleType.CENTER_CROP);

            mHolder.iv.setHeightRatio(getRandomHeight(position));
            setAnimation(convertView, position);

//            final Holder finalMHolder = mHolder;


            mHolder.iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //String url = (String) view.getTag();
                    SelfieDetails.launch(SelfieTimeline.this, v.findViewById(R.id.imageView)
                            , image_urls.get(position), descs.get(position),
                            fb_ids.get(position), names.get(position), likes.get(position), img_url_id.get(position));
                }
            });

            return convertView;
        }

    }


    private void initFloatingMenu() {


        ImageView icon = new ImageView(this); // Create an icon
        icon.setImageResource(R.drawable.camera);

        final FloatingActionButton actionButton = new FloatingActionButton.Builder(this)
                .setContentView(icon)
                .build();


        SubActionButton.Builder itemBuilder = new SubActionButton.Builder(this);
// change the camera icon
        Drawable cameraDrawable = getResources().getDrawable(R.drawable.aperture);
        ImageView cameraIcon = new ImageView(this);

        SubActionButton cameraButton = itemBuilder.setContentView(cameraIcon).build();
        cameraButton.setBackgroundDrawable(cameraDrawable);


        Drawable attachDrawable = getResources().getDrawable(R.drawable.image);
        ImageView attachIcon = new ImageView(this);
        SubActionButton attachButton = itemBuilder.setContentView(attachIcon).build();
        attachButton.setBackgroundDrawable(attachDrawable);


        final FloatingActionMenu actionMenu = new FloatingActionMenu.Builder(this)
                .addSubActionView(cameraButton)
                .addSubActionView(attachButton)
                .attachTo(actionButton)

                .build();


        cameraButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //function to open camera and capture an image
                if (actionMenu.isOpen()) {
                    actionMenu.close(true);
                }
                clickPicture();
            }
        });

        attachButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //function to select an existing image
                if (actionMenu.isOpen()) {
                    actionMenu.close(true);
                }
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
            Log.d("raj", "image captured");
            startActivityForResult(picIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("raj", "req code = " + requestCode + "res code = " + resultCode + "RESULT_OK=" + RESULT_OK);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            //add code for displaying picture and uploading it
            Log.d("raj", "image sending");
            File file = saveBitmap(imageBitmap);
            String path = file.getPath();
            dialog(path);
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
            File file = saveBitmap(bm);
            String path = file.getPath();
            dialog(path);
        }
    }

    void dialog(final String path) {
        Log.d("raj", "dialog called");

        Intent in = new Intent(SelfieTimeline.this, uploadPreview.class);
        in.putExtra("path", path);
        startActivity(in);

    }

    public class imageUrlLoader extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... params) {

            HttpClient httpClient = new DefaultHttpClient();
            try {
                HttpPost post = new HttpPost("http://bitsmate.in/furore/selfie_timeline.php?page_no=" + number);
                HttpResponse response = httpClient.execute(post);
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);
                parsedata(data);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d("raj", "image urls = " + image_urls);
            main_cpb.setVisibility(View.GONE);
            if (!done) {
                mGridViewAdapter = new GridViewAdapter();
                gridView.setAdapter(mGridViewAdapter);
            } else {
                mGridViewAdapter.notifyDataSetChanged();
            }
            loadmore.setVisibility(View.VISIBLE);
            cpb.setVisibility(View.GONE);
        }

        public static final String IMG_URL = "img_url", ID = "id", FB_ID = "fb_id", DESC = "s_desc", USER_NAME = "user_name", LIKES = "likes";

        private void parsedata(String data) {


//            Log.d("raj", data);
            try {
                JSONObject object = new JSONObject(data);
//                Log.d("raj", "object = " + object);
                for (int i = 0; i < object.length(); i++) {
                    JSONObject subObject = object.getJSONObject("" + i);
                    Log.d("raj", "" + subObject);
                    String img_url = "http://bitsmate.in/furore/uploads/" + subObject.getString(IMG_URL);
                    String id = subObject.getString(ID);
                    String fb_id = subObject.getString(FB_ID);
                    String desc = subObject.getString(DESC);
                    String username = subObject.getString(USER_NAME);
                    String like = subObject.getString(LIKES);
                    image_urls.add(img_url);
                    fb_ids.add(fb_id);
                    ids.add(id);
                    descs.add(desc);
                    likes.add(like);
                    names.add(username);
                    img_url_id.add(subObject.getString(IMG_URL));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private float getRandomHeight(int position) {
        return lista.get(position % 4);

    }

    private File saveBitmap(Bitmap bm) {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Furore15";
        File dir = new File(file_path);
        if (!dir.exists())
            dir.mkdirs();
        Date date = new Date();
        File file = new File(dir, date.getTime() + ".jpeg");
        Log.d("date", "" + date.getTime());
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.JPEG, 85, fOut);
            fOut.flush();
            fOut.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return file;
    }


}
