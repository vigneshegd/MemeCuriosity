package com.swcuriosity.memes.ui;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.PorterDuff;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.provider.ContactsContract;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.facebook.FacebookSdk;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.swcuriosity.memes.BuildConfig;
import com.swcuriosity.memes.ColorPickerAdapter;
import com.swcuriosity.memes.R;
import com.swcuriosity.memes.adapter.FontsAdapter;
import com.swcuriosity.memes.main.BaseActivity;
import com.swcuriosity.memes.utils.AppConstants;
import com.swcuriosity.memes.utils.DialogManager;
import com.swcuriosity.memes.utils.FontProvider;
import com.swcuriosity.memes.utils.ProfileImageSelectionUtil;
import com.swcuriosity.memes.viewmodel.Font;
import com.swcuriosity.memes.viewmodel.Layer;
import com.swcuriosity.memes.viewmodel.TextLayer;
import com.swcuriosity.memes.widget.MotionView;
import com.swcuriosity.memes.widget.SlidingUpPanelLayout;
import com.swcuriosity.memes.widget.entity.ImageEntity;
import com.swcuriosity.memes.widget.entity.MotionEntity;
import com.swcuriosity.memes.widget.entity.TextEntity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.swcuriosity.memes.utils.DialogManager.getDialog;

/**
 * Created by Vicky N on 3/12/2017.
 */

public class DesignScreen extends BaseActivity implements TextEditorDialogFragment.OnTextLayerCallback {
    @BindView(R.id.relative_view)
    RelativeLayout m_lytMainView;
    @BindView(R.id.main_view)
    LinearLayout m_RlMainView;
    @BindView(R.id.dimen_11_img)
    ImageView dimenImageView;

    @BindView(R.id.add_image)
    ImageView mAddImageView;

    @BindView(R.id.img_share)
    ImageView mImgShare;
    @BindView(R.id.fb_share)
    ImageView mImgFb;
    @BindView(R.id.aatext)
    ImageView mImgTxt;
    @BindView(R.id.change_bg_color)
    ImageView mImgBg;
    @BindView(R.id.add_logo)
    ImageView mImgLogo;

    String removFlag = "0";

    private Handler m_handler = new Handler();
    private Runnable m_runnable;
    private ArrayList<Integer> colorPickerColors;
    private View addTextRootView;
    StorageReference spaceRef;
    FirebaseStorage storage;
    StorageReference imagesRef;
    StorageReference storageRef;
    View child;
    private SlidingUpPanelLayout mLayout;
    boolean isFbInstalled = false;
    private FontProvider fontProvider;
    public static final int SELECT_STICKER_REQUEST_CODE = 123;
    int motionflag = 0;
    protected MotionView motionView;
    protected View textEntityEditPanel;
    protected LinearLayout menuEditPanel;
    ImageButton mImgDelete;
    String fab = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ui_design_screen);
        ButterKnife.bind(this);
        final Intent intent = getIntent();
        if (intent.hasExtra("removeflag")) {
            removFlag = getIntent().getStringExtra("removeflag");
        }
        if (intent.hasExtra("fab")) {
            fab = getIntent().getStringExtra("fab");

        }
        FacebookSdk.sdkInitialize(getApplicationContext());
        setfacebook();
        initView();
        this.fontProvider = new FontProvider(getResources());


//         storage = FirebaseStorage.getInstance();
//         storageRef = storage.getReference();
//         imagesRef = storageRef.child("images");
//         spaceRef = storageRef.child("images/space.jpg");
//        mLayout = (SlidingUpPanelLayout) findViewById(R.id.sliding_layout);

        int[] rainbow = getResources().getIntArray(R.array.rainbow);
        colorPickerColors = new ArrayList<>();

        for (int i = 0; i < rainbow.length; i++) {
            colorPickerColors.add(rainbow[i]);
            // Do something with the paint.
        }
        textEntityEditPanel.setVisibility(View.INVISIBLE);
        menuEditPanel.setVisibility(View.VISIBLE);

//        new CountDownTimer(500, 100) {
//
//            public void onTick(long millisUntilFinished) {
//            }
//
//            public void onFinish() {
//                mLayout.setScrollableView(((ImageFragment) fragmentsList.get(0)).imageRecyclerView);
//            }
//
//        }.start();
        motionflag = 0;
        mAddImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditPhoto();
            }
        });
        if (fab.equalsIgnoreCase("1")) {
            {
                showMenuPopup();

            }
//            mAddImageView.callOnClick();
//            EditPhoto();
        }

