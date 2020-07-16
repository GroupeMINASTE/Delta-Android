package fr.zabricraft.delta.api;

import android.content.Context;

import org.json.JSONObject;

import java.io.Serializable;

import fr.zabricraft.delta.extensions.StringExtension;
import fr.zabricraft.delta.utils.Account;
import fr.zabricraft.delta.utils.Algorithm;
import fr.zabricraft.delta.utils.AlgorithmIcon;
import fr.zabricraft.delta.utils.AlgorithmParser;
import fr.zabricraft.delta.utils.Database;

public class APIAlgorithm implements Serializable {

    public Integer id;
    public String name;
    public APIUser owner;
    public String last_update;
    public String lines;
    public String notes;
    public AlgorithmIcon icon;
    public Boolean public_;

    public APIAlgorithm(Integer id, String name, String lines, String notes, AlgorithmIcon icon, Boolean public_) {
        this.id = id;
        this.name = name;
        this.lines = lines;
        this.notes = notes;
        this.icon = icon;
        this.public_ = public_;
    }

    public APIAlgorithm(JSONObject object) {
        try {
            this.id = object.has("id") ? object.getInt("id") : null;
            this.name = object.has("name") ? object.getString("name") : null;
            this.owner = object.has("owner") ? new APIUser(object.getJSONObject("owner")) : null;
            this.last_update = object.has("last_update") ? object.getString("last_update") : null;
            this.lines = object.has("lines") ? object.getString("lines") : null;
            this.notes = object.has("notes") ? object.getString("notes") : null;
            this.icon = object.has("icon") ? new AlgorithmIcon(object.getJSONObject("icon")) : null;
            this.public_ = object.has("public") ? object.getBoolean("public") : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public JSONObject toJSON() {
        JSONObject object = new JSONObject();
        try {
            if (id != null) {
                object.put("id", id);
            }
            if (name != null) {
                object.put("name", name);
            }
            if (lines != null) {
                object.put("lines", lines);
            }
            if (notes != null) {
                object.put("notes", notes);
            }
            if (icon != null) {
                object.put("icon", icon.toJSON());
            }
            if (public_ != null) {
                object.put("public", public_);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return object;
    }

    public Algorithm toAlgorithm() {
        return new AlgorithmParser(0, id, owner != null && Account.current.user != null && owner.id.equals(Account.current.user.id), name, StringExtension.toDate(last_update), icon, lines).execute();
    }

    public Algorithm saveToDatabase(Context context) {
        // Parse algorithm from downloaded data
        Algorithm algorithm = toAlgorithm();

        // Check if algorithm is already in database
        Algorithm fromDatabase = Database.getInstance(context).getAlgorithm(-1, id);
        if (fromDatabase != null) {
            // If yes, set the local id
            algorithm.setLocalId(fromDatabase.getLocalId());
        }

        // Update (or insert) this algorithm
        return Database.getInstance(context).updateAlgorithm(algorithm);
    }

    public void fetchMissingData(Context context, APIRequest.CompletionHandler completionHandler) {
        // Make a request to API
        new APIRequest("GET", "/algorithm/algorithm.php", context, completionHandler).with("id", id != null ? id : 0).execute();
    }

    public void upload(Context context, APIRequest.CompletionHandler completionHandler) {
        // Check if algorithm already has an ID
        if (id != null && !id.equals(0)) {
            // Update it
            new APIRequest("PUT", "/algorithm/algorithm.php", context, completionHandler).with("id", id).withBody(toJSON()).execute();
        } else {
            // Create it
            new APIRequest("POST", "/algorithm/algorithm.php", context, completionHandler).withBody(toJSON()).execute();
        }
    }

}
