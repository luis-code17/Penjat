import java.io.Serializable;

public class Users implements Serializable {
    private String name;
    private String user;
    private String password;
    private boolean admin;
    private int punts;

    public Users(String name, String user, String password, boolean admin, int punts) {
        this.name = name;
        this.user = user;
        this.password = password;
        this.admin = admin;
        this.punts = punts;
    }

    //getters
    public String getName() {
        return name;
    }
    public String getUser() {
        return user;
    }
    public String getPassword() {
        return password;
    }
    public boolean getAdmin() {
        return admin;
    }
    public int getPunts() {
        return punts;
    }

    //setters
    public void setName(String name) {
        this.name = name;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public void setAdmin(boolean admin) {
        this.admin = admin;
    }
    public void setPunts(int punts) {
        this.punts = punts;
    }

    public void addPunts(int punts) {
        this.punts += punts;
    }

    public String toString() {
        return "Name: " + name + ", User: " + user + ", Password: " + password + ", Admin: " + admin + ", Punts: " + punts;
    }




}