//        motionView.setEnabled(false);
    }

    public void displayPop() {
        LayoutInflater inflater = getLayoutInflater();
        View dialoglayout = inflater.inflate(R.layout.alert_layout, (ViewGroup) getCurrentFocus());
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setView(dialoglayout);
        final CharSequence[] options = {getResources().getString(R.string.camera), getResources().getString(R.string.gallery), getResources().getString(R.string.add_text)};
        AlertDialog.Builder builder = new AlertDialog.Builder(DesignScreen.this, R.style.MyDialogTheme);

//        View view = LayoutInflater.from(DesignScreen.this).inflate(R.layout.alert_layout,  null);
//
//        builder.setView(dialoglayout);
//        builder.setView(dialoglayout);
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals(getResources().getString(R.string.camera))) {
                    if (addCameraPermissions()) {
                        cameraFunc();

                    }
                } else if (options[item].equals(getResources().getString(R.string.gallery))) {
                    saveGal = 0;
                    if (addStoragePermissions()) {
                        galleryFunc();

                    }

                } else if (options[item].equals(getResources().getString(R.string.add_text))) {
                    openAddTextPopupWindow("", -1);

                }
                dialog.dismiss();
            }
        });
        builder.show();
    }

    int saveGal = 0;

    private boolean addCameraPermissions() {
        boolean addPermission = true;
        if (android.os.Build.VERSION.SDK_INT >= 23) {
            addPermission = checkCameraRequestPermissions();
        }
        return addPermission;
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

                Map<String, Integer> m_mapPerms1 = new HashMap<>();
                // Initialize the map with both permissions
                m_mapPerms1.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                m_mapPerms1.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < m_strPermissions.length; i++)
                        m_mapPerms1.put(m_strPermissions[i], grantResults[i]);
                    // Check for both permissions
                    if (m_mapPerms1.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && m_mapPerms1.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                        //Next screen
                        if (saveGal == 1) {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto, LOGO);
                            cameraCheck = 1;

                        }
                        if (saveGal == 0) {
                            galleryFunc();
                        }
                        if (saveGal == 2) {
                            saveImage(m_RlMainView.getDrawingCache());

                        }

                    } else {
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)

                                ) {
//                            showDialogOK(getString(R.string.storage_cal),
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            switch (which) {
//                                                case DialogInterface.BUTTON_POSITIVE:
//                                                    checkGalleryRequestPermissions();
//                                                    break;
//                                                case DialogInterface.BUTTON_NEGATIVE:
//                                                    DialogManager.showToast( DesignScreen.this,getString(R.string.permission_denied));
//                                                    // proceed with logic by disabling the related features or quit the app.
//                                                    finish();
//                                                    break;
//                                            }
//                                        }
//                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            DialogManager.showToast(DesignScreen.this, getString(R.string.go_to_settings_and_enable_permissions));
//                            finish();
                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
            break;
            case 300: {

                Map<String, Integer> m_mapPerms = new HashMap<>();
                // Initialize the map with both permissions
                // Initialize the map with both permissions
                m_mapPerms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);

                // Fill with actual results from user
                if (grantResults.length > 0) {
                    for (int i = 0; i < m_strPermissions.length; i++)
                        m_mapPerms.put(m_strPermissions[i], grantResults[i]);
                    // Check for both permissions
                    if (m_mapPerms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        //Next screen
                        cameraFunc();
                    } else {
                        //permission is denied (this is the first time, when "never ask again" is not checked) so ask again explaining the usage of permission
                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)) {
//                            showDialogOK(getString(R.string.camera_cal),
//                                    new DialogInterface.OnClickListener() {
//                                        @Override
//                                        public void onClick(DialogInterface dialog, int which) {
//                                            switch (which) {
//                                                case DialogInterface.BUTTON_POSITIVE:
//                                                    checkCameraRequestPermissions();
//                                                    break;
//                                                case DialogInterface.BUTTON_NEGATIVE:
//                                                    DialogManager.showToast( DesignScreen.this,getString(R.string.permission_denied));
//                                                    // proceed with logic by disabling the related features or quit the app.
//                                                    finish();
//                                                    break;
//                                            }
//                                        }
//                                    });
                        }
                        //permission is denied (and never ask again is  checked)
                        //shouldShowRequestPermissionRationale will return false
                        else {
                            DialogManager.showToast(DesignScreen.this, getString(R.string.go_to_settings_and_enable_permissions));
//                            finish();
                            //proceed with logic by disabling the related features or quit the app.
                        }
                    }
                }
            }
            break;

        }
    }

    private MenuItem mAddtLogoMenuItem;
    private MenuItem mRemovePhotoMenuItem;
    private PopupMenu popup;

    private void EditPhoto() {

        //Creating the instance of PopupMenu
        popup = new PopupMenu(this, mAddImageView);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.photos_menu, popup.getMenu());
        mAddtLogoMenuItem = popup.getMenu().findItem(R.id.add_logo_menu);
        mRemovePhotoMenuItem = popup.getMenu().findItem(R.id.remove_photo);

        if (removFlag.equals("0")) {
            mRemovePhotoMenuItem.setVisible(false);
        } else {
            mRemovePhotoMenuItem.setVisible(true);
        }
        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {
                int id = item.getItemId();

                //noinspection SimplifiableIfStatement
                if (id == R.id.take_photo) {
                    if (addCameraPermissions()) {
                        cameraFunc();

                    }

                }
                if (id == R.id.gallery) {
                    saveGal = 0;
                    if (addStoragePermissions()) {
                        galleryFunc();

                    }
                }
                if (id == R.id.remove_photo) {

                    removFlag = "0";
                    mRemovePhotoMenuItem.setVisible(false);
                    dimenImageView.setImageBitmap(null);
                    showMenuPopup();

                }
                if (id == R.id.add_logo_menu) {
                    saveGal = 1;
                    if (addStoragePermissions()) {
                        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                        startActivityForResult(pickPhoto, LOGO);
                        cameraCheck = 1;

                    }

                }
                return true;
            }
        });
        popup.setGravity(Gravity.LEFT);
        if (this != null) {
            popup.show();//showing popup menu

        } else {
            Log.d("desig", "not eork");
        }

    }

    private void addSticker(final Bitmap logoImg) {
        motionView.post(new Runnable() {
            @Override
            public void run() {
                Layer layer = new Layer();
//                Bitmap pica = BitmapFactory.decodeResource(getResources(), stickerResId);

                ImageEntity entity = new ImageEntity(layer, logoImg, motionView.getWidth(), motionView.getHeight());

                motionView.addEntityAndPosition(entity);
            }
        });
    }

    private void shareImage(Bitmap finalBitmap) {

//        Uri contentUri = FileProvider.getUriForFile(DesignScreen.this, getPackageName(), newFile);

        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        Uri screenshotUri = Uri.parse("android.resource://com.android.test/*");
        try {
            InputStream stream = getContentResolver().openInputStream(screenshotUri);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        sharingIntent.setType("image/jpeg");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, screenshotUri);
        startActivity(Intent.createChooser(sharingIntent, "Share image using"));
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

    private boolean stringIsNotEmpty(String string) {
        if (string != null && !string.equals("null")) {
            if (!string.trim().equals("")) {
                return true;
            }
        }
        return false;
    }

//    public void addEmoji(String emojiName) {
//        photoEditorSDK.addEmoji(emojiName, emojiFont);
//        if (mLayout != null)
//            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//    }
//
//    public void addImage(Bitmap image) {
//        photoEditorSDK.addImage(image);
//        if (mLayout != null)
//            mLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
//    }
//
//    private void addText(String text, int colorCodeTextView) {
//        photoEditorSDK.addText(text, colorCodeTextView);
//    }
//
//    private void clearAllViews() {
//        photoEditorSDK.clearAllViews();
//    }
//
//    private void undoViews() {
//        photoEditorSDK.viewUndo();
//    }
//
//    private void eraseDrawing() {
//        photoEditorSDK.brushEraser();
//    }
//

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.main_add_sticker) {
            Intent intent = new Intent(this, StickerSelectActivity.class);
            startActivityForResult(intent, SELECT_STICKER_REQUEST_CODE);
            return true;
        } else if (item.getItemId() == R.id.main_add_text) {
//            addTextSticker();
        }
        return super.onOptionsItemSelected(item);
    }

    private TextLayer createTextLayer(String inputText, int colorCodeTextView) {
        TextLayer textLayer = new TextLayer();
        Font font = new Font();

        font.setColor(TextLayer.Limits.INITIAL_FONT_COLOR);
        font.setSize(TextLayer.Limits.INITIAL_FONT_SIZE);
        font.setTypeface(fontProvider.getDefaultFontName());

        textLayer.setFont(font);

//        if (BuildConfig.DEBUG) {
        textLayer.setText(inputText);

        if (colorCodeTextView != -1)
            textLayer.getFont().setColor(colorCodeTextView);

        else {
            textLayer.getFont().setColor(getResources().getColor(R.color.white));

//            }
//            if (colorCodeTextView != -1)
//                textLayer.setTextColor(colorCodeTextView);
        }

        return textLayer;
    }

    protected void addTextSticker(String inputText, int colorCodeTextView) {
        TextLayer textLayer = createTextLayer(inputText, colorCodeTextView);
        TextEntity textEntity = new TextEntity(textLayer, motionView.getWidth(),
                motionView.getHeight(), fontProvider);
        motionView.addEntityAndPosition(textEntity);

        // move text sticker up so that its not hidden under keyboard
        PointF center = textEntity.absoluteCenter();
        center.y = center.y * 0.5F;
        textEntity.moveCenterTo(center);

        // redraw
        motionView.invalidate();

//        startTextEntityEditing();
    }

    private void openColorPickerPopupWindow(int colorCode) {
        colorBackgroundView = colorCode;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View addTextPopupWindowRootView = inflater.inflate(R.layout.add_color_picker, null);
        TextView addTextDoneTextView = (TextView) addTextPopupWindowRootView.findViewById(R.id.add_text_done_tv);
        TextView addTextCancelTextView = (TextView) addTextPopupWindowRootView.findViewById(R.id.add_text_cancel_tv);
        RecyclerView addTextColorPickerRecyclerView = (RecyclerView) addTextPopupWindowRootView.findViewById(R.id.add_text_color_picker_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DesignScreen.this, LinearLayoutManager.HORIZONTAL, false);
        addTextColorPickerRecyclerView.setLayoutManager(layoutManager);
        addTextColorPickerRecyclerView.setHasFixedSize(true);
        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(DesignScreen.this, colorPickerColors);
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
//                addTextEditText.setTextColor(colorCode);
                colorTempBackgroundView = colorCode;
                m_RlMainView.setBackgroundColor(colorTempBackgroundView);
            }
        });
        addTextColorPickerRecyclerView.setAdapter(colorPickerAdapter);

        final PopupWindow pop = new PopupWindow(DesignScreen.this);
        pop.setContentView(addTextPopupWindowRootView);
        pop.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        pop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(null);
        pop.showAtLocation(addTextPopupWindowRootView, Gravity.TOP, 0, 0);

        addTextDoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                colorBackgroundView = colorTempBackgroundView;
