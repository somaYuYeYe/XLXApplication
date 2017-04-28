package wza.slx.com.xlxapplication.test;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.net.Uri;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.telephony.TelephonyManager;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import android.widget.TextView;

import org.apache.http.util.EncodingUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import wza.slx.com.xlxapplication.utils.CheckUtils;

public class Utilitiessave {

    /**
     * @param origin
     * @return md5 value
     */
    public static String MD5Encode(String origin) {
        return toMd5(origin.getBytes());
    }


    public static String toMd5(byte[] bytes) {
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(bytes);
            return toHexString(algorithm.digest(), "");
        } catch (NoSuchAlgorithmException e) {
            Log.v("util", "toMd5():" + e);
            throw new RuntimeException(e);
        }
    }

    private static String toHexString(byte[] bytes, String separator) {
        StringBuilder hexString = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xFF & b);
            if (hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex).append(separator);
        }
        return hexString.toString();
    }

    /**
     * 半角转换为全角 说明：解决TextView中文字排版参差不齐的问题
     * 将textview中的字符全角化。即将所有的数字、字母及标点全部转为全角字符，使它们与汉字同占两个字节 by:liubing
     *
     * @param input
     * @return
     */
    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    /**
     * 去除特殊字符或将所有中文标号替换为英文标号 说明：解决TextView中文字排版参差不齐的问题 by:liubing
     *
     * @param str
     * @return
     */
    public static String StringFilter(String str) {
        str = str.replaceAll("【", "[").replaceAll("】", "]")
                .replaceAll("！", "!").replaceAll("：", ":").replace("，", ",")
                .replace("。", ".").replace("……", "......");// 替换中文标号
        String regEx = "[『』]"; // 清除掉特殊字符
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    /**
     * 网络是否可用
     *
     * @param context
     * @return
     */
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager mgr = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] info = mgr.getAllNetworkInfo();
        if (info != null) {
            for (int i = 0; i < info.length; i++) {
                if (info[i].getState() == State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }


    // 判断email格式是否正确
    public static boolean isEmail(String email) {
        String str = "^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$";
        Pattern p = Pattern.compile(str);
        Matcher m = p.matcher(email);

        return m.matches();
    }

    /**
     * 手机号验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isMobile(String str) {
        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^[1][3,4,5,7,8][0-9]{9}$"); // 验证手机号
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

    /**
     * 电话号码验证
     *
     * @param str
     * @return 验证通过返回true
     */
    public static boolean isPhone(String str) {
        Pattern p1 = null, p2 = null;
        Matcher m = null;
        boolean b = false;
        p1 = Pattern.compile("^[0][1-9]{2,3}-[0-9]{5,10}$"); // 验证带区号的
        p2 = Pattern.compile("^[1-9]{1}[0-9]{5,8}$"); // 验证没有区号的
        if (str.length() > 9) {
            m = p1.matcher(str);
            b = m.matches();
        } else {
            m = p2.matcher(str);
            b = m.matches();
        }
        return b;
    }

    /**
     * 判断是否为整型字符串
     *
     * @param str
     * @return
     */
    public static boolean isNumber(String str) {
        // +表示1个或多个（如"3"或"225"），*表示0个或多个（[0-9]*）（如""或"1"或"22"），?表示0个或1个([0-9]?)(如""或"7")
        boolean isNum = str.matches("[0-9]+");
        return isNum;
    }

    /**
     * 检查字符串为空
     *
     * @param str
     * @return
     */
    public static boolean isEmpty(String str) {
        if (str == null || "".equals(str)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 字符串插入数组
     *
     * @param arr
     * @param str
     * @return
     */
    public static String[] insert(String[] arr, String str) {
        int size = arr.length;
        String[] tmp = new String[size + 1];
        System.arraycopy(arr, 0, tmp, 0, size);
        tmp[size] = str;
        return tmp;
    }

    /**
     * 获取GPS经纬度
     *
     * @param context
     * @return
     */
    public static String getLocation(Context context) {
        String position = "";

        LocationManager locationManager = (LocationManager) context
                .getSystemService(Context.LOCATION_SERVICE);
        Location location = locationManager
                .getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            location = locationManager
                    .getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

        if (location != null) {
            double latitude = location.getLatitude();
            double longitude = location.getLongitude();
            position = latitude + "," + longitude;
        }

        return position;
    }

    /**
     * 检测网路类型
     *
     * @param context
     * @return
     */
    public static String checkNetworkInfo(Context context) {
        // TODO Auto-generated method stub
        ConnectivityManager conMan = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE); // mobile
        // 3G
        // Data
        // Network
        State wifi = conMan.getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState();
        if (wifi == State.CONNECTED)
            return "WIFI";
        // new
        // AlertDialog.Builder(context).setMessage(wifi.toString()).setPositiveButton("WIFI",
        // null).show();//显示wifi网络连接状态
        State mobile = conMan.getNetworkInfo(ConnectivityManager.TYPE_MOBILE)
                .getState();
        if (mobile == State.CONNECTED)
            return "3G";
        // new
        // AlertDialog.Builder(context).setMessage(mobile.toString()).setPositiveButton("3G",
        // null).show();//显示3G网络连接状态
        return "";
    }


    /**
     * 拷贝文件
     *
     * @param context
     * @param fileName
     * @param path
     * @return
     */
    public static boolean copyApkFromAssets(Context context, String fileName,
                                            String path) {
        boolean copyIsFinish = false;
        try {
            InputStream is = context.getAssets().open(fileName);
            File file = new File(path);
            file.createNewFile();
            FileOutputStream fos = new FileOutputStream(file);
            byte[] temp = new byte[1024];
            int i = 0;
            while ((i = is.read(temp)) > 0) {
                fos.write(temp, 0, i);
            }
            fos.close();
            is.close();
            copyIsFinish = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return copyIsFinish;
    }

    /**
     * 获取渠道编号
     *
     * @param context
     * @return
     */
    public static String getChannelCode(Context context) {
        String code = getMetaData(context, "CHANNEL");
        if (code != null) {
            return code;
        }
        return "C_000";
    }

    private static String getMetaData(Context context, String key) {
        try {
            ApplicationInfo ai = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
            Object value = ai.metaData.get(key);
            if (value != null) {
                return value.toString();
            }
        } catch (Exception e) {
            //
        }
        return null;
    }

    /**
     * 检测网络是否链接
     *
     * @param context
     * @return
     */
    public static boolean checkNet(Context context) {

        // 获取手机所以连接管理对象（包括wi-fi，net等连接的管理）
        ConnectivityManager conn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (conn != null) {
            // 网络管理连接对象
            NetworkInfo info = conn.getActiveNetworkInfo();
            if (info != null && info.isConnected()) {
                // 判断当前网络是否连接
                if (info.getState() == State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }


    /**
     * 获取版本号
     *
     * @return 当前应用的版本号
     */
    public static String getVersion(Context cxt) {
        try {
            PackageManager manager = cxt.getPackageManager();
            PackageInfo info = manager.getPackageInfo(cxt.getPackageName(), 0);
            String version = info.versionName;
            return version;
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    /*
     * 获取手机的唯一设备号
     */
    public static String getDeviceId(Context context) {

        TelephonyManager telephonyManager = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        String phone_imei = telephonyManager.getDeviceId();
        return phone_imei;
    }

    /**
     * 获取Mac
     *
     * @param context <uses-permission android:name="android.permission.ACCESS_WIFI_STATE" />
     * @return
     */
    public static String getMac(Context context) {
        WifiManager wifi = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);

        WifiInfo info = wifi.getConnectionInfo();
        String mac = info.getMacAddress();
        if (CheckUtils.isNull(info.getMacAddress())) {
            mac = "123";
        }
        return mac;

    }

    /**
     * 产生6位随机数
     *
     * @return
     */
    public static String getRandom() {
        Random random = new Random();
        String result = "";
        for (int i = 0; i < 6; i++) {
            result += Math.abs(random.nextInt(10));
        }
        return result;
    }

    /**
     * 格式化身份证字符串6-4-4-4格式
     *
     * @param id_card
     * @return
     */
    public static String formatIDCard(String id_card, String sep) {
        id_card = id_card.replaceAll(sep, "");
        if (id_card.length() > 6) {
            id_card = id_card.substring(0, 6) + sep + id_card.substring(6);
        }
        if (id_card.length() > 11) {
            id_card = id_card.substring(0, 11) + sep + id_card.substring(11);
        }
        if (id_card.length() > 16) {
            id_card = id_card.substring(0, 16) + sep + id_card.substring(16);
        }
        if (id_card.length() > 21) {
            id_card = id_card.substring(0, 21);
        }
        return id_card;
    }

    /**
     * 把字符串转为年月日的格式
     *
     * @param date
     * @return
     */
    public static String StrToNorDate(String date) {
        String[] d = date.split("-");
        if (d.length >= 3) {
            StringBuffer sb = new StringBuffer();
            sb.append(d[0]);
            sb.append("年");
            sb.append(d[1]);
            sb.append("月");
            sb.append(d[2]);
            sb.append("日");
            return sb.toString();
        }
        return date;
    }

    /**
     * 判定输入汉字
     *
     * @param c
     * @return
     */

    public static boolean isChinese(char c) {

        Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);

        if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
                || ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
                || ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
                || ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
                || ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {

            return true;

        }

        return false;

    }

    /**
     * 获取App版本
     *
     * @param mContext
     * @return
     */
    public static String getAppVersion(Context mContext) {
        PackageInfo info = null;

        PackageManager manager = mContext.getPackageManager();

        try {

            info = manager.getPackageInfo(mContext.getPackageName(), 0);

        } catch (NameNotFoundException e) {

            e.printStackTrace();

        }
        return info.versionCode + "";
    }

    /**
     * 随机指定范围内N个不重复的数
     *
     * @param max 指定范围最大值
     * @param min 指定范围最小值
     * @param n   随机数个数
     * @return int[] 随机数结果集
     */

    public static int[] randomArray(int min, int max, int n) {

        int len = max - min + 1;

        if (max < min || n > len) {

            return null;

        }

        // 初始化给定范围的待选数组

        int[] source = new int[len];

        for (int i = min; i < min + len; i++) {

            source[i - min] = i;

        }

        int[] result = new int[n];

        Random rd = new Random();

        int index = 0;

        for (int i = 0; i < result.length; i++) {

            index = Math.abs(rd.nextInt() % len--);

            // 将随机到的数放入结果集

            result[i] = source[index];

            // 将待选数组中被随机到的数，用待选数组(len-1)下标对应的数替换

            source[index] = source[len];

        }

        return result;

    }

    /**
     * 获取手机的一些信息
     *
     * @return
     */
    public static String getPhoneInfo() {
        StringBuffer sb = new StringBuffer();
        sb.append("Product: " + android.os.Build.PRODUCT + ", CPU_ABI: "
                + android.os.Build.CPU_ABI + ", TAGS: " + android.os.Build.TAGS
                + ", VERSION_CODES.BASE: "
                + android.os.Build.VERSION_CODES.BASE + ", MODEL: "
                + android.os.Build.MODEL + ", SDK: "
                + android.os.Build.VERSION.SDK + ", VERSION.RELEASE: "
                + android.os.Build.VERSION.RELEASE + ", DEVICE: "
                + android.os.Build.DEVICE + ", DISPLAY: "
                + android.os.Build.DISPLAY + ", BRAND: "
                + android.os.Build.BRAND + ", BOARD: " + android.os.Build.BOARD
                + ", FINGERPRINT: " + android.os.Build.FINGERPRINT + ", ID: "
                + android.os.Build.ID + ", MANUFACTURER: "
                + android.os.Build.MANUFACTURER + ", USER: "
                + android.os.Build.USER);

        return sb.toString();
    }

    /**
     * 发短信
     */
    public static void sendMessages(Context context, String phone, String mess) {

        Intent mIntent = new Intent(Intent.ACTION_VIEW);
        mIntent.putExtra("address", phone);
        mIntent.putExtra("sms_body", mess);
        mIntent.setType("vnd.android-dir/mms-sms");
        context.startActivity(mIntent);

    }

    /**
     * 拨打电话
     */
    public static void callPhone(final Context context, final String phone) {

        String number = phone.replace("-", ",");
        Intent phoneIntent = new Intent("android.intent.action.CALL",
                Uri.parse("tel:" + number));
        context.startActivity(phoneIntent);

    }

    /**
     * 拨打电话
     */
    public static void callPhone(final Context context, final String phone, final String name) {

        String number = phone.replace("-", ",");
        Intent phoneIntent = new Intent("android.intent.action.CALL",
                Uri.parse("tel:" + number));
        context.startActivity(phoneIntent);

    }

    private static final double PI = 3.14159265;

    /**
     * 根据两点间经纬度坐标（double值），计算两点间距离，单位为米
     *
     * @return
     */
    public static double[] getAround(double lat, double lon, int raidus) {

        Double latitude = lat;
        Double longitude = lon;

        Double degree = (24901 * 1609) / 360.0;
        double raidusMile = raidus;

        Double dpmLat = 1 / degree;
        Double radiusLat = dpmLat * raidusMile;
        Double minLat = latitude - radiusLat;
        Double maxLat = latitude + radiusLat;

        Double mpdLng = degree * Math.cos(latitude * (PI / 180));
        Double dpmLng = 1 / mpdLng;
        Double radiusLng = dpmLng * raidusMile;
        Double minLng = longitude - radiusLng;
        Double maxLng = longitude + radiusLng;
        // System.out.println("["+minLat+","+minLng+","+maxLat+","+maxLng+"]");
        return new double[]{minLat, minLng, maxLat, maxLng};
    }


    public static String getFromAssets(Context context, String fileName) {

        String s = "";
        try {

            // 得到资源中的asset数据流
            InputStream in = context.getResources().getAssets().open(fileName);

            int length = in.available();
            byte[] buffer = new byte[length];

            in.read(buffer);
            in.close();
            s = EncodingUtils.getString(buffer, "UTF-8");
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return fileName;
    }

    /**
     * 获取Ip地址
     *
     * @param context
     * @return ip地址
     */
    public static String getIpAddress(Context context) {
        String state = checkNetworkInfo(context);
        if ("WIFI".equals(state)) {
            try {
                for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces();
                     en.hasMoreElements(); ) {
                    NetworkInterface intf = en.nextElement();
                    for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses();
                         enumIpAddr.hasMoreElements(); ) {
                        InetAddress inetAddress = enumIpAddr.nextElement();
                        if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
                            return inetAddress.getHostAddress().toString();
                        }
                    }
                }
            } catch (SocketException ex) {
                ex.printStackTrace();
            }
        }
        return "";
    }


    /**
     * 根据Uri获取图片绝对路径，解决Android4.4以上版本Uri转换
     *
     * @param context
     * @param imageUri
     * @author yaoxing
     * @date 2014-10-12
     */
    @TargetApi(19)
    public static String getImageAbsolutePath(Activity context, Uri imageUri) {
        if (context == null || imageUri == null)
            return null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, imageUri)) {
            if (isExternalStorageDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(imageUri)) {
                String id = DocumentsContract.getDocumentId(imageUri);
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri, null, null);
            } else if (isMediaDocument(imageUri)) {
                String docId = DocumentsContract.getDocumentId(imageUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[]{split[1]};
                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        } // MediaStore (and general)
        else if ("content".equalsIgnoreCase(imageUri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(imageUri))
                return imageUri.getLastPathSegment();
            return getDataColumn(context, imageUri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(imageUri.getScheme())) {
            return imageUri.getPath();
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = {column};
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    /**
     * get请求url拼接,需要在请求地址后面加 ? 和该String
     *
     * @param map
     * @return
     */
    public static String appendGetUrl(Map<String, Object> map) {
        if (map == null || map.size() < 1) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        Set<String> set = map.keySet();
        for (String str : set) {
            String param;
            if (map.get(str) == null) {
                param = "";
            } else {
                param = map.get(str).toString().trim();
            }
            try {
                str = URLEncoder.encode(str, "utf-8");
                param = URLEncoder.encode(param, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            builder.append("&" + str + "=" + param);
        }
        builder.deleteCharAt(0);
        return builder.toString();
    }

    public static boolean checkSpecialChar(String str) throws PatternSyntaxException {
        // 清除掉所有特殊字符
        String regEx = ".*[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#¥%……&*（）——+|{}【】‘；：”“’。，、？\\\\]+.*";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.matches();
    }


    /**
     * get请求url拼接,需要在请求地址后面加 ? 和该String
     *
     * @param map
     * @return
     */
    public static String encodeGetParams(Map<String, String> map) {
        if (map == null || map.size() < 1) {
            return "";
        }
        StringBuilder builder = new StringBuilder();
        Set<String> set = map.keySet();
        for (String str : set) {
            String param;
            if (map.get(str) == null) {
                param = "";
            } else {
                param = map.get(str).toString();
            }
            try {
                str = URLEncoder.encode(str, "utf-8");
                param = URLEncoder.encode(param, "utf-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            builder.append("&" + str + "=" + param);
        }
        builder.deleteCharAt(0);
        return builder.toString();
    }

    /**
     * 把Map<String,Object>转换成Map<String,String>
     *
     * @param map
     * @return
     */
    public static Map<String, String> ConvertObjMap2String(Map<String, Object> map) {
        Map<String, String> Map = new HashMap<String, String>();
        for (java.util.Map.Entry<String, Object> entry : map.entrySet()) {
            if (entry.getValue() instanceof String) {
                Map.put(entry.getKey(), (String) entry.getValue());
            } else if (entry.getValue() instanceof Integer) {
                Map.put(entry.getKey(), String.valueOf(entry.getValue()));
            } else if (entry.getValue() == null) {
                Map.put(entry.getKey(), "");
            } else {
                Map.put(entry.getKey(), String.valueOf(entry.getValue()));
            }
        }
        return Map;
    }


    /**
     * 将分为单位的转换为元 （除100）
     *
     * @param amount
     * @return
     * @throws Exception
     */
    public static String changeF2Y(String amount) {
        /**金额为分的格式 */
        String CURRENCY_FEN_REGEX = "\\-?[0-9]+";
        if (!amount.matches(CURRENCY_FEN_REGEX)) {
            try {
                throw new Exception("金额格式有误");
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            return BigDecimal.valueOf(Long.valueOf(amount)).divide(new BigDecimal(100)).toString();
        } catch (NumberFormatException e) {
            return "";
        }
    }

    // 保留小数点后两位小数
    public static double Number2(double pDouble) {
        BigDecimal bd = new BigDecimal(pDouble);
        BigDecimal bd1 = bd.setScale(2, bd.ROUND_HALF_UP);
        pDouble = bd1.doubleValue();
        long ll = Double.doubleToLongBits(pDouble);
        return pDouble;
    }

    // 保留小数点后两位小数,显示两个0  例如2显示2.00
    public static String Number(float pDouble) {

        DecimalFormat df = new DecimalFormat("##0.00");
        System.out.println(df.format(pDouble));
        return df.format(pDouble);
    }

    //保留两位小数，四舍五入
    public static float setFloat(float fl) {
        BigDecimal bd = new BigDecimal((double) fl);
        bd = bd.setScale(2, 4);
        fl = bd.floatValue();
        return fl;
    }

    //改变textView部分字体颜色
    public static void setTextColor(TextView tv, String text, int start, int length) {
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        //ForegroundColorSpan 为文字前景色，BackgroundColorSpan为文字背景色
        ForegroundColorSpan redSpan = new ForegroundColorSpan(Color.parseColor("#FFA000"));
        builder.setSpan(redSpan, start, start + length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        if (tv != null)
            tv.setText(builder);
    }

    //判断是否是https、http,ftp,file链接
    public static boolean isWebUrl(String url) {
        String regex = "^(https?|ftp|file)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
        Pattern patt = Pattern.compile(regex);
        Matcher matcher = patt.matcher(url);
        boolean isMatch = matcher.matches();
        return isMatch;
//		String reg = "^([a-zA-Z0-9]([a-zA-Z0-9\\-]{0,61}[a-zA-Z0-9])?\\.)+[a-zA-Z]{2,6}$";
//		Pattern pat = Pattern.compile(reg);
//		Matcher mat = pat.matcher(url);
//		boolean isMat = mat.matches();

    }

    /**
     *  
     * <p>
     *  根据手机的分辨率从 px(像素) 的单位 转成为 dp 
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     *  
     *  根据手机的分辨率从 dp 的单位 转成为 px(像素) 
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 格式化小数为小数点后两位的字符串
     *
     * @param value
     * @return
     */
    public static String formatDouble(double value) {
//        BigDecimal   b   =   new   BigDecimal(value);
//        double   f1   =   b.setScale(2,   BigDecimal.ROUND_HALF_UP).doubleValue();

        DecimalFormat df = new DecimalFormat("0.00");//格式化小数，不足的补0
        return df.format(value);//返回的是String类型的
    }
}
