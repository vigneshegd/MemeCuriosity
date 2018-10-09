package com.swcuriosity.memes.utils;

import android.media.Ringtone;
import android.widget.ImageView;


import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.swcuriosity.memes.viewmodel.ImageUploadInfo;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;


public class AppConstants {
    public static final String shared_pref_name = "Moos_Barber_Prefernce";



    public static  ImageUploadInfo SelectedCategory=null;
    public static  String EDITABLE="1";
    public static  String CategoryName="";

    public static int SELECTED_DESIGN =11;
    public static  String SELECTED_URL = "";
    public static  String  currentImage="";
    public static int CURRENT_POS =11;
    public static GlideDrawable CurrrentGlideImage ;
    public static ImageView CurrrentImageViewGlide ;
    public static List<ImageUploadInfo> MemeList ;
    public static final  String DEVICE_ID = "DEVICE_ID";
    public static final String CATEGORY_LIST = "CATEGORY_LIST";
    public static final String TEMPLATES_LIST = "TEMPLATES_LIST";
    public static final String LOGGED_OUT = "LOGGED_OUT";

    public static final String Topic = "memes-curiosity";
//    public static final String Topic = "memes-curiosity_demo";

    public static final String AD_MOB = "AD_MOB";
    public static final String BANNER = "BANNER";
    public static final String INTER = "INTER";


    public static final String FULL_NAME = "FULL_NAME";
    //smaatapps server
    //public static final String BASE_URL = "http://smaatapps.com/moos/index.php/";

    //test server
    //public static final String BASE_URL = "http://50.87.171.216/moos/index.php/";

    //live server
//    public static final String BASE_URL = "http://themoosapp.com/moos/index.php/";



}
