package com.swcuriosity.memes.ui;

/**
 * Created by Vicky N on 11/5/2017.
 */

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.swcuriosity.memes.R;
import com.swcuriosity.memes.adapter.CategoryAdapter;
import com.swcuriosity.memes.adapter.MemeImageAdapter;
import com.swcuriosity.memes.firebase.FirebaseApiCall;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.swcuriosity.memes.utils.GlobalMethods.getTemplatesHash;

public class MemeTemplates extends BaseActivity {
    @BindView(R.id.back_imge)
    ImageView mImgBack;
    String oldKey = "", currentKey = "";
    Boolean isListAvailable = true;
    int scroll = 1;
    int limit = 200;
    @BindView(R.id.title)
    TextView mTxtTitle;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;
    // Creating DatabaseReference.
    DatabaseReference databaseReference, databaseReference1;

    // Creating RecyclerView.
    RecyclerView recyclerView;

    // Creating RecyclerView.Adapter.
    RecyclerView.Adapter adapter;

    // Creating Progress dialog
    ProgressDialog progressDialog;
    GridLayoutManager m_gridLayManager;


    String categoryId, Notify;
    // Creating List of ImageUploadInfo class.
    ArrayList<ImageUploadInfo> list = new ArrayList<>();
    ArrayList<ImageUploadInfo> list1 = new ArrayList<>();
    HashMap<String, ImageUploadInfo> mMapCategoryList = new HashMap<>();
    HashMap<String, ArrayList<ImageUploadInfo>> mTemplatesList = new HashMap<>();

    public static final String Database_Path = "MemeTemplate";
    public static final String Database_Path1 = "MemeTemplateList";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_images);
        ButterKnife.bind(this);

        categoryId = getIntent().getStringExtra("image_id");
        AppConstants.EDITABLE = getIntent().getStringExtra("editable");
        AppConstants.CategoryName = getIntent().getStringExtra("ImageName");
        Notify = getIntent().getStringExtra("notify");


        /*if(Notify!=null){
            if(Notify.equals("1")){
                Intent intent = new Intent(this, MemeTemplates.class);
                intent.putExtra("image_id", categoryId);
                intent.putExtra("ImageName",AppConstants.CategoryName);
                intent.putExtra("editable",AppConstants.EDITABLE);
                intent.putExtra("notify","0");
                finish();

                startActivity(intent);
            }
        }*/
        // Assign id to RecyclerView.
        googleAdBanner();

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        // Assign id to RecyclerView.

        // Setting RecyclerView size true.
        recyclerView.setHasFixedSize(true);

        // Setting RecyclerView layout as LinearLayout.
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DialogManager.showProgress(this);
        setHeader();

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                list = new ArrayList<>();
                adapter = new MemeImageAdapter(getApplicationContext(), list);
                m_gridLayManager = new GridLayoutManager(MemeTemplates.this, 2);

            }
        });


//                m_gridLayManager.setReverseLayout(true);

        recyclerView.setLayoutManager(m_gridLayManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        // Assign activity this to progress dialog.
        progressDialog = new ProgressDialog(this);

        // Setting up message in Progress dialog.
        progressDialog.setMessage(getResources().getString(R.string.loading));

        // Showing progress dialog.
//        progressDialog.show();

        // Setting up Firebase image upload folder path in databaseReference.
        // The path is already defined in MainActivity.
//        databaseReference = FirebaseDatabase.getInstance().getReference(Database_Path)  ;
        databaseReference1 = FirebaseDatabase.getInstance().getReference(Database_Path1 + "/" + categoryId);

        // Adding Add Value Event Listener to databaseReference.
//        databaseReference.child("id").equals(categoryId);

        scroll = 0;
        if (isNetworkAvailable()) {


            final Query query = databaseReference1.orderByKey().limitToLast(limit);

            FirebaseApiCall.getInstance().requestData(query, this);

            //                // Hiding the progress dialog.
//

        } else {
            DialogManager.showToast(this, getResources().getString(R.string.no_internet));

            final Query query = databaseReference1.orderByKey().limitToLast(limit);

            FirebaseApiCall.getInstance().requestData(query, this);

            DialogManager.hideProgress();
        }

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrolled(final RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                GridLayoutManager layoutManager = ((GridLayoutManager) recyclerView.getLayoutManager());
                int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
                if (lastVisibleItemPosition == list.size() - 1) {
//                if(isListAvailable){
                    int setLimit = limit + 1;
                    scroll = 1;
                    mProgressBar.setVisibility(View.VISIBLE);
                    Query query = databaseReference1.limitToLast(setLimit).orderByKey().endAt(oldKey);
//                    FirebaseApiCall.getInstance().requestData(query, MemeTemplates.this);

                    query.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot snapshot) {
                            list1 = new ArrayList<>();
                            mProgressBar.setVisibility(View.GONE);
                            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                                ImageUploadInfo imageUploadInfo = postSnapshot.getValue(ImageUploadInfo.class);
                                String str = postSnapshot.getKey();
                                imageUploadInfo.setId(str);
                                list1.add(imageUploadInfo);
                            }
                            if (list1.size() > 0) {
                                Collections.reverse(list1);
                                list1.remove(0);
                                if (list1.size() > 0) {
                                    oldKey = list1.get(list1.size() - 1).getId();
                                    if (oldKey.equalsIgnoreCase(currentKey)) {
                                        isListAvailable = false;
                                    } else {
                                        currentKey = oldKey;
                                        list.addAll(list1);
                                        adapter.notifyDataSetChanged();
                                    }
                                }


                            }

                            // Hiding the progress dialog.
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                            // Hiding the progress dialog.


                        }
                    });
                }
