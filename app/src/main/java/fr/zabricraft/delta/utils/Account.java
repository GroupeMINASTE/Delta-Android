package fr.zabricraft.delta.utils;

import android.content.Context;
import android.preference.PreferenceManager;
import android.util.Log;

import org.json.JSONObject;

import androidx.annotation.Nullable;
import fr.zabricraft.delta.api.APIRequest;
import fr.zabricraft.delta.api.APIResponseStatus;
import fr.zabricraft.delta.api.APIUser;

public class Account {

    // Singleton instances
    public static final Account current = new Account();

    // Account properties
    public String access_token = null;
    public APIUser user = null;

    // Init with nothing
    private Account() {}

    // Init from JSON
    private Account(JSONObject object) {
        try {
            this.access_token = object.has("access_token") ? object.getString("access_token") : null;
            this.user = object.has("user") ? new APIUser(object.getJSONObject("user")) : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Login from preferences
    public void login(Context context) {
        // Check token from preferences
        String access_token = PreferenceManager.getDefaultSharedPreferences(context).getString("access_token", null);
        if (access_token != null) {
            // Login to account
            Account.current.login(access_token, context, new CompletionHandler() {
                @Override
                public void completionHandler(APIResponseStatus status) {
                    // Check status
                    Log.d("DELTA", status.toString());
                }
            });
        }
    }

    // Login with an access token
    public void login(String access_token, Context context, CompletionHandler completionHandler) {
        // Save token
        this.access_token = access_token;

        // Fetch api with token
        new APIRequest("GET", "/auth/account.php", context, new APIRequest.CompletionHandler() {
            @Override
            public void completionHandler(@Nullable Object object, APIResponseStatus status) {
                // Check response validity
                if (object instanceof JSONObject && status == APIResponseStatus.ok) {
                    // Convert it to Account
                    Account account = new Account((JSONObject) object);

                    // Store token and user
                    Account.this.access_token = account.access_token;
                    Account.this.user = account.user;
                }

                // Call completion handler
                completionHandler.completionHandler(status);
            }
        });
    }

    // Login with credentials
    public void login(String username, String password, Context context, CompletionHandler completionHandler) {
        // Fetch api with credentials
        new APIRequest("GET", "/auth/access_token.php", context, new APIRequest.CompletionHandler() {
            @Override
            public void completionHandler(@Nullable Object object, APIResponseStatus status) {
                // Check response validity
                if (object instanceof JSONObject && status == APIResponseStatus.created) {
                    // Convert it to Account
                    Account account = new Account((JSONObject) object);

                    // Store token and user
                    Account.this.access_token = account.access_token;
                    Account.this.user = account.user;
                }

                // Call completion handler
                completionHandler.completionHandler(status);
            }
        }).withCredentials(username, password).execute();
    }

    // Register
    public void register(String name, String username, String password, Context context, CompletionHandler completionHandler) {
        try {
            // Construct body
            JSONObject body = new JSONObject();
            body.put("name", name);
            body.put("username", username);
            body.put("password", password);

            // Fetch api with data
            new APIRequest("POST", "/auth/account.php", context, new APIRequest.CompletionHandler() {
                @Override
                public void completionHandler(@Nullable Object object, APIResponseStatus status) {
                    // Check response validity
                    if (object instanceof JSONObject && status == APIResponseStatus.created) {
                        // Convert it to Account
                        Account account = new Account((JSONObject) object);

                        // Store token and user
                        Account.this.access_token = account.access_token;
                        Account.this.user = account.user;
                    }

                    // Call completion handler
                    completionHandler.completionHandler(status);
                }
            }).withBody(body).execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Logout
    public void logout(Context context, CompletionHandler completionHandler) {
        // Delete token from API
        new APIRequest("DELETE", "/auth/access_token.php", context, new APIRequest.CompletionHandler() {
            @Override
            public void completionHandler(@Nullable Object object, APIResponseStatus status) {
                // Check response validity
                if (object != null && status == APIResponseStatus.ok) {
                    // Clear from preferences
                    PreferenceManager.getDefaultSharedPreferences(context).edit().remove("access_token").apply();
                }

                // Call completion handler
                completionHandler.completionHandler(status);
            }
        }).execute();
    }

    // Account completion handler
    interface CompletionHandler {

        void completionHandler(APIResponseStatus status);

    }

}
