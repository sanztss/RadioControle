package accessweb.com.br.radiocontrole.util;

import android.content.Context;

import com.amazonaws.mobileconnectors.cognito.CognitoSyncManager;
import com.amazonaws.regions.Regions;
import com.amazonaws.mobileconnectors.cognito.Dataset;

/**
 * Created by Des. Android on 13/07/2017.
 */

public class CognitoSyncClientManager {

    private static CognitoSyncManager client;

    /**
     * Initializes the CognitoSyncClient. This must be called before calling any
     * methods in this class.
     *
     * @param context a context of the app
     */
    public static void init(Context context) {
        if (client == null) {
            /**
             * Make sure to use context.getApplicationContext() here so that the
             * static client will not hold a context of the activity
             */
            client = new CognitoSyncManager(context.getApplicationContext(), Regions.US_EAST_1,
                    CognitoClientManager.getCredentials());
        }
    }

    /**
     * Wipes all user data cached locally, including identity id, session
     * credentials, dataset metadata, and all records. Any data that hasn't been
     * synced will be lost. This method should be called after logging out.
     */
    public static void wipeData() {
        checkSyncAvailability();
        client.wipeData();
    }

    /**
     * Opens or creates a dataset. If the dataset doesn't exist, an empty one
     * with the given name will be created. Otherwise, dataset is loaded from
     * local storage. If a dataset is marked as deleted but hasn't been deleted
     * on remote via refreshDatasetMetadata(), it will throw
     * IllegalStateException.
     *
     * @param datasetName a name of your dataset
     * @return an instance of dataset loaded from local storage
     */
    public static Dataset openOrCreateDataset(String datasetName) {
        checkSyncAvailability();
        return client.openOrCreateDataset(datasetName);
    }

    private static void checkSyncAvailability() {
        if (client == null) {
            throw new IllegalStateException("Sync client not initialized yet");
        }
    }
}
