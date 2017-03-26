package wza.slx.com.xlxapplication.net;

import wza.slx.com.xlxapplication.BuildConfig;

/**
 * Created by homelink on 2017/3/24.
 */

public class Constant {

    // test
    private static final String TEST_ROOT = "http://chat.wanlefu.com/wlf-weChat-web/";// test evironment

    // PRODUCTION
    private static final String PROD_ROOT = "http://wchat.wanlefu.com/wlf-weChat-web/";// production evironment

    public static final String ROOT = TEST_ROOT;
//    static {
//        if(BuildConfig.){
//
//        }
//    }


    // token
    public static final String ACCESSREPORTTOKEN = "appApi/accessReportToken";  // token

    // register
    public static final String REGISTER = "appApi/register"; // register

    // qustion
    public static final String SAVEQUESTIONNAIRE = "appApi/saveQuestionnaire"; // save quesiton appApi/saveQuestionnaire

    // sendSmsVerifyCode
    public static final String SENDSMSVERIFYCODE = "appApi/sendSmsVerifyCode"; // sendSmsVerifyCode

    // checkLoginName
    public static final String CHECKLOGINNAME = "appApi/checkLoginName";// appApi/checkLoginName

    // /appApi/checkIdCard
    public static final String CHECKIDCARD = "appApi/checkIdCard"; // checkIdCard

    // appApi/updatePwd
    public static final String UPDATEPWD = "appApi/updatePwd"; // updatePwd

}
