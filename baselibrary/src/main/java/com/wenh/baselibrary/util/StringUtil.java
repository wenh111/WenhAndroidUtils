package com.wenh.baselibrary.util;


import android.graphics.Paint;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.apache.log4j.Logger;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 为FE提供各类过滤字符串的接口
 *
 * @author Junquan 2010.01.20
 */
public class StringUtil {

    private static Logger logger = Logger.getLogger(StringUtil.class);

    /**
     * 默认编码 utf8
     */
    public static String DEFAULT_CHARSET = "utf8";

    public static String GET = "get";

    /**
     * 将字符串的首字母转大写
     * @param name 需要转换的字符串
     * @return
     */
    public static String getMethodName(String name) {
        // 进行字母的ascii编码前移，效率要高于截取字符串进行转换的操作
        char[] array = name.toCharArray();
        if (array[0] >= 97 && array[0] <= 122)
            array[0] ^= 32;
        return GET + String.valueOf(array);
    }


    /**
     * 格式化设备号码,添0,使得号码能到4位数
     * @param deviceId
     * @return
     */
    public static String formatDeviceId(String deviceId){
        if(deviceId == null){
            return "";
        }

        try {
            int number = Integer.valueOf(deviceId);
            return String.format(Locale.getDefault(), "%04d", number);
        } catch (Exception e) {

        }
        return deviceId;
    }

    private static String string = "abcdefghijklmnopqrstuvwxyz";
    /**
     * 生成随机字符串
     * @return
     */
    public static String randomStr(){
        return randomStr(16);
    }

    private static int getRandom(int count) {
        return (int) Math.round(Math.random() * (count));
    }

    private static String randomStr(int length){
        StringBuffer sb = new StringBuffer();
        int len = string.length();
        for (int i = 0; i < length; i++) {
            sb.append(string.charAt(getRandom(len-1)));
        }
        return sb.toString();
    }


    /**
     * unicode 转字符串
     */
    public static String unicode2String(String unicode) {

        StringBuffer string = new StringBuffer();

        String[] hex = unicode.split("u");

        logger.info(Arrays.toString(hex));

        for (int i = 1; i < hex.length; i++) {

            // 转换出每一个代码点
            int data = Integer.parseInt(hex[i], 16);

            // 追加成string
            string.append((char) data);
        }

        return string.toString();
    }

    /**
     * 字符串转换unicode
     */
    public static String string2Unicode(String string) {

        StringBuffer unicode = new StringBuffer();

        for (int i = 0; i < string.length(); i++) {

            // 取出每一个字符
            char c = string.charAt(i);

            // 转换为unicode
            unicode.append("u" + Integer.toHexString(c));
        }

        return unicode.toString();
    }


