package com.example.dsi.furore;

import android.app.IntentService;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Environment;
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;


public class uploadImage extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */

    public static final String IMAGE_KEY = "selfie_img", FB_KEY = "fb_id";

    public uploadImage(String name) {
        super(name);
    }

    public uploadImage() {
        super("uploadImage");
    }


    @Override
    protected void onHandleIntent(Intent intent) {

        Log.d("raj", "uploading started");

        Bitmap bm = (Bitmap) intent.getParcelableExtra("bitmap");
        String id = intent.getStringExtra("fb_id");

        callUpload(bm, id);

    }

    private void callUpload(Bitmap bm, String id) {

        File file = saveBitmap(bm);


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

            httpPost.setEntity(entity);

            HttpResponse response = httpClient.execute(httpPost, localContext);
            HttpEntity entity1 = response.getEntity();
            String responseString = EntityUtils.toString(entity1);
            Log.d("raj", responseString);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    private File saveBitmap(Bitmap bm) {
        String file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                "/Furore15";
        File dir = new File(file_path);
        if (!dir.exists())
            dir.mkdirs();
        Date date = new Date();
        File file = new File(dir, date.getTime() + ".png");
        Log.d("date", "" + date.getTime());
        FileOutputStream fOut = null;
        try {
            fOut = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fOut);
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
