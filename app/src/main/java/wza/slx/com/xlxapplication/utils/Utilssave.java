package wza.slx.com.xlxapplication.utils;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.media.ExifInterface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import wza.slx.com.xlxapplication.base.App;


/**
 */

public class Utilssave {
    private static final String MINSU = "picpath";


//    /**
//     * 调用拨号
//     *
//     * @param number
//     */
//    public static void callPhone(Activity context, String number) {
//        //用intent启动拨打电话
//        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + number));
//        context.startActivity(intent);
//    }

    /**
     * 金额格式化处理
     *
     * @param price
     * @return
     */

    public static String getPriceFormat(int price) {

        DecimalFormat myformat = new DecimalFormat();
        myformat.applyPattern("##,##0.00");

        double priceD = price / 100.00;
        return myformat.format(priceD);
    }

    public static String getPriceInt(int price) {

        int priceD = price / 100;
        return String.valueOf(priceD);
    }


//    /**
//     * 呼叫客服
//     *
//     * @param context
//     */
//    public static void callService(final Context context) {
//        String tel = SharedPreferencesUtil.getOther(context, "telphone");
//        if (TextUtils.isEmpty(tel)) {
//            tel = MinsuConfig.servicePhone;
//        }
//        callPhone(context, tel);
//    }


