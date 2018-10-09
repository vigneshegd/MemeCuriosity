package com.swcuriosity.memes.ui;

import android.Manifest;
import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;


import com.crashlytics.android.Crashlytics;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.swcuriosity.memes.R;
import com.swcuriosity.memes.firebase.FirebaseApiCall;
import com.swcuriosity.memes.firebase.ForceUpdateChecker;
import com.swcuriosity.memes.main.BaseActivity;
import com.swcuriosity.memes.utils.AppConstants;
import com.swcuriosity.memes.utils.DialogManager;
import com.swcuriosity.memes.utils.GlobalMethods;
import com.swcuriosity.memes.viewmodel.ImageUploadInfo;
import com.swcuriosity.memes.viewmodel.admob;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by vignesh on 10/26/2016.
 */

public class SplashScreen extends BaseActivity  implements ForceUpdateChecker.OnUpdateNeededListener {
    private boolean m_boolIsLocationEnabled = false,isAlertShow=false;
    private Handler m_handler = new Handler();
    private Runnable m_runnable;
    private ProgressBar m_progressBar;
    private int m_intPermsRequestCode = 200;
    private boolean m_boolIsGpsPopup = false;
    DatabaseReference databaseAddReference;
    public static final String Database_add_Path = "admob";
private boolean permission=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_splash_screen);
        permission=addPermissions();
         if (permission) {
            initView();
        }

