package com.swcuriosity.memes.main;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.Toast;
;

//import com.facebook.share.model.SharePhoto;
//import com.facebook.share.model.SharePhotoContent;
//import com.facebook.share.widget.ShareDialog;
import com.crashlytics.android.Crashlytics;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.database.FirebaseDatabase;
import com.swcuriosity.memes.GalleryUtils;
import com.swcuriosity.memes.R;
import com.swcuriosity.memes.ui.DesignScreen;
import com.swcuriosity.memes.ui.SplashScreen;
import com.swcuriosity.memes.utils.AppConstants;
import com.swcuriosity.memes.utils.DialogManager;
import com.swcuriosity.memes.utils.GlobalMethods;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.ConnectException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class BaseActivity extends AppCompatActivity {
    public static Activity mActivity;
    private int m_intPermsRequestCode = 200;
    String mSelectedLanuageCode;

    protected static final int CAMERA_CODE = 0x0;
    protected static final int GALLERY_INTENT_CALLED = 0x1;
    protected static final int GALLERY_KITKAT_INTENT_CALLED = 0x2;
    protected static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_GALLERY = 0x3;
    protected static final int MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_CAMERA = 0x4;
    final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

    protected Bitmap bitmap;
    protected boolean _taken;
    protected String selectedImagePath;
    protected String selectedOutputPath;
    private static FirebaseDatabase firebaseDatabase;


    private static final String PHOTO_PATH = "PhotoEditor";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mActivity = this;
        Fabric.with(this, new Crashlytics());

        if (firebaseDatabase == null) {
            firebaseDatabase=FirebaseDatabase.getInstance();
            firebaseDatabase.setPersistenceEnabled(true);
//            firebaseDatabase.keepSynced(true);

        }


        // Sample AdMob app ID: ca-app-pub-3940256099942544~3347511713
        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder().setDefaultFontPath("fonts/OpenSans-Regular.otf").build());


    }

    protected void setupUI(View view) {

        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {

                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(mActivity);
                    return false;
                }
            });
        }
        if (view instanceof ViewGroup) {

            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View mInnerView = ((ViewGroup) view).getChildAt(i);
                setupUI(mInnerView);
            }
        }
    }
    public String savTempImage(Bitmap image) {
        String savedImagePath = null;

        String imageFileName = "Meme_" + "temp" + ".jpg";
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        + "/Memes/temp");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath);
            Toast.makeText(mActivity, "IMAGE SAVED", Toast.LENGTH_LONG).show();
        }
        return savedImagePath;
    }

    public void OnClickShare(Bitmap finalBitmap) {

//        Bitmap bitmap =getBitmapFromView(idForSaveView);
        try {
            File file = new File(this.getExternalCacheDir(), "Memes.png");
            FileOutputStream fOut = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 80, fOut);
            fOut.flush();
            fOut.close();
            file.setReadable(true, false);
            final Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            intent.setType("image/png");
            startActivity(Intent.createChooser(intent, "Share image via"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void shareimage(Bitmap mBitmap){
//        Drawable mDrawable = mImageView.getDrawable();
//        Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

        String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "Image Description", null);
        Uri uri = Uri.parse(path);

        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("image/jpeg");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        startActivity(Intent.createChooser(intent, "Share Image"));
    }

    public String saveImage(Bitmap image) {
        String savedImagePath = null;

        String imageFileName = "Meme_" + System.currentTimeMillis() + ".jpg";
        File storageDir = new File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                        + "/Memes");
        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath);

            DialogManager.showToast(this,getResources().getString(R.string.image_saved));
        }
        return savedImagePath;
    }


    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }
    public void fbShare(Bitmap img) {
        try {
//                        File f = new File(pictureFile1, "hop_img.png");
//            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(pictureFile1));

            SharePhoto photo = new SharePhoto.Builder()
                    .setBitmap(img)
                    .build();
            SharePhotoContent content1 = new SharePhotoContent.Builder()
                    .addPhoto(photo)
                    .build();

            ShareDialog shareDialog = new ShareDialog(mActivity);
            shareDialog.show(content1, ShareDialog.Mode.AUTOMATIC);
//            DialogManager.showToast(DesignScreen.this, "shared");
        } catch (Exception e) {

        }
    }



    protected void hideSoftKeyboard(Activity mActivity) {
        try {
            if (mActivity != null && !mActivity.isFinishing()) {
                InputMethodManager mInputMethodManager = (InputMethodManager) mActivity
                        .getSystemService(INPUT_METHOD_SERVICE);

                if (mActivity.getCurrentFocus() != null
                        && mActivity.getCurrentFocus().getWindowToken() != null) {
                    mInputMethodManager.hideSoftInputFromInputMethod(mActivity
                            .getCurrentFocus().getWindowToken(),  InputMethodManager.HIDE_NOT_ALWAYS);

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public static void hideKeyboard(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        //Find the currently focused view, so we can grab the correct window token from it.
        View view = activity.getCurrentFocus();
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = new View(activity);
        }
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    public  void setLanguage(String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getBaseContext().getResources().updateConfiguration(config,
                getBaseContext().getResources().getDisplayMetrics());
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

//
//    public void launchScreenNoStack(Class<?> clazz) {
//        Intent mIntent = new Intent(mActivity, clazz);
//        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(mIntent);
//        overridePendingTransition(R.anim.slide_in_right,
//                R.anim.slide_out_left);
//        finish();
//    }


    public void launchScreen(Class<?> clazz) {
        Intent mIntent = new Intent(mActivity, clazz);
        mActivity.startActivity(mIntent);
        mActivity.overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
        mActivity.finish();
    }
    public void launchScreenNoAnim(Class<?> clazz) {
        Intent mIntent = new Intent(mActivity, clazz);
        mActivity.startActivity(mIntent);
        mActivity.finish();
    }

    public void nextScreen(Class<?> clazz) {

        Intent mIntent = new Intent(mActivity, clazz);
        mActivity.startActivity(mIntent);
        mActivity.overridePendingTransition(R.anim.slide_in_right,
                R.anim.slide_out_left);
    }
    public void nextScreenNoAnim(Class<?> clazz) {

        Intent mIntent = new Intent(mActivity, clazz);
        mActivity.startActivity(mIntent);

    }
    public void previousScreen(Class<?> clazz) {
        Intent mIntent = new Intent(mActivity, clazz);
        mActivity.startActivity(mIntent);
        mActivity.overridePendingTransition(R.anim.slide_out_right,
                R.anim.slide_in_left);
        mActivity.finish();
    }

    public void finishScreen() {
        mActivity.finish();
        mActivity.overridePendingTransition(R.anim.slide_out_right,
                R.anim.slide_in_left);
    }

    public void sysOut(String msg) {
        System.out.println(msg);
        Log.d(getString(R.string.app_name), msg);
    }
    @Override
    protected void onPause() {
        super.onPause();
        Application.activityStoped();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mActivity = this;
        Application.activityResumed();
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        Application.activityStoped();
//    }

    public void onrequestSuccess(Object response) {

    }

//    public void onrequestFailure(RetrofitError retrofitError) {
//
//        //DialogManager.showToast(this, getString(R.string.server_error));
//
//    }
//


    public void onRequestSuccess(Object responseObj) {
    }


    public void onRequestFailure(Throwable t) {

        try {
            System.out.println("errorCode.getCause() Msg--------" + t.getMessage());
            System.out.println("errorCode.getCause() --------" + t.getCause().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (t instanceof IOException ||t.getCause() instanceof ConnectException || t.getCause() instanceof java.net.UnknownHostException
                || t.getMessage() == null) {

            DialogManager.showMsgPopup(this,getString(R.string.no_internet));

        } else if (t.getCause() instanceof java.net.SocketTimeoutException) {


            DialogManager.showMsgPopup(this,getString(R.string.connect_time_out));

        } else {
            DialogManager.showMsgPopup(this,t.getMessage());

        }



    }


    protected void openMedia() {

        final CharSequence[] items = {getString(R.string.camera), getString(R.string.gallery)};

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.upload_dialog_title));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int item) {
                if (item == 0) {
                    startCameraActivity();
                    dialog.dismiss();
                } else if (item == 1) {
                    openGallery();
                    dialog.dismiss();
                }
            }
        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    // Method to show user msg material menu
    public void showMenu(final int caller) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getString(R.string.access_media_permissions_msg));
        builder.setPositiveButton(getString(R.string.continue_txt), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (caller == 1) {
                    ActivityCompat.requestPermissions(BaseActivity.this,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_CAMERA);
                } else {
                    ActivityCompat.requestPermissions(BaseActivity.this,
                            new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_GALLERY);
                }
            }
        });
        builder.setNegativeButton(getString(R.string.not_now), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Toast.makeText(BaseActivity.this, getString(R.string.media_access_denied_msg), Toast.LENGTH_SHORT).show();
            }
        });
        builder.show();

    }

    // startCameraActivity() method fires once user shows camera option and CAMERA_CODE is an requestCode that will be used
    protected void startCameraActivity() {
        int permissionCheck = PermissionChecker.checkCallingOrSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            Intent photoPickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                    getOutputMediaFile());
            photoPickerIntent.putExtra("outputFormat",
                    Bitmap.CompressFormat.JPEG.toString());
            startActivityForResult(
                    Intent.createChooser(photoPickerIntent, getString(R.string.upload_picker_title)),
                    CAMERA_CODE);
        } else {
            showMenu(1);
        }
    }

    /**
     * Create a file for saving an image
     */
    protected Uri getOutputMediaFile() {

        if (isSDCARDMounted()) {
            File mediaStorageDir = new File(
                    Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), PHOTO_PATH);
            // Create a storage directory if it does not exist
            if (!mediaStorageDir.exists()) {
                if (!mediaStorageDir.mkdirs()) {
                    Log.d("MediaAbstractActivity", getString(R.string.directory_create_fail));
                    return null;
                }
            }
            // Create a media file name
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            File mediaFile;
            selectedOutputPath = mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg";
            Log.d("MediaAbstractActivity", "selected camera path "
                    + selectedOutputPath);
            mediaFile = new File(selectedOutputPath);
            return Uri.fromFile(mediaFile);
        } else {
            return null;
        }
    }

    protected boolean isSDCARDMounted() {
        String status = Environment.getExternalStorageState();
        return status.equals(Environment.MEDIA_MOUNTED);

    }

    // openGallery() method fires once user shows gallery option and GALLERY_INTENT_CALLED and GALLERY_KITKAT_INTENT_CALLED
    // are requestCode(s) that will be used in ProfileMediaSherlockFragmetActivity
    protected void openGallery() {
        int permissionCheck = PermissionChecker.checkCallingOrSelfPermission(this,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            if (!isKitKat) {
                Intent intent = new Intent();
                intent.setType("image/jpeg");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(
                        Intent.createChooser(intent, getString(R.string.upload_picker_title)),
                        GALLERY_INTENT_CALLED);
            } else {
                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/jpeg");
                startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
            }
        } else {
            showMenu(2);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_GALLERY: {
                // If request is cancelled, the result arrays are empty.
                int permissionCheck = PermissionChecker.checkCallingOrSelfPermission(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    if (!isKitKat) {
                        Intent intent = new Intent();
                        intent.setType("image/jpeg");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(
                                Intent.createChooser(intent, getString(R.string.upload_picker_title)),
                                GALLERY_INTENT_CALLED);
                    } else {
                        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
                        intent.addCategory(Intent.CATEGORY_OPENABLE);
                        intent.setType("image/jpeg");
                        startActivityForResult(intent, GALLERY_KITKAT_INTENT_CALLED);
                    }
                } else {
                    Toast.makeText(this, getString(R.string.media_access_denied_msg), Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            case MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE_CAMERA: {
                // If request is cancelled, the result arrays are empty.
                int permissionCheck = PermissionChecker.checkCallingOrSelfPermission(this,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
                    Intent photoPickerIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            getOutputMediaFile());
                    photoPickerIntent.putExtra("outputFormat",
                            Bitmap.CompressFormat.JPEG.toString());
                    // photoPickerIntent.putExtra("return-data", true);
                    startActivityForResult(
                            Intent.createChooser(photoPickerIntent, getString(R.string.upload_picker_title)),
                            CAMERA_CODE);
                } else {
                    Toast.makeText(this, getString(R.string.media_access_denied_msg), Toast.LENGTH_SHORT).show();
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected String getPath(final Uri uri) {
        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(this, uri)) {
            // ExternalStorageProvider
            if (GalleryUtils.isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/"
                            + split[1];
                }
            }
            // DownloadsProvider
            else if (GalleryUtils.isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"),
                        Long.valueOf(id));
                return GalleryUtils.getDataColumn(this, contentUri, null, null);
            }
            // MediaProvider
            else if (GalleryUtils.isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{split[1]};

                return GalleryUtils.getDataColumn(this, contentUri, selection,
                        selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return GalleryUtils.getDataColumn(this, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    @Override
    protected void onDestroy() {
        if (bitmap != null) {
            bitmap.recycle();
            bitmap = null;
        }
        System.gc();
        super.onDestroy();
        Application.activityStoped();

    }
    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



    public void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new android.app.AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .create()
                .show();
    }
    public boolean checkCameraRequestPermissions() {
        int m_intPermissionCamera= ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        List<String> m_listPermissionsNeeded = new ArrayList<>();

        if (m_intPermissionCamera != PackageManager.PERMISSION_GRANTED) {
            m_listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }

//
        if (!m_listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, m_listPermissionsNeeded.toArray(new String[m_listPermissionsNeeded.size()]), 300);
            return false;
        }
        return true;
    }
    public boolean checkGalleryRequestPermissions() {
        int m_intReadStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int m_intStoragePermission = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        List<String> m_listPermissionsNeeded = new ArrayList<>();

        if (m_intReadStoragePermission != PackageManager.PERMISSION_GRANTED) {
            m_listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (m_intStoragePermission != PackageManager.PERMISSION_GRANTED) {
            m_listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
//
        if (!m_listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, m_listPermissionsNeeded.toArray(new String[m_listPermissionsNeeded.size()]), 200);
            return false;
        }
        return true;
    }

}

