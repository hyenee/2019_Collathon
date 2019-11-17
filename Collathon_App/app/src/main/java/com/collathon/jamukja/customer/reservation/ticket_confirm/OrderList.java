package com.collathon.jamukja.customer.reservation.ticket_confirm;

public class OrderList{
    String order_id;
    String order_menu;
    String order_count;

    public OrderList(String order_id, String order_menu, String order_count){
        this.order_id = order_id;
        this.order_menu = order_menu;
        this.order_count = order_count;
    }
}