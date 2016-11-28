package models;

/**
 * Created by manasb on 12-11-2016.
 */
public class User {

    private String username;

    public User(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
