package com.pws.pateast.broadcast;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.pws.pateast.download.DownloadItemHelper;
import com.pws.pateast.utils.Preference;

import javax.inject.Inject;

import static com.pws.pateast.broadcast.SocketBroadcastReceiver.RESTART_SOCKET_RECEIVER;

public class BootCompletedIntentReceiver extends BroadcastReceiver {
    private static final String BOOT_COMPLETED = "android.intent.action.BOOT_COMPLETED";
    private static final String CONNECTIVITY_CHANGE = "android.net.conn.CONNECTIVITY_CHANGE";
    private static final String WIFI_STATE_CHANGED = "android.net.wifi.WIFI_STATE_CHANGED";
    private static final String TAG = BootCompletedIntentReceiver.class.getSimpleName();
    private Preference preference;

    @Override
    public void onReceive(Context context, Intent intent) {
        preference = Preference.get(context);
        if (preference.getUser() == null)
            return;
        if (BOOT_COMPLETED.equals(intent.getAction())) {
            context.sendBroadcast(new Intent(RESTART_SOCKET_RECEIVER));
            DownloadItemHelper.updateAssignmentDownloadStatus(context);
        } else if (CONNECTIVITY_CHANGE.equals(intent.getAction()) || WIFI_STATE_CHANGED.equals(intent.getAction())) {
            context.sendBroadcast(new Intent(RESTART_SOCKET_RECEIVER));
        }
    }
}