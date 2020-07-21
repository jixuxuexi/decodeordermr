package com.wingdata.util;

import com.alibaba.fastjson.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @version 1.0
 * @创建人 zwd
 * @创建时间 2020/7/20
 * @描述
 */
public class DecodeUtil {
    /**
     *
     * @param decodeData
     * 解密
     * @return
     */
    public static String  decode(String decodeData) {
        try {
            URL url = new URL("http://152.136.45.2/index.php");
            HttpURLConnection connection = (HttpURLConnection)url.openConnection();
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
            PrintWriter pw = new PrintWriter(new BufferedOutputStream(connection.getOutputStream()));
            pw.write("keyIndex=1&data=" + decodeData);
            pw.flush();
            pw.close();
            BufferedReader br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            String line = null;
            StringBuilder result = new StringBuilder();

            while((line = br.readLine()) != null) {
                result.append(line + "\n");
            }

            connection.disconnect();
            String data = unicodeToStringA(result.toString());
            data = unicodeToString(data);
            return JSONObject.parseObject(new String(data)).toJSONString();
        } catch (Exception var10) {
            var10.printStackTrace();
            return null;
        }
    }

    /**
     * unicode 转 为 字符串 , 去除多余的 \
     * @param str
     * @return
     */
    public static String unicodeToStringA(String str) {
        Pattern pattern = Pattern.compile("(\\\\\\\\u(\\p{XDigit}{4}))");

        char ch;
        for(Matcher matcher = pattern.matcher(str); matcher.find(); str = str.replace(matcher.group(1), ch + "")) {
            ch = (char)Integer.parseInt(matcher.group(2), 16);
        }

        return str;
    }

    /**
     * unicode 转 为 字符串
     * @param str
     * @return
     */
    public  static String unicodeToString(String str) {
        Pattern pattern = Pattern.compile("(\\\\u(\\p{XDigit}{4}))");

        char ch;
        for(Matcher matcher = pattern.matcher(str); matcher.find(); str = str.replace(matcher.group(1), ch + "")) {
            ch = (char)Integer.parseInt(matcher.group(2), 16);
        }

        return str;
    }

    public static void main(String[] args) {
        String decode = decode("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");

//        System.out.println(decode.getClass().getName());
        System.out.println(decode);
//        System.out.println(decode);
    }
}