//                addText(addTextEditText.getText().toString(), colorCodeTextView);
//                addTextSticker(addTextEditText.getText().toString(), colorCodeTextView);imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                m_RlMainView.setBackgroundColor(colorBackgroundView);

                pop.dismiss();
//                motionView.setEnabled(true);

            }
        });

        addTextCancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addText(addTextEditText.getText().toString(), colorCodeTextView);
//                addTextSticker(addTextEditText.getText().toString(), colorCodeTextView);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                m_RlMainView.setBackgroundColor(colorBackgroundView);

                pop.dismiss();
//                motionView.setEnabled(true);

            }
        });
    }
//

    private int colorCodeTextView = -1;
    private int colorBackgroundView = -1;
    private int colorTempBackgroundView = -1;

    private void openAddTextPopupWindow(String text, int colorCode) {
        colorCodeTextView = colorCode;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addTextPopupWindowRootView = inflater.inflate(R.layout.add_text_popup_window, null);
        final EditText addTextEditText = (EditText) addTextPopupWindowRootView.findViewById(R.id.add_text_edit_text);

        TextView addTextDoneTextView = (TextView) addTextPopupWindowRootView.findViewById(R.id.add_text_done_tv);
        TextView addTextCancelTextView = (TextView) addTextPopupWindowRootView.findViewById(R.id.add_text_cancel_tv);
        RecyclerView addTextColorPickerRecyclerView = (RecyclerView) addTextPopupWindowRootView.findViewById(R.id.add_text_color_picker_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DesignScreen.this, LinearLayoutManager.HORIZONTAL, false);
        addTextColorPickerRecyclerView.setLayoutManager(layoutManager);
        addTextColorPickerRecyclerView.setHasFixedSize(true);
        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(DesignScreen.this, colorPickerColors);
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                addTextEditText.setTextColor(colorCode);
                colorCodeTextView = colorCode;
            }
        });
        addTextColorPickerRecyclerView.setAdapter(colorPickerAdapter);
        if (stringIsNotEmpty(text)) {
            addTextEditText.setText(text);
            addTextEditText.setTextColor(colorCode == -1 ? getResources().getColor(R.color.white) : colorCode);
        }
        final PopupWindow pop = new PopupWindow(DesignScreen.this);
        pop.setContentView(addTextPopupWindowRootView);
        pop.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        pop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(null);

        pop.showAtLocation(addTextPopupWindowRootView, Gravity.TOP, 0, 0);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
        pop.setOutsideTouchable(false);

        addTextEditText.requestFocus();

        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(addTextPopupWindowRootView.getWindowToken(), 0);


            }
        });

        addTextDoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addText(addTextEditText.getText().toString(), colorCodeTextView);
                if (!addTextEditText.getText().toString().isEmpty()) {

                    addTextSticker(addTextEditText.getText().toString(), colorCodeTextView);
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    pop.dismiss();
                }else{
                    DialogManager.showToast(DesignScreen.this,"Add your text");
                }
                //                motionView.setEnabled(true);

            }
        });
        addTextCancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addText(addTextEditText.getText().toString(), colorCodeTextView);
//                addTextSticker(addTextEditText.getText().toString(), colorCodeTextView);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                pop.dismiss();
//                motionView.setEnabled(true);

            }
        });

    }

    private void openEditTextPopupWindow(String text, int colorCode, final TextEntity textEntity) {
        colorCodeTextView = colorCode;
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View addTextPopupWindowRootView = inflater.inflate(R.layout.add_text_popup_window, null);
        final EditText addTextEditText = (EditText) addTextPopupWindowRootView.findViewById(R.id.add_text_edit_text);
        addTextEditText.requestFocus();

        TextView addTextDoneTextView = (TextView) addTextPopupWindowRootView.findViewById(R.id.add_text_done_tv);
        TextView addTextCancelTextView = (TextView) addTextPopupWindowRootView.findViewById(R.id.add_text_cancel_tv);
        RecyclerView addTextColorPickerRecyclerView = (RecyclerView) addTextPopupWindowRootView.findViewById(R.id.add_text_color_picker_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(DesignScreen.this, LinearLayoutManager.HORIZONTAL, false);
        addTextColorPickerRecyclerView.setLayoutManager(layoutManager);
        addTextColorPickerRecyclerView.setHasFixedSize(true);
        ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(DesignScreen.this, colorPickerColors);
        colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
            @Override
            public void onColorPickerClickListener(int colorCode) {
                addTextEditText.setTextColor(colorCode);
                colorCodeTextView = colorCode;
            }
        });
        addTextColorPickerRecyclerView.setAdapter(colorPickerAdapter);
        if (stringIsNotEmpty(text)) {
            addTextEditText.setText(text);
            addTextEditText.setTextColor(colorCode == -1 ? getResources().getColor(R.color.white) : colorCode);
            addTextEditText.setSelection(addTextEditText.getText().length());

        }
        final PopupWindow pop = new PopupWindow(DesignScreen.this);
        pop.setContentView(addTextPopupWindowRootView);
        pop.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        pop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(null);
        pop.showAtLocation(addTextPopupWindowRootView, Gravity.TOP, 0, 0);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.HIDE_IMPLICIT_ONLY);
        pop.setOutsideTouchable(false);
        pop.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                // TODO Auto-generated method stub
                hideKeyboard(DesignScreen.this);
            }
        });
        addTextDoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!addTextEditText.getText().toString().isEmpty()) {

                    textEntity.getLayer().setText(addTextEditText.getText().toString());
                    textEntity.getLayer().getFont().setColor(colorCodeTextView);
                    textEntity.updateEntity();
                    motionView.invalidate();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    pop.dismiss();
                }else{
                    DialogManager.showToast(DesignScreen.this,"Add your text");
                }
