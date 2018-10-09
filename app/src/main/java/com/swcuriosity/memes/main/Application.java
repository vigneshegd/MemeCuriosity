package com.swcuriosity.memes.main;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDex;
import android.util.Log;
//import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.crash.FirebaseCrash;
import com.google.firebase.remoteconfig.FirebaseRemoteConfig;
import com.swcuriosity.memes.firebase.ForceUpdateChecker;

import java.io.File;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.HashMap;
import java.util.Map;

import io.fabric.sdk.android.Fabric;

public class Application extends android.app.Application {
    static Context mAppContext;
    private static boolean activityVisible;
    private static final String TAG = Application.class.getSimpleName();

    public static boolean isActivityVisible() {
        return activityVisible;
    }

    public static void activityResumed() {
        activityVisible = true;
    }

    public static void activityStoped() {
        activityVisible = false;
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Fabric.with(this, new Crashlytics());

//        Crashlytics.log(Log.VERBOSE, TAG, "onCreate");

            final Fabric fabric = new Fabric.Builder(this)
                    .kits(new Crashlytics())
                    .debuggable(true)           // Enables Crashlytics debugger
                    .build();
            Fabric.with(fabric);
//        }


        Application.mAppContext = getApplicationContext();
        Thread.setDefaultUncaughtExceptionHandler(mUncaughtExceptionHandler);


        final FirebaseRemoteConfig firebaseRemoteConfig = FirebaseRemoteConfig.getInstance();




        // set in-app defaults
        Map<String, Object> remoteConfigDefaults = new HashMap();
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_REQUIRED, false);
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_CURRENT_VERSION, "1.0.0");
        remoteConfigDefaults.put(ForceUpdateChecker.KEY_UPDATE_URL,
                "https://play.google.com/store/apps/details?id=com.swcuriosity.memes");

        firebaseRemoteConfig.setDefaults(remoteConfigDefaults);
        firebaseRemoteConfig.fetch(60) // fetch every minutes
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "remote config is fetched.");
                            firebaseRemoteConfig.activateFetched();
                        }
                    }
                });
    }



    public static Context getAppContext() {

        return Application.mAppContext;
    }

    @Override
    protected void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        MultiDex.install(this);
    }

    private UncaughtExceptionHandler mUncaughtExceptionHandler = new UncaughtExceptionHandler() {

        @Override
        public void uncaughtException(Thread thread, Throwable exception) {

Log.d("vicky1",exception.toString());
            exception.printStackTrace();
//            FirebaseCrash.report(exception);

            // restartApp();
                  Crashlytics.log(Log.VERBOSE, TAG,exception.toString());

//            android.os.Process.killProcess(android.os.Process.myPid());

        }
    };

    private void restartApp() {
        /*PendingIntent mPendingIntent = PendingIntent.getActivity(
                getAppContext(), 192837, new Intent(getAppContext(),
                        SplashScreen.class), PendingIntent.FLAG_ONE_SHOT);

        AlarmManager mAlarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        mAlarmManager.set(AlarmManager.RTC, System.currentTimeMillis() + 3000,
                mPendingIntent);

        System.exit(0);*/
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if (appDir.exists()) {
            String[] children = appDir.list();
            for (String s : children) {
                if (!s.equals("lib")) {
                    @SuppressWarnings("unused")
                    boolean deleteResult = deleteDir(new File(appDir, s));
                }
            }
        }

    }

    private boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }

//    public synchronized Tracker getGoogleAnalyticsTracker() {
//        AnalyticsTrackers analyticsTrackers = AnalyticsTrackers.getInstance();
//        return analyticsTrackers.get(AnalyticsTrackers.Target.APP);
//    }

    /***
     * Tracking screen view
     *
     * @param screenName screen name to be displayed on GA dashboard
     */
//    public void trackScreenView(String screenName) {
//        Tracker t = getGoogleAnalyticsTracker();
//
//        // Set screen name.
//        t.setScreenName(screenName);
//
//        // Send a screen view.
//        t.send(new HitBuilders.ScreenViewBuilder().build());
//
//        GoogleAnalytics.getInstance(this).dispatchLocalHits();
//    }

    /***
     * Tracking exception
     *
     * @param e exception to be tracked
     */
//    public void trackException(Exception e) {
//        if (e != null) {
//            Tracker t = getGoogleAnalyticsTracker();
//
//            t.send(new HitBuilders.ExceptionBuilder()
//                    .setDescription(
//                            new StandardExceptionParser(this, null)
//                                    .getDescription(Thread.currentThread()
//                                            .getName(), e)).setFatal(false)
//                    .build());
//        }
//    }

    /***
     * Tracking event
     *
     * @param category event category
     * @param action   action of the event
     * @param label    label
     */
//    public void trackEvent(String category, String action, String label) {
//        Tracker t = getGoogleAnalyticsTracker();
//
//        // Build and send an Event.
//        t.send(new HitBuilders.EventBuilder().setCategory(category)
//                .setAction(action).setLabel(label).build());
//    }
}
