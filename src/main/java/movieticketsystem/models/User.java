package movieticketsystem.models;

public class User {

    private String username;
    private String firstName;
    private String lastName;
    private boolean admin;


    public User(String username, String firstName, String lastName, boolean admin) {
        this.username = username;
        this.firstName = firstName;
        this.lastName = lastName;
        this.admin = admin;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public boolean getAdmin() {
        return admin;
    }

    public void setAdmin(boolean admin) {
        this.admin = admin;
    }

    @Override
    public String toString() {
        return "User{" + "username=" + username + ", firstName=" + firstName + ", lastName=" + lastName + ", isAdmin=" + admin + '}';
    }

}