//                motionView.setEnabled(true);

            }
        });
        addTextCancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addText(addTextEditText.getText().toString(), colorCodeTextView);
//                addTextSticker(addTextEditText.getText().toString(), colorCodeTextView);
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                pop.dismiss();
//                motionView.setEnabled(true);

            }
        });
    }

    //
//    private void updateView(int visibility) {
//        topShadow.setVisibility(visibility);
//        topShadowRelativeLayout.setVisibility(visibility);
//        bottomShadow.setVisibility(visibility);
//        bottomShadowRelativeLayout.setVisibility(visibility);
//    }
//
//    private void updateBrushDrawingView(boolean brushDrawingMode) {
//        photoEditorSDK.setBrushDrawingMode(brushDrawingMode);
//        if (brushDrawingMode) {
//            updateView(View.INVISIBLE);
//            drawingViewColorPickerRecyclerView.setVisibility(View.VISIBLE);
//            doneDrawingTextView.setVisibility(View.VISIBLE);
//            eraseDrawingTextView.setVisibility(View.VISIBLE);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(PhotoEditorActivity.this, LinearLayoutManager.HORIZONTAL, false);
//            drawingViewColorPickerRecyclerView.setLayoutManager(layoutManager);
//            drawingViewColorPickerRecyclerView.setHasFixedSize(true);
//            ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(PhotoEditorActivity.this, colorPickerColors);
//            colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
//                @Override
//                public void onColorPickerClickListener(int colorCode) {
//                    photoEditorSDK.setBrushColor(colorCode);
//                }
//            });
//            drawingViewColorPickerRecyclerView.setAdapter(colorPickerAdapter);
//        } else {
//            updateView(View.VISIBLE);
//            drawingViewColorPickerRecyclerView.setVisibility(View.INVISIBLE);
//            doneDrawingTextView.setVisibility(View.INVISIBLE);
//            eraseDrawingTextView.setVisibility(View.INVISIBLE);
//        }
//    }
//
//    private void returnBackWithSavedImage() {
//        updateView(View.INVISIBLE);
//        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(
//                RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
//        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//        parentImageRelativeLayout.setLayoutParams(layoutParams);
//        new CountDownTimer(1000, 500) {
//            public void onTick(long millisUntilFinished) {
//
//            }
//
//            public void onFinish() {
//                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//                String imageName = "IMG_" + timeStamp + ".jpg";
//                Intent returnIntent = new Intent();
//                returnIntent.putExtra("imagePath", photoEditorSDK.saveImage("PhotoEditorSDK", imageName));
//                setResult(Activity.RESULT_OK, returnIntent);
//                finish();
//            }
//        }.start();
//    }
    Intent fbIntent, watsappIntet;

    //     ImageView dimenImageView_1;
    private void initView() {
//        PackageManager pm = getApplicationContext().getPackageManager();
//        isFbInstalled = GlobalMethods.isPackageInstalled("com.facebook.katana", pm);
//
//        if(isFbInstalled){
//            mImgFb.setVisibility(View.VISIBLE);
//        }else{
//            mImgFb.setVisibility(View.INVISIBLE);
//
//        }

        String fbPackageName = "com.facebook.katana";
        String watsppPackageName = "com.watsapp";
        fbIntent = getPackageManager().getLaunchIntentForPackage(fbPackageName);
        if (fbIntent == null) {
            mImgFb.setVisibility(View.GONE);
        } else {
            mImgFb.setVisibility(View.VISIBLE);
        }
//        watsappIntet = getPackageManager().getLaunchIntentForPackage(fbPackageName);
//        if(watsappIntet == null) {
//            mImgFb.setVisibility(View.INVISIBLE);
//        }else{
//            mImgFb.setVisibility(View.VISIBLE);
//        }

        if (AppConstants.CurrrentImageViewGlide != null) {
            dimenImageView.setImageDrawable(AppConstants.CurrrentImageViewGlide.getDrawable());
        }//        linear = (RelativeLayout) findViewById(R.id.linear_parent);
//        m_RlMainView.setDrawingCacheEnabled(true);
//        linear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
////                        showPickCamera(DesignScreen.this);
//            }
//        });
        motionView = (MotionView) findViewById(R.id.main_motion_view);
        menuEditPanel = (LinearLayout) findViewById(R.id.action_layout2);
        textEntityEditPanel = findViewById(R.id.main_motion_text_entity_edit_panel);
        mImgDelete = (ImageButton) findViewById(R.id.image_entity_delete);
        mImgDelete.setVisibility(View.INVISIBLE);
        mImgDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteLogo();
            }
        });
        motionView.setMotionViewCallback(motionViewCallback);
//                motionView.setOnClickListener(new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        motionView.setEnabled(false);
//                    }
//                });
//                addSticker(R.drawable.pikachu_2);
        colorBackgroundView = getResources().getColor(R.color.black);
        colorTempBackgroundView = colorBackgroundView;
        initTextEntitiesListeners();

//        m_lytMainView.removeAllViews();
    }


    public void oncamera() {
//        ( (DesignScreen).this)showPickCamera(DesignScreen.this);

    }

    @OnClick({R.id.savebtn, R.id.fb_share, R.id.aatext, R.id.add_image, R.id.img_share, R.id.change_bg_color
            , R.id.back_imge, R.id.add_logo, R.id.rel_header, R.id.bottom_layout, R.id.action_layout})
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.aatext:
                openAddTextPopupWindow("", -1);
                break;

            case R.id.savebtn:
                disableEntity();
//                textEntityEditPanel.setVisibility(View.INVISIBLE);
//                m_RlMainView.setDrawingCacheEnabled(true);
                saveGal = 2;
                if (addStoragePermissions()) {
                    saveImage(m_RlMainView.getDrawingCache());

                }

                break;
            case R.id.fb_share:
                disableEntity();
                if (isNetworkAvailable()) {

                    fbShare(m_RlMainView.getDrawingCache());

                    disableFB();
                } else {
                    DialogManager.showToast(this, getResources().getString(R.string.no_internet));

                }
                break;
            case R.id.img_share:
                disableEntity();
//
                shareimage(m_RlMainView.getDrawingCache());
                disableShare();
