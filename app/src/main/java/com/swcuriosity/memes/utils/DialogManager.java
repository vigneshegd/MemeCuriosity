package com.swcuriosity.memes.utils;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.swcuriosity.memes.R;
import com.swcuriosity.memes.main.BaseActivity;


public class DialogManager extends BaseActivity {

    static Dialog mDialog;
    static Dialog progress,progressNotify;

    static DialogFragment mDialogFrag;
    static Toast toast;


    public void showAlertDialog(Context mContext) {

    }

    public static void showToast(Context mContext, String mString) {
        if(toast!=null) {
            toast.cancel();
        }
        toast=  Toast.makeText(mContext, mString, Toast.LENGTH_SHORT);
        toast.show();
    }


    public static Dialog getDialog(Context mContext, int mLayout) {


        Dialog mDialog = new Dialog(mContext);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mDialog.setContentView(mLayout);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);

        return mDialog;
    }

    public static Dialog getDialog_without_dim(Context mContext, int mLayout) {


        Dialog mDialog = new Dialog(mContext);
        mDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams
                .SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        mDialog.setContentView(mLayout);
        mDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mDialog.setCancelable(true);
        mDialog.setCanceledOnTouchOutside(true);

        return mDialog;
    }

    private static Dialog getLoadingDialog(Context mContext) {

        mDialog = getDialog(mContext, R.layout.progress);
//        mDialog.setCancelable(false);
        mDialog.setCanceledOnTouchOutside(false);
        // mDialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,
        // WindowManager.LayoutParams.MATCH_PARENT);
        // mDialog.getWindow().setGravity(Gravity.CENTER);
        // ImageView _imagView = (ImageView)
        // mDialog.findViewById(R.id.imageView1);
        // handler = new MyHandler(context, _imagView);
        // _index = 1;
        // _timer = ne  w Timer();
        // TickClass tick = new TickClass(context);
        // _timer.schedule(tick, 100, 100);

        return mDialog;
    }

    public static void showProgressNotify(Context context) {
        hideProgressNotify();
        progressNotify = getLoadingDialog(context);

        progressNotify.show();

    }

    public static void showProgress(Context context) {
        hideProgress();
        progress = getLoadingDialog(context);

        progress.show();

    }

    public static void hideProgressNotify() {

        // if (_timer != null) {
        // _timer.cancel();
        // }
        if (progressNotify != null && progressNotify.isShowing()) {
            progressNotify.dismiss();
        }

    }

    public static void hideProgress(Context context) {

        // if (_timer != null) {
        // _timer.cancel();
        // }
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }

    }

    public static void hideProgress() {

        // if (_timer != null) {
        // _timer.cancel();
        // }
        if (progress != null && progress.isShowing()) {
            progress.dismiss();
        }

    }

    public static void showMsgPopup(final Context mContext, final String msg) {
        mDialog = getDialog(mContext, R.layout.popup_msg_layout);

        Button m_btnOk = (Button) mDialog.findViewById(R.id.submitbutton);
        TextView mTitte = (TextView) mDialog.findViewById(R.id.msg_txt);
        mTitte.setText(msg.toUpperCase());

        m_btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }
    public static void showAnswerPopup(final Context mContext, final String msg) {
        if(mDialog!=null){
            mDialog.dismiss();
        }
        mDialog = getDialog(mContext, R.layout.popup_answer_layout);
        Button m_btnOk = (Button) mDialog.findViewById(R.id.submitbutton);
        TextView manswer = (TextView) mDialog.findViewById(R.id.msg_txt);
        TextView mTitte = (TextView) mDialog.findViewById(R.id.titletextview);
        manswer.setText(msg);
        mTitte.setText("Answer is");
        m_btnOk.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();
            }
        });

        mDialog.show();
    }
    public static void showConfirmPopup(final Context mContext, final String msg) {
        mDialog = getDialog(mContext, R.layout.popup_exit);

        TextView mTitte = (TextView) mDialog.findViewById(R.id.title_text);
        TextView mYes = (TextView) mDialog.findViewById(R.id.yes_txt);
        TextView mNo = (TextView) mDialog.findViewById(R.id.no_txt);
        mTitte.setText(msg);

        mYes.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mActivity.finish();
                mDialog.dismiss();


            }
        });
        mNo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mDialog.dismiss();


            }
        });

        mDialog.show();
    }
    public static Ringtone m_ringNotificationToneUri = null;

