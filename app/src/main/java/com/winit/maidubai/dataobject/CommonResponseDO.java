package com.winit.maidubai.dataobject;

public class CommonResponseDO extends BaseDO {
    public String message               = "";
    public String serverTime            = "";
    public int status                   = 0;
    public int errorcode                = 0;
    //only for GiftCard Balance
    public double balance               = 0.0d;
}
