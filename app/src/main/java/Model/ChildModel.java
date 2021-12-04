package Model;

public class ChildModel {

    public ChildModel(String name, String email, boolean isChecked) {
        this.name = name;
        this.isChecked = isChecked;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String firstname) {
        this.name = firstname;
    }

    String name = "";

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    String email = "";


    public boolean isChecked() {
        return isChecked;
    }

    public void setIsChecked(boolean isChecked) {
        this.isChecked = isChecked;
    }

    boolean isChecked = false;
}
