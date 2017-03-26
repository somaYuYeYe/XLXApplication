package wza.slx.com.xlxapplication.utils;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by wz on 2016/5/3.
 */
public class CheckUtils {
    public static boolean isNullList(List list) {
        if (null == list || list.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isNullMap(Map map) {
        if (null == map || map.isEmpty()) {
            return true;
        }
        return false;
    }

    public static boolean isNull(Object o) {
        if (null == o || "".equals(o.toString()) || "null".equalsIgnoreCase(o.toString())) {
            return true;
        }
        return false;
    }

    public static boolean isNumber(String str) {
        Pattern p = Pattern.compile("^\\d+$");
        Matcher m = p.matcher(str);
        return m.matches();
    }

    public static boolean isIdcard(String id) {
        Pattern p = Pattern.compile("^\\d{17}[\\d|x]|\\d{15}$");
        Matcher m = p.matcher(id);
        return m.matches();
    }

    public static boolean isMobile(String mobiles) {
        Pattern p = Pattern.compile("^1\\d{10}$");
        Matcher m = p.matcher(mobiles);
        return m.matches();
    }

    /**
     * 得到一个字符串的长度,显示的长度,一个汉字2,英文字符长度为1
     * @param  s 需要得到长度的字符串
     * @return int 得到的字符串长度
     */
    public static int getLength(String s) {
        int valueLength = 0;
        String chinese = "[\u4e00-\u9fa5]";
        // 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
        for (int i = 0; i < s.length(); i++) {
            // 获取一个字符
            String temp = s.substring(i, i + 1);
            // 判断是否为中文字符
            if (temp.matches(chinese)) {
                // 中文字符长度为1
                valueLength += 2;
            } else {
                // 其他字符长度为0.5
                valueLength += 1;
            }
        }
        //进位取整
        return  valueLength;
    }

    /**
     *  台湾通行证
     * @param taiwan
     * @return
     */
    public static boolean isTaiwanCard(String taiwan){
//        Pattern p = Pattern.compile("^\\d{8}|\\d{10}$");
//        Matcher m = p.matcher(taiwan);
        Pattern p1 = Pattern.compile("^\\d{8}$");
        Matcher m1 = p1.matcher(taiwan);
        Pattern p2 = Pattern.compile("^\\d{10}$");
        Matcher m2 = p2.matcher(taiwan);
        return m1.matches() || m2.matches();
    }

}
