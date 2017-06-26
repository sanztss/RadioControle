package accessweb.com.br.radiocontrole.util;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.widget.Toast;

import accessweb.com.br.radiocontrole.R;

/**
 * Created by Des. Android on 26/06/2017.
 */

public class AlarmReceiver extends BroadcastReceiver {
    private String TAG = "AlarmLog";

    @Override
    public void onReceive(Context context, Intent intent) {

        //Toast.makeText(context, "Incoming Call Received", Toast.LENGTH_LONG).show();
        String message = intent.getExtras().getString("message");
        showNotification(context, message);

    }

    public void showNotification(Context context, String message) {
        Uri soundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mBuilder =   new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_bell_white)
                .setContentTitle("Rádio Controle") // title for notification
                .setStyle(new NotificationCompat.BigTextStyle().bigText(message))
                .setContentText(message)
                .setSound(soundUri)// message for notification
                .setAutoCancel(true); // clear notification after click
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(0, mBuilder.build());
    }
}