//        Crashlytics.getInstance().crash(); // Force a crash
//        getHashmap();
//        System.out.println("Device ID--- "+getStringValue(this, AppConstants.DEVICE_ID));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        m_handler.removeCallbacks(m_runnable);
//        m_handler.
        finishScreen();

    }

    private void getHashmap()
    {
        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.swcuriosity.memes", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String base64string = new String(Base64.encode(md.digest(), 0));
                Log.e("hash_key", base64string);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }
    private boolean addPermissions() {
        boolean addPermission = true;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            addPermission = checkAndRequestPermissions();
        }
        return addPermission;
    }




    private void initView() {
        m_handler = new Handler();
        m_runnable = new Runnable() {
            @Override
            public void run() {
                {
                    databaseAddReference = FirebaseDatabase.getInstance().getReference(Database_add_Path);
//                    Query query = databaseAddReference.;
//
                    NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                    notificationManager.cancel(NOTIFICATION_ID);
                    notificationManager.cancelAll();
                    FirebaseApiCall.getInstance().requestData(databaseAddReference, SplashScreen.this);

                    if (isAlertShow){

                    }else {
                        launchScreen(CategoryActivity.class);

                    }

//                   if (GlobalMethods.isLoggedIn(SplashScreen.this))
//                    {
////                        getHashmap();
//
//                        launchScreen(HomeScreen.class);
//                    }else{
////                       getHashmap();
//                        launchScreen(LoginScreen.class);
//                    }


                }
            }
        };
        m_handler.postDelayed(m_runnable, 2100);

    }
    @Override
    protected void onResume() {
        super.onResume();

        isAlertShow=false;
if (permission){
    ForceUpdateChecker.with(this).onUpdateNeeded(this).check();

}


//        DialogManager.showToast(this,"onres");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }

    @Override
    protected void onPause() {
//        DialogManager.showToast(this,"onpause");

        super.onPause();
    }

    @Override
    protected void onStop() {
//        DialogManager.showToast(this,"onstop");

        super.onStop();
    }
    //
//    @Override
//    public void onOkClick() {
//        if (m_boolIsGpsPopup) {
//            m_boolIsGpsPopup = false;
//            m_boolIsLocationEnabled = true;
//            Intent myIntent = new Intent(
//                    Settings.ACTION_LOCATION_SOURCE_SETTINGS);
//            startActivity(myIntent);
//        } else {
//            finish();
//        }
//
//    }


    private boolean checkAndRequestPermissions() {
        int m_intPermissionSendMessage = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int m_intReadStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int m_intStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
//        int callPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
//        int microPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
//        int m_intReadCalendarPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR);
//        int m_intWriteCalendarPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_CALENDAR);
//        int m_intLocationPermission=ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
//        int m_intLocationCoarsePermission=ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION);
        List<String> m_listPermissionsNeeded = new ArrayList<>();
        if (m_intPermissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            m_listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (m_intReadStoragePermission != PackageManager.PERMISSION_GRANTED) {
            m_listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (m_intStoragePermission != PackageManager.PERMISSION_GRANTED) {
            m_listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
//        if (m_intReadCalendarPermission != PackageManager.PERMISSION_GRANTED) {
//            m_listPermissionsNeeded.add(Manifest.permission.READ_CALENDAR);
//        }
//        if (m_intWriteCalendarPermission != PackageManager.PERMISSION_GRANTED) {
//            m_listPermissionsNeeded.add(Manifest.permission.WRITE_CALENDAR);
//        }
//        if (m_intLocationPermission != PackageManager.PERMISSION_GRANTED) {
//            m_listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
//        }
//        if (m_intLocationCoarsePermission != PackageManager.PERMISSION_GRANTED) {
//            m_listPermissionsNeeded.add(Manifest.permission.ACCESS_COARSE_LOCATION);
//        }
        if (!m_listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, m_listPermissionsNeeded.toArray(new String[m_listPermissionsNeeded.size()]), m_intPermsRequestCode);
            return false;
        }
        return true;
    }

//    private boolean checkAndRequestPermissions1() {
//        int m_intPermissionCall= ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
//        List<String> m_listPermissionsNeeded = new ArrayList<>();
//
//        if (m_intPermissionCall != PackageManager.PERMISSION_GRANTED) {
//            m_listPermissionsNeeded.add(Manifest.permission.CAMERA);
//
//        }
//        if (!m_listPermissionsNeeded.isEmpty()) {
//            ActivityCompat.requestPermissions(this, m_listPermissionsNeeded.toArray(new String[m_listPermissionsNeeded.size()]), m_intPermsRequestCode);
//            return false;
//        }
//        return true;
//    }

    @Override
    public void onRequestPermissionsResult(int m_intRequestCode, String[] m_strPermissions, int[] grantResults) {

        switch (m_intRequestCode) {
            case 200: {

                Map<String, Integer> m_mapPerms = new HashMap<>();
                // Initialize the map with both permissions
                m_mapPerms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                m_mapPerms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                m_mapPerms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
//                m_mapPerms.put(Manifest.permission.READ_CALENDAR, PackageManager.PERMISSION_GRANTED);
//                m_mapPerms.put(Manifest.permission.WRITE_CALENDAR, PackageManager.PERMISSION_GRANTED);
                m_mapPerms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);
                m_mapPerms.put(Manifest.permission.ACCESS_COARSE_LOCATION, PackageManager.PERMISSION_GRANTED);


                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < m_strPermissions.length; i++)
                        m_mapPerms.put(m_strPermissions[i], grantResults[i]);
                    // Check for both permissions
                    if (m_mapPerms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                            && m_mapPerms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && m_mapPerms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
//                            && m_mapPerms.get(Manifest.permission.READ_CALENDAR) == PackageManager.PERMISSION_GRANTED
//                            && m_mapPerms.get(Manifest.permission.WRITE_CALENDAR) == PackageManager.PERMISSION_GRANTED
                            && m_mapPerms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            && m_mapPerms.get(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
                            ) {
                        //Next screen
                            initView();
                    } else {
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_CALENDAR)
//                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_CALENDAR)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
//                                showDialogOK(getString(R.string.camera_storage_location_cal),
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            switch (which) {
//                                                case DialogInterface.BUTTON_POSITIVE:
//                                                    checkAndRequestPermissions();
//                                                    break;
//                                                case DialogInterface.BUTTON_NEGATIVE:
//                                                    DialogManager.showToast( SplashScreen.this,getString(R.string.permission_denied));
//                                                    // proceed with logic by disabling the related features or quit the app.
//                                                    finish();
//                                                    break;
//                                            }
//                                        }
//                                    });

                            initView();

                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            DialogManager.showToast( SplashScreen.this,getString(R.string.go_to_settings_and_enable_permissions));
//                            finish();
                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }
    }



    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof DataSnapshot) {
            DataSnapshot snapshot = (DataSnapshot) responseObj;
            try{
                HashMap<String, String> admob = (HashMap<String, String>) snapshot.getValue();
                GlobalMethods.storeValuetoPreference(SplashScreen.this, GlobalMethods.STRING_PREFERENCE, AppConstants.AD_MOB, admob.get("adMobid"));
                GlobalMethods.storeValuetoPreference(SplashScreen.this, GlobalMethods.STRING_PREFERENCE, AppConstants.BANNER, admob.get("bannerId"));
                GlobalMethods.storeValuetoPreference(SplashScreen.this, GlobalMethods.STRING_PREFERENCE, AppConstants.INTER,admob.get("intersitialId"));

                MobileAds.initialize(this, admob.get("adMobid"));

            }catch (Exception e){
                e.printStackTrace();
            }


        }


    }
    AlertDialog dialog;
    @Override
    public void onUpdateNeeded(final String updateUrl) {
        isAlertShow=true;
        if(dialog!=null) {
            if (dialog.isShowing()) {
                dialog.dismiss();
            }
        }
         dialog = new AlertDialog.Builder(this)
                .setTitle("New version available")
                .setCancelable(false)

                .setMessage("Please update our new version of Memes Curiosity.")
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
//                                isAlertShow=false;
                                redirectStore(updateUrl);
                            }
                        }).setNegativeButton("No, thanks",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isAlertShow=false;
                                launchScreen(CategoryActivity.class);
                                m_handler.removeCallbacks(m_runnable);
                            }
                        }).create();

        dialog.show();


    }
    private void redirectStore(String updateUrl) {
//        isAlertShow=false;
        final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(updateUrl));
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

}
