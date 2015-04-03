package com.example.dsi.furore;

import android.app.Application;
import android.graphics.BitmapFactory;

import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;

/**
 * Created by Ramesh on 3/25/2015.
 */
public class FuroreApplication extends Application {

    public static final String USER_NAME = "name";
    public static final String USER_IMAGE = "user_image";
    public static final String USER_ID = "fb_id";

    public static DisplayImageOptions defaultOptions;
    public static ImageLoaderConfiguration config;

    @Override
    public void onCreate() {
        super.onCreate();
        // UNIVERSAL IMAGE LOADER SETUP
        File cacheDir = StorageUtils.getCacheDirectory(getApplicationContext());
        BitmapFactory.Options decodingOptions = new BitmapFactory.Options();
        defaultOptions = new DisplayImageOptions.Builder()
                .cacheOnDisk(true).cacheInMemory(true)
                .imageScaleType(ImageScaleType.EXACTLY)
                .decodingOptions(decodingOptions)
//                .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
//                .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
//                .showImageOnFail(R.drawable.ic_error) // resource or drawable
                .displayer(new FadeInBitmapDisplayer(300)).build();

        config = new ImageLoaderConfiguration.Builder(
                getApplicationContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new WeakMemoryCache())
                .diskCacheSize(100 * 1024 * 1024).build();

        ImageLoader.getInstance().init(config);
        // END - UNIVERSAL IMAGE LOADER SETUP
    }
}
