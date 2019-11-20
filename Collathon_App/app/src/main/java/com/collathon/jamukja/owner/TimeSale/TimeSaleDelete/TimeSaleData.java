package com.collathon.jamukja.owner.TimeSale.TimeSaleDelete;

public class TimeSaleData {
    private String menuName, saleTime;
    private int shopID, timesaleID, salePrice, menuCount;

    public String getMenuName() {
        return this.menuName ;
    }
    public void setMenuName(String menuName) {
        this.menuName = menuName ;
    }

    public String getSaleTime() {
        return this.saleTime ;
    }
    public void setSaleTime(String saleTime) {
        this.saleTime = saleTime ;
    }

    public int getShopID() {
        return this.shopID ;
    }
    public void setShopID(int shopID) {
        this.shopID = shopID ;
    }

    public int getTimesaleID() {
        return this.timesaleID ;
    }
    public void getTimesaleID(int timesaleID) {
        this.timesaleID = timesaleID ;
    }

    public int getSalePrice() {
        return this.salePrice ;
    }
    public void setSalePrice(int salePrice) {
        this.salePrice = salePrice ;
    }

    public int getMenuCount() {
        return this.menuCount ;
    }
    public void setMenuCount(int menuCount) {
        this.menuCount = menuCount ;
    }

}
