package fr.zabricraft.delta.api;

import android.content.Context;

import org.json.JSONObject;

import java.io.Serializable;

import fr.zabricraft.delta.extensions.StringExtension;
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

    public APIAlgorithm(JSONObject object) {
        try {
            this.id = object.has("id") ? object.getInt("id") : null;
            this.name = object.has("name") ? object.getString("name") : null;
            this.owner = object.has("owner") ? new APIUser(object.getJSONObject("owner")) : null;
            this.last_update = object.has("last_update") ? object.getString("last_update") : null;
            this.lines = object.has("lines") ? object.getString("lines") : null;
            this.notes = object.has("notes") ? object.getString("notes") : null;
            this.icon = object.has("icon") ? new AlgorithmIcon(object.getJSONObject("icon")) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Algorithm toAlgorithm() {
        return new AlgorithmParser(0, id, false, name, StringExtension.toDate(last_update), icon, lines).execute();
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

    public void fetchMissingData(APIRequest.CompletionHandler completionHandler) {
        // Make a request to API
        new APIRequest("GET", "/algorithm/algorithm.php", completionHandler).with("id", id != null ? id : 0).execute();
    }

}
