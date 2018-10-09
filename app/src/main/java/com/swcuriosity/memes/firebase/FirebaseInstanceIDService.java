package com.swcuriosity.memes.firebase;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.swcuriosity.memes.utils.AppConstants;
import com.swcuriosity.memes.utils.GlobalMethods;

import static com.swcuriosity.memes.utils.GlobalMethods.getStringValue;


public class FirebaseInstanceIDService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        GlobalMethods.storeValuetoPreference(this, GlobalMethods.STRING_PREFERENCE, AppConstants.DEVICE_ID, FirebaseInstanceId.getInstance().getToken());
        System.out.println("Device getToken()---" + getStringValue(this, AppConstants.DEVICE_ID) +
                "\nDevice getCreationTime()---" + FirebaseInstanceId.getInstance().getCreationTime() +
                "\nDevice getId()---" + FirebaseInstanceId.getInstance().getId());
    }

}