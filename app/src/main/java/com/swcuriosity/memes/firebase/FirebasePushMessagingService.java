package com.swcuriosity.memes.firebase;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;


import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.swcuriosity.memes.R;
import com.swcuriosity.memes.main.BaseActivity;
import com.swcuriosity.memes.ui.CategoryActivity;
import com.swcuriosity.memes.ui.MemeTemplates;
import com.swcuriosity.memes.ui.SplashScreen;
import com.swcuriosity.memes.utils.AppConstants;
import com.swcuriosity.memes.utils.DialogManager;

import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Random;



public class FirebasePushMessagingService extends FirebaseMessagingService {

    private static String mTitle = "", mDetail = "", mImgUrl = "", categoryId = "", mIsexit = "", mCategoryName = "", mCounts = "", mName = "";

    String mLanguage;

    public void onMessageReceived(RemoteMessage remoteMessage) {

        System.out.println("onMessageReceived msg---" + remoteMessage);
        Map data = remoteMessage.getData();
        System.out.println("data msg---" + data);
         mDetail = (String) data.get("detail");
        mTitle = (String) data.get("title");
        mImgUrl = (String) data.get("imageUrl");
        mIsexit = (String) data.get("editable");
        mCategoryName = (String) data.get("name");
        categoryId = (String) data.get("categoryID");
//        System.out.println("intentStr msg---" + intentStr);

//        final String message = "Test Msg";
//        if (intentStr == null) {
//            try {
//                JSONObject json = new JSONObject( remoteMessage.getData());
////
//                mDetail = (String) json.getString("detail");
//                mTitle = (String) json.getString("title");
//                mImgUrl = (String) json.getString("imageUrl");
////                mNotificationId = (String) json.getString(AppConstants.notification_idTag);
////                mCounts = (String) json.getString(AppConstants.countsTag);
////                mName = (String) json.getString(AppConstants.name);
////                mMessage = (String) json.getString(AppConstants.msgTag);
//
//            } catch (Exception e) {
//
//            }
//
//        }
        if (mTitle == null) {
            mTitle = "";
        }
        if (mImgUrl == null) {
            mImgUrl = "";
        }
        if (mDetail != null && !mDetail.equalsIgnoreCase("")) {


                Boolean appisBackground = isAppIsInBackground(this);
                if (appisBackground == true) {


                    String m_strNotifiMsg = mDetail + " ";
//
//                    Notify(this,m_strNotifiMsg, categoryId);
                    Notify(this,mDetail, categoryId);



                } else {
//                    BaseActivity.mActivity.runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//
//                    });
                }


            }



    }

    private void Notify(Context ctx,String notificationMessage, String categoryId) {
        new generatePictureStyleNotification(this,this.getString(R.string.app_name), notificationMessage, mImgUrl,categoryId,mIsexit , mCategoryName).execute();
  /*

        System.out.println("Notify method call---");
        Intent intent = new Intent(this, SplashScreen.class);
//        intent.putExtra("pushNotification", pushFullMessage);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        final int not_nu = generateRandom();

        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
                Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this,
                not_nu, intent, PendingIntent.FLAG_ONE_SHOT);

        //SELECTED_TONE_URI

//        String m_selTone = (String) GlobalMethodslobalMethods.getValueFromPreference(this, GlobalMethods.STRING_PREFERENCE, AppConstants.SELECTED_TONE_URI);
//        Uri uri = Uri.parse(m_selTone);

//        m_ringNotificationToneUri = RingtoneManager.getRingtone(getActivity(), uri);
//        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        android.support.v4.app.NotificationCompat.Builder notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(
                this)
                .setSmallIcon(R.drawable.ic_color_lens_black_24dp) //Small Icon from drawable
//                .setLargeIcon(R.mipmap.ic_launcher) //Large Icon from drawable
                .setLargeIcon(BitmapFactory.decodeResource(ctx.getResources(),
                        R.mipmap.ic_launcher))
                .setContentTitle(this.getString(R.string.app_name))
                .setContentText(notificationMessage)
                .setAutoCancel(true)
                .setLocalOnly(true)
//                .setSound(uri, AudioManager.STREAM_NOTIFICATION)
//                .setStyle(
//                        new android.support.v4.app.NotificationCompat.BigPictureStyle().
//                                bigPicture(BitmapFactory.decodeResource(ctx.getResources(),
//                                R.mipmap.ic_launcher)))

//                .setStyle(new Notification.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(ctx.getResources(),
//                        R.mipmap.ic_launcher)))

                .setColor(ContextCompat.getColor(this, R.color.app_color))
                .setStyle(
                        new android.support.v4.app.NotificationCompat.BigPictureStyle().bigPicture(BitmapFactory.decodeResource(ctx.getResources(),
                                R.mipmap.ic_launcher)))

                .setContentIntent(pendingIntent);


        notificationBuilder.setPriority(Notification.PRIORITY_HIGH);
//        notificationBuilder.setDefaults(Notification.DEFAULT_SOUND);
//        notificationBuilder.setVibrate(new long[]{0, 100, 200, 300});

        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(Context.NOTIFICATION_SERVICE);
//        notificationBuilder.setSound(uri);
        notificationManager.notify(not_nu, notificationBuilder.build());
    */
    }

