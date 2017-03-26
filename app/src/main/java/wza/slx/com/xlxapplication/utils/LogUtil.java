package wza.slx.com.xlxapplication.utils;

import android.util.Log;

import wza.slx.com.xlxapplication.BuildConfig;

/**
 * Created by homelink on 2017/3/26.
 */

public class LogUtil {

    private static Boolean LOG_SWITCH = BuildConfig.LOG_DEBUG; // 日志文件总开关
    private static char LOG_TYPE = 'v';                        // 输入日志类型，w代表只输出告警信息等，v代表输出所有信息
    public static final String TAG = LogUtil.class.getSimpleName();            // 本对象表示输出的Log名称


    public static void w(String tag, Object msg) { // 警告信息
        log(tag, msg.toString(), 'w');
    }

    public static void e(String tag, Object msg) { // 错误信息
        log(tag, msg.toString(), 'e');
    }

    public static void d(String tag, Object msg) {// 调试信息
        log(tag, msg.toString(), 'd');
    }

    public static void i(String tag, Object msg) {//
        log(tag, msg.toString(), 'i');
    }

    public static void v(String tag, Object msg) {
        log(tag, msg.toString(), 'v');
    }

    public static void w(String tag, String text) {
        log(tag, text, 'w');
    }

    public static void e(String tag, String text) {
        log(tag, text, 'e');
    }

    public static void d(String tag, String text) {
        log(tag, text, 'd');
    }

    public static void i(String tag, String text) {
        log(tag, text, 'i');
    }

    public static void v(String tag, String text) {
        log(tag, text, 'v');
    }

    /**
     * 根据tag, msg和等级，输出日志
     *
     * @param tag
     * @param msg
     * @param level
     * @return void
     * @since v 1.0
     */
    private static void log(String tag, String msg, char level) {
        if (CheckUtils.isNull(msg)) {
            return;
        }
        if (LOG_SWITCH) {
            if ('e' == level && ('e' == LOG_TYPE || 'v' == LOG_TYPE)) { // 输出错误信息
                Log.e(tag, TAG + msg);
            } else if ('w' == level && ('w' == LOG_TYPE || 'v' == LOG_TYPE)) {
                Log.w(tag, TAG + msg);
            } else if ('d' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
                Log.d(tag, TAG + msg);
            } else if ('i' == level && ('d' == LOG_TYPE || 'v' == LOG_TYPE)) {
                Log.i(tag, TAG + msg);
            } else {
                Log.v(tag, TAG + msg);
            }
        }
    }

}
