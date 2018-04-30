package com.winit.maidubai.dataobject;

/**
 * Created by Kishore.G on 5/26/2016.
 */
public class TrxDetailsDO extends BaseDO {

    public int trxDetailId                   = 0;
    public String trxCode                    = "";
    public int productId                     = 0;
    public int sequence                      = 0;
    public int discountRefId                 = 0;
    public double quantity                   = 0.0d;
    public double unitPrice                  = 0.0d;
    public double totalAmountWODiscount      = 0.0d;
    public double discountAmount             = 0.0d;
    public double orderAmount                = 0.0d;
    public int initialQuantity               = 0;
    public String UOM                        = "";

    public String reference                  = "";
    public String smallImage                 = "";
    public String mediumImage                = "";
    public String largeImage                 = "";
    public String originalImage              = "";
    public String productCode                = "";
    public String productName                = "";
    public String productDescription         = "";
    public String productALTDescription         = "";
//VAT DETAILS
    public int VAT=0;
    public double TotalVAT=0;
}
