package com.example.dsi.furore;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.nineoldandroids.animation.Animator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class SelfieDetails extends ActionBarActivity {


    public boolean likedNow = false;
    public static int pos;
    public static final String ID = "SelfieDetail:id", LIKES = "SelfieDetail:likes", EXTRA_IMAGE = "SelfieDetail:image", DESCRIPTON = "SelfieDetail:description", DP = "SelfieDetail:dp", NAME = "SelfieDetail:name";
    public static ImageLoader imageLoader = ImageLoader.getInstance();
    DisplayImageOptions options;
    CircleImageView dp;
    TextView dp_name, noLikes;
    ImageView like;
    Integer liked = -1;
    Intent in;
    //    CircularProgressBar pb;
    CardView cardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selfie_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.app_bar);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        in = getIntent();
        cardView = (CardView) findViewById(R.id.card_view);
        cardView.setVisibility(View.GONE);
        String image_url = in.getStringExtra(EXTRA_IMAGE);
        String desc = in.getStringExtra(DESCRIPTON);
        String dp_ = "https://graph.facebook.com/" + in.getStringExtra(DP) + "/picture?type=small";
        String username = in.getStringExtra(NAME);
        ImageView image = (ImageView) findViewById(R.id.image);
        TextView desc_tv = (TextView) findViewById(R.id.desc_tv);
