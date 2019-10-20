package org.remain4life.mvvm.helpers;

import android.content.Context;
import android.content.ContextWrapper;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class ConnectivityStatus extends ContextWrapper {

    public ConnectivityStatus(Context base) {
        super(base);
    }

    /**
     * Checks is Internet connected
     * @param context current context
     * @return true if connected
     */
    public static boolean isConnected(Context context){

        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo connection = null;
        if (manager != null) {
            connection = manager.getActiveNetworkInfo();
        }
        return connection != null && connection.isConnectedOrConnecting();
    }
}
