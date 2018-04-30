package com.winit.maidubai.dataobject;

import java.util.ArrayList;

/**
 * Created by Jayasai on 6/17/2016.
 */
public class ProductDO extends BaseDO {
    public int ProductId;
    public int ParentId;
    public String SalesOrgCode;
    public String BaseUOM;
    public int ColorId;
    public boolean IsSellable;
    public boolean IsActive;
    public boolean IsOutOfStock;
    public String URLName;
    public int CreatedBy;
    public String CreatedOn;
    public int ModifiedBy;
    public String ModifiedOn;
    public int Timestamp;
    public boolean IsInventoryControlled;
    public float Weight;
    public String GroupLevel1;
    public String GroupLevel2;
    public String GroupLevel3;
    public String GroupLevel4;
    public String GroupLevel5;
    public String GroupLevel6;
    public String EAUOM;

    public String id = "";
    public int categoryId;
    public String code = "";
    public String name = "";
    public String description = "";
    public String AltDescription = "";
    public String pagerImage;
    public String listImage;
    public String gridImage;
    public String btlSize = "";
    public float price;
    public String UOM = "";
    public String Sequence = "";
    public int btlCount=1;
    public int minquantity=1;
    public boolean isChecked;
    public boolean isInCart;
    public ArrayList<ProductAttribute> arrProductAttributes;
    public ProductOrderLimitDO productOrderLimitDO;
///?Vat
    public int VAT=0;
    public double VATAmount=0;
}
