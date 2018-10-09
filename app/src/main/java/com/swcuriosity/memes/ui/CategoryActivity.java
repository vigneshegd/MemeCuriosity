package com.swcuriosity.memes.ui;

/**
 * Created by vigneswaran_467at17 on 06-11-2017.
 */

import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.crashlytics.android.Crashlytics;
import com.firebase.client.Firebase;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import com.google.firebase.messaging.FirebaseMessaging;
import com.swcuriosity.memes.R;
import com.swcuriosity.memes.adapter.CategoryAdapter;
import com.swcuriosity.memes.firebase.FirebaseApiCall;
import com.swcuriosity.memes.firebase.ForceUpdateChecker;
import com.swcuriosity.memes.main.BaseActivity;
import com.swcuriosity.memes.utils.AppConstants;
import com.swcuriosity.memes.utils.DialogManager;
import com.swcuriosity.memes.utils.GlobalMethods;
import com.swcuriosity.memes.viewmodel.ImageUploadInfo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import android.support.design.widget.FloatingActionButton;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.fabric.sdk.android.Fabric;

public class CategoryActivity extends BaseActivity {
    @BindView(R.id.back_imge)
    ImageView mImgBack;
    // Creating DatabaseReference.
    DatabaseReference databaseAddReference, databaseReference1;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter;

    // Creating Progress dialog
    ProgressDialog progressDialog;

    // Creating List of ImageUploadInfo class.
    List<ImageUploadInfo> list = new ArrayList<>();
    HashMap<String, ImageUploadInfo> mMapCategoryList = new HashMap<>();
    public static final String Database_Path1 = "MemeCategoryList/Active";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_images);
        Fabric.with(this, new Crashlytics());
        ButterKnife.bind(this);

        NotificationManager notificationManager = (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
//                    notificationManager.cancel(NOTIFICATION_ID);
        notificationManager.cancelAll();

        // Assign id to RecyclerView.
        setHeader();

        FirebaseMessaging.getInstance().subscribeToTopic(AppConstants.Topic);

//        String token= FirebaseInstanceId.getInstance().getToken();
//        DialogManager.showToast(this,token);
//        Log.d("firebase token",token);
        googleAdBanner();
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);
        DialogManager.showProgress(this);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(CategoryActivity.this));


        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(CategoryActivity.this);

        // Setting up message in Progress dialog.
        progressDialog.setMessage(getResources().getString(R.string.loading));

        // Showing progress dialog.
//        progressDialog.show();
//        DialogManager.hideProgress();
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setVisibility(View.VISIBLE);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String id = "";
                Intent intent = new Intent(CategoryActivity.this, DesignScreen.class);
                AppConstants.SELECTED_URL = "";
                AppConstants.CurrrentImageViewGlide = null;
                intent.putExtra("image_id", id);
                intent.putExtra("removeflag", "0");
                intent.putExtra("fab", "1");
                mActivity.startActivity(intent);

//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
            }
        });


        if (isNetworkAvailable()) {


            // Setting up Firebase image upload folder path in databaseReference.
            // The path is already defined in MainActivity.
            databaseReference1 = FirebaseDatabase.getInstance().getReference(Database_Path1);
            Query query = databaseReference1.orderByChild("priority");

            FirebaseApiCall.getInstance().requestData(query, this);
//
        } else {
            DialogManager.showToast(this, getResources().getString(R.string.no_internet));


            DialogManager.hideProgress();
            // Hiding the progress dialog.
//
            databaseReference1 = FirebaseDatabase.getInstance().getReference(Database_Path1);
            Query query = databaseReference1.orderByChild("priority");

            FirebaseApiCall.getInstance().requestData(query, this);

        }
    }

    private void setCategoryAdapter() {
        adapter = new CategoryAdapter(getApplicationContext(), list);
        GridLayoutManager m_gridLayManager = new GridLayoutManager(CategoryActivity.this, 2);
        recyclerView.setLayoutManager(m_gridLayManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {

        DialogManager.showConfirmPopup(this, getResources().getString(R.string.are_you_sure_want_to_exit));
    }

    private AdView mAdView;

    String bannerID, AdmobID;
    View adContainer;

    private void googleAdBanner() {

        bannerID = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE, AppConstants.BANNER);
        AdmobID = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE, AppConstants.AD_MOB);
//        MobileAds.initialize(this, AdmobID);
//bannerID="ca-app-pub-3940256099942544/6300978111";
//        bannerID="ca-app-pub-2383535204616417/5491566575";
//
//        AdmobID="ca-app-pub-2383535204616417~9024166210";
        adContainer = findViewById(R.id.adMobView);
        adContainer.setVisibility(View.GONE);

        if (bannerID != null) {
            mAdView = new AdView(CategoryActivity.this);

            mAdView.setAdSize(AdSize.BANNER);
            mAdView.setAdUnitId(bannerID);
            ((RelativeLayout) adContainer).addView(mAdView);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.loadAd(adRequest);

            adContainer.setVisibility(View.GONE);
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    adContainer.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAdClosed() {

                }

                @Override
                public void onAdFailedToLoad(int errorCode) {
//                    Log.d("_errorCode",errorCode+"");
//                DialogManager.showToast(CategoryActivity.this,errorCode+"");
                    adContainer.setVisibility(View.GONE);
                }

                @Override
                public void onAdLeftApplication() {
                }

                @Override
                public void onAdOpened() {
                }
            });
        }
    }

    private void setHeader() {
        mImgBack.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onPause() {
        if (mAdView != null) {
            mAdView.pause();
        }
        super.onPause();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mAdView != null) {
            mAdView.resume();
        }
    }

    @Override
    public void onDestroy() {
        if (mAdView != null) {
            mAdView.destroy();
        }
        super.onDestroy();
    }


    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);
        if (responseObj instanceof DataSnapshot) {
            DataSnapshot snapshot = (DataSnapshot) responseObj;
            list.clear();
            mMapCategoryList = (HashMap<String, ImageUploadInfo>) snapshot.getValue();
            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                ImageUploadInfo imageUploadInfo = postSnapshot.getValue(ImageUploadInfo.class);

                String str = postSnapshot.getKey();
                imageUploadInfo.setId(str);
                list.add(imageUploadInfo);
            }
//            GlobalMethods.storeValuetoPreference(CategoryActivity.this, GlobalMethods.ARRAY_LIST_PREFERENCE, AppConstants.CATEGORY_LIST, list);

            if (adapter == null) {
                setCategoryAdapter();
            } else {
                adapter.notifyDataSetChanged();

            }

        }


    }

}