package fr.zabricraft.delta.api;

import org.json.JSONObject;

import java.io.Serializable;

public class APIUser implements Serializable {

    public Long id;
    public String name;
    public String username;

    public APIUser(JSONObject object) {
        try {
            this.id = object.has("id") ? object.getLong("id") : null;
            this.name = object.has("name") ? object.getString("name") : null;
            this.username = object.has("username") ? object.getString("username") : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
