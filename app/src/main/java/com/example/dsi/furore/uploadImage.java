package com.example.dsi.furore;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.RemoteViews;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.File;
import java.io.IOException;


public class uploadImage extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public static final String IMAGE_KEY = "selfie_img", FB_KEY = "fb_id", DESC_KEY = "s_desc", USER_NAME = "user_name";

    NotificationManager notificationManager;
    Notification notification;

    public uploadImage(String name) {
        super(name);
    }

    public uploadImage() {
        super("uploadImage");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("raj", "uploading started");
        String path = intent.getStringExtra("path");
        String id = intent.getStringExtra("fb_id");
        String description = intent.getStringExtra("desc");
        SharedPreferences preferences = getSharedPreferences(Utility.PREFS, MODE_APPEND);
        String name = preferences.getString(FuroreApplication.USER_NAME, "");
        callNotificationProgressBar();
        callUpload(path, id, description, name);

    }

    private void callNotificationProgressBar() {
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification(R.drawable.main_logo, "Uploading..", System.currentTimeMillis());
        notification.contentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.upload_progress);
        notification.contentView.setTextViewText(R.id.textView, "Uploading your image...");
        notification.contentView.setProgressBar(R.id.progressBar, 100, 0, true);
//        notification.contentView.setImageViewResource(R.id.imageRandom,R.drawable.something);
        notification.defaults = Notification.FLAG_NO_CLEAR;
        notificationManager.notify(555, notification);
    }

    private void callUpload(String path, String id, String desc, String name) {

        File file = new File(path);


        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost("http://bitsmate.in/furore/upload_image.php");
        HttpContext localContext = new BasicHttpContext();


        try {
            MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

            /*for (int index = 0; index < nameValuePairs.size(); index++) {
                if (nameValuePairs.get(index).getName().equalsIgnoreCase("image")) {
                    // If the key equals to "image", we use FileBody to transfer the data
                    entity.addPart(nameValuePairs.get(index).getName(), new FileBody(new File(nameValuePairs.get(index).getValue())));
                } else {
                    // Normal string data
                    entity.addPart(nameValuePairs.get(index).getName(), new StringBody(nameValuePairs.get(index).getValue()));
                }
            }

*/
            entity.addPart(IMAGE_KEY, new FileBody(file));
            entity.addPart(FB_KEY, new StringBody(id));
            entity.addPart(DESC_KEY, new StringBody(desc));
            entity.addPart(USER_NAME, new StringBody(name));
            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost, localContext);
            HttpEntity entity1 = response.getEntity();
            String responseString = EntityUtils.toString(entity1);
            Log.d("raj", responseString);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    @Override
    public void onDestroy() {
        notificationManager.cancel(555);
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notification = new Notification(R.drawable.main_logo, "Upload complete!", System.currentTimeMillis());
        notification.contentView = new RemoteViews(getApplicationContext().getPackageName(), R.layout.upload_progress);
        notification.contentView.setTextViewText(R.id.textView, "Upload complete!");
        notification.contentView.setProgressBar(R.id.progressBar, 100, 100, false);
//        notification.contentView.setImageViewResource(R.id.imageRandom,R.drawable.something);
        notification.defaults = Notification.FLAG_NO_CLEAR;
        notificationManager.notify(55, notification);


        super.onDestroy();
    }
}
