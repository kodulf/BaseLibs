package kodulf.baselibs.utils;

import android.text.TextUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils{
    public static final String EMPTY ="";
    public static boolean isBlank(String str) {
        int strLen;
        if (str == null || (strLen = str.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(str.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }
    public static boolean isNotBlank(String str) {
        return !StringUtils.isBlank(str);
    }

    /**
     * 判断ip地址是否合法
     * @param ipAddress
     * @return
     */
    public static boolean checkIP(String ipAddress)
    {
        String ip = "([1-9]|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])(\\.(\\d|[1-9]\\d|1\\d{2}|2[0-4]\\d|25[0-5])){3}";
        Pattern pattern = Pattern.compile(ip);
        Matcher matcher = pattern.matcher(ipAddress);
        return matcher.matches();
    }

    //是否包含中文
    public static boolean isContainChinese(String str) {

        Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]");
        Matcher m = p.matcher(str);
        if (m.find()) {
            return true;
        }
        return false;
    }

    public static boolean onlyNumber(String str){
        return str.matches("^[0-9]*$");
    }

    public static boolean checkPassword(String stc){
        return stc.matches("^[a-zA-Z0-9]{6,10}$");
    }

    /**
     * <p>Deletes all whitespaces from a String as defined by
     * {@link Character#isWhitespace(char)}.</p>
     *
     * <pre>
     * StringUtils.deleteWhitespace(null)         = null
     * StringUtils.deleteWhitespace("")           = ""
     * StringUtils.deleteWhitespace("abc")        = "abc"
     * StringUtils.deleteWhitespace("   ab  c  ") = "abc"
     * </pre>
     *
     * @param str  the String to delete whitespace from, may be null
     * @return the String without whitespaces, <code>null</code> if null String input
     */
    public static String deleteWhitespace(String str) {
        if (isEmpty(str)) {
            return str;
        }
        int sz = str.length();
        char[] chs = new char[sz];
        int count = 0;
        for (int i = 0; i < sz; i++) {
            if (!Character.isWhitespace(str.charAt(i))) {
                chs[count++] = str.charAt(i);
            }
        }
        if (count == sz) {
            return str;
        }
        return new String(chs, 0, count);
    }
    // Empty checks
    //-----------------------------------------------------------------------
    /**
     * <p>Checks if a String is empty ("") or null.</p>
     *
     * <pre>
     * StringUtils.isEmpty(null)      = true
     * StringUtils.isEmpty("")        = true
     * StringUtils.isEmpty(" ")       = false
     * StringUtils.isEmpty("bob")     = false
     * StringUtils.isEmpty("  bob  ") = false
     * </pre>
     *
     * <p>NOTE: This method changed in Lang version 2.0.
     * It no longer trims the String.
     * That functionality is available in isBlank().</p>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is empty or null
     */
    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }

    /**
     * <p>Checks if a String is not empty ("") and not null.</p>
     *
     * <pre>
     * StringUtils.isNotEmpty(null)      = false
     * StringUtils.isNotEmpty("")        = false
     * StringUtils.isNotEmpty(" ")       = true
     * StringUtils.isNotEmpty("bob")     = true
     * StringUtils.isNotEmpty("  bob  ") = true
     * </pre>
     *
     * @param str  the String to check, may be null
     * @return <code>true</code> if the String is not empty and not null
     */
    public static boolean isNotEmpty(String str) {
        return !StringUtils.isEmpty(str);
    }


    /**
     * 隐藏手机号中间4位
     * @param phone
     * @return
     */
    public static String phoneEncryption(String phone){
        if(TextUtils.isEmpty(phone)){
            return "";
        }
        if(phone.length()<7){
            return phone;
        }
        StringBuffer sb=new StringBuffer();
        sb.append(phone.substring(0,3));
        sb.append("****");
        sb.append(phone.substring(7,phone.length()));
        return sb.toString();
    }

    public static String nameEncryption(String name){
        if(TextUtils.isEmpty(name)){
            return "";
        }
        if(name.length()<2){
            return name;
        }
        StringBuffer sb=new StringBuffer();
        sb.append("*");
        sb.append(name.substring(1,name.length()));
        return sb.toString();
    }

    /**
     * 不对dailyId做空校验，在外部处理
     */
    public static String fillDailyId(Long dailyId){
        String temp=dailyId.toString();
        if(temp.length()<6){
            StringBuffer sb=new StringBuffer();
            for(int i=6;i>temp.length();i--){
                sb.append("0");
            }
            sb.append(temp);
            return sb.toString();
        }else{
            return temp;
        }
    }

}

