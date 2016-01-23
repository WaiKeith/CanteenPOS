package my.edu.chiawaikeith.canteenpos.Domains;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by win77 on 17/12/2015.
 */
public class Reminders implements Serializable {
    public int reminder_id;
    public int acc_id;
    public Date reminder_date;
    public String reminder_time;
    public String reminder_desc;

    public String getReminder_title() {
        return reminder_title;
    }

    public void setReminder_title(String reminder_title) {
        this.reminder_title = reminder_title;
    }

    public String reminder_title;

    public int getReminder_id() {
        return reminder_id;
    }

    public void setReminder_id(int reminder_id) {
        this.reminder_id = reminder_id;
    }

    public int getAcc_id() {
        return acc_id;
    }

    public void setAcc_id(int acc_id) {
        this.acc_id = acc_id;
    }

    public Date getReminder_date() {
        return reminder_date;
    }

    public void setReminder_date(Date reminder_date) {
        this.reminder_date = reminder_date;
    }

    public String getReminder_time() {
        return reminder_time;
    }

    public void setReminder_time(String reminder_time) {
        this.reminder_time = reminder_time;
    }

    public String getReminder_desc() {
        return reminder_desc;
    }

    public void setReminder_desc(String reminder_desc) {
        this.reminder_desc = reminder_desc;
    }
}
