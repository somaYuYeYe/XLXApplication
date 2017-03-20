package wza.slx.com.xlxapplication.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.CallLog;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static android.R.attr.id;

/**
 * Created by homelink on 2017/3/20.
 */

public class Utils {

    private static final String[] PHONES_PROJECTION = new String[]{
//            Contacts.Phones.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Photo._ID, // 0
            ContactsContract.CommonDataKinds.Photo.DISPLAY_NAME, /// 1
            ContactsContract.CommonDataKinds.Phone.NUMBER, // 2
            ContactsContract.CommonDataKinds.Phone.PHOTO_ID, // 3
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID}; // 4

    private static final String[] SMS_PROJECTION = new String[]{
//            Conta,

            ContactsContract.CommonDataKinds.Phone.NUMBER,
            ContactsContract.Contacts.Photo.PHOTO_ID,
            ContactsContract.CommonDataKinds.Phone.CONTACT_ID};

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        if (TextUtils.isEmpty(str)) {
            return false;
        }
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 得到手机通讯录联系人信息
     **/
    public static void getPhoneContacts(Activity ac) {
        ContentResolver resolver = ac.getContentResolver();

        // 获取手机联系人
        Cursor phoneCursor = resolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                PHONES_PROJECTION, null, null, null);


        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                //得到手机号码
                String phoneNumber = phoneCursor.getString(2);
                //当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;

                //得到联系人名称
                String contactName = phoneCursor.getString(1);

                //得到联系人头像ID
                Long photoid = phoneCursor.getLong(3);

                //得到联系人ID
                Long contactid = phoneCursor.getLong(4);


                //得到联系人头像Bitamp
                Bitmap contactPhoto = null;

                //photoid 大于0 表示联系人有头像 如果没有给此人设置头像则给他一个默认的
                if (photoid > 0) {
                    Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, contactid);
                    InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(resolver, uri);
                    contactPhoto = BitmapFactory.decodeStream(input);
                } else {
//                    contactPhoto = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.contact_photo);
                }

