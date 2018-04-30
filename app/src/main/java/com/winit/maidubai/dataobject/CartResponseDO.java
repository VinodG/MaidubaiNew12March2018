package com.winit.maidubai.dataobject;

import java.util.ArrayList;

/**
 * Created by Ankur.A on 04-05-2016.
 */
public class CartResponseDO extends BaseDO{

    public int status       = 0;
    public String message   = "";
    public ArrayList<CartListDO> cartListDOs = new ArrayList<CartListDO>();
    public CartDetailsDO cartDetailsDO  = new CartDetailsDO();
}

