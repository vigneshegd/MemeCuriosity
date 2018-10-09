package com.swcuriosity.memes.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
//
//import com.facebook.FacebookSdk;
//import com.facebook.login.LoginManager;
//import com.google.android.gms.auth.api.Auth;
//import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
//import com.google.android.gms.common.ConnectionResult;
//import com.google.android.gms.common.api.GoogleApiClient;
//import com.google.android.gms.common.api.ResultCallback;
//import com.google.android.gms.common.api.Status;
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//import com.moos.customer.R;
//import com.moos.customer.adapter.LanguageAdapter;
//import com.moos.customer.apiinterface.APIRequestHandler;
//import com.moos.customer.apiinterface.AppApiConstants;
//import com.moos.customer.entity.AlarmEntity;
//import com.moos.customer.entity.CommonInputEntity;
//import com.moos.customer.entity.LanguageList;
//import com.moos.customer.entity.NotificationToneEntity;
//import com.moos.customer.entity.UserDetailsEntityRes;
//import com.moos.customer.fragments.AppointmentFragment;
//import com.moos.customer.fragments.SettingFragment;
//import com.moos.customer.main.BaseActivity;
//import com.moos.customer.main.BaseFragment;
//import com.moos.customer.ui.HomeScreen;
//import com.moos.customer.ui.LoginScreen;
//import com.twitter.sdk.android.Twitter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.swcuriosity.memes.viewmodel.ImageUploadInfo;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//import de.hdodenhof.circleimageview.CircleImageView;
//
//import static com.moos.customer.main.BaseActivity.mActivity;
//import static com.moos.customer.ui.LoginScreen.loginType;
//import static com.moos.customer.utils.DialogManager.getDialog;
//