//        pb = (CircularProgressBar) findViewById(R.id.pb);
        dp = (CircleImageView) findViewById(R.id.details_dp);
        dp_name = (TextView) findViewById(R.id.details_name);
        {
            options = new DisplayImageOptions.Builder()
                    .cacheOnDisc(true).cacheInMemory(true)
                    .imageScaleType(ImageScaleType.EXACTLY)
                    .showImageOnLoading(R.drawable.furore_logo) // resource or drawable
                    .showImageForEmptyUri(R.drawable.furore_logo) // resource or drawable
                    .showImageOnFail(R.drawable.furore_logo) // resource or drawable
                    .displayer(new SimpleBitmapDisplayer()).build();
        }
        ViewCompat.setTransitionName(image, EXTRA_IMAGE);
        imageLoader.displayImage(image_url
                , image, options);
        desc_tv.setText(desc);
        dp_name.setText(username);
        imageLoader.displayImage(dp_, dp, options);
        like = (ImageView) findViewById(R.id.ivLike1);
        noLikes = (TextView) findViewById(R.id.noLikes);
        noLikes.setText(in.getStringExtra(LIKES));

        new checkLike(in.getStringExtra(ID)).execute();


        like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (liked == 0) {
                    YoYo.with(Techniques.BounceIn).withListener(new Animator.AnimatorListener() {
                        @Override
                        public void onAnimationStart(Animator animation) {
                            int likes = Integer.parseInt(in.getStringExtra(LIKES));
                            like.setImageResource(R.drawable.star_gold_);
                            new insertLike(in.getStringExtra(ID)).execute();
                            likes++;
                            noLikes.setText("" + likes);
                        }

                        @Override
                        public void onAnimationEnd(Animator animation) {
                        }

                        @Override
                        public void onAnimationCancel(Animator animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animator animation) {

                        }
                    }).duration(500).playOn(v);
                } else if (liked == 1) {
                    uploadPreview.callToast("You have already liked this image!!!!!", SelfieDetails.this);
                }

            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_selfie_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_report) {

            new report(in.getStringExtra(ID)).execute();
            return true;
        }
        if (id == R.id.home) {
            NavUtils.navigateUpFromSameTask(this);
        }

        return super.onOptionsItemSelected(item);
    }


    public class insertLike extends AsyncTask<Void, Void, Void> {

        String fb_id, pic_id;
        SharedPreferences preferences = getSharedPreferences(Utility.PREFS, MODE_APPEND);

        public insertLike(String pic_id) {
            fb_id = preferences.getString(FuroreApplication.USER_ID, "-1");
            this.pic_id = pic_id;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            likedNow = true;

        }

        @Override
        protected Void doInBackground(Void... params) {


            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://bitsmate.in/furore/insert_likes.php");
            HttpContext localContext = new BasicHttpContext();


            try {
                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                entity.addPart("fb_id", new StringBody(fb_id));
                entity.addPart("pic_id", new StringBody(pic_id));
                httpPost.setEntity(entity);

                HttpResponse response = httpClient.execute(httpPost, localContext);
                HttpEntity entity1 = response.getEntity();
                String responseString = EntityUtils.toString(entity1);
                Log.d("raj", responseString);
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }
    }

    public class checkLike extends AsyncTask<Void, Void, Void> {
        SharedPreferences preferences = getSharedPreferences(Utility.PREFS, MODE_APPEND);
        String id, fb_id;


        public checkLike(String id) {
            this.id = id;
            this.fb_id = preferences.getString(FuroreApplication.USER_ID, "-1");
        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://bitsmate.in/furore/check_likes.php");
            HttpContext localContext = new BasicHttpContext();


            try {
                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                entity.addPart("fb_id", new StringBody(fb_id));
                entity.addPart("pic_id", new StringBody(id));
                httpPost.setEntity(entity);

                HttpResponse response = httpClient.execute(httpPost, localContext);
                HttpEntity entity1 = response.getEntity();
                String responseString = EntityUtils.toString(entity1);
                String obj = responseString.substring(responseString.indexOf('{'), responseString.length());
                try {
                    JSONObject object = new JSONObject(obj);
                    liked = Integer.parseInt(object.getString("status"));
                    Log.d("raj", "" + liked);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (liked == 1) {
                like.setImageResource(R.drawable.star_gold_);
            }
//            pb.setVisibility(View.GONE);
            YoYo.with(Techniques.SlideInRight).duration(300).withListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                    cardView.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            }).playOn(cardView);
        }
    }

    public static void launch(SelfieTimeline activity,
                              View transitionView, String url,
                              String description, String dp,
                              String user_name, String likes, String id, int position) {
        ActivityOptionsCompat options =
                ActivityOptionsCompat.makeSceneTransitionAnimation(
                        activity, transitionView, EXTRA_IMAGE);
        Intent intent = new Intent(activity, SelfieDetails.class);
        intent.putExtra(EXTRA_IMAGE, url);
        intent.putExtra(DESCRIPTON, description);
        intent.putExtra(DP, dp);
        intent.putExtra(NAME, user_name);
        intent.putExtra(LIKES, likes);
        intent.putExtra(ID, id);
        pos = position;
        ActivityCompat.startActivityForResult(activity, intent, 1234, options.toBundle());
    }

    public class report extends AsyncTask<Void, Void, Void> {

        String pic_id;

        public report(String pic_id) {
            this.pic_id = pic_id;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            uploadPreview.callSuperToast("This image is reported successfully", getApplicationContext());
        }

        @Override
        protected Void doInBackground(Void... params) {

            HttpClient httpClient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost("http://bitsmate.in/furore/report.php");
            HttpContext localContext = new BasicHttpContext();


            try {
                MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

                entity.addPart("pic_id", new StringBody(pic_id));
                httpPost.setEntity(entity);

                HttpResponse response = httpClient.execute(httpPost, localContext);
                HttpEntity entity1 = response.getEntity();
                String responseString = EntityUtils.toString(entity1);
                Log.d("raj", responseString);
            } catch (IOException e) {
                e.printStackTrace();
            }


            return null;
        }
    }

    @Override
    public void onBackPressed() {
        Intent returnIntent = new Intent();
        returnIntent.putExtra("like", likedNow);
        Log.d("raj", "" + likedNow);
        returnIntent.putExtra("position", pos);
        setResult(RESULT_OK, returnIntent);
        finish();
    }


}
