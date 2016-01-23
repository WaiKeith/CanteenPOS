package my.edu.chiawaikeith.canteenpos.Domains;

import java.io.Serializable;
import java.util.Date;

import my.edu.chiawaikeith.canteenpos.Database.Contracts.OrderCon;

/**
 * Created by win77 on 13/12/2015.
 */
public class Transactions extends OrderCon implements Serializable{
    public int transac_id,item_qty,acc_id;
    public double payment_amount,total_gst;
    public Date order_date_time;
    public String order_status;

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

    public int getAcc_id() {
        return acc_id;
    }

    public void setAcc_id(int acc_id) {
        this.acc_id = acc_id;
    }

    public double getPayment_amount() {
        return payment_amount;
    }

    public void setPayment_amount(double payment_amount) {
        this.payment_amount = payment_amount;
    }

    public double getTotal_gst() {
        return total_gst;
    }

    public void setTotal_gst(double total_gst) {
        this.total_gst = total_gst;
    }

    public Date getOrder_date_time() {
        return order_date_time;
    }

    public void setOrder_date_time(Date order_date_time) {
        this.order_date_time = order_date_time;
    }

    public String getOrder_status() {
        return order_status;
    }

    public void setOrder_status(String order_status) {
        this.order_status = order_status;
    }
}