    public static void hideSoftInput(Activity activity) {

        InputMethodManager imm =
                (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
//        imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), 0);
        if (activity.getWindow().getAttributes().softInputMode !=
                WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN) {
            if (activity.getCurrentFocus() != null)
                imm.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }

    /**
     * 是否显示软件盘
     * 这个方法需要在UI绘制完成之后调用，因为这个方法中调用的view.getheight（UI绘制完成前此方法返回0）
     * 更好的建议是 {@link SoftKeyBoardListener}
     * @return
     */
    public static  boolean isSoftInputShown(Activity activity) {
        return getSupportSoftInputHeight(activity) != 0;
    }

    /**
     * 获取软件盘的高度
     *
     * @return
     */
    private static int getSupportSoftInputHeight(Activity mActivity) {
        Rect r = new Rect();
        /**
         * decorView是window中的最顶层view，可以从window中通过getDecorView获取到decorView。
         * 通过decorView获取到程序显示的区域，包括标题栏，但不包括状态栏。
         */
        mActivity.getWindow().getDecorView().getWindowVisibleDisplayFrame(r);
        LogUtil.i("info"," getWindowVisibleDisplayFrame  r.bottom " + r.bottom);
        //获取屏幕的高度
        int screenHeight = mActivity.getWindow().getDecorView().getRootView().getHeight();
        LogUtil.i("info",".getDecorView().getRootView().getHeight() " + screenHeight);

        //计算软件盘的高度
        int softInputHeight = screenHeight - r.bottom;
        LogUtil.i("info","before  softInputHeight = " +softInputHeight);
        /**
         * 某些Android版本下，没有显示软键盘时减出来的高度总是144，而不是零，
         * 这是因为高度是包括了虚拟按键栏的(例如华为系列)，所以在API Level高于20时，
         * 我们需要减去底部虚拟按键栏的高度（如果有的话）
         */
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int displayHeight = metrics.heightPixels;
        if(displayHeight != screenHeight){
            softInputHeight = softInputHeight - getSoftButtonsBarHeight(mActivity);
        }
//        if (Build.VERSION.SDK_INT >= 20) {
//            // When SDK Level >= 20 (Android L), the softInputHeight will contain the height of softButtonsBar (if has)
//            softInputHeight = softInputHeight - getSoftButtonsBarHeight(mActivity);
//        }


        if (softInputHeight < 0) {
            LogUtil.i("info ","EmotionKeyboard--Warning: value of softInputHeight is below zero!");
        }
        //存一份到本地
        if (softInputHeight > 0) {
//            sp.edit().putInt(SHARE_PREFERENCE_SOFT_INPUT_HEIGHT, softInputHeight).apply();
            LogUtil.i("wz", "softInputHeight = " + softInputHeight);
        }
        return softInputHeight;
    }

    /**
     * 底部虚拟按键栏的高度
     *
     * @return
     */
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    private static int getSoftButtonsBarHeight(Activity mActivity) {
        DisplayMetrics metrics = new DisplayMetrics();
        //这个方法获取可能不是真实屏幕的高度
        mActivity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int usableHeight = metrics.heightPixels;

        LogUtil.i("info" , " getDefaultDisplay().getMetrics = " + usableHeight);

        //获取当前屏幕的真实高度
        mActivity.getWindowManager().getDefaultDisplay().getRealMetrics(metrics);

        int realHeight = metrics.heightPixels;

        LogUtil.i("info" , " getWindowManager().getDefaultDisplay().getRealMetrics( = " + realHeight);

        LogUtil.i("info" , " getRealMetrics ---- .getMetrics  ====== " + (realHeight - usableHeight) );

        if (realHeight > usableHeight) {
            return realHeight - usableHeight;
        } else {
            return 0;
        }
    }


    /**
     * 保存图片
     *
     * @return
     */
    public static String savePic(Bitmap bitmap) {

        String sdStatus = Environment.getExternalStorageState();
        if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // 检测sd是否可用
            Log.i("TestFile",
                    "SD card is not avaiable/writeable right now.");
            return "";
        }
        String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + ".jpg";

        FileOutputStream b = null;
        File file = new File("/sdcard/" + MINSU + "/");
        file.mkdirs();// 创建文件夹
        String fileName = "/sdcard/" + MINSU + "/" + name;


        try {
            b = new FileOutputStream(fileName);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件

            return fileName;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                b.flush();
                b.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


    /**
     * Try to return the absolute file path from the given Uri
     *
     * @param context
     * @param uri
     * @return the file path or null
     */
    public static String getRealFilePath2(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }


    /**
     * 读取图片的旋转的角度，还是三星的问题，需要根据图片的旋转角度正确显示
     *
     * @param path 图片绝对路径
     * @return 图片的旋转角度
     */
    public static int getBitmapDegree(String path) {
        int degree = 0;
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);
            // 获取图片的旋转信息
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 获取EXIF信息
     */
    public static ExifInterface getExif(String path) {
        try {
            // 从指定路径下读取图片，并获取其EXIF信息
            ExifInterface exifInterface = new ExifInterface(path);

            return exifInterface;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;

    }


    /**
     * 遍历删除文件夹
     *
     * @param file
     */

    public static void deleteFile(File file) {
        if (file.exists()) { // 判断文件是否存在
            if (file.isFile()) { // 判断是否是文件
                file.delete(); // delete()方法 你应该知道 是删除的意思;
            } else if (file.isDirectory()) { // 否则如果它是一个目录
                File files[] = file.listFiles(); // 声明目录下所有的文件 files[];
                for (int i = 0; i < files.length; i++) { // 遍历目录下所有的文件
                    deleteFile(files[i]); // 把每个文件 用这个方法进行迭代
                }
            }
            file.delete();
        }
    }


    /**
     * 获取软键盘是否弹出
     *  {@link SoftKeyBoardListener}
     *  {@link #isSoftInputShown(Activity)}
     * @param rootView
     * @return
     */
    @Deprecated
    public static boolean isKeyboardShown(View rootView) {
        final int softKeyboardHeight = 100;
        Rect r = new Rect();
        rootView.getWindowVisibleDisplayFrame(r);
        DisplayMetrics dm = rootView.getResources().getDisplayMetrics();
        int heightDiff = rootView.getBottom() - r.bottom;
        LogUtil.i("info","isKeyboardShown  rootView.getBottom() = " + rootView.getBottom()+ "  r.bottom " + r.bottom + " 差 r ==== " + heightDiff
                + " result == " +( heightDiff > softKeyboardHeight * dm.density?" true ":"false"));
        return heightDiff > softKeyboardHeight * dm.density;
    }


    /**
     * 获取随机数
     *
     * @param min
     * @param max
     * @return
     */
    public static int getRandom(int min, int max) {


        //     (数据类型)(最小值+Math.random()*(最大值-最小值+1))

        return (int) (min + Math.random() * (max - min + 1));

    }


    /**
     * 获取手机数据
     *
     * @return
     */
    public static Map<String, Object> getPhoneInfo() {
        TelephonyManager tm = (TelephonyManager) App.instance.getSystemService(Context.TELEPHONY_SERVICE);
        String deviceId = tm.getDeviceId();//获取智能设备唯一编号
        String tel = tm.getLine1Number();//获取本机号码
        String imei = tm.getSimSerialNumber();//获得SIM卡的序号
        String imsi = tm.getSubscriberId();//得到用户Id
        String phoneModel = android.os.Build.MODEL; // 手机型号
        String phoneBrand = android.os.Build.BRAND;//手机品牌
        String osVersion = android.os.Build.VERSION.RELEASE; //安卓版本
//        double lat = LocationInfo.getLatitude(App.instance);
//        double lng = LocationInfo.getLongitude(App.instance);
//        BDLocation bdLocation = LocationInfo.getBDLocation(App.instance);

        Map<String, Object> info = new HashMap<>();
//        info.put("versionName", Utilities.getVersion(App.instance));
//        info.put("versionCode", MinsuConfig.MINSUVERSIONCODE);
        info.put("deviceId", deviceId);
        info.put("phoneModel", phoneModel);
        info.put("phoneBrand", phoneBrand);
        info.put("imei", imei);
        info.put("imsi", imsi);
        info.put("macAddress", getMac());
        info.put("tel", tel);
        info.put("osVersion", osVersion);
        info.put("osType", "1");
//        info.put("appName", MinsuConfig.MINSUAPPNAME);
        info.put("netWork", netType2Int(GetNetworkType()) + "");
//        info.put("channelName", Utils.getAppMetaData(ApplicationEx.instance, "UMENG_CHANNEL"));
        info.put("deviceIP", getIp(App.instance));

//        if (lng > 0 && lat > 0) {
//
//            String lngStr = getNumberFormat(lng, 15);
//            String latStr = getNumberFormat(lat, 15);
//
//            info.put("locationCoordinate", lngStr + "," + latStr);  //坐标
//
//        }


//        if (bdLocation != null) {
//
//            String city = bdLocation.getCity();
//            if (com.ziroom.ziroomcustomer.util.StringUtil.notNull(city)) {
//
//                String baseCity = Base64.encodeToString(city.getBytes(), Base64.NO_WRAP);
//
//                info.put("locationCityName", baseCity);  //定位城市
//            }
//
//            String cityCode = bdLocation.getCityCode();
//            if (com.ziroom.ziroomcustomer.util.StringUtil.notNull(cityCode)) {
//                info.put("locationCityCode", cityCode + "");  //定位城市citycode
//            }
//
//        }


//        if (BuildConfig.DEBUG) {
//            StringBuffer sb = new StringBuffer();
//
//            sb.append("versionName" + "---" + Utilities.getVersion(ZiroomApplication.instance) + "---");
//            sb.append("versionCode" + "---" + Utilities.getAppVersion(ZiroomApplication.instance) + "---");
//            sb.append("deviceId" + "---" + deviceId + "---");
//            sb.append("phoneModel" + "---" + phoneModel + "---");
//            sb.append("phoneBrand" + "---" + phoneBrand + "---");
//            sb.append("imei" + "---" + imei + "---");
//            sb.append("imsi" + "---" + imsi + "---");
//            sb.append("macAddress" + "---" + getMac() + "---");
//            sb.append("tel" + "---" + tel + "---");
//            sb.append("osVersion" + "---" + osVersion + "---");
//            sb.append("osType" + "---" + "1" + "---");
//            sb.append("appName" + "---" + getApplicationName() + "---");
//            sb.append("netWork" + "---" + netType2Int(GetNetworkType()) + "" + "---");
//            sb.append("channelName" + "---" + getAppMetaData(ZiroomApplication.instance, "UMENG_CHANNEL"));
//
//            Log.e("lanzhihong", sb.toString());
//        }

        return info;
    }

    @NonNull
    public static String getNumberFormat(double value, int maxNum) {
        NumberFormat nf = NumberFormat.getInstance();
        nf.setMaximumFractionDigits(maxNum);
        nf.setGroupingUsed(false);
        return nf.format(value);
    }


    /**
     * 获取手机mac地址
     *
     * @return
     */
    public static String getMac() {
        try {
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface iF = interfaces.nextElement();

                byte[] addr = iF.getHardwareAddress();
                if (addr == null || addr.length == 0) {
                    continue;
                }

                StringBuilder buf = new StringBuilder();
                for (byte b : addr) {
                    buf.append(String.format("%02X:", b));
                }
                if (buf.length() > 0) {
                    buf.deleteCharAt(buf.length() - 1);
                }
                String mac = buf.toString();
                if (iF.getName().contains("wlan")) {
                    return mac;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取app名称
     *
     * @return
     */
    public static String getApplicationName() {
        PackageManager packageManager = null;
        ApplicationInfo applicationInfo = null;
        try {
            packageManager = App.instance.getPackageManager();
//            applicationInfo = packageManager.getApplicationInfo(Utilities.getPackageName(ApplicationEx.instance), 0);
        } catch (/*PackageManager.NameNotFound*/Exception e) {
            applicationInfo = null;
        }
        String applicationName =
                (String) packageManager.getApplicationLabel(applicationInfo);
        return applicationName;
    }

    /**
     * 网络类型
     *
     * @param type
     * @return
     */
    public static int netType2Int(String type) {
        switch (type) {
            case "WIFI":
                return 4;
            case "2G":
                return 1;
            case "3G":
                return 2;
            case "4G":
                return 3;
        }
        return -1;
    }

    /**
     * 获取手机网络类型
     *
     * @return
     */
    public static String GetNetworkType() {
        String strNetworkType = "";

        NetworkInfo networkInfo = ((ConnectivityManager) App.instance.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                strNetworkType = "WIFI";
            } else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                String _strSubTypeName = networkInfo.getSubtypeName();


                // TD-SCDMA   networkType is 17
                int networkType = networkInfo.getSubtype();
                switch (networkType) {
                    case TelephonyManager.NETWORK_TYPE_GPRS:
                    case TelephonyManager.NETWORK_TYPE_EDGE:
                    case TelephonyManager.NETWORK_TYPE_CDMA:
                    case TelephonyManager.NETWORK_TYPE_1xRTT:
                    case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
                        strNetworkType = "2G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_UMTS:
                    case TelephonyManager.NETWORK_TYPE_EVDO_0:
                    case TelephonyManager.NETWORK_TYPE_EVDO_A:
                    case TelephonyManager.NETWORK_TYPE_HSDPA:
                    case TelephonyManager.NETWORK_TYPE_HSUPA:
                    case TelephonyManager.NETWORK_TYPE_HSPA:
                    case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
                    case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
                    case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
                        strNetworkType = "3G";
                        break;
                    case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
                        strNetworkType = "4G";
                        break;
                    default:
                        // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
                        if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) {
                            strNetworkType = "3G";
                        } else {
                            strNetworkType = _strSubTypeName;
                        }

                        break;
                }

                LogUtil.e("lanzhihong", "Network getSubtype : " + Integer.valueOf(networkType).toString());
            }
        }

        LogUtil.e("lanzhihong", "Network Type : " + strNetworkType);

        return strNetworkType;
    }

    /**
     * 获取application中指定的meta-data
     *
     * @return 如果没有获取成功(没有对应值，或者异常)，则返回值为空
     */
    public static String getAppMetaData(Context ctx, String key) {
        if (ctx == null || TextUtils.isEmpty(key)) {
            return null;
        }
        String resultData = null;
        try {
            PackageManager packageManager = ctx.getPackageManager();
            if (packageManager != null) {
                ApplicationInfo applicationInfo = packageManager.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
                if (applicationInfo != null) {
                    if (applicationInfo.metaData != null) {
                        resultData = applicationInfo.metaData.getString(key);
                    }
                }


            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }


        return resultData;
    }


    /**
     * wifi  获取ip
     *
     * @param context
     * @return
     */
    public static String getWifiIpAddress(Context context) {
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiManager.getConnectionInfo();
        // 获取32位整型IP地址
        int ipAddress = wifiInfo.getIpAddress();

        //返回整型地址转换成“*.*.*.*”地址
        return String.format("%d.%d.%d.%d",
                (ipAddress & 0xff), (ipAddress >> 8 & 0xff),
                (ipAddress >> 16 & 0xff), (ipAddress >> 24 & 0xff));
    }


    /**
     * 移动网络获取IP
     *
     * @return
     */
    public static String getIpAddress() {
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface
                    .getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress()
                            && inetAddress instanceof Inet4Address) {
                        // if (!inetAddress.isLoopbackAddress() && inetAddress
                        // instanceof Inet6Address) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 获取IP
     *
     * @param context
     * @return
     */
    public static String getIp(Context context) {

        if (netType2Int(GetNetworkType()) == 4) {  //WIFI

            return getWifiIpAddress(context);

        } else {

            return getIpAddress();

        }

    }


    /**
     * 转换ObJ MAP  到 String
     *
     * @param map
     * @return
     */
    public static Map<String, String> ConvertObjMap2String(Map<String, Object> map) {

        Map<String, String> newMap = new HashMap<String, String>();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof String) {
                newMap.put(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Integer) {
                newMap.put(entry.getKey(), String.valueOf(entry.getValue()));
            } else if (entry == null || entry.getValue() == null) {
                LogUtil.e("lanzhihong", "Utils.ConvertObjMap2String=== null");
            } else {
                newMap.put(entry.getKey(), String.valueOf(entry.getValue()));
                LogUtil.e("lanzhihong", "Utils.ConvertObjMap2String===" + entry.getValue() + "======" + entry.getValue() == null ? "null" : entry.getValue().getClass());
            }
        }

        return newMap;
    }

    public static int px2sp(Context context, float pxValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (pxValue / fontScale + 0.5f);
    }

    public static int dp2px(Context context, int dp) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        return (int) ((dp * displayMetrics.density) + 0.5);
    }


    /**
     * 获取文件类型
     *
     * @param filePath
     * @return
     */
    public static String getImageType(String filePath) {
        FileInputStream is = null;
        String value = null;
        try {
            is = new FileInputStream(filePath);
            byte[] b = new byte[3];
            is.read(b, 0, b.length);
            value = bytesToHexString(b);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (null != is) {
                try {
                    is.close();
                } catch (IOException e) {
                }
            }
        }
        if ("FFD8FF".equals(value)) {
            return "jpg";
        } else if ("FFD8FF".equals(value)) {
            return "jpg";
        } else if ("47494638".equals(value)) {
            return "gif";
        } else if ("424D".equals(value)) {
            return "bmp";
        }
        return value;
    }

    private static String bytesToHexString(byte[] src) {
        StringBuilder builder = new StringBuilder();
        if (src == null || src.length <= 0) {
            return null;
        }
        String hv;
        for (int i = 0; i < src.length; i++) {
            hv = Integer.toHexString(src[i] & 0xFF).toUpperCase();
            if (hv.length() < 2) {
                builder.append(0);
            }
            builder.append(hv);
        }
        return builder.toString();
    }


    /**
     * 提供（相对）精确的除法运算，当发生除不尽的情况时，精确到
     * 小数点以后10位，以后的数字四舍五入。
     *
     * @param v1 被除数
     * @param v2 除数
     * @return 两个参数的商
     */

    public static double div(double v1, double v2) {
        return div(v1, v2, 2);
    }

    /**
     * 提供（相对）精确的除法运算。当发生除不尽的情况时，由scale参数指
     * 定精度，以后的数字四舍五入。
     *
     * @param v1    被除数
     * @param v2    除数
     * @param scale 表示表示需要精确到小数点以后几位。
     * @return 两个参数的商
     */

    public static double div(double v1, double v2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The   scale   must   be   a   positive   integer   or   zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    /**
     * 判断是否数字
     *
     * @param str
     * @return
     */
    public static boolean isNumeric(String str) {
        for (int i = 0; i < str.length(); i++) {
            System.out.println(str.charAt(i));
            if (!Character.isDigit(str.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    /**
     * 判断当前日期是星期几
     *
     * @param pTime 设置的需要判断的时间  //格式如2012-09-08
     * @return dayForWeek 判断结果
     * @Exception 发生异常
     */

//  String pTime = "2012-03-12";
    public static String getWeekFromDate(String pTime) {

        String Week = "星期";


        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {

            c.setTime(format.parse(pTime));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.MONDAY) {
            Week += "一";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.TUESDAY) {
            Week += "二";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.WEDNESDAY) {
            Week += "三";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.THURSDAY) {
            Week += "四";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.FRIDAY) {
            Week += "五";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY) {
            Week += "六";
        }
        if (c.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY) {
            Week += "日";
        }


        return Week;
    }


    /**
     * 转换  int 类型月份为  十二月
     *
     * @param month
     * @return
     */
    public static String getMonthFromDate(int month) {

        month += 1;  //month  0 开始

        char[] upper = "零一二三四五六七八九十".toCharArray();

        StringBuffer sb = new StringBuffer();

        if (month <= 10) {
            sb.append(upper[month]);
        } else {
            sb.append("十").append(upper[month % 10]);
        }

        if ("零".equals(sb.toString())) {
            sb.setLength(0);
            sb.append("十二");
        }

        return sb.toString() + "月";
    }


    /**
     * 生成一个文件名  md5(yyyyMMdd_hhmmss+radom)
     *
     * @return
     */
    public static String createFileName() {

        // 随机生成文件编号
        int random = new Random().nextInt(10000);
        String name = new DateFormat().format("yyyyMMdd_hhmmss", Calendar.getInstance(Locale.CHINA)) + String.valueOf(random); //+ ".jpg";   //去除后缀
        String fileName = Utilitiessave.toMd5(name.getBytes());

        return fileName;

    }

    /**
     * 空后者全是数字
     *
     * @param userName
     * @return
     */
    public static boolean isNullOrNumber(String userName) {
        if (TextUtils.isEmpty(userName)) {
            return true;
        }
        Pattern p = Pattern.compile("^\\d+$");
        Matcher m = p.matcher(userName);
        return m.matches();
    }


    /* 百度静态图方法
    *
    * @param lnglat
    * @param width
    * @return
    * @paraight
    */
    public static String getStaticPicFromBaiduMap(String lnglat, int width, int height, int zoom) {
        return "http://api.map.baidu.com/staticimage?width=" + width + "&height=" +
                height + "&center=" + lnglat + "&zoom=" + zoom + "&scale=1";
    }


}