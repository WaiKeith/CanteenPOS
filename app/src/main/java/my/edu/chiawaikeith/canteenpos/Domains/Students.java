package my.edu.chiawaikeith.canteenpos.Domains;


import java.io.Serializable;

/**
 * Created by win77 on 15/12/2015.
 */
public class Students implements Serializable{

    public String stud_id;
    public String stud_name;
    public String stud_ic_no;
    public String stud_DOB;
    public String stud_contact_no;
    public String stud_course;

    public String getStud_course() {
        return stud_course;
    }

    public void setStud_course(String stud_course) {
        this.stud_course = stud_course;
    }

    public String stud_email;
    public String stud_address;

    public String getStud_id() {
        return stud_id;
    }

    public void setStud_id(String stud_id) {
        this.stud_id = stud_id;
    }

    public String getStud_name() {
        return stud_name;
    }

    public void setStud_name(String stud_name) {
        this.stud_name = stud_name;
    }

    public String getStud_ic_no() {
        return stud_ic_no;
    }

    public void setStud_ic_no(String stud_ic_no) {
        this.stud_ic_no = stud_ic_no;
    }

    public String getStud_DOB() {
        return stud_DOB;
    }

    public void setStud_DOB(String stud_DOB) {
        this.stud_DOB = stud_DOB;
    }

    public String getStud_contact_no() {
        return stud_contact_no;
    }

    public void setStud_contact_no(String stud_contact_no) {
        this.stud_contact_no = stud_contact_no;
    }

    public String getStud_email() {
        return stud_email;
    }

    public void setStud_email(String stud_email) {
        this.stud_email = stud_email;
    }

    public String getStud_address() {
        return stud_address;
    }

    public void setStud_address(String stud_address) {
        this.stud_address = stud_address;
    }
}