    public static int generateRandom() {
        Random random = new Random();
        return random.nextInt(9999 - 1000) + 1000;
    }

    private boolean isAppIsInBackground(Context context) {
        boolean isInBackground = true;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT_WATCH) {
            List<ActivityManager.RunningAppProcessInfo> runningProcesses = am
                    .getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo processInfo : runningProcesses) {
                if (processInfo.importance == ActivityManager.RunningAppProcessInfo
                        .IMPORTANCE_FOREGROUND) {
                    for (String activeProcess : processInfo.pkgList) {
                        if (activeProcess.equals(context.getPackageName())) {
                            isInBackground = false;
                        }
                    }
                }
            }
        } else {
            List<ActivityManager.RunningTaskInfo> taskInfo = am.getRunningTasks(1);
            ComponentName componentInfo = taskInfo.get(0).topActivity;
            if (componentInfo.getPackageName().equals(context.getPackageName())) {
                isInBackground = false;
            }
        }
        return isInBackground;
    }
    public class generatePictureStyleNotification extends AsyncTask<String, Void, Bitmap> {

        private Context mContext;
        private String title, message, imageUrl,categoryID,cName,cEditable;

        public generatePictureStyleNotification(Context context, String title, String message, String imageUrl,String category,String categoryEdt,String categoryName) {
            super();
            this.mContext = context;
            this.title = title;
            this.message = message;
            this.imageUrl = imageUrl;
            this.categoryID = category;
            this.cEditable = categoryEdt;
            this.cName = categoryName;

        }

        @Override
        protected Bitmap doInBackground(String... params) {

            InputStream in;
            try {
                URL url = new URL(this.imageUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                in = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(in);
                return myBitmap;
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @RequiresApi(api = Build.VERSION_CODES.O)
        @Override
        protected void onPostExecute(Bitmap result) {
            super.onPostExecute(result);

            Intent intent = new Intent(mContext, MemeTemplates.class);
            intent.putExtra("image_id", categoryID);
            intent.putExtra("ImageName",cName);
            intent.putExtra("editable",cEditable);
            intent.putExtra("notify","1");

            final int not_nu = generateRandom();
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP |
//                    Intent.FLAG_ACTIVITY_SINGLE_TOP);
//            PendingIntent pendingIntent = PendingIntent.getActivity(this,
//                    not_nu, intent, PendingIntent.FLAG_ONE_SHOT);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK
                    |Intent.FLAG_ACTIVITY_CLEAR_TASK);

            PendingIntent pendingIntent = PendingIntent.getActivity(mContext, not_nu, intent, PendingIntent.FLAG_ONE_SHOT);

            NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT <=Build.VERSION_CODES.M) {

                android.support.v4.app.NotificationCompat.Builder notificationBuilder = new android.support.v4.app.NotificationCompat.Builder(
                        mContext)
                        .setSmallIcon(R.mipmap.ic_launcher) //Small Icon from drawable
//                .setLargeIcon(R.mipmap.ic_launcher) //Large Icon from drawable
                        .setContentIntent(pendingIntent)
                        .setContentTitle(title)
                        .setContentText(message)
                        .setAutoCancel(true)
                        .setLocalOnly(true)
                        .setStyle(new NotificationCompat.BigPictureStyle().bigPicture(result))
                        .setColor(ContextCompat.getColor(mContext, R.color.app_color));

                notificationManager.notify(not_nu, notificationBuilder.build());

            }else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
               Notification notif = new Notification.Builder(mContext)
                       .setContentIntent(pendingIntent)
                       .setContentTitle(title)
                       .setContentText(message)
                       .setSmallIcon(R.mipmap.notification_icon)
                       .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),
                               R.mipmap.ic_launcher))
                       .setStyle(new Notification.BigPictureStyle().bigPicture(result))
                       .setChannelId("notify_001")
                       .build();
               notif.flags |= Notification.FLAG_AUTO_CANCEL;

               NotificationChannel channel = new NotificationChannel("notify_001",
                        "Software Curiosity",
                        NotificationManager.IMPORTANCE_DEFAULT);
                notificationManager.createNotificationChannel(channel);
               notificationManager.notify(1, notif);
            }else{
               Notification notif = new Notification.Builder(mContext)
                       .setContentIntent(pendingIntent)
                       .setContentTitle(title)
                       .setContentText(message)
                       .setSmallIcon(R.mipmap.notification_icon)
                       .setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),
                               R.mipmap.ic_launcher))
                       .setStyle(new Notification.BigPictureStyle().bigPicture(result))
                       .build();
               notif.flags |= Notification.FLAG_AUTO_CANCEL;
               notificationManager.notify(1, notif);

           }

        }
    }
}
