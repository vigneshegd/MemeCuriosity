package com.swcuriosity.memes.ui;

import android.Manifest;
import android.app.Dialog;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.facebook.FacebookSdk;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.swcuriosity.memes.R;
import com.swcuriosity.memes.firebase.FirebaseApiCall;
import com.swcuriosity.memes.main.BaseActivity;
import com.swcuriosity.memes.utils.AppConstants;
import com.swcuriosity.memes.utils.DialogManager;
import com.swcuriosity.memes.utils.GlobalMethods;
import com.swcuriosity.memes.viewmodel.ImageUploadInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Vicky N on 11/16/2017.
 */

public class ViewPagerActivity extends BaseActivity {
    @BindView(R.id.add_photo)
    ViewPager m_txtuserAva;

    @BindView(R.id.edit_photo)
    ImageView mEditImg;
    @BindView(R.id.savebtn)
    ImageView mSaveImg;


    @BindView(R.id.getAnsbtn)
    TextView mAns;

    // Creating DatabaseReference.
    DatabaseReference databaseReference;

    @BindView(R.id.fb_share)
    ImageView mImgFb;
    @BindView(R.id.img_share)
    ImageView mImgShare;
    // Creating RecyclerView.

    // Creating RecyclerView.Adapter.
    Dialog mDialog;
    // Creating Progress dialog
    ProgressDialog progressDialog;
    private Handler m_handler = new Handler();
    private Runnable m_runnable;

    // Creating List of ImageUploadInfo class.
    List<ImageUploadInfo> list = new ArrayList<>();
    CustomPagerAdapter mCustomPagerAdapter;
    List<ImageUploadInfo> MainImageDownloadList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_pager);
        ButterKnife.bind(this);
        // Assign id to RecyclerView.
        progressDialog=new ProgressDialog(this);
        progressDialog.setMessage(getResources().getString(R.string.loading));
        FacebookSdk.sdkInitialize(getApplicationContext());

        setfacebook();
        Intent intent = getIntent();
        Bundle args = intent.getBundleExtra("BUNDLE");
        MainImageDownloadList = (ArrayList<ImageUploadInfo>) args.getSerializable("ARRAYLIST");



        if(AppConstants.EDITABLE!=null){
            if(AppConstants.EDITABLE.equalsIgnoreCase("0")){
                mAns.setVisibility(View.GONE);
//                mEditImg.setVisibility(View.GONE);
            }else if(AppConstants.EDITABLE.equalsIgnoreCase("2")) {
                mAns.setVisibility(View.VISIBLE);
            }else {
                mAns.setVisibility(View.GONE);
            }
        }
        mEditImg.setVisibility(View.VISIBLE);
       mDialog = DialogManager.getDialog(this, R.layout.progress);
       mCustomPagerAdapter = new CustomPagerAdapter(this, MainImageDownloadList);
        m_txtuserAva.setAdapter(mCustomPagerAdapter);
        m_txtuserAva.setCurrentItem(AppConstants.CURRENT_POS);
        m_txtuserAva.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
               AppConstants.CURRENT_POS=position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
//        AppConstants.CURRENT_POS;
    }
    private void setfacebook() {

        String fbPackageName = "com.facebook.katana";

        Intent fbIntent = getPackageManager().getLaunchIntentForPackage(fbPackageName);
        if (fbIntent == null) {
            mImgFb.setVisibility(View.GONE);
        } else {
            mImgFb.setVisibility(View.VISIBLE);
        }
    }

    @OnClick({R.id.savebtn, R.id.edit_photo, R.id.img_share,R.id.fb_share,R.id.getAnsbtn})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.edit_photo:
//                String id=MainImageUploadInfoList.get(position).getId();
                getCurrentImage(3);
                break;
            case R.id.img_share:
                getCurrentImage(2);
                break;

            case R.id.savebtn:
                getCurrentImage(1);

