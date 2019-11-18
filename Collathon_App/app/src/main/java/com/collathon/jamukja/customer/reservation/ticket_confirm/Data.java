package com.collathon.jamukja.customer.reservation.ticket_confirm;

public class Data {
    private String id;
    private String shop;
    private String menu;
    private String count;
    private String time;
    private OrderList orderList;

    public String getId(){ return id; }

    public void setId(String id) { this.id = id; }

    public String getShop() {
        return shop;
    }

    public void setShop(String shop) {
        this.shop = shop;
    }

    public String getMenu(){ return menu; }

    public void setMenu(String menu) { this.menu = menu; }

    public String getCount() { return count; }

    public void setCount(String count) { this.count = count; }

    public String getTime(){ return time; }

    public void setTime(String time) {this.time = time; }

    public OrderList getOrderList(){ return orderList; }

    public void setOrderList(OrderList orderList) {this.orderList = orderList; }

    public Data(String id, String menu, String count){
        this.id = id;
        this.menu = menu;
        this.count = count;
    }

    public Data(String id, String shop, String menu, String count, String time){
        this.id = id;
        this.shop = shop;
        this.menu = menu;
        this.count = count;
        this.time = time;
    }
}
