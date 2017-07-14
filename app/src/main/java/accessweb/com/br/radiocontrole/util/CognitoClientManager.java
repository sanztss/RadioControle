package accessweb.com.br.radiocontrole.util;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.regions.Regions;

import java.util.HashMap;
import java.util.Map;

import accessweb.com.br.radiocontrole.activity.MainActivity;

/**
 * Created by Des. Android on 03/07/2017.
 */

public class CognitoClientManager {

    private static CognitoCachingCredentialsProvider provider;

    /**
     * Initializes the CognitoClient. This must be called before
     * getCredentials().
     *
     * @param context a context of the app
     */
    public static void init(Context context) {
        if (provider == null){
            provider = new CognitoCachingCredentialsProvider(context, "us-east-1:9434eddb-ce1a-4204-bb3b-4a7f88b97b17", Regions.US_EAST_1);
            //provider.getCredentials();
            Log.i("CognitoClientManager", "provider.getIdentityId()");
        }
    }

    /**
     * Sets the logins map used to authenticated with Amazon Cognito. Note: You
     * should manually call refresh on the credentials provider after adding
     * logins to the provider as your Identity Id may have changed.
     *
     * @param providerName the name of 3rd identity provider
     * @param token openId token
     */
    public static void addLogins(String providerName, String token) {
        checkCredentialAvailability();
        Map<String, String> logins = provider.getLogins();
        if (logins == null) {
            logins = new HashMap<String, String>();
        }
        logins.put(providerName, token);
        provider.setLogins(logins);

        Log.w("logins", "" + logins);
    }

    /**
     * Clears the logins map used to authenticated with Amazon Cognito. Note:
     * You should manually call refresh on the credentials provider after
     * clearing logins to the provider as your Identity Id may have changed.
     */
    public static void clearLogins() {
        checkCredentialAvailability();
        provider.setLogins(new HashMap<String, String>());
    }

    /**
     * Forces this credentials provider to refresh its credentials.
     */

    public static void refresh() {
        checkCredentialAvailability();
        provider.refresh();
    }

    /**
     * @return whether the current user is authenticated.
     */
    public static boolean isAuthenticated() {
        checkCredentialAvailability();
        Map<String, String> logins = provider.getLogins();
        if (logins == null || logins.isEmpty()) {
            return false;
        }
        return true;
    }

    private static void checkCredentialAvailability() {
        if (provider == null) {
            throw new IllegalStateException("provider not initialized yet");
        }
    }

    /**
     * Gets the singleton instance of the CredentialsProvider. init() must be
     * call prior to this.
     *
     * @return an instance of CognitoCachingCredentialsProvider
     */
    public static CognitoCachingCredentialsProvider getCredentials() {
        checkCredentialAvailability();
        return provider;
    }
}