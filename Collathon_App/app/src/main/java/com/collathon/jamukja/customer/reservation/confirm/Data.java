package com.collathon.jamukja.customer.reservation.confirm;

public class Data {
    private String id;
    private String shop;
    private String menuCount;
    private String time;
    private String table;

    public String getId(){ return id; }

    public void setId(String id) { this.id = id; }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getMenuCount(){ return menuCount; }

    public void setMenuCount(String menuCount) { this.menuCount = menuCount; }

    public String getTime(){ return time; }

    public void setTime(String time) {this.time = time; }

    public String getTable(){ return table; }

    public void setTable(String table) { this.table = table; }

    public Data(String id, String shop, String menuCount, String time){
        this.id = id;
        this.shop = shop;
        this.menuCount = menuCount;
        this.time = time;
    }

    public Data(String id, String shop, String menuCount, String time, String table){
        this.id = id;
        this.shop = shop;
        this.menuCount = menuCount;
        this.time = time;
        this.table = table;
    }
}
