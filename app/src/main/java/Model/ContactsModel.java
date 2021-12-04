package Model;

import java.io.Serializable;

public class ContactsModel implements Serializable {

    int value = 0; /* 0 -> checkbox disable, 1 -> checkbox enable */
    String email = "";
    String image = "";
    String secName = "";
    public String firstname = "";
    public String lastname = "";
    public String status = "";
    public String message = "";
    public String userId = "";
    public String id = "";
    public String name = "";
    public String type = "";
    public String admin = "";
    public String deleted = "";
    public String createdAt = "";
    public String lastModifiedAt = "";
    public String icon = "";
    public String avatar = "";
    public String displayName = "";


    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getLastname() {
        return lastname;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;

    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setSecName(String secName) {
        // TODO Auto-generated method stub
        this.secName = secName;

    }

    public String getsecName() {
        // TODO Auto-generated method stub
        return secName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}