    /**
     * 在html标签或属性中A: 左尖括号：< 转成 &lt; 右尖括号：> 转成 &gt; 单引号：' 转成 &#39; 双引号：" 转成
     * &quot;
     */
    public static String escapeInH(String str) {
        if (str == null || ("").equals(str.trim())) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        int lth = str.length();

        for (int i = 0; i < lth; i++) {
            char c = str.charAt(i);

            switch (c) {

                case 60: // <
                    sb.append("&lt;");
                    break;
                case 62: // >
                    sb.append("&gt;");
                    break;
                case 39: // '
                    sb.append("&#39;");
                    break;
                case 34: // "
                    sb.append("&quot;");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return new String(sb.toString());
    }

    public static String escapeInH(Number num) {
        String str = null;
        if (num != null) {
            str = num.toString();
        }

        return escapeInH(str);
    }

    /**
     * 在html标签或属性中B: 左尖括号：< 转成 &lt; 右尖括号：> 转成 &gt; 单引号：' 转成 &#39; 双引号：" 转成
     * &quot; &符号：& 转成&amp;
     */
    public static String escapeInX(String str) {
        if (str == null || ("").equals(str.trim())) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        int lth = str.length();

        for (int i = 0; i < lth; i++) {
            char c = str.charAt(i);

            switch (c) {

                case 60: // <
                    sb.append("&lt;");
                    break;
                case 62: // >
                    sb.append("&gt;");
                    break;
                case 39: // '
                    sb.append("&#39;");
                    break;
                case 34: // "
                    sb.append("&quot;");
                    break;
                case 38: // &
                    sb.append("&amp;");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return new String(sb.toString());
    }

    public static String escapeInX(Number num) {
        String str = null;
        if (num != null) {
            str = num.toString();
        }

        return escapeInX(str);
    }

    /**
     * 在普通JS环境: 单引号：' 转成 \' 双引号：" 转成 \" 反斜杠：\ 转成 \\ 正斜杠：/ 转成 \/ 换行符 转成 \n 回车符 转成
     * \r
     */
    public static String escapeInJ(String str) {
        if (str == null || ("").equals(str.trim())) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        int lth = str.length();

        for (int i = 0; i < lth; i++) {
            char c = str.charAt(i);

            switch (c) {

                case 39: // '
                    sb.append("\\'");
                    break;
                case 34: // "
                    sb.append("\\\"");
                    break;
                case 47: // /
                    sb.append("\\/");
                    break;
                case 92: // \
                    sb.append("\\\\");
                    break;
                case 13: // 回车 \r
                    sb.append("\\r");
                    break;
                case 10: // 换行 \n
                    sb.append("\\n");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return new String(sb.toString());
    }

    public static String escapeInJ(Number num) {
        String str = null;
        if (num != null) {
            str = num.toString();
        }

        return escapeInJ(str);
    }

    /**
     * 在JS环境的innerHTML: 左尖括号：< 转成 &lt; 右尖括号：> 转成 &gt; 单引号：' 转成 \' 双引号：" 转成 \"
     * 反斜杠：\ 转成 \\ 正斜杠：/ 转成 \/ 换行符 转成 \n 回车符 转成 \r
     */
    public static String escapeInJH(String str) {
        if (str == null || ("").equals(str.trim())) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        int lth = str.length();

        for (int i = 0; i < lth; i++) {
            char c = str.charAt(i);

            switch (c) {

                case 60: // <
                    sb.append("&lt;");
                    break;
                case 62: // >
                    sb.append("&gt;");
                    break;
                case 39: // '
                    sb.append("\\'");
                    break;
                case 34: // "
                    sb.append("\\\"");
                    break;
                case 47: // /
                    sb.append("\\/");
                    break;
                case 92: // \
                    sb.append("\\\\");
                    break;
                case 13: // 回车 \r
                    sb.append("\\r");
                    break;
                case 10: // 换行 \n
                    sb.append("\\n");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return new String(sb.toString());
    }

    public static String escapeInJH(Number num) {
        String str = null;
        if (num != null) {
            str = num.toString();
        }

        return escapeInJH(str);
    }

    /**
     * 在标签onclick等事件函数参数中: 左尖括号：< 转成 &lt; 右尖括号：> 转成 &gt; &符号：& 转成&amp; 单引号：' 转成
     * \&#39; 双引号：" 转成 \&quot; 反斜杠：\ 转成 \\ 正斜杠：/ 转成 \/ 换行符 转成 \n 回车符 转成 \r
     */
    public static String escapeInHJ(String str) {
        if (str == null || ("").equals(str.trim())) {
            return "";
        }

        StringBuffer sb = new StringBuffer();
        int lth = str.length();

        for (int i = 0; i < lth; i++) {
            char c = str.charAt(i);

            switch (c) {

                case 60: // <
                    sb.append("&lt;");
                    break;
                case 62: // >
                    sb.append("&gt;");
                    break;
                case 39: // '
                    sb.append("\\&#39;");
                    break;
                case 34: // "
                    sb.append("\\&quot;");
                    break;
                case 38: // &
                    sb.append("&amp;");
                    break;
                case 47: // /
                    sb.append("\\/");
                    break;
                case 92: // \
                    sb.append("\\\\");
                    break;
                case 13: // 回车 \r
                    sb.append("\\r");
                    break;
                case 10: // 换行 \n
                    sb.append("\\n");
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        return new String(sb.toString());
    }

    public static String escapeInHJ(Number num) {
        String str = null;
        if (num != null) {
            str = num.toString();
        }

        return escapeInHJ(str);
    }

    /**
     * 在URL参数中: 对非字母、数字字符进行转码(%加字符的ASCII格式)
     *
     * @throws UnsupportedEncodingException
     */
    public static String escapeInU(String str)
            throws UnsupportedEncodingException {
        if (str == null || ("").equals(str.trim())) {
            return "";
        }
        return URLEncoder.encode(str, DEFAULT_CHARSET);
    }

    /**
     * 在URL参数中 - 逆
     *
     * @throws UnsupportedEncodingException
     */
    public static String UnEscapeInU(String str)
            throws UnsupportedEncodingException {
        if (str == null || ("").equals(str.trim())) {
            return "";
        }
        return URLDecoder.decode(str, DEFAULT_CHARSET);
    }

    public static String getMD5(String src) {
        byte[] defaultBytes = src.getBytes();
        StringBuffer hexString = new StringBuffer();
        try {
            MessageDigest algorithm = MessageDigest.getInstance("MD5");
            algorithm.reset();
            algorithm.update(defaultBytes);
            byte messageDigest[] = algorithm.digest();

            for (int i = 0; i < messageDigest.length; i++) {
                String hex = Integer.toHexString(0xFF & messageDigest[i]);
                if(hex.length() == 1){
                   hex = "0" + hex;
                }
                hexString.append(hex);
            }
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return hexString.toString();
    }

    public static String getMD5(byte[] source) {
        String s = null;
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
                'a', 'b', 'c', 'd', 'e', 'f'};// 用来将字节转换成16进制表示的字符
        try {
            MessageDigest md = MessageDigest
                    .getInstance("MD5");
            md.update(source);
            byte tmp[] = md.digest();// MD5 的计算结果是一个 128 位的长整数，
            // 用字节表示就是 16 个字节
            char str[] = new char[16 * 2];// 每个字节用 16 进制表示的话，使用两个字符， 所以表示成 16
            // 进制需要 32 个字符
            int k = 0;// 表示转换结果中对应的字符位置
            for (int i = 0; i < 16; i++) {// 从第一个字节开始，对 MD5 的每一个字节// 转换成 16
                // 进制字符的转换
                byte byte0 = tmp[i];// 取第 i 个字节
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];// 取字节中高 4 位的数字转换,// >>>
                // 为逻辑右移，将符号位一起右移
                str[k++] = hexDigits[byte0 & 0xf];// 取字节中低 4 位的数字转换

            }
            s = new String(str);// 换后的结果转换为字符串

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return s;
    }

    public static String getDoubleMD5(String src) {
        if (src != null) {
            src = getMD5(src);
            src = getMD5(src);
        }
        return src;
    }

    /**
     * 把中文转成Unicode码
     *
     * @param str
     * @return
     */
    public static String stringToUnicode(String str) {
        if (str == null) {
            str = "";
        }
        String result = "";
        for (int i = 0; i < str.length(); i++) {
            int chr1 = (char) str.charAt(i);
            result += "\\u" + Integer.toHexString(chr1);
        }
        return result;
    }





    /**
     * 将字符串转成unicode
     * @param str 待转字符串
     * @return unicode字符串
     */
    public String convert(String str)
    {
        str = (str == null ? "" : str);
        String tmp;
        StringBuffer sb = new StringBuffer(1000);
        char c;
        int i, j;
        sb.setLength(0);
        for (i = 0; i < str.length(); i++)
        {
            c = str.charAt(i);
            sb.append("%");
            j = (c >>>8); //取出高8位
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);
            j = (c & 0xFF); //取出低8位
            tmp = Integer.toHexString(j);
            if (tmp.length() == 1)
                sb.append("0");
            sb.append(tmp);

        }
        return (new String(sb));
    }




    /**
     * 将unicode 字符串
     * @param str 待转字符串
     * @return 普通字符串
     */
    public String revert(String str)
    {
        str = (str == null ? "" : str);
        if (str.indexOf("%") == -1)//如果不是unicode码则原样返回
            return str;

        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < str.length() - 6;)
        {
            String strTemp = str.substring(i, i + 6);
            String value = strTemp.substring(2);
            int c = 0;
            for (int j = 0; j < value.length(); j++)
            {
                char tempChar = value.charAt(j);
                int t = 0;
                switch (tempChar)
                {
                    case 'a':
                        t = 10;
                        break;
                    case 'b':
                        t = 11;
                        break;
                    case 'c':
                        t = 12;
                        break;
                    case 'd':
                        t = 13;
                        break;
                    case 'e':
                        t = 14;
                        break;
                    case 'f':
                        t = 15;
                        break;
                    default:
                        t = tempChar - 48;
                        break;
                }

                c += t * ((int) Math.pow(16, (value.length() - j - 1)));
            }
            sb.append((char) c);
            i = i + 6;
        }
        return sb.toString();
    }



    /**
     * 判断是否为中文字符
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
     * 中文算2个，字母算1个
     *
     * @param str
     * @return
     */
    public static int getLengthOfStringChinese(String str) {
        int length = 0;
        for (char c : str.toCharArray()) {
            if (isChinese(c)) length += 2;
            else length++;
        }
        return length;
    }

    /**
     * 中文算2个，字母算1个
     *
     * @param str
     * @param startIndex
     * @param endIndex
     * @return
     */
    public static String subStringChinese(String str, int startIndex, int endIndex) {
        int length = 0;
        int size = endIndex - startIndex;
        List<Character> charList = new ArrayList<Character>();
        for (char c : str.toCharArray()) {
            if (isChinese(c)) length += 2;
            else length++;
            charList.add(c);
            if (length >= size) break;
        }
        StringBuilder builder = new StringBuilder();
        for (Character c : charList) {
            builder.append(c);
        }
        return builder.toString();
    }

    /**
     * regular expression matcher helper
     *
     * @param pattern regular expression
     * @param str     string to be matched
     * @return
     */
    public static boolean regMatch(String pattern, String str) {
        Pattern p = Pattern.compile(pattern);
        Matcher matcher = p.matcher(str);
        return matcher.matches();
    }


    public static List<String> autoWrapText(List<String> list, TextView textView) {
        List<String> stringList = new ArrayList<>();
        int textWidth = textView.getWidth();
        if (textWidth <= 0) {
            stringList.add(StringUtil.join(list, "、"));
            return stringList;
        }

        Paint paint = new Paint();
        paint.set(textView.getPaint());

        List<String> lineText = new ArrayList<>();


        float lastWidth = 0;

        for (String text : list) {
            lineText.add(text);
            float width = paint.measureText(String.join("、", lineText) + "、");
            if (width > textWidth) {
                if (lineText.size() == 1) {
                    int lines = (int)(width / textWidth);
                    stringList.add(text);
                    lineText.clear();
                    for (int i = 0; i < lines; i++) stringList.add("");
                } else {
                    lineText.remove(text);
                    stringList.add(String.join("、", lineText));
                    lineText.clear();

                    if (width - lastWidth > textWidth) {
                        int lines = (int)((width - lastWidth) / textWidth);
                        stringList.add(text);
                        for (int i = 0; i < lines; i++) stringList.add("");
                    } else lineText.add(text);
                }
            }
            lastWidth = width;
        }
        if (lineText.size() > 0) stringList.add(String.join("、", lineText));

        return stringList;
    }


    /**
     * similar to JavaScript's String.prototype.join
     *
     * @param array
     * @param separator
     * @return
     */
    public static String join(String[] array, String separator) {
        StringBuilder builder = new StringBuilder();
        for (String string : array) {
            if (!string.isEmpty()) {
                if (builder.length() > 0) builder.append(separator);
                builder.append(string);
            }
        }
        return builder.toString();
    }

    public static String join(List<String> array, String separator) {
        return join(array.toArray(new String[array.size()]), separator);
    }




    /**
     * 字符串转json map对象
     * @param json
     * @return
     */
    public static Map<String,Object> json2Map(String json){
        GsonBuilder gb = new GsonBuilder();
        Gson g = gb.create();
        Map<String, Object> map = g.fromJson(json, new TypeToken<Map<String, Object>>() {}.getType());
        return map;
    }


}

