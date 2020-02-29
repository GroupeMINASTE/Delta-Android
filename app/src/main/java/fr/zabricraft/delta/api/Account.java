package fr.zabricraft.delta.api;

public class Account {

    private static Account instance = new Account();
    private String access_token;

    public static Account getInstance() {
        return instance;
    }

    public String getAccessToken() {
        return access_token;
    }

    public void setAccessToken(String access_token) {
        this.access_token = access_token;
    }

}
