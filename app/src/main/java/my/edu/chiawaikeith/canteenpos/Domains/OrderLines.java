package my.edu.chiawaikeith.canteenpos.Domains;

import java.io.Serializable;

/**
 * Created by win77 on 13/12/2015.
 */
public class OrderLines implements Serializable{
    public int order_line_id,transac_id,item_qty,food_id;
    public double single_price,total_price;
    String food_name;

    public int getOrder_line_id() {
        return order_line_id;
    }

    public void setOrder_line_id(int order_line_id) {
        this.order_line_id = order_line_id;
    }

    public int getTransac_id() {
        return transac_id;
    }

    public void setTransac_id(int transac_id) {
        this.transac_id = transac_id;
    }

    public int getItem_qty() {
        return item_qty;
    }

    public void setItem_qty(int item_qty) {
        this.item_qty = item_qty;
    }

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public double getSingle_price() {
        return single_price;
    }

    public void setSingle_price(double single_price) {
        this.single_price = single_price;
    }

    public double getTotal_price() {
        return total_price;
    }

    public void setTotal_price(double total_price) {
        this.total_price = total_price;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }
}
