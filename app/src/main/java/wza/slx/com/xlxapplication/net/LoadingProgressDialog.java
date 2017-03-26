package wza.slx.com.xlxapplication.net;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;

/**
 * Created by homelink on 2017/3/26.
 */

public class LoadingProgressDialog {

    private static Context mContext;
    private static ProgressDialog instance;

    public static void createDialog(Context context, boolean cancelAble) {
        mContext = context;
        instance = new ProgressDialog(context);
        instance.setCancelable(cancelAble);
    }

    public static ProgressDialog getDialog() {
        return instance;
    }

    public static boolean isShowing() {
        if (instance != null) {
            return instance.isShowing();
        }
        return false;
    }

    public static void dismiss() {
        if (instance != null) {
            instance.dismiss();
        }
    }

    public static void show(Context context) {
        if (context instanceof Activity) {
            mContext = (Activity) context;
            Activity ac = (Activity) context;
            if (ac.isFinishing()) {
                return;
            }

            if (instance == null) {
                createDialog(context, false);
            } else {
                Context dialogContext = instance.getContext();
                if (dialogContext instanceof Activity && ((Activity) dialogContext).isFinishing()) {
                    instance = null;
                    createDialog(context, false);
                } else if (!isShowing()) {
                    instance.show();
                }
            }
        }
    }

}
