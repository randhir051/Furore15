package com.example.dsi.furore;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

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

    public static final String IMAGE_KEY = "selfie_img", FB_KEY = "fb_id", DESC_KEY = "";

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
        String description = "";

        callUpload(path, id, description);

    }

    private void callUpload(String path, String id, String desc) {

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

            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost, localContext);
            HttpEntity entity1 = response.getEntity();
            String responseString = EntityUtils.toString(entity1);
            Log.d("raj", responseString);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}
