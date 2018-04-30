package com.winit.maidubai;

import android.app.Application;
import android.content.Context;
import android.support.multidex.MultiDexApplication;

import com.facebook.FacebookSdk;
import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.winit.maidubai.common.AppConstants;
import com.winit.maidubai.common.FilesStorage;
import com.winit.maidubai.httpimage.FileSystemPersistence;
import com.winit.maidubai.httpimage.HttpImageManager;
import com.zopim.android.sdk.api.ZopimChat;

/**
 * Created by Girish Velivela on 12-07-2016.
 */
public class MaiDubaiApplication extends MultiDexApplication {

    public static String APP_DB_LOCK = "DB_LOCK";
    public static Picasso picasso;
    public static LruCache picassoLruCache;
    private HttpImageManager mHttpImageManager;
    public static Context mContext;
    public HttpImageManager getHttpImageManager() {
        return mHttpImageManager;
    }
    @Override
    public void onCreate() {
        super.onCreate();
//        ZopimChat.init(AppConstants.ZOOPIM_KEY);
        if(mContext ==null)
            mContext = this;
        FacebookSdk.sdkInitialize(getApplicationContext());

        picassoLruCache = new LruCache(this);
// Set cache
        picasso = new Picasso.Builder(this) //
                .memoryCache(picassoLruCache) //
                .build();

        mHttpImageManager = new HttpImageManager(
                HttpImageManager.createDefaultMemoryCache(),
                new FileSystemPersistence(FilesStorage.getImageCacheDirectory()));
    }
}
