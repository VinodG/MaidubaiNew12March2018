package com.winit.maidubai.dataobject;

/**
 * Created by Kishore.G on 5/2/2016.
 */
public class CartListDO extends BaseDO{
    public int shoppingCartId                      = 0;
    public String sessionId                        = "";
    public int userId                              = 0;
    public int productId                           = 0;
    public String productCode                      = "";
    public String productName                      = "";
    public String name                             = "";
    public String AltDescription                   = "";
    public int quantity                            = 0;
    public double unitPrice                         = 0.0d;
    public double totalPriceWODiscount              = 0.0d;
    public double discountAmount                    = 0.0d;
    public double totalPrice                        = 0.0d;
    public double discountedUnitPrice               = 0d;
    public String productColor                      = "";
    public String productImage                      = "";
    public String smallImage                        = "";
    public String mediumImage                       = "";
    public String largeImage                        = "";
    public String originalImage                     = "";
    public String modifiedOn                        = "";
    public String UOM                               = "";
//VAT
    public int  vatPerc                             = 0;
    public double vatAmount                           = 0;


}
