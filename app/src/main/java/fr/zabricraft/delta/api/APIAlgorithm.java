package fr.zabricraft.delta.api;

import android.content.Context;

import org.json.JSONObject;

import fr.zabricraft.delta.extensions.StringExtension;
import fr.zabricraft.delta.utils.Algorithm;
import fr.zabricraft.delta.utils.AlgorithmIcon;
import fr.zabricraft.delta.utils.AlgorithmParser;
import fr.zabricraft.delta.utils.Database;

public class APIAlgorithm {

    public Integer id;
    public String name;

    public String last_update;
    public String lines;
    public AlgorithmIcon icon;

    public APIAlgorithm(JSONObject object) {
        try {
            this.id = object.has("id") ? object.getInt("id") : null;
            this.name = object.has("name") ? object.getString("name") : null;

            this.last_update = object.has("last_update") ? object.getString("last_update") : null;
            this.lines = object.has("lines") ? object.getString("lines") : null;
            this.icon = object.has("icon") ? new AlgorithmIcon(object.getJSONObject("icon")) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Algorithm saveToDatabase(Context context) {
        // Parse algorithm from downloaded data
        Algorithm algorithm = new AlgorithmParser(0, id, false, name, StringExtension.toDate(last_update), icon, lines).execute();

        // Check if algorithm is already in database
        Algorithm fromDatabase = Database.getInstance(context).getAlgorithm(-1, id);
        if (fromDatabase != null) {
            // If yes, set the local id
            algorithm.setLocalId(fromDatabase.getLocalId());
        }

        // Update (or insert) this algorithm
        return Database.getInstance(context).updateAlgorithm(algorithm);
    }

}
