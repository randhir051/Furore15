package com.example.dsi.furore;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.facebook.Request;
import com.facebook.Response;
import com.facebook.Session;
import com.facebook.SessionState;
import com.facebook.UiLifecycleHelper;
import com.facebook.model.GraphUser;
import com.facebook.widget.LoginButton;
import com.github.johnpersano.supertoasts.SuperCardToast;
import com.github.johnpersano.supertoasts.SuperToast;

import java.util.Arrays;
import java.util.List;

public class Facebook extends ActionBarActivity {

    private LoginButton loginBtn;
    Button rules;
    private UiLifecycleHelper uiHelper;
    private static final List<String> PERMISSIONS = Arrays.asList("publish_actions");


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        final SharedPreferences prefs = getSharedPreferences(Utility.PREFS, 0);

        uiHelper = new UiLifecycleHelper(this, statusCallback);
        uiHelper.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facebook);
        loginBtn = (LoginButton) findViewById(R.id.authButton);
        loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Boolean connected;
                Context context = Facebook.this;
                ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwrok = cm.getActiveNetworkInfo();
                connected = activeNetwrok != null && activeNetwrok.isConnectedOrConnecting();
                if (connected == true) {
                } else {
                    uploadPreview.callSuperToast("Please Connect Your Device To Internet", Facebook.this);
                }*/
            }
        });

        rules = (Button) findViewById(R.id.rules);
        rules.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(Facebook.this).setTitle("Rules").setMessage("-Rule1\n-Rule2\n-Rule3").setPositiveButton("GOT IT!", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
            }
        });

        loginBtn.setUserInfoChangedCallback(new LoginButton.UserInfoChangedCallback() {
            @Override
            public void onUserInfoFetched(GraphUser user) {
                if (user != null) {
//                    This will get you the user name and id and the url will get you the profile picture and you can use intent to pass the name :p

                    Log.d("here", "the usename and image");
                    user.getId();
                    String url = "https://graph.facebook.com/" + user.getId() + "/picture?type=large";
                    prefs.edit().putBoolean("log_in", true).putString("fb_id", user.getId()).putString("name", user.getName()).putString("user_image", url).apply();
                } else if (user == null) {
                    prefs.edit().putBoolean("log_in", false).apply();
                }
            }
        });


    }


    private Session.StatusCallback statusCallback = new Session.StatusCallback() {
        @Override
        public void call(Session session, SessionState state,
                         Exception exception) {
            if (state.isOpened()) {
                Log.d("FacebookSampleActivity", "Facebook session opened");
            } else if (state.isClosed()) {
                Log.d("FacebookSampleActivity", "Facebook session closed");
            }
        }
    };

    // Call this method to post an image on the user's fb wall AND CHANGE THE BITMAP
    public void postImage(String path) {
        if (checkPermissions()) {
//            Bitmap img = BitmapFactory.decodeResource(getResources(),
//                    R.drawable.header_image);
            Bitmap img = BitmapFactory.decodeFile(path);
            Request uploadRequest = Request.newUploadPhotoRequest(
                    Session.getActiveSession(), img, new Request.Callback() {
                        @Override
                        public void onCompleted(Response response) {
                            Toast.makeText(Facebook.this,
                                    "Photo uploaded successfully",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
            uploadRequest.executeAsync();
        } else {
            requestPermissions();
        }
    }

    // CAll this method to post a status on the user's fb wall AND CHANGE THE MESSAGE
    public void postStatusMessage() {
        if (checkPermissions()) {
            //Whatever is there in this string message will be updated as status
            String message = "Hey there, posting status from furore app :D";
            Request request = Request.newStatusUpdateRequest(
                    Session.getActiveSession(), message,
                    new Request.Callback() {
                        @Override
                        public void onCompleted(Response response) {
                            if (response.getError() == null)
                                Toast.makeText(Facebook.this,
                                        "Status updated successfully",
                                        Toast.LENGTH_LONG).show();
                        }
                    });
            request.executeAsync();
        } else {
            requestPermissions();
        }
    }


    public boolean checkPermissions() {
        Session s = Session.getActiveSession();
        if (s != null) {
            return s.getPermissions().contains("publish_actions");
        } else
            return false;
    }

    public void requestPermissions() {
        Session s = Session.getActiveSession();
        if (s != null)
            s.requestNewPublishPermissions(new Session.NewPermissionsRequest(
                    this, PERMISSIONS));
    }


    @Override
    public void onResume() {
        super.onResume();
        uiHelper.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        uiHelper.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        uiHelper.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        uiHelper.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle savedState) {
        super.onSaveInstanceState(savedState);
        uiHelper.onSaveInstanceState(savedState);
    }

// This AsyncTask is to get the profile picture as a bitmap.. call the AsyncTask if you need the bitmap of the user's dp
/*
    public class image extends AsyncTask<String, Void, Bitmap> {


        @Override
        protected Bitmap doInBackground(String... params) {

            String urldisplay = params[0];
            Bitmap bitmap = null;
            try {
                URL url = new URL(urldisplay);
                bitmap = BitmapFactory.decodeStream(url.openConnection().getInputStream());
                HttpURLConnection.setFollowRedirects(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return bitmap;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
//        super.onPostExecute(bitmap);
//            img.setImageBitmap(bitmap);
        }
    }

*/

}
