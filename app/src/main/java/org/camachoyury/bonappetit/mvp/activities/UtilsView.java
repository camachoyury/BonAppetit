package org.camachoyury.bonappetit.mvp.activities;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.view.View;

/**
 * Created by yury on 3/17/17.
 */

public class UtilsView {


    public static void showLongMessage(View view, Context context, int stringId){
        Snackbar.make(view, context.getString(stringId), Snackbar.LENGTH_LONG).show();

    }
}
