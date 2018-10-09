package com.swcuriosity.memes.viewmodel;

public class admob {
    String adMobid;

    public String getAdMobid() {
        return adMobid;
    }

    public void setAdMobid(String adMobid) {
        this.adMobid = adMobid;
    }

    public String getBannerId() {
        return bannerId;
    }

    public void setBannerId(String bannerId) {
        this.bannerId = bannerId;
    }

    public String getIntersitialId() {
        return intersitialId;
    }

    public void setIntersitialId(String intersitialId) {
        this.intersitialId = intersitialId;
    }

    String bannerId;
    String intersitialId;

    public admob(String adMobid, String bannerId, String intersitialId) {

        this.adMobid=adMobid;
        this.bannerId = bannerId;
        this.intersitialId= intersitialId;
    }
}
