package com.collathon.jamukja.owner.TimeSale.TimeSaleAdd;

import android.text.Editable;
import android.text.TextWatcher;

public class TimeSaleData {
    private String menuName;
    private int shopID, menuPrice, salePrice;

    public String getMenuName() {
        return this.menuName ;
    }
    public void setMenuName(String menuName) {
        this.menuName = menuName ;
    }

    public int getShopID() {
        return this.shopID ;
    }
    public void setShopID(int shopID) {
        this.shopID = shopID ;
    }

    public int getMenuPrice() {
        return this.menuPrice ;
    }
    public void setMenuPrice(int menuPrice) {
        this.menuPrice = menuPrice ;
    }

    public int getSalePrice() {
        return this.salePrice ;
    }
    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice ;
    }

}