//                mContactsName.add(contactName);
//                mContactsNumber.add(phoneNumber);
//                mContactsPhonto.add(contactPhoto);

                Log.i("info getPhoneContacts", "contactName " + contactName +
                        "  |  phoneNumber " + phoneNumber + "  |  contactid "
                        + contactid + "  |  photoid " + photoid);
            }

            phoneCursor.close();
        }
    }

    /**
     * 得到手机SIM卡联系人人信息
     **/
    public static void getSIMContacts(Activity ac) {
        ContentResolver resolver = ac.getContentResolver();
        // 获取Sims卡联系人
        Uri uri = Uri.parse("content://icc/adn");
        Cursor phoneCursor = resolver.query(uri, PHONES_PROJECTION, null, null,
                null);

        if (phoneCursor != null) {
            while (phoneCursor.moveToNext()) {

                // 得到手机号码
                String phoneNumber = phoneCursor.getString(1);
                // 当手机号码为空的或者为空字段 跳过当前循环
                if (TextUtils.isEmpty(phoneNumber))
                    continue;
                // 得到联系人名称
                String contactName = phoneCursor.getString(0);

                //Sim卡中没有联系人头像
//
//                mContactsName.add(contactName);
//                mContactsNumber.add(phoneNumber);
                Log.i("info getPhoneContacts", "contactName " + contactName + "  | phoneNumber " + phoneNumber);
            }

            phoneCursor.close();
        }
    }


    /**
     * 获取所有短信
     *
     * @return
     */
    public static String getSmsInPhone(Activity ac) {
        String[] ps = {Telephony.Sms._ID,
                Telephony.Sms.ADDRESS,
                Telephony.Sms.PERSON,
                Telephony.Sms.BODY,
                Telephony.Sms.DATE,
                Telephony.Sms.TYPE,
            };
        final String SMS_URI_ALL = "content://sms/";

        StringBuilder smsBuilder = new StringBuilder();

        try {
            Uri uri = Uri.parse(SMS_URI_ALL);
            String[] projection = new String[]{"_id", "address", "person",
                    "body", "date", "type"};
            Cursor cur = ac.getContentResolver().query(uri, projection, null,
                    null, "date desc"); // 获取手机内部短信

            if (cur.moveToFirst()) {
                int index_Address = cur.getColumnIndex("address");
                int index_Person = cur.getColumnIndex("person");
                int index_Body = cur.getColumnIndex("body");
                int index_Date = cur.getColumnIndex("date");
                int index_Type = cur.getColumnIndex("type");

                do {
                    String strAddress = cur.getString(index_Address);
                    int intPerson = cur.getInt(index_Person);
                    String strbody = cur.getString(index_Body);
                    long longDate = cur.getLong(index_Date);
                    int intType = cur.getInt(index_Type);

                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd hh:mm:ss");
                    Date d = new Date(longDate);
                    String strDate = dateFormat.format(d);

                    String strType = "";
                    if (intType == 1) {
                        strType = "接收";
                    } else if (intType == 2) {
                        strType = "发送";
                    } else {
                        strType = "null";
                    }

                    smsBuilder.append("[ ");
                    smsBuilder.append(strAddress + ", ");
                    smsBuilder.append(intPerson + ", ");
                    smsBuilder.append(strbody + ", ");
                    smsBuilder.append(strDate + ", ");
                    smsBuilder.append(strType);
                    smsBuilder.append(" ]\n\n");
                } while (cur.moveToNext());

                if (!cur.isClosed()) {
                    cur.close();
                    cur = null;
                }
            } else {
                smsBuilder.append("no result!");
            } // end if

            smsBuilder.append("getSmsInPhone has executed!");

        } catch (SQLiteException ex) {
            Log.d("SQLiteException in getSmsInPhone", ex.getMessage());
        }

        return smsBuilder.toString();
    }

    /**
     * 利用系统CallLog获取通话历史记录
     *
     * @return
     */
    public static String getCallHistoryList(Context context, ContentResolver cr) {

        Cursor cs;
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Log.i("info", "permission  === 没有权限");
            return " 没有授权 ";
        }
        cs = cr.query(CallLog.Calls.CONTENT_URI,//系统方式获取通讯录存储地址
                new String[]{
                        CallLog.Calls.CACHED_NAME,//姓名
                        CallLog.Calls.NUMBER,    //号码
                        CallLog.Calls.TYPE,  //呼入/呼出(2)/未接
                        CallLog.Calls.DATE,  //拨打时间
                        CallLog.Calls.DURATION   //通话时长
                }, null, null, CallLog.Calls.DEFAULT_SORT_ORDER);
        String callHistoryListStr = "";
        int i = 0;
        if (cs != null && cs.getCount() > 0) {
            for (cs.moveToFirst(); !cs.isAfterLast() & i < 50; cs.moveToNext()) {
                String str0 = cs.getString(0);
                String str1 = cs.getString(1);
                String str2 = cs.getString(2);
                String str3 = cs.getString(3);
                String str4 = cs.getString(4);

                String callName = cs.getString(0);
                String callNumber = cs.getString(1);
                //通话类型
                int callType = Integer.parseInt(cs.getString(2));
                String callTypeStr = "";
                switch (callType) {
                    case CallLog.Calls.INCOMING_TYPE:
                        callTypeStr = "呼入";
                        break;
                    case CallLog.Calls.OUTGOING_TYPE:
                        callTypeStr = "呼出";
                        break;
                    case CallLog.Calls.MISSED_TYPE:
                        callTypeStr = "未接";
                        break;
                }
                //拨打时间
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                Date callDate = new Date(Long.parseLong(cs.getString(3)));
                String callDateStr = sdf.format(callDate);
                //通话时长
                int callDuration = Integer.parseInt(cs.getString(4));
                int min = callDuration / 60;
                int sec = callDuration % 60;
                String callDurationStr = min + "分" + sec + "秒";
                String callOne = "类型：" + callTypeStr + ", 称呼：" + callName + ", 号码："
                        + callNumber + ", 通话时长：" + callDurationStr + ", 时间:" + callDateStr
                        + "\n---------------------\n";

                callHistoryListStr += callOne;
                i++;
            }
        }

        return callHistoryListStr;
    }

    public static void checkPermis(Activity ac, String permission) {
        String[] pers = {Manifest.permission.READ_CALL_LOG,
                Manifest.permission.READ_CONTACTS, Manifest.permission.READ_SMS};
        //版本判断
        if (Build.VERSION.SDK_INT >= 23) {
            //减少是否拥有权限
            int checkCallPhonePermission = ContextCompat.checkSelfPermission(ac, permission);
            if (checkCallPhonePermission != PackageManager.PERMISSION_GRANTED) {
                //弹出对话框接收权限
                ActivityCompat.requestPermissions(ac, new String[]{permission}, id);
                return;
            } else {
                Log.i("info check per", "have permiss = " + permission);
            }
        }
    }


}
