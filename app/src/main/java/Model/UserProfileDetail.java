package Model;

import java.io.Serializable;

/**
 * Created by Deeksha on 11-01-2016.
 */
public class UserProfileDetail implements Serializable {

    String title = "";
    String detail1 = "";
    String detail2 = "";
    String web_link = "";
    String exp_start = "";
    String exp_end = "";


    public String getWeb_link() {
        return web_link;
    }

    public void setWeb_link(String web_link) {
        this.web_link = web_link;
    }


    public UserProfileDetail(String title, String detail1, String detail2) {
        this.title = title;
        this.detail1 = detail1;
        this.detail2 = detail2;
    }

    public UserProfileDetail(String title, String detail1) {

        this.title = title;
        this.detail1 = detail1;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String text) {
        this.title = text;
    }

    public String getDetail1() {
        return detail1;
    }

    public void setDetail1(String detail1) {
        this.detail1 = detail1;
    }

    public void setDetail2(String detail2) {
        this.detail2 = detail2;
    }

    public String getDetail2() {
        return detail2;
    }


    public String getExp_start() {
        return exp_start;
    }

    public void setExp_start(String exp_start) {
        this.exp_start = exp_start;
    }

    public String getExp_end() {
        return exp_end;
    }

    public void setExp_end(String exp_end) {
        this.exp_end = exp_end;
    }


    //official information attributes


    public String designation = "";
    public String firstname = "";
    public String lastname = "";
    public String role = "";
    public String email = "";

}
