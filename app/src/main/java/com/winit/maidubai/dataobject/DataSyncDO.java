package com.winit.maidubai.dataobject;

import java.util.ArrayList;

/**
 * Created by sudheer.jampana on 8/29/2016.
 */
public class DataSyncDO extends BaseDO {
    public int status      = 0;
    public String message   = "";
    public String serverTime = "";
    public int objStatus = 0;

    public ArrayList<ProductDO> productDOs= new ArrayList<>();
    public ArrayList<ProductImagesDO> productImagesDOs= new ArrayList<>();
    public ArrayList<CustomerDO> customerDOs= new ArrayList<>();
    public ArrayList<GalleryDO> galleryDOs= new ArrayList<>();
    public ArrayList<HelpCategoryDO> helpCategoryDOs = new ArrayList<>();
    public ArrayList<HelpQuestionDO> helpQuestionDOs = new ArrayList<>();
    public ArrayList<HelpAnswerDO> helpAnswerDOs = new ArrayList<>();
    public ArrayList<PriceListDO> priceListDOs= new ArrayList<>();
    public ArrayList<PriceDO> priceDOs= new ArrayList<>();
    public ArrayList<ItemUOMDO> itemUOMDOs= new ArrayList<>();
    public ArrayList<ShoppingCartDO> shoppingCartDOs= new ArrayList<>();
    public ArrayList<ShoppingCartDetailsDO> shoppingCartDetailsDOs= new ArrayList<>();
    public ArrayList<ShoppingCartAddressDO> shoppingCartAddressDOs= new ArrayList<>();
    public ArrayList<TrxHeaderDO> trxHeaderDOs= new ArrayList<>();
    public ArrayList<TrxDetailsDO> trxDetailsDOs= new ArrayList<>();
    public ArrayList<TrxAddressDO> trxAddressDOs= new ArrayList<>();
    public ArrayList<CategoryDO> categoryDOs= new ArrayList<>();
    public ArrayList<ProductCategoryDO> productCategoryDOs= new ArrayList<>();
    public ArrayList<ProductAttribute> productAttributes = new ArrayList<>();
    public ArrayList<ProductAttributeDetailsDO> productAttributeDetailsDOs = new ArrayList<>();
    public ArrayList<OrderNumberDO> orderNumberDOs = new ArrayList<>();
    public ArrayList<AreaDO> areaDOs = new ArrayList<>();
    public ArrayList<DeliveryDay> deliveryDays = new ArrayList<>();
    public ArrayList<DeletedRecordDO> deletedRecordDOs = new ArrayList<>();
    public ArrayList<HydrationMeterSettingDO> hydrationMeterSettingDOs = new ArrayList<>();
    public ArrayList<HydrationMeterReadingDO> hydrationMeterReadingDOs = new ArrayList<>();
    public ArrayList<OrderCancelDO> orderCancelDOs = new ArrayList<>();
    public ArrayList<ProductOrderLimitDO> productOrderLimitDOs = new ArrayList<>();
    public ArrayList<ProductOrderLimitDO> productMinOrderLimitDOs = new ArrayList<>();
    public ArrayList<RecurringOrderDO> recurringOrderDOs = new ArrayList<>();
    public ArrayList<EmirateDO> emirateDOs = new ArrayList<>();
    public ArrayList<AreaRoutesDO> areaRoutesDOs = new ArrayList<>();
    public ArrayList<AreaRouteSupervisorsDO> areaRouteSupervisorsDOs = new ArrayList<>();

}
