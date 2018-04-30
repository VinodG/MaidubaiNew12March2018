package com.winit.maidubai.dataobject;

import java.util.ArrayList;

/**
 * Created by Girish Velivela on 25-08-2016.
 */
public class CategoryDO {

    public int categoryId;
    public String code = "";
    public String name = "";

    public final static int WATER_RANGE = 106;
    public final static int ACCESSORIES = 107;
    public ArrayList<ProductDO> arrProductDOs;

    public int ParentId;
    public int Level;
    public String URLName;
    public int Sequence;
    public boolean IsActive;
    public int CreatedBy;
    public String CreatedOn;
    public int ModifiedBy;
    public String ModifiedOn;
    public int Timestamp;
    public boolean ShowInMenu;

}
