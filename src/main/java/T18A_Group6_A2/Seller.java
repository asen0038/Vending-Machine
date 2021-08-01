package T18A_Group6_A2;

public class Seller {

    private final String username;
    private final String password;

    public Seller(String username, String password){
        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