//    public static void playTone(final Activity mContext) {
//
//        final String toneuri = (String) GlobalMethods.getValueFromPreference(mContext, GlobalMethods.STRING_PREFERENCE, AppConstants.SELECTED_TONE_URI);
//        try {
//            if (!(m_ringNotificationToneUri == null)) {
//                m_ringNotificationToneUri.stop();
//            }
//            if (toneuri != null) {
//
//                mContext.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        Uri uri = Uri.parse(toneuri);
//
//                        m_ringNotificationToneUri = RingtoneManager.getRingtone(mContext, uri);
//                        m_ringNotificationToneUri.play();
//                    }
//                });
//
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

//    public static void showMsgNotifiPopup(final Activity mContext, String msg, final String type) {
//        mDialog.dismiss();
//        mDialog = getDialog(mContext, R.layout.popup_msg_layout);
//
//        if (type.equals(AppConstants.BLOCKED_ACC)) {
//            mDialog.setCanceledOnTouchOutside(false);
//
//            mDialog.setCancelable(false);
//        } else {
//            playTone(mContext);
//            mDialog.setCanceledOnTouchOutside(true);
//            mDialog.setCancelable(true);
//        }
//        Button m_btnOk = (Button) mDialog.findViewById(R.id.submitbutton);
//        TextView mTitte = (TextView) mDialog.findViewById(R.id.msg_txt);
//        mTitte.setText(msg.toUpperCase());
//
//        m_btnOk.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//                if (type != null) {
//                    if (type.equals(AppConstants.BLOCKED_ACC)) {
//                        GlobalMethods.logoutFunc(mContext);
//                        Intent mIntent = new Intent(mContext, LoginScreen.class);
//                        mActivity.startActivity(mIntent);
//                        mActivity.overridePendingTransition(R.anim.slide_out_right,
//                                R.anim.slide_in_left);
//                        mActivity.finish();
//                    } else {
//                        ((HomeScreen) mContext).openPushnotification(type);
//
//                    }
//
//                }
//
//            }
//        });
//
//        mDialog.show();
//    }


//    public static void showMapPopup(final Context mContext, String Latitut, String longitude) {
//        mDialog = getDialog(mContext, R.layout.popup_map);
//        Button m_btnOk = (Button) mDialog.findViewById(R.id.submitbutton);
//        TextView mTitte = (TextView) mDialog.findViewById(R.id.msg_txt);
//
//        m_btnOk.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//
//            }
//        });
//        mDialog.show();
//    }


    private static Dialog mAlertDialog;



//    public static void showForgotPwdPopup(final Context context) {
//
//        mDialog = DialogManager.getDialog(context, R.layout.popup_forgot_pwd);
//
//        final EditText m_edtEmail = (EditText) mDialog.findViewById(R.id.email_edtxt);
//        Button m_btnSubmit = (Button) mDialog.findViewById(R.id.submit_btn);
//        ImageView m_imgClose = (ImageView) mDialog.findViewById(R.id.close_img);
//
//        m_btnSubmit.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//
//                String mEmailValid = m_edtEmail.getText().toString().trim();
//                if (mEmailValid.isEmpty()) {
//                    DialogManager.showToast(context, context.getString(R.string
//                            .error_empty_email));
//                } else if (!GlobalMethods.isEmailValid(mEmailValid)) {
//                    DialogManager.showToast(context, context.getString(R.string
//                            .error_invalid_email));
//                } else {
//                    mDialog.dismiss();
//                    DialogManager.showToast(context, context.getString(R.string.success_forgot_password));
//                }
//            }
//        });
//        m_imgClose.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                mDialog.dismiss();
//            }
//        });
//        mDialog.show();
//    }

    static Dialog mAlertDialogWithCallback;


}