public class GlobalMethods {
    public static int STRING_PREFERENCE = 1;
    public static int INT_PREFERENCE = 2;
    public static int BOOLEAN_PREFERENCE = 3;
    public static int ARRAY_LIST_PREFERENCE = 4;
    public static int LONG_PREFERENCE = 5;
    public static ArrayList<Boolean> langselect = new ArrayList<Boolean>();
    public static void storeValuetoPreference(Context context, int preference, String key, Object value) {
        SharedPreferences sharedPreference = context.getSharedPreferences(AppConstants.shared_pref_name,
                Context.MODE_PRIVATE);
        SharedPreferences.Editor edit = sharedPreference.edit();
        if (preference == STRING_PREFERENCE) {
            edit.putString(key, (String) value);
        }
        if (preference == INT_PREFERENCE) {
            edit.putInt(key, (Integer) value);
        }
        if (preference == BOOLEAN_PREFERENCE) {
            edit.putBoolean(key, (Boolean) value);
        }
        if (preference == ARRAY_LIST_PREFERENCE) {
            Gson gson = new Gson();
            String arrayList1 = gson.toJson(value);
            edit.putString(key, arrayList1);
        }
        edit.commit();

    }




    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }


    public static boolean isConnectingToInternet(Context _context) {
        ConnectivityManager connectivity = (ConnectivityManager) _context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo[] info = connectivity.getAllNetworkInfo();
            if (info != null)
                for (int i = 0; i < info.length; i++)
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }

        }
        return false;
    }

    public static boolean isEmailValid(String email) {
        boolean isValid = false;

        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;

        }
        return isValid;
    }

    public static float setRatingValue(String mRating) {
        float mRati = 0.0f;

        try {
            if (mRating != null && !mRating
                    .equalsIgnoreCase("")) {
                mRati = Float.parseFloat(mRating);
            }

        } catch (Exception ignored) {

        }
        return mRati;
    }


    public static String convertingcurrenttimetoutc(String time) {

        //String inputPattern = "dd-MM-yyyy hh:mm:ss a";
        //String outputPattern = "dd-MM-yyyy hh:mm:ss a";

        String inputPattern = "";

        inputPattern = "dd-MM-yy";



        String outputPattern = "";

        outputPattern = "dd-MM-yyyy";


        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern, Locale.US);
        outputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public static String convertingcurrenttimetoutctime(String time) {

        //String inputPattern = "dd-MM-yyyy hh:mm:ss a";
        //String outputPattern = "dd-MM-yyyy hh:mm:ss a";

        String inputPattern = "";

        inputPattern = "dd-MM-yy";



        String outputPattern = "";

        outputPattern = "dd-MM-yyyy";


        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        outputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public static String convertingtimeformat(String time) {

        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm a",Locale.US);
        SimpleDateFormat DesiredFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
        String str = "";
        Date date = null;
        try {
            DesiredFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            date = sourceFormat.parse(time);
            str = DesiredFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str;
    }


    public static String convertingutctocurrenttime(String time) {

        SimpleDateFormat sourceFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
        SimpleDateFormat DesiredFormat = new SimpleDateFormat("hh:mm a,dd-MMM-yyyy",Locale.US);
        String str = "";
        Date date = null;
        try {
            sourceFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
            date = sourceFormat.parse(time);
            str = DesiredFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return str.toUpperCase(Locale.ENGLISH);
    }




    public static boolean isValidMobile(String phone)
    {
        boolean check;
        if(!Pattern.matches("[a-zA-Z]+", phone))
        {
            if(phone.length() < 6 || phone.length() > 13)
            {
                check = false;

            }
            else
            {
                check = true;
            }
        }
        else
        {
            check=false;
        }
        return check;
    }
    /*public static  void initGoogleLoginService() {
        GoogleSignInOptions googleSigninOption = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(mActivity.getString(com.moosapp.R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        try {
            mGoogleApiClient = new GoogleApiClient.Builder(mActivity)
                    .enableAutoManage((FragmentActivity) mActivity, new GoogleApiClient.OnConnectionFailedListener() {
                        @Override
                        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                            //Google Auth Failed
                            DialogManager.showToast(mActivity, connectionResult.getErrorMessage());
                        }
                    })
                    .addApi(Auth.GOOGLE_SIGN_IN_API, googleSigninOption)
                    .build();
        } catch (Exception e) {
                e.printStackTrace();
        }

    }*/


    public static Object getValueFromPreference(Context context, int preference, String key) {
        SharedPreferences sharedPreference = context.getSharedPreferences(AppConstants.shared_pref_name,
                Context.MODE_PRIVATE);

        if (preference == STRING_PREFERENCE) {
            return (Object) sharedPreference.getString(key, "");
        }
        if (preference == INT_PREFERENCE) {
            return (Object) sharedPreference.getInt(key, 0);
        }
        if (preference == BOOLEAN_PREFERENCE) {
            return (Object) sharedPreference.getBoolean(key, false);
        }
        if (preference == ARRAY_LIST_PREFERENCE) {


            String arrayList = sharedPreference.getString(key, null);

            return (Object) arrayList;
        }

        return null;

    }

    public static ArrayList<ImageUploadInfo> geCategoryListList(Context mcontext) {
        String arrayList=(String) getValueFromPreference(mcontext, GlobalMethods.ARRAY_LIST_PREFERENCE,
                AppConstants.CATEGORY_LIST);
        Gson gson = new Gson();
        Type type = new TypeToken<ArrayList<ImageUploadInfo> >() {
        }.getType();
        ArrayList< ImageUploadInfo> arrayList1 = gson.fromJson(arrayList, type);
        if(arrayList1!=null){

            return arrayList1;
        }

        return arrayList1;
    }
    public static HashMap<String,ArrayList<ImageUploadInfo> > getTemplatesHash(Context mcontext) {
    String arrayList=(String) getValueFromPreference(mcontext, GlobalMethods.ARRAY_LIST_PREFERENCE,
            AppConstants.TEMPLATES_LIST);
    Gson gson = new Gson();
    Type type = new TypeToken<HashMap<String,ArrayList<ImageUploadInfo> >>() {
    }.getType();
   HashMap<String,ArrayList<ImageUploadInfo> > arrayList1 = gson.fromJson(arrayList, type);


        return arrayList1;
}
    public static ArrayList<ImageUploadInfo> getTemplatesList(Context mcontext,String key) {
        String arrayList=(String) getValueFromPreference(mcontext, GlobalMethods.ARRAY_LIST_PREFERENCE,
                AppConstants.TEMPLATES_LIST);
        Gson gson = new Gson();
        Type type = new TypeToken<HashMap<String,ArrayList<ImageUploadInfo> >>() {
        }.getType();
        HashMap<String,ArrayList<ImageUploadInfo> > arrayList1 = gson.fromJson(arrayList, type);

        if(arrayList1!=null){
            ArrayList<ImageUploadInfo> list= arrayList1.get(key);
            return list;
        }

        return null;
    }
    //
//    public static ArrayList<NotificationToneEntity> getReminderAudioList(Context mcontext) {
//        String arrayList=(String) getValueFromPreference(mcontext, GlobalMethods.ARRAY_LIST_PREFERENCE,
//                AppConstants.REMAINDER_TONE);
//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList< NotificationToneEntity> >() {
//        }.getType();
//        ArrayList< NotificationToneEntity> arrayList1 = gson.fromJson(arrayList, type);
//
//        return arrayList1;
//    }
    public static String getLocalTime(String inputDate) {
        Date dateobj;
        dateobj = null;
        String lv_dateFormateInLocalTimeZone = "";
        String lv_localTimeZone = "GMT";
        try {

            //create a new Date object using the UTC timezone
            SimpleDateFormat Inputformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
            Inputformat.setTimeZone(TimeZone.getTimeZone("UTC"));
            dateobj = Inputformat.parse(inputDate);
//            SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a,dd-MMM-yyyy",Locale.US);

            //SimpleDateFormat lv_formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z'('Z')'");

            displayFormat.setTimeZone(TimeZone.getDefault());
            lv_dateFormateInLocalTimeZone = displayFormat.format(dateobj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lv_dateFormateInLocalTimeZone;
    }

    public static String getLocalTimeinAMPM(String inputDate) {
        Date dateobj;
        dateobj = null;
        String lv_dateFormateInLocalTimeZone = "";
        String lv_localTimeZone = "GMT";
        try {

            //create a new Date object using the UTC timezone
            SimpleDateFormat Inputformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
            Inputformat.setTimeZone(TimeZone.getTimeZone("UTC"));
            dateobj = Inputformat.parse(inputDate);
            SimpleDateFormat displayFormat = new SimpleDateFormat("hh:mm a",Locale.US);
            //SimpleDateFormat lv_formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z'('Z')'");

            displayFormat.setTimeZone(TimeZone.getDefault());
            lv_dateFormateInLocalTimeZone = displayFormat.format(dateobj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lv_dateFormateInLocalTimeZone;
    }

    public static String getUTCTime(String inputDate) {
        Date dateobj;
        dateobj = null;
        String lv_dateFormateInLocalTimeZone = "";
        try {

            //create a new Date object using the UTC timezone
            SimpleDateFormat Inputformat = new SimpleDateFormat("yyyy-MM-dd hh:mm a",Locale.US);
            Inputformat.setTimeZone(TimeZone.getDefault());
            dateobj = Inputformat.parse(inputDate);
            SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);
            //SimpleDateFormat lv_formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z'('Z')'");
            displayFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
            lv_dateFormateInLocalTimeZone = displayFormat.format(dateobj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lv_dateFormateInLocalTimeZone;
    }


    @SuppressWarnings("deprecation")
    public static String getLocalCorrectTime(String inputDate, String inputTime) {
        String returnTime = "";
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.US);

            SimpleDateFormat inputTimeFormat = new SimpleDateFormat("HH:mm:ss",Locale.US);

            SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a",Locale.US);

            inputDate = inputDate.split("T")[0];

            Date d = new Date();
            Date d1 = new Date();
            try {
                d = inputDateFormat.parse(inputDate);
                d1 = inputTimeFormat.parse(inputTime);

                d.setHours(d1.getHours());
                d.setMinutes(d1.getMinutes());

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(d);

                String timeZone = getDeviceTimeZone();

                if (timeZone.substring(0, 1).toString().equalsIgnoreCase("+")) {

                    calendar.add(Calendar.HOUR, Integer.parseInt(timeZone.substring(1, 3)));

                    calendar.add(Calendar.MINUTE, Integer.parseInt(timeZone.substring(3, 5)));
                } else {

                    calendar.add(Calendar.HOUR, -Integer.parseInt(timeZone.substring(1, 3)));

                    calendar.add(Calendar.MINUTE, -Integer.parseInt(timeZone.substring(3, 5)));
                }

                d = calendar.getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            returnTime = displayFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnTime;

    }

    public static String getLocalDisplayformat(String inputDate) {
        String returnTime = "";
        try {

            SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.US);

            SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MMM-yyyy hh:mm a",Locale.US);


            Date d = new Date();

            try {
                d = inputDateFormat.parse(inputDate);

                returnTime = displayFormat.format(d);
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnTime;
    }

    public static String getLocalDisplayformat1(String inputDate) {
        String returnTime = "";
        try {

            SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss",Locale.US);

            SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy hh:mm a",Locale.US);


            Date d = new Date();

            try {
                d = inputDateFormat.parse(inputDate);

                returnTime = displayFormat.format(d);
            } catch (Exception e) {
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return returnTime;
    }


    @SuppressWarnings("deprecation")
    public static String getServerCorrectTime(String inputDate, String inputTime) {
        String returnTime = "";
        try {
            SimpleDateFormat inputDateFormat = new SimpleDateFormat("dd-MM-yyyy",Locale.US);

            SimpleDateFormat inputTimeFormat = new SimpleDateFormat("HH:mm",Locale.US);

            SimpleDateFormat displayFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US);

            inputDate = inputDate.split("T")[0];

            Date d = new Date();
            Date d1 = new Date();
            try {
                d = inputDateFormat.parse(inputDate);
                d1 = inputTimeFormat.parse(inputTime);

                d.setHours(d1.getHours());
                d.setMinutes(d1.getMinutes());

                Calendar calendar = Calendar.getInstance();

                calendar.setTime(d);

                String timeZone = getDeviceTimeZone();

                if (timeZone.substring(0, 1).toString().equalsIgnoreCase("+")) {

                    calendar.add(Calendar.HOUR, -Integer.parseInt(timeZone.substring(1, 3)));

                    calendar.add(Calendar.MINUTE, -Integer.parseInt(timeZone.substring(3, 5)));
                } else {

                    calendar.add(Calendar.HOUR, Integer.parseInt(timeZone.substring(1, 3)));

                    calendar.add(Calendar.MINUTE, Integer.parseInt(timeZone.substring(3, 5)));
                }

                d = calendar.getTime();

            } catch (ParseException e) {
                e.printStackTrace();
            }
            returnTime = displayFormat.format(d);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnTime;

    }

    public static String getDeviceTimeZone() {

        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT"), Locale.getDefault());
        Date currentLocalTime = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("Z");
        String localTime = dateFormat.format(currentLocalTime);

        return localTime;
    }

    public static String getUTCtime() {

        SimpleDateFormat dateFormatGmt = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        dateFormatGmt.setTimeZone(TimeZone.getTimeZone("GMT"));

//Local time zone
        SimpleDateFormat dateFormatLocal = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

//Time in GMT
        //return dateFormatLocal.parse( dateFormatGmt.format(new Date()) );


        String gmtTime = dateFormatGmt.format(new Date());
        return gmtTime;
    }

    public static File storeImage(Bitmap imageData, String filename) {
        // get path to external storage (SD card)
        String iconsStoragePath = Environment.getExternalStorageDirectory().getPath() + File.separator;
        String filePath = iconsStoragePath + filename;

        try {

            FileOutputStream fileOutputStream = new FileOutputStream(filePath);

            BufferedOutputStream bos = new BufferedOutputStream(fileOutputStream);

            // choose another format if PNG doesn't suit you
            imageData.compress(CompressFormat.PNG, 100, bos);
            Log.e("Succ", "Succ");
            bos.flush();
            bos.close();

        } catch (FileNotFoundException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
        } catch (IOException e) {
            Log.w("TAG", "Error saving image file: " + e.getMessage());
        }
        return new File(filePath);

    }
    public static WebView mTemsWebView;







    public static String getStringValue(Context context, String Key) {

        String mString = (String) GlobalMethods.getValueFromPreference(context,
                GlobalMethods.STRING_PREFERENCE, Key);

        return mString;

    }

//    public static void blink_animation(Context context,View view){
//        Animation myFadeInAnimation = AnimationUtils.loadAnimation(context,
//                R.anim.fade);
//        view.startAnimation(myFadeInAnimation);
//    }
//    public static ArrayList<LanguageList> mLanguageList = new ArrayList<LanguageList>();
//
//    public static ArrayList<LanguageList> getLanguageList(Context mContext) {
//        String[] mLanguageStrings = mContext.getResources().getStringArray(R.array.language);
//        String[] mLanguageCodeStrings = mContext.getResources().getStringArray(R.array.lanuagecode);
//
//        mLanguageList.clear();
//        for (int i = 0; i < mLanguageStrings.length; i++) {
//            LanguageList mLanguageEntity = new LanguageList();
//            mLanguageEntity.setLanguage(mLanguageStrings[i]);
//            mLanguageEntity.setLanguageCode(mLanguageCodeStrings[i]);
//            mLanguageList.add(mLanguageEntity);
//        }
//        /*LanguageList mLanguageEntity = new LanguageList();
//        mLanguageEntity.setLanguage("Choose From Album");
//        mLanguageEntity.setLanguageCode("");
//        mLanguageList.add(mLanguageList.size()-1,mLanguageEntity);*/
//        return mLanguageList;
//    }
//    public static void showLanguagePopup(ArrayList<LanguageList> mLanuageList, final LanguageCallback mCallback,
//                                         final Activity mContext, final TextView lantxtview, final boolean refresh) {
//
//        final Dialog mDialog = DialogManager.getDialog(mContext, R.layout.popup_spinner_view);
//        mDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        ListView mLvqualificationlist = (ListView) mDialog.findViewById(R.id.spinner_listview);
//        Window window = mDialog.getWindow();
//        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
//        window.setGravity(Gravity.CENTER);
//        RelativeLayout main_layout = (RelativeLayout)mDialog.findViewById(R.id.parent_lay);
//        Button mClose = (Button) mDialog.findViewById(R.id.cancelbutton);
//        Button mSubmit = (Button) mDialog.findViewById(R.id.submitbutton);
//        ArrayList<String> mLangList = new ArrayList<String>();
//        final ArrayList<String> mCodeList = new ArrayList<String>();
//        langselect.clear();
//        for (int i = 0; i < mLanuageList.size(); i++) {
//            mLangList.add(mLanuageList.get(i).getLanguage());
//            mCodeList.add(mLanuageList.get(i).getLanguageCode());
//            //langselect.add(false);
//
//            String mLanguage = (String) GlobalMethods.getValueFromPreference(mContext, GlobalMethods.STRING_PREFERENCE, AppConstants.STORED_LANGUAGE);
//            if((mLanguage==null)|| (mLanguage.isEmpty()||mLanguage.equals(""))) {
//                if (mLanuageList.get(i).getLanguage().equalsIgnoreCase("English")) {
//                    langselect.add(true);
//                } else {
//                    langselect.add(false);
//
//                }
//            }
//            else{
//                if (GlobalMethods.getStringValue(mContext,AppConstants.STORED_LANGUAGE).equals(mLanuageList.get(i).getLanguage())) {
//                    langselect.add(true);
//                } else {
//                    langselect.add(false);
//                }
//            }
//
//
//        }
//
//        main_layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(mDialog.isShowing())
//                {
//                    mDialog.dismiss();
//                }
//            }
//        });
//        mClose.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                mDialog.dismiss();
//            }
//        });
//        mSubmit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                /*lantxtview.setText("LANGUAGE - "+AppConstants.SELECTED_LAUNGUAGE);*/
//
//                /*GlobalMethods.storeValuetoPreference(mContext,1,"Language",AppConstants.SELECTED_LAUNGUAGE);*/
//
//                GlobalMethods.storeValuetoPreference(mContext, GlobalMethods
//                                .STRING_PREFERENCE,
//                        AppConstants.STORED_LANGUAGE, AppConstants.SELECTED_LAUNGUAGE);
//                GlobalMethods.storeValuetoPreference(mContext, GlobalMethods
//                                .STRING_PREFERENCE,
//                        AppConstants.STORED_LANGUAGE_CODE, AppConstants.SELECTED_LAUNGUAGE_CODE);
//                ((BaseActivity) mContext).setLanguage(AppConstants.SELECTED_LAUNGUAGE_CODE);
//
//                if(refresh)
//                {
//                    RefreshView(mContext);
//                }else{
//                    mCallback.onOkClick();
//                    /*Intent intent= new Intent(mContext, LoginScreen.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//                    mContext.startActivity(intent);*/
//
//                }
//
//
//                mDialog.dismiss();
//            }
//        });
//
//
//
//        LanguageAdapter adapter = new LanguageAdapter(mContext, mLangList,mCodeList,langselect,mSubmit,mClose);
//        mLvqualificationlist.setAdapter(adapter);
//
//        mLvqualificationlist.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//
//            private String mLanguageStr;
//            private String mLanguageCode;
//
//            @Override
//            public void onItemClick(AdapterView<?> parent, View arg1, int pos, long arg3) {
//
//                mLanguageStr = (String) parent.getItemAtPosition(pos);
//                mLanguageCode = mCodeList.get(pos);
//                mCallback.onItemClick(mLanguageStr, mLanguageCode);
//                //mDialog.dismiss();
//
//            }
//        });
//        mClose.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//            }
//        });
//        mDialog.show();
//    }
//    public static ArrayList<String> getStringList(Context mcontext,String IdName) {
//        String arrayList=(String) getValueFromPreference(mcontext, GlobalMethods.ARRAY_LIST_PREFERENCE,
//                IdName);
//        Gson gson = new Gson();
//        Type type = new TypeToken<ArrayList< String> >() {
//        }.getType();
//        ArrayList<String> arrayList1 = gson.fromJson(arrayList, type);
//
//        return arrayList1;
//    }
//
//    public static ArrayList<AlarmEntity> getAlarmlist(Context context) {
//        SharedPreferences settings;
//        List<AlarmEntity> favorites = null;
//
//        //String jsonFavorites = settings.getString(FAVORITES, null);
//        Log.e("ALARM_ARRAYALARM_ARRAY","ALARM_ARRAYALARM_ARRAY"+GlobalMethods.getStringValue(context, AppConstants.ALARM_ARRAY));
//        String jsonFavorites = GlobalMethods.getStringValue(context, AppConstants.ALARM_ARRAY);
//        if(!jsonFavorites.isEmpty())
//        {
//            Gson gson = new Gson();
//            AlarmEntity[] favoriteItems = gson.fromJson(jsonFavorites,AlarmEntity[].class);
//
//            favorites = Arrays.asList(favoriteItems);
//            favorites = new ArrayList<AlarmEntity>(favorites);
//        }
//
//        return (ArrayList<AlarmEntity>) favorites;
//    }
//
//
//    /*public static int getAlarmlistposition(Context context,String appoinmentid) {
//        SharedPreferences settings;
//        int alarmpos = 0;
//        Log.e("ALARM_ARRAYALARM_ARRAY","ALARM_ARRAYALARM_ARRAY"+GlobalMethods.getStringValue(context, AppConstants.ALARM_ARRAY));
//        String jsonFavorites = GlobalMethods.getStringValue(context, AppConstants.ALARM_ARRAY);
//        if(!jsonFavorites.isEmpty())
//        {
//            Gson gson = new Gson();
//            AlarmEntity[] favoriteItems = gson.fromJson(jsonFavorites,AlarmEntity[].class);
//
//            List<AlarmEntity> favorites = Arrays.asList(favoriteItems);
//            favorites = new ArrayList<AlarmEntity>(favorites);
//
//            for(int i=0;i<favorites.size();i++)
//            {
//                if(favorites.get(i).getAppoinment_id().equalsIgnoreCase(appoinmentid))
//                {
//                    return i;
//                }
//            }
//        }
//
//        return alarmpos;
//    }*/
//
//
//    public static  void RefreshView(Context ctx) {
//        ((HomeScreen) ctx).refreshView();
//    }
//
//
//    public static String getTranslationText(Context ctx,String mTransText)
//    {
//
//        String[] api_text_current = ctx.getResources().getStringArray(R.array.api_text);
//        String[] api_text_eng = ctx.getResources().getStringArray(R.array.api_text_eng);
//
//        for (int l = 0; l < api_text_eng.length; l++) {
//            if (mTransText.equalsIgnoreCase(api_text_eng[l])) {
//                return api_text_current[l];
//            }
//        }
//        return mTransText;
////        if(mTransText.equalsIgnoreCase("Email and password does not match"))
////        {
////            mTransText = ctx.getString(R.string.email_password_match);
////        }else if(mTransText.equalsIgnoreCase("Account registered with facebook, Please login via facebook"))
////        {
////            mTransText = ctx.getString(R.string.acc_reg_fb);
////        }else if(mTransText.equalsIgnoreCase("Account registered with  Google, Please login via Google"))
////        {
////            mTransText = ctx.getString(R.string.acc_reg_google);
////        }else if(mTransText.equalsIgnoreCase("Account registered with Twitter, Please login via Twitter"))
////        {
////            mTransText = ctx.getString(R.string.acc_reg_twitter);
////        }else if(mTransText.equalsIgnoreCase("Email id already registered with Moos, Please try login"))
////        {
////            mTransText = ctx.getString(R.string.acc_reg_signup);
////        }else if(mTransText.equalsIgnoreCase("Reset password link sent to email address"))
////        {
////            mTransText = ctx.getString(R.string.rst_pwd_sucess);
////        }else if(mTransText.equalsIgnoreCase("Email not registered with Moos."))
////        {
////            mTransText = ctx.getString(R.string.email_not_registerd);
////        }else if(mTransText.equalsIgnoreCase("Not able to come on time"))
////        {
////            mTransText = ctx.getString(R.string.s1);
////        }else if(mTransText.equalsIgnoreCase("Changed my mind"))
////        {
////            mTransText = ctx.getString(R.string.s2);
////        }else if(mTransText.equalsIgnoreCase("Found a better barber"))
////        {
////            mTransText = ctx.getString(R.string.s3);
////        }else if(mTransText.equalsIgnoreCase("Low rating"))
////        {
////            mTransText = ctx.getString(R.string.s4);
////        }else if(mTransText.equalsIgnoreCase("Selected service is not available"))
////        {
////            mTransText = ctx.getString(R.string.s5);
////        } if(mTransText.equalsIgnoreCase("Prayer Time"))
////        {
////          mTransText = ctx.getString(R.string.prayer_time);
////        }
////
////        return mTransText;
//    }
//
//    public static String GetChoosedLanguageService(Context ctx,String service)
//    {
//        String[] servicelist_english = ctx.getResources().getStringArray(R.array.services_items_array_english);
//        String[] servicelist_others = ctx.getResources().getStringArray(R.array.services_items_array);
//        for(int l = 0;l<servicelist_english.length;l++)
//        {
//            if(service.equalsIgnoreCase(servicelist_english[l]))
//            {
//                return servicelist_others[l];
//            }
//        }
//        return service;
//    }

    public static String getLocalTime(String inputDate, String outputFormat) {
        Date dateobj;
        dateobj = null;
        String lv_dateFormateInLocalTimeZone = "";
        String lv_localTimeZone = "GMT";
        try {

            //create a new Date object using the UTC timezone
            SimpleDateFormat Inputformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Inputformat.setTimeZone(TimeZone.getTimeZone("UTC"));
            dateobj = Inputformat.parse(inputDate);
//            SimpleDateFormat displayFormat = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            SimpleDateFormat displayFormat = new SimpleDateFormat(outputFormat);

            //SimpleDateFormat lv_formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss z'('Z')'");

            displayFormat.setTimeZone(TimeZone.getDefault());
            lv_dateFormateInLocalTimeZone = displayFormat.format(dateobj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lv_dateFormateInLocalTimeZone;
    }
    public static boolean isPackageInstalled(String packagename, PackageManager packageManager) {
        try {
            packageManager.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }
    public static Dialog mDialog;
}