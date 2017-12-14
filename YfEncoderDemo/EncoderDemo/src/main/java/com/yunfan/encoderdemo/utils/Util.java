/**
 * @版权 : 深圳云帆世纪科技有限公司
 * @作者 : 刘群山
 * @日期 : 2015年4月20日
 */
package com.yunfan.encoderdemo.utils;

import android.content.Context;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Util {
    public static final String TAG = "Yf_Util";

    public static String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "."
                + ((i >> 16) & 0xFF) + "." + ((i >> 24) & 0xFF);
    }

    public static String formatTime() {
        Date date = new Date(System.currentTimeMillis());
        DateFormat dateTimeInstance = SimpleDateFormat.getDateTimeInstance();
        return dateTimeInstance.format(date);
    }

    public static String formatCountTime(int time) {
        Date date = new Date(time * 1000 - 8 * 60 * 60 * 1000);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm:ss");
        return simpleDateFormat.format(date);
    }

    public static int getScreenWidth(Context context) {
        final Point p = new Point();
        final Display d = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        d.getSize(p);
        if (p.x > p.y) {
            return p.y;
        } else {
            return p.x;
        }
    }

    public static int getScreenHeight(Context context) {
        final Point p = new Point();
        final Display d = ((WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        d.getSize(p);
        if (p.x > p.y) {
            return p.x;
        } else {
            return p.y;
        }
    }

    public static final String ping(String ip) {
        Log.d(TAG, "start to ping in thread:" + ip);
        String result = null;
        try {

//            String ip = "www.baidu.com";// 除非百度挂了，否则用这个应该没问题~

            Process p = Runtime.getRuntime().exec("ping -c 3 -w 100 " + ip);//ping3次
// 读取ping的内容，可不加。
            InputStream input = p.getInputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(input));
            StringBuffer stringBuffer = new StringBuffer();
            String content = "";
            while ((content = in.readLine()) != null) {
                stringBuffer.append(content);
            }
            Log.e(TAG, "result content : " + stringBuffer.toString());
// PING的状态
            int status = p.waitFor();
            if (status == 0) {
                result = "successful~";
                return result;
            } else {
                result = "failed~ cannot reach the IP address";
            }
        } catch (IOException e) {
            result = "failed~ IOException";
        } catch (InterruptedException e) {
            result = "failed~ InterruptedException";
        } finally {
            Log.e(TAG, "result = " + result);
        }
        return result;

    }

}