//                storeImageInFirebase(linear.getDrawingCache());
                break;
            case R.id.fb_share:
                getCurrentImage(4);
                break;
            case R.id.getAnsbtn:
                getCurrentImage(5);

        }
    }

    private void disableFB() {
        mImgFb.setEnabled(false);

        m_handler = new Handler();
        m_runnable = new Runnable() {
            @Override
            public void run() {
                {

                    mImgFb.setEnabled(true);
                }
            }
        };
        m_handler.postDelayed(m_runnable, 3500);


    }

    private void disableShare() {
        mImgShare.setEnabled(false);

        m_handler = new Handler();
        m_runnable = new Runnable() {
            @Override
            public void run() {
                {

                    mImgShare.setEnabled(true);
                }
            }
        };
        m_handler.postDelayed(m_runnable, 2500);


    }

    private void disableAns() {
        mAns.setEnabled(false);

        m_handler = new Handler();
        m_runnable = new Runnable() {
            @Override
            public void run() {
                {

                    mAns.setEnabled(true);
                }
            }
        };
        m_handler.postDelayed(m_runnable, 2500);


    }
    private boolean addStoragePermissions() {
        boolean addPermission = true;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            addPermission = checkGalleryRequestPermissions();
        }
        return addPermission;
    }
    @Override
    public void onRequestPermissionsResult(int m_intRequestCode, String[] m_strPermissions, int[] grantResults) {

        switch (m_intRequestCode) {
            case 200: {

                Map<String, Integer> m_mapPerms = new HashMap<>();
                // Initialize the map with both permissions
                m_mapPerms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                m_mapPerms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < m_strPermissions.length; i++)
                        m_mapPerms.put(m_strPermissions[i], grantResults[i]);
                    // Check for both permissions
                    if (m_mapPerms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && m_mapPerms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        //Next screen
                        saveImage(imageView.getDrawingCache());

                    } else {
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

                                ) {
                 /*           showDialogOK(getString(R.string.storage_cal),
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkGalleryRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    DialogManager.showToast(ViewPagerActivity.this, getString(R.string.permission_denied));
                                                    // proceed with logic by disabling the related features or quit the app.
                                                    finish();
                                                    break;
                                            }
                                        }
                                    });
                 */       }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            DialogManager.showToast(ViewPagerActivity.this, getString(R.string.go_to_settings_and_enable_permissions));
//                            finish();
                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
        }
    }

    ImageView imageView;
            private void getCurrentImage(int flag) {
//        View currentView=m_txtuserAva.getChildAt(AppConstants.CURRENT_POS);
        View myView = m_txtuserAva.findViewWithTag("View"+m_txtuserAva.getCurrentItem());

         imageView=(ImageView) myView.findViewById(R.id.imageView);
        imageView.setDrawingCacheEnabled(true);
        if(imageView.getDrawingCache()!=null){
        if(flag==1){
            if(addStoragePermissions()){
                saveImage(imageView.getDrawingCache());

            }
        }
        else if(flag==2){
//            OnClickShare(imageView.getDrawingCache());
            shareimage(imageView.getDrawingCache());
            disableShare();
        }else  if(flag==3){

//            GlideDrawable res=imageView.ge();
            AppConstants.CurrrentImageViewGlide=imageView;
            Intent intent = new Intent(mActivity, DesignScreen.class);
            intent.putExtra("removeflag","1");
            AppConstants.SELECTED_URL=MainImageDownloadList.get(AppConstants.CURRENT_POS).getImageURL();
            intent.putExtra("image_id",MainImageDownloadList.get(AppConstants.CURRENT_POS).getId());
            mActivity.startActivity(intent);

        }else  if(flag==4){
            if (isNetworkAvailable()) {

                fbShare(imageView.getDrawingCache());

                disableFB();
            }
            else {
                DialogManager.showToast(this,getResources().getString(R.string.no_internet));

            }

        }
        else  if(flag==5){
            if (isNetworkAvailable()) {
                
                answerPopup(MainImageDownloadList.get(AppConstants.CURRENT_POS).getImageName());
            }
            else {
                DialogManager.showToast(this,getResources().getString(R.string.no_internet));

            }

        }
        }else{
            DialogManager.showToast(this,getResources().getString(R.string.please_wait));

        }

        ;

    }
    InterstitialAd mInterstitialAd;

    String interstrialID,AdmobID;
private void googleAd(final String answer){
    mInterstitialAd = new InterstitialAd(this);
    interstrialID = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE, AppConstants.INTER);
    AdmobID = (String) GlobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE, AppConstants.AD_MOB);
    mInterstitialAd.setAdUnitId(interstrialID);
    AdRequest adRequest = new AdRequest.Builder().build();
    // Load ads into Interstitial Ads
    mInterstitialAd.loadAd(adRequest);


    mInterstitialAd.setAdListener(new AdListener() {
        @Override
        public void onAdLoaded() {
            progressDialog.dismiss();
            showInterstitial();
            DialogManager.showAnswerPopup(ViewPagerActivity.this,answer);

        }

        @Override
        public void onAdClosed() {

        }

        @Override
        public void onAdFailedToLoad(int errorCode) {
//                    Log.d("_errorCode",errorCode+"");
//                DialogManager.showToast(CategoryActivity.this,errorCode+"");
//            adContainer.setVisibility(View.GONE);
            progressDialog.dismiss();
            DialogManager.showAnswerPopup(ViewPagerActivity.this,answer);

        }

        @Override
        public void onAdLeftApplication() {
        }

        @Override
        public void onAdOpened() {
        }
    });

 }
    private void showInterstitial() {
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }
    }
        private void answerPopup(String answer) {
                if(answer!=null){
                    if(!answer.isEmpty()){
                        progressDialog.show();
                        googleAd(answer);
                    }else {
                        DialogManager.showToast(this,"Answer not available");

                    }
                }
               else{
                    DialogManager.showToast(this,"Answer not available");
                }

    }

    GlideDrawable imageGlide;
    ProgressBar progressBar;
    ImageView currentImage;
    public class CustomPagerAdapter extends PagerAdapter {

        Context mContext;
        LayoutInflater mLayoutInflater;
        //    List<AttachmentEntity> AttachmentEntity;
        List<ImageUploadInfo> MainImageUploadInfoList;

        public CustomPagerAdapter(Context context, List<ImageUploadInfo> mAttachmentEntity) {
            mContext = context;
            MainImageUploadInfoList = mAttachmentEntity;
            mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        }

        @Override
        public int getCount() {
            return MainImageUploadInfoList.size();
        }


        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
            View itemView = mLayoutInflater.inflate(R.layout.pager_adapter, container, false);
            itemView.setTag("View"+position);
            currentImage = (ImageView) itemView.findViewById(R.id.imageView);

            progressBar = (ProgressBar) itemView.findViewById(R.id.progress_bar);

            String url = MainImageUploadInfoList.get(position).getImageURL();
//            mDialog.show();
            Glide.with(mContext).load(url).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model,
                                           Target<GlideDrawable> target, boolean isFirstResource) {

//                    mDialog.dismiss();

//                    progressBar.setVisibility(View.GONE);
                    return false;

                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    imageGlide=resource;
//                    mDialog.dismiss();
//                    progressBar.setVisibility(View.GONE);


//                    String ImagePath = ((ViewPagerActivity) mActivity).savTempImage(resource);
//
//                    AppConstants.currentImage = ImagePath;
                    return false;
                }
            })

                    .into(currentImage);
            container.addView(itemView);

            return itemView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((RelativeLayout) object);
        }
    }
}