//                OnClickShare
                break;
            case R.id.add_logo:

                showBorderPadding();
                break;
            case R.id.add_image:
                EditPhoto();
                break;
            case R.id.change_bg_color:
                openColorPickerPopupWindow(colorBackgroundView);
                break;
            case R.id.back_imge:
                onBackPressed();
                break;
            case R.id.bottom_layout:
                disableEntity();
                break;
            case R.id.rel_header:
                disableEntity();
                break;
            case R.id.action_layout:
                disableEntity();
                break;
        }


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

    //    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        return true;
    }

    public void onTouchMotion() {
        long downTime = SystemClock.uptimeMillis();
        long eventTime = SystemClock.uptimeMillis() + 100;
        float x = 0.0f;
        float y = 0.0f;
// List of meta states found here: developer.android.com/reference/android/view/KeyEvent.html#getMetaState()
        int metaState = 0;
        MotionEvent motionEvent = MotionEvent.obtain(
                downTime,
                eventTime,
                MotionEvent.ACTION_UP,
                x,
                y,
                metaState
        );

// Dispatch touch event to view
        motionView.dispatchTouchEvent(motionEvent);
    }

    public void disableEntity() {
        RemoveBorderTextEntity();
        onTouchMotion();
        motionView.updateUIFromDesign();
        motionView.setMotionViewCallback(motionViewCallback);
        motionView.performClick();
        motionView.invalidate();
        mImgDelete.setVisibility(View.INVISIBLE);
        textEntityEditPanel.setVisibility(View.INVISIBLE);
        menuEditPanel.setVisibility(View.VISIBLE);
        motionView.invalidate();
        m_RlMainView.setDrawingCacheEnabled(true);

    }


//    public void addText(String text, int colorCodeTextView) {
//        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        addTextRootView = inflater.inflate(com.ahmedadeltito.photoeditorsdk.R.layout.photo_editor_sdk_text_item_list, null);
//        TextView addTextView = (TextView) addTextRootView.findViewById(com.ahmedadeltito.photoeditorsdk.R.id.photo_editor_sdk_text_tv);
//        addTextView.setGravity(Gravity.CENTER);
//        addTextView.setText(text);
//        if (colorCodeTextView != -1)
//            addTextView.setTextColor(colorCodeTextView);
//        MultiTouchListener multiTouchListener = new MultiTouchListener(deleteView,
//                parentView, this.imageView, onPhotoEditorSDKListener);
//        multiTouchListener.setOnMultiTouchListener(this);
//        addTextRootView.setOnTouchListener(multiTouchListener);
//        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
//                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
//        params.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
//        parentView.addView(addTextRootView, params);
//        addedViews.add(addTextRootView);
//        if (onPhotoEditorSDKListener != null)
//            onPhotoEditorSDKListener.onAddViewListener(ViewType.TEXT, addedViews.size());
//    }

    private void storeImageInFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        UploadTask uploadTask = spaceRef.putBytes(data);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle unsuccessful uploads
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                // taskSnapshot.getMetadata() contains file metadata such as size, content-type, and download URL.
//                Uri downloadUrl = taskSnapshot.getDownloadUrl();
            }
        });
    }

    int cameraCheck = 0;
    public static String ImageName = "Memes.png";
    public static int SELECT_FILE = 0;
    public static int REQUEST_CAMERA = 1;
    public static int LOGO = 3;
    public static int CROP_PIC = 2;

    public void cameraFunc() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        File f = new File(Environment.getExternalStorageDirectory(), ImageName);
        Uri photoURI = FileProvider.getUriForFile(DesignScreen.this, getApplicationContext().getPackageName() + ".provider", f);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY);
        for (ResolveInfo resolveInfo : resInfoList) {
            String packageName = resolveInfo.activityInfo.packageName;
            grantUriPermission(packageName, photoURI, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        cameraCheck = 1;

        startActivityForResult(intent, REQUEST_CAMERA);

    }

    public void galleryFunc() {

        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto, SELECT_FILE);
        cameraCheck = 1;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);
        if (cameraCheck == 1) {
            cameraCheck = 0;
            if (resultCode == RESULT_OK) {
                if (requestCode == SELECT_FILE ||
                        requestCode == REQUEST_CAMERA || requestCode == LOGO) {
                    onSelectFromResult(imageReturnedIntent, requestCode);
                } else if (requestCode == CROP_PIC) {
//                    cameraCheck=1;
                    if (imageReturnedIntent != null) {
                        try {

                            String filePath = Environment.getExternalStorageDirectory()
                                    + "/temporary_holder.png";


                            Bitmap m_bitmapChoosedImg = null;
                            m_bitmapChoosedImg = BitmapFactory.decodeFile(filePath);

                            removFlag = "1";

                            dimenImageView.setImageBitmap(null);
                            dimenImageView.invalidate();
                            dimenImageView.setImageBitmap(m_bitmapChoosedImg);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                }
            }
        }


    }

    public void cropCapturedImage(Uri picUri) {
        cameraCheck = 1;
        Intent cropIntent = new Intent("com.android.camera.action.CROP");
        //indicate image type and Uri
        cropIntent.setDataAndType(picUri, "image/*");
        //set crop properties
        cropIntent.putExtra("crop", "true");
        //indicate aspect of desired crop
//        cropIntent.putExtra("aspectX", 6);
//        cropIntent.putExtra("aspectY", 6);
//        cropIntent.putExtra("outputX", 600);
//        cropIntent.putExtra("outputY", 600);
        cropIntent.putExtra("return-data", false);

        File f = new File(Environment.getExternalStorageDirectory(),
                "/temporary_holder.png");
        try {
            f.createNewFile();
        } catch (IOException ex) {
            Log.e("io", ex.getMessage());
        }

        Uri uriOutput = Uri.fromFile(f);

        cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriOutput);

        startActivityForResult(cropIntent, CROP_PIC);
    }

    Uri selectedImage = null;

    private void onSelectFromResult(Intent data, int selectmode) {
        Bitmap choosed_img = null;

        if (selectmode == LOGO) {
            if (data != null) {
                try {

                    choosed_img = ProfileImageSelectionUtil.getImage(data, this);

                    selectedImage = data.getData();
                    choosed_img = ProfileImageSelectionUtil.getCorrectOrientationImage
                            (this, selectedImage, choosed_img);
                    selectedImage = getImageUri(this, choosed_img);
//                    dimenImageView_1.invalidate();
//                    dimenImageView_1.setImageBitmap(choosed_img);
                    addSticker(choosed_img);


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        if (selectmode == SELECT_FILE) {
            if (data != null) {
                try {

                    choosed_img = ProfileImageSelectionUtil.getImage(data, this);

                    selectedImage = data.getData();
                    choosed_img = ProfileImageSelectionUtil.getCorrectOrientationImage
                            (this, selectedImage, choosed_img);
                    selectedImage = getImageUri(this, choosed_img);
                    dimenImageView.invalidate();
                    dimenImageView.setImageBitmap(choosed_img);
                    removFlag = "1";
                    cropCapturedImage(selectedImage);
                    /*
                    choosed_img = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                    String  path = MediaStore.Images.Media.insertImage(getActivity().getContentResolver(), choosed_img, "Title", null);
                    Uri uri = Uri.parse(path);
                    uri = getImageUri(getActivity(),
                            getCorrectOrientationImage(getActivity(),uri,choosed_img));
                    cropCapturedImage(uri);*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else if (selectmode == REQUEST_CAMERA) {


            File file = new File(Environment.getExternalStorageDirectory() + File.separator + ImageName);

            //Crop the captured image using an other intent
            try {
                /*the user's device may not support cropping*/
                Bitmap bitmap = null;
                try {
                    bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), getImageContentUri(this, file));
//                    Uri crop_uri = getImageUri(mActivity,getCorrectOrientationImage(this, getImageContentUri(this, file), bitmap));
                    bitmap = getCorrectOrientationImage(this, getImageContentUri(this, file), bitmap);
                    String filePath = file.getAbsolutePath();


//                        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
//                        bitmap = BitmapFactory.decodeFile(file.getAbsolutePath(), bitmapOptions);
                    dimenImageView.setImageDrawable(null);

                    dimenImageView.setImageBitmap(bitmap);

                    removFlag = "1";
//                    cropCapturedImage(crop_uri);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } catch (ActivityNotFoundException aNFE) {
                //display an error message if user device doesn't support
                String errorMessage = "Sorry - your device doesn't support the crop action!";
                Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
            }

        }


    }


    public static Uri getImageContentUri(Context context, File imageFile) {
        String filePath = imageFile.getAbsolutePath();
        Cursor cursor = context.getContentResolver().query(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Images.Media._ID},
                MediaStore.Images.Media.DATA + "=? ",
                new String[]{filePath}, null);

        if (cursor != null && cursor.moveToFirst()) {
            int id = cursor.getInt(cursor
                    .getColumnIndex(MediaStore.MediaColumns._ID));
            Uri baseUri = Uri.parse("content://media/external/images/media");
            return Uri.withAppendedPath(baseUri, "" + id);
        } else {
            if (imageFile.exists()) {
                ContentValues values = new ContentValues();
                values.put(MediaStore.Images.Media.DATA, filePath);
                return context.getContentResolver().insert(
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
            } else {
                return null;
            }
        }
    }

    public static Bitmap getCorrectOrientationImage(Context context, Uri selectedImage, Bitmap image) {

        Bitmap image1;
        Log.e("selectedImage", "selectedImage" + selectedImage);
        String[] filePathColumn = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(selectedImage,
                filePathColumn, null, null, null);
        cursor.moveToFirst();

        int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
        String filePath = cursor.getString(columnIndex);
        cursor.close();
        int rotate = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(filePath);
            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION, 1);

            Matrix matrix = new Matrix();

            if (orientation == ExifInterface.ORIENTATION_ROTATE_90) {
                rotate = 90;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_180) {
                rotate = 180;
            } else if (orientation == ExifInterface.ORIENTATION_ROTATE_270) {
                rotate = 270;
            }
            if (rotate != 0) {
                int w = image.getWidth();
                int h = image.getHeight();

                matrix.preRotate(rotate);
// Rotate the bitmap
                image = Bitmap.createBitmap(image, 0, 0, w, h, matrix, true);
                image1 = image;
                try {
                    image = image.copy(Bitmap.Config.ARGB_8888, true);

                } catch (OutOfMemoryError e) {
                    return image;
                }

            }
        } catch (Exception exception) {
            Log.d("check", "Could not rotate the image");
        }
        return image;
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
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
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }


    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.PNG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public String saveMediaEntry(ContentResolver cr, Bitmap source,
                                 String title, String description) {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, title);
        values.put(MediaStore.Images.Media.DESCRIPTION, description);
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");

        Uri url = null;
        String stringUrl = null;       /* value to be returned */

        try {
            url = cr.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);

            if (source != null) {
                OutputStream imageOut = cr.openOutputStream(url);
                try {
                    //             int w = source.getWidth();
                    //             int h = source.getHeight();
//
                    //         Matrix matrix = new Matrix();

                    //         matrix.preRotate(0);
                    //         source = Bitmap.createBitmap(source, 0, 0, w, h, matrix, true);
                    //           source = source.copy(Bitmap.Config.ARGB_8888, true);
                    // ProfileImageSelectionUtil.getCorrectOrientationImage(getActivity(), source);
                    source.compress(Bitmap.CompressFormat.PNG, 100, imageOut);
                } finally {
                    imageOut.close();
                }

                //             long id = ContentUris.parseId(url);
                // Wait until MINI_KIND thumbnail is generated.
                //                 Bitmap miniThumb = Images.Thumbnails.getThumbnail(cr, id,
                //                   Images.Thumbnails.MINI_KIND, null);
                // This is for backward compatibility.
                //       Bitmap microThumb = StoreThumbnail(cr, miniThumb, id, 50F, 50F,
                //                         Images.Thumbnails.MICRO_KIND);
            } else {
                //   Log.e(TAG, "Failed to create thumbnail, removing original");
                cr.delete(url, null, null);
                url = null;
            }
        } catch (Exception e) {
            //   Log.e(TAG, "Failed to insert image", e);
            if (url != null) {
                cr.delete(url, null, null);
                url = null;
            }
        }

        if (url != null) {
            stringUrl = url.toString();
        }

        return stringUrl;


    }

