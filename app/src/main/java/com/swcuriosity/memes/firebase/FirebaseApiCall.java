package com.swcuriosity.memes.firebase;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.swcuriosity.memes.main.BaseActivity;
import com.swcuriosity.memes.ui.CategoryActivity;
import com.swcuriosity.memes.utils.AppConstants;
import com.swcuriosity.memes.utils.DialogManager;
import com.swcuriosity.memes.utils.GlobalMethods;
import com.swcuriosity.memes.viewmodel.ImageUploadInfo;

import java.util.HashMap;

/**
 * Created by Vicky N on 2/5/2018.
 */

public class FirebaseApiCall {
    public static FirebaseApiCall sInstance=new FirebaseApiCall();

    public static FirebaseApiCall getInstance() {
        return sInstance;
    }

    public void requestData(Query query, final BaseActivity baseActivity) {
//        DialogManager.showProgress(baseActivity);


        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                DialogManager.hideProgress();
                baseActivity.onRequestSuccess(snapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Hiding the progress dialog.
                DialogManager.hideProgress();

            }
        });

    }
}