//                }

            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_FLING) {
                    // Do something
                } else if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                    // Do something
                } else {
                    // Do something
                }
            }
        });
    }

    private void setHeader() {
        mImgBack.setVisibility(View.VISIBLE);
        if ((AppConstants.CategoryName != null) && (!AppConstants.CategoryName.isEmpty())) {


            if ((AppConstants.EDITABLE != null) && (AppConstants.EDITABLE.equalsIgnoreCase("0"))) {
                mTxtTitle.setText(AppConstants.CategoryName);
            }else if ((AppConstants.EDITABLE != null) && (AppConstants.EDITABLE.equalsIgnoreCase("2"))) {
                mTxtTitle.setText(AppConstants.CategoryName);
            } else {
                mTxtTitle.setText(AppConstants.CategoryName + " " + getResources().getString(R.string.templates));

            }
        } else {
            mTxtTitle.setText(getResources().getString(R.string.meme_templates));
        }

    }

    @OnClick({R.id.back_imge})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.back_imge:
                backPressed();
                break;

        }

    }

    private void backPressed() {
        if(Notify.equals("1")) {
            previousScreen(CategoryActivity.class);
        }
        else {
            finishScreen();
        }

    }

    private AdView mAdView;

    @Override
    public void onBackPressed() {
        backPressed();
    }


    String bannerID, AdmobID;
    View adContainer;

    private void googleAdBanner() {

        bannerID = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE, AppConstants.BANNER);
        AdmobID = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE, AppConstants.AD_MOB);
//        MobileAds.initialize(this, AdmobID);

        adContainer = findViewById(R.id.adMobView);
        adContainer.setVisibility(View.GONE);

        if (bannerID != null) {
            mAdView = new AdView(MemeTemplates.this);

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

    DataSnapshot snapshot;

    @Override
    public void onRequestSuccess(Object responseObj) {
        super.onRequestSuccess(responseObj);

        if (responseObj instanceof DataSnapshot) {
            snapshot = (DataSnapshot) responseObj;

            if (scroll == 0) {
                list.clear();
                for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                    ImageUploadInfo imageUploadInfo = postSnapshot.getValue(ImageUploadInfo.class);
                    String str = postSnapshot.getKey();
                    imageUploadInfo.setId(str);
                    list.add(imageUploadInfo);
                }
                Collections.reverse(list);

                mTemplatesList.put(categoryId, list);
//                GlobalMethods.storeValuetoPreference(MemeTemplates.this,GlobalMethods.ARRAY_LIST_PREFERENCE,AppConstants.TEMPLATES_LIST,mTemplatesList);

                if (list.size() > 0) {
                    oldKey = list.get(list.size() - 1).getId();

                    currentKey = oldKey;
                }

                AppConstants.MemeList = list;
                adapter.notifyDataSetChanged();
                DialogManager.hideProgress();

            }

        } else if (scroll == 1) {

            list1 = new ArrayList<>();
            for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                ImageUploadInfo imageUploadInfo = postSnapshot.getValue(ImageUploadInfo.class);
                String str = postSnapshot.getKey();
                imageUploadInfo.setId(str);
                list1.add(imageUploadInfo);
            }
            if (list1.size() > 0) {
                Collections.reverse(list1);
                list1.remove(0);
                if (list1.size() > 0) {
                    oldKey = list1.get(list1.size() - 1).getId();
                    if (oldKey.equalsIgnoreCase(currentKey)) {
                        isListAvailable = false;
                    } else {
                        currentKey = oldKey;
                        list.addAll(list1);
                        adapter.notifyDataSetChanged();
                    }
                }


            }

            // Hiding the progress dialog.

        }


    }

}