//    private void fbShare(Bitmap img) {
//        try {
////                        File f = new File(pictureFile1, "hop_img.png");
////            Bitmap b = BitmapFactory.decodeStream(new FileInputStream(pictureFile1));
//
//            SharePhoto photo = new SharePhoto.Builder()
//                    .setBitmap(img)
//                    .build();
//            SharePhotoContent content1 = new SharePhotoContent.Builder()
//                    .addPhoto(photo)
//                    .build();
//
//            ShareDialog shareDialog = new ShareDialog(DesignScreen.this);
//            shareDialog.show(content1, ShareDialog.Mode.AUTOMATIC);
////            DialogManager.showToast(DesignScreen.this, "shared");
//        } catch (Exception e) {
//
//        }
//    }

    private void SaveImage(Bitmap finalBitmap) {
        File fname;
        String root = Environment.getExternalStorageDirectory().toString();
        File myDir = new File(root + "/Memes");


        if (!myDir.exists())
            myDir.mkdirs();

        File file = new File(myDir + "/" + "Meme_" + System.currentTimeMillis() + ".png");
        try {
            FileOutputStream fOut = new FileOutputStream(file);
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 80, fOut);
            fOut.flush();
            fOut.close();
            DialogManager.showToast(DesignScreen.this, "saved");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void deleteEntity() {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            motionView.deletedSelectedEntity();

            motionView.invalidate();
        }
    }

    private void deleteLogo() {
        ImageEntity imageEntity = currentImageEntity();
        if (imageEntity != null) {
            motionView.deletedSelectedEntity();

            motionView.invalidate();
        }
        mImgDelete.setVisibility(View.INVISIBLE);
        menuEditPanel.setVisibility(View.VISIBLE);

    }

    private void startTextEntityEditing() {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            TextEditorDialogFragment fragment = TextEditorDialogFragment.getInstance(textEntity.getLayer().getText());
            fragment.show(getFragmentManager(), TextEditorDialogFragment.class.getName());
        }

    }

    private void RemoveBorderTextEntity() {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            textEntity.setIsSelected(false);
            //            TextEditorDialogFragment fragment = TextEditorDialogFragment.getInstance(textEntity.getLayer().getText());
//            fragment.show(getFragmentManager(), TextEditorDialogFragment.class.getName());
        }
        ImageEntity imageEntity = currentImageEntity();
        if (imageEntity != null) {
            imageEntity.setIsSelected(false);
        }
        textEntityEditPanel.setVisibility(View.INVISIBLE);
        mImgDelete.setVisibility(View.INVISIBLE);
        menuEditPanel.setVisibility(View.VISIBLE);

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
            DialogManager.showToast(this, getResources().getString(R.string.image_saved));

        }
        return savedImagePath;
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

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        sendBroadcast(mediaScanIntent);
    }

    @Nullable
    private TextEntity currentTextEntity() {
        if (motionView != null && motionView.getSelectedEntity() instanceof TextEntity) {
            return ((TextEntity) motionView.getSelectedEntity());
        } else {
            return null;
        }
    }

    private ImageEntity currentImageEntity() {
        if (motionView != null && motionView.getSelectedEntity() instanceof ImageEntity) {
            return ((ImageEntity) motionView.getSelectedEntity());
        } else {
            return null;
        }
    }

    private final MotionView.MotionViewCallback motionViewCallback = new MotionView.MotionViewCallback() {
        @Override
        public void onEntitySelected(@Nullable MotionEntity entity) {

            hideKeyboard(DesignScreen.this);

            if (entity instanceof ImageEntity) {
                mImgDelete.setVisibility(View.VISIBLE);
                menuEditPanel.setVisibility(View.INVISIBLE);
                textEntityEditPanel.setVisibility(View.INVISIBLE);
            } else if (entity instanceof TextEntity) {
                mImgDelete.setVisibility(View.INVISIBLE);
                textEntityEditPanel.setVisibility(View.VISIBLE);
                menuEditPanel.setVisibility(View.INVISIBLE);
            } else {

                mImgDelete.setVisibility(View.INVISIBLE);
                menuEditPanel.setVisibility(View.VISIBLE);

                textEntityEditPanel.setVisibility(View.INVISIBLE);
            }

//            startTextEntityEditing();
        }

        @Override
        public void onEntityDoubleTap(@NonNull MotionEntity entity) {

            editTextEntity();
        }
    };

    private void increaseTextEntitySize() {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            textEntity.getLayer().getFont().increaseSize(TextLayer.Limits.FONT_SIZE_STEP);
            textEntity.updateEntity();
            motionView.invalidate();
        }
    }

    private void decreaseTextEntitySize() {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            textEntity.getLayer().getFont().decreaseSize(TextLayer.Limits.FONT_SIZE_STEP);
            textEntity.updateEntity();
            motionView.invalidate();
        }
    }

    private void changeTextEntityColor() {
        TextEntity textEntity = currentTextEntity();
        if (textEntity == null) {
            return;
        }

        int initialColor = textEntity.getLayer().getFont().getColor();

        ColorPickerDialogBuilder
                .with(DesignScreen.this)
                .setTitle(R.string.select_color)
                .initialColor(initialColor)
                .wheelType(ColorPickerView.WHEEL_TYPE.CIRCLE)
                .density(8) // magic number
                .setPositiveButton(R.string.ok, new ColorPickerClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                        TextEntity textEntity = currentTextEntity();
                        if (textEntity != null) {
                            textEntity.getLayer().getFont().setColor(selectedColor);
                            textEntity.updateEntity();
                            motionView.invalidate();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                })
                .build()
                .show();
    }

    private void changeTextEntityFont() {
        final List<String> fonts = fontProvider.getFontNames();
        FontsAdapter fontsAdapter = new FontsAdapter(this, fonts, fontProvider);
        new AlertDialog.Builder(this)
                .setTitle(R.string.select_font)
                .setAdapter(fontsAdapter, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int which) {
                        TextEntity textEntity = currentTextEntity();
                        if (textEntity != null) {
                            textEntity.getLayer().getFont().setTypeface(fonts.get(which));
                            textEntity.updateEntity();
                            motionView.invalidate();
                        }
                    }
                })
                .show();
    }

    private void editTextEntity() {
        TextEntity textEntityEdit = currentTextEntity();

        if (textEntityEdit != null) {
            String currentText = textEntityEdit.getLayer().getText();
            int initialColor = textEntityEdit.getLayer().getFont().getColor();
            openEditTextPopupWindow(currentText, initialColor, textEntityEdit);
//            colorCodeTextView = initialColor;
//            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//            View addTextPopupWindowRootView = inflater.inflate(R.layout.add_text_popup_window, null);
//            final EditText addTextEditText = (EditText) addTextPopupWindowRootView.findViewById(R.id.add_text_edit_text);
//            TextView addTextDoneTextView = (TextView) addTextPopupWindowRootView.findViewById(R.id.add_text_done_tv);
//            TextView addTextCancelTextView = (TextView) addTextPopupWindowRootView.findViewById(R.id.add_text_cancel_tv);
//            RecyclerView addTextColorPickerRecyclerView = (RecyclerView) addTextPopupWindowRootView.findViewById(R.id.add_text_color_picker_recycler_view);
//            LinearLayoutManager layoutManager = new LinearLayoutManager(DesignScreen.this, LinearLayoutManager.HORIZONTAL, false);
//            addTextColorPickerRecyclerView.setLayoutManager(layoutManager);
//            addTextColorPickerRecyclerView.setHasFixedSize(true);
//            ColorPickerAdapter colorPickerAdapter = new ColorPickerAdapter(DesignScreen.this, colorPickerColors);
//            colorPickerAdapter.setOnColorPickerClickListener(new ColorPickerAdapter.OnColorPickerClickListener() {
//                @Override
//                public void onColorPickerClickListener(int colorCode) {
//                    addTextEditText.setTextColor(colorCode);
//                    colorCodeTextView = colorCode;
//                }
//            });
//            addTextColorPickerRecyclerView.setAdapter(colorPickerAdapter);
//            if (stringIsNotEmpty(currentText)) {
//                addTextEditText.setText(currentText);
//                addTextEditText.setTextColor(initialColor == -1 ? getResources().getColor(R.color.white) : initialColor);
//            }
//            final PopupWindow pop = new PopupWindow(DesignScreen.this);
//            pop.setContentView(addTextPopupWindowRootView);
//            pop.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
//            pop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
//            pop.setFocusable(true);
//            pop.setBackgroundDrawable(null);
//            pop.showAtLocation(addTextPopupWindowRootView, Gravity.TOP, 0, 0);
//            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
//            addTextDoneTextView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                addText(addTextEditText.getText().toString(), colorCodeTextView);
//
//                    textEntityEdit.getLayer().setText(addTextEditText.getText().toString());
//                    textEntityEdit.getLayer().getFont().setColor(colorCodeTextView);
//                    textEntityEdit.updateEntity();
//                    motionView.invalidate();
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                    pop.dismiss();
////                motionView.setEnabled(true);
//
//                }
//            });
//            addTextCancelTextView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
////                addText(addTextEditText.getText().toString(), colorCodeTextView);
////                addTextSticker(addTextEditText.getText().toString(), colorCodeTextView);
//                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
//                    pop.dismiss();
////                motionView.setEnabled(true);
//
//                }
//            });
        }

    }

    private void initTextEntitiesListeners() {
        findViewById(R.id.text_entity_font_size_increase).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                increaseTextEntitySize();
            }
        });
        findViewById(R.id.text_entity_font_size_decrease).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                decreaseTextEntitySize();
            }
        });
        findViewById(R.id.text_entity_color_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTextEntityColor();
            }
        });
        findViewById(R.id.text_entity_font_change).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeTextEntityFont();
            }
        });
        findViewById(R.id.text_entity_edit).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                startTextEntityEditing();
                editTextEntity();
            }
        });
        findViewById(R.id.text_entity_delete).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteEntity();
                textEntityEditPanel.setVisibility(View.INVISIBLE);
                menuEditPanel.setVisibility(View.VISIBLE);
            }
        });

    }

    @Override
    public void textChanged(@NonNull String text) {
        TextEntity textEntity = currentTextEntity();
        if (textEntity != null) {
            TextLayer textLayer = textEntity.getLayer();
            if (!text.equals(textLayer.getText())) {
                textLayer.setText(text);
                textEntity.updateEntity();
                motionView.invalidate();
            }
        }
    }

    public void sendMessae(View v) throws FileNotFoundException {


        //You can read the image from external drove too
        Uri uri = Uri.parse("android.resource://com.code2care.example.whatsappintegrationexample/drawable/mona");


        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);

        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.setType("image/jpeg");
        intent.setPackage("com.whatsapp");
        startActivity(intent);

    }

    TextView topPaddingTextView, bottomPaddingTextView;
    int topPadding = 0, currenTopPadding, currenBottomPadding, bottomPadding = 0;
    int paddingMul = 5;

    private void showBorderPadding() {

        int progress1 = 0, progress2 = 0;
        currenTopPadding = topPadding;
        currenBottomPadding = bottomPadding;
        if (currenTopPadding > 0) {
            progress1 = currenTopPadding / paddingMul;

        }
        if (currenBottomPadding > 0) {
            progress2 = currenBottomPadding / paddingMul;

        }

        final PopupWindow pop = new PopupWindow(DesignScreen.this);

        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View addTextPopupWindowRootView = inflater.inflate(R.layout.popup_border_padding, null);
        SeekBar seekBar1 = (SeekBar) addTextPopupWindowRootView.findViewById(R.id.seekBar1);
        SeekBar seekBar2 = (SeekBar) addTextPopupWindowRootView.findViewById(R.id.seekBar2);

        seekBar2.getProgressDrawable().setColorFilter(Color.parseColor("#9e4c4e"), PorterDuff.Mode.SRC_IN);
        seekBar1.getProgressDrawable().setColorFilter(Color.parseColor("#9e4c4e"), PorterDuff.Mode.SRC_IN);

        topPaddingTextView = (TextView) addTextPopupWindowRootView.findViewById(R.id.top_padding);

        bottomPaddingTextView = (TextView) addTextPopupWindowRootView.findViewById(R.id.bottom_padding);


        TextView addTextDoneTextView = (TextView) addTextPopupWindowRootView.findViewById(R.id.add_text_done_tv);
        TextView addTextCancelTextView = (TextView) addTextPopupWindowRootView.findViewById(R.id.add_text_cancel_tv);

        seekBar1.setMax(50);
        seekBar2.setMax(50);
        seekBar1.setProgress(progress1);
        seekBar2.setProgress(progress2);

        topPaddingTextView.setText(String.valueOf(progress1));
        bottomPaddingTextView.setText(String.valueOf(progress2));
        pop.setContentView(addTextPopupWindowRootView);
        pop.setWidth(LinearLayout.LayoutParams.WRAP_CONTENT);
        pop.setHeight(LinearLayout.LayoutParams.WRAP_CONTENT);
        pop.setFocusable(true);
        pop.setBackgroundDrawable(null);
        pop.showAtLocation(addTextPopupWindowRootView, Gravity.TOP, 0, 0);
//        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
        SeekBar.OnSeekBarChangeListener yourSeekBarListener = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //add code here
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //add code here
            }

            @Override
            public void onProgressChanged(SeekBar seekBark, int progress1, boolean fromUser) {
                //add code here
                topPaddingTextView.setText(progress1 + "");

                currenTopPadding = progress1 * paddingMul;
                dimenImageView.setPadding(0, currenTopPadding, 0, currenBottomPadding);
            }
        };
        seekBar1.setOnSeekBarChangeListener(yourSeekBarListener);

        SeekBar.OnSeekBarChangeListener yourSeekBarListener2 = new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                //add code here
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //add code here
            }

            @Override
            public void onProgressChanged(SeekBar seekBark, int progress, boolean fromUser) {
                //add code here
                bottomPaddingTextView.setText(progress + "");
                currenBottomPadding = progress * paddingMul;

                dimenImageView.setPadding(0, currenTopPadding, 0, currenBottomPadding);


            }
        };
        seekBar2.setOnSeekBarChangeListener(yourSeekBarListener2);
        addTextDoneTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                topPadding = currenTopPadding;
                bottomPadding = currenBottomPadding;
                dimenImageView.setPadding(0, topPadding, 0, bottomPadding);


                pop.dismiss();

            }
        });
        addTextCancelTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pop.dismiss();

                dimenImageView.setPadding(0, topPadding, 0, bottomPadding);


            }
        });
    }

    public void showMenuPopup() {
        mDialog = getDialog(this, R.layout.alert_layout);

        TextView mCamera = (TextView) mDialog.findViewById(R.id.camera);
        TextView mGallery = (TextView) mDialog.findViewById(R.id.gallery);
        TextView mAddText = (TextView) mDialog.findViewById(R.id.addtext);
//        TextView mCancelTxt = (TextView) mDialog.findViewById(R.id.cancel_action);

        mCamera.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                if (addCameraPermissions()) {
                    cameraFunc();

                }


            }
        });
        mGallery.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                saveGal = 0;
                if (addStoragePermissions()) {
                    galleryFunc();

                }
            }
        });
        mAddText.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                openAddTextPopupWindow("", -1);

            }
        });
//        mCancelTxt.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//               }
//        });
        mDialog.show();
    }

    Dialog mDialog;

    @Override
    public void onBackPressed() {
        disableEntity();
        DialogManager.showConfirmPopup(DesignScreen.this, getResources().getString(R.string.are_you_sure_want_to_discard));
    }
}
