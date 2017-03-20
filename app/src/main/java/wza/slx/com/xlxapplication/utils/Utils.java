package wza.slx.com.xlxapplication.utils;

import android.text.TextUtils;
import android.view.TextureView;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by homelink on 2017/3/20.
 */

public class Utils {
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
}
