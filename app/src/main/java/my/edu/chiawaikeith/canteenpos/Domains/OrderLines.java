package my.edu.chiawaikeith.canteenpos.Domains;

import java.io.Serializable;

/**
 * Created by win77 on 13/12/2015.
 */
public class OrderLines implements Serializable{
    public int order_line_id,transac_id,item_qty,food_id;
    public double single_price,total_price,sub_total;

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

    public double getSub_total() {
        return sub_total;
    }

    public void setSub_total(double sub_total) {
        this.sub_total = sub_total;
    }
}
