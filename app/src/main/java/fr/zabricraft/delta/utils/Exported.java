package fr.zabricraft.delta.utils;

import org.json.JSONObject;

public class Exported {

    public String json;
    public String sql;

    public Exported(JSONObject object) {
        try {
            this.json = object.has("json") ? object.getString("json") : null;
            this.sql = object.has("sql") ? object.getString("sql") : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String toString() {
        // Create a string builder
        StringBuilder string = new StringBuilder();

        // Check for JSON data
        if (json != null) {
            string.append("# Account data as JSON\n");
            string.append(json);
            string.append("\n\n");
        }

        // Check for SQL data
        if (sql != null) {
            string.append("# Account data as SQL\n");
            string.append(sql);
            string.append("\n\n");
        }

        // Return final string
        return string.toString();
    }
}
