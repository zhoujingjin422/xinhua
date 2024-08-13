package com.xinhua.language.movieheaven.utils;

import static androidx.lifecycle.Lifecycle.Event.ON_START;

import android.app.Activity;
import android.app.Application;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.OnLifecycleEvent;
import androidx.lifecycle.ProcessLifecycleOwner;

import com.xinhua.language.movieheaven.AutoClickApplication;

import java.util.Date;

/**
 * Prefetches App Open Ads.
 * 开屏广告
 */
public class AppOpenManager implements LifecycleObserver, Application.ActivityLifecycleCallbacks {
    private static final String LOG_TAG = "AppOpenManager";
//    private AppOpenAd appOpenAd = null;

//    private AppOpenAd.AppOpenAdLoadCallback loadCallback;

    private final AutoClickApplication myApplication;
    public Activity currentActivity;
    private static boolean isShowingAd = false;
    private long loadTime = 0;
    private boolean isFirst = true;

    /**
     * Constructor
     */
    public AppOpenManager(AutoClickApplication myApplication) {
        this.myApplication = myApplication;
        this.myApplication.registerActivityLifecycleCallbacks(this);
        ProcessLifecycleOwner.get().getLifecycle().addObserver(this);
    }


    /**
     * Utility method to check if ad was loaded more than n hours ago.
     */
    private boolean wasLoadTimeLessThanNHoursAgo(long numHours) {
        long dateDifference = (new Date()).getTime() - this.loadTime;
        long numMilliSecondsPerHour = 3600000;
        return (dateDifference < (numMilliSecondsPerHour * numHours));
    }



    @Override
    public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle bundle) {

    }

    @Override
    public void onActivityStarted(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityResumed(@NonNull Activity activity) {
        currentActivity = activity;
    }

    @Override
    public void onActivityPaused(@NonNull Activity activity) {

    }

    @Override
    public void onActivityStopped(@NonNull Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle bundle) {

    }

    @Override
    public void onActivityDestroyed(@NonNull Activity activity) {
        currentActivity = null;
    }

    /**
     * LifecycleObserver methods
//     */
//    @OnLifecycleEvent(ON_START)
//    public void onStart() {
//        showAdIfAvailable();
//        Log.d(LOG_TAG, "onStart");
//    }
}