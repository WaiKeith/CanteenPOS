package my.edu.chiawaikeith.canteenpos.Domains;


import java.io.Serializable;

/**
 * Created by win77 on 15/12/2015.
 */
public class Foods implements Serializable{

    public int food_id,stall_id;
    public String food_name,food_category,food_image_path;
    public double food_price,food_gst_price;

    public int getFood_id() {
        return food_id;
    }

    public void setFood_id(int food_id) {
        this.food_id = food_id;
    }

    public String getFood_name() {
        return food_name;
    }

    public void setFood_name(String food_name) {
        this.food_name = food_name;
    }

    public int getStall_id() {
        return stall_id;
    }

    public void setStall_id(int stall_id) {
        this.stall_id = stall_id;
    }

    public String getFood_category() {
        return food_category;
    }

    public void setFood_category(String food_category) {
        this.food_category = food_category;
    }

    public String getFood_image_path() {
        return food_image_path;
    }

    public void setFood_image_path(String food_image_path) {
        this.food_image_path = food_image_path;
    }

    public double getFood_price() {
        return food_price;
    }

    public void setFood_price(double food_price) {
        this.food_price = food_price;
    }

    public double getFood_gst_price() {
        return food_gst_price;
    }

    public void setFood_gst_price(double food_gst_price) {
        this.food_gst_price = food_gst_price;
    }
}