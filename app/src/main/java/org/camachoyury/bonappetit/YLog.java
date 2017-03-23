package org.camachoyury.bonappetit;

import android.util.Log;

/**
 * Created by yury on 3/23/17.
 */

public class YLog {

   public static boolean logEnable = BuildConfig.DEBUG;

    public static void debug(String tag, String message){
        if (logEnable)
            Log.d(tag, message);
    }

    public static void error(String tag, String message){
        if (logEnable)
            Log.e(tag, message);
    }

    public static void warning(String tag, String message){
        if (logEnable)
            Log.w(tag, message);
    }

    public static void info(String tag, String message){
        if (logEnable)
            Log.i(tag, message);
    }
}
