package com.example.dell.v_clock.object;

/**
 * Created by andy on 7/8/17.
 */

public class GuestHistory {


    private String guestName;
    private String arriveTime;
    private String base64Pic;

    public GuestHistory(String guestName, String arriveTime, String base64Pic) {
        this.guestName = guestName;
        this.arriveTime = arriveTime;
        this.base64Pic = base64Pic;
    }

    public String getGuestName() {
        return guestName;
    }

    public String getArriveTime() {
        return arriveTime;
    }

    public String getBase64Pic() {
        return this.base64Pic;
    }
}
