package com.wenh.baselibrary.util;

import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.logging.Logger;

/**
 * Created by MONDAY on 2016/8/6.
 */
public class LicenseUtil {

    private static Logger logger = Logger.getLogger("LicenseUtil");

    /**
     * 验证码为一个6位数的数字, 由mac地址经过一定算法生成
     * @param context
     * @param number 需要验证的字符串
     * @return
     */
    public static boolean validate(Context context, String number){
        // 获取本机的验证码
        String license = getLicense(context);

        // 比较license
        return license.equals(number);
    }




    /**
     * 获取验证码
     * @param context
     * @return
     */
    public static String getLicense(Context context){

        // 获取本机的mac地址
        String mac = getMachineUUID(context);

        // 将mac地址转化成license
        String license = StringUtil.getMD5(mac);
        char[] licenseChars = license.toCharArray();
        StringBuffer sb = new StringBuffer();
        for(char c : licenseChars){
            sb.append((int)c);
        }
        license = sb.substring(0, 6);

        logger.info(license);

        return license;
    }


    /**
     * 获取本机的mac地址
     * mac地址根据板子不同，获取方式有所不同，
     * 此方法下，先获取无线上的mac地址，如果获取失败，再获取有线上的
     * @param context
     * @return
     */
    public static String getMachineUUID(Context context) {
        // 获取本机的mac地址
        String mac = getLocalMac();
        if(mac == null){
            WifiManager wifi = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
            WifiInfo info = wifi.getConnectionInfo();
            if(info != null){
                mac = info.getMacAddress();
            }
//            mac = getOfflineMac();
        }

        if(mac == null){
            mac = getLocalMacAddressFromBusybox();
        }

        if(mac != null){
            mac = mac.toLowerCase().trim().replace(":", "")
                    .replace("-", "");
        }

        if(mac == null){
            // 说明本机已损坏，不能使用，直接抛出异常
            //throw new RuntimeException("获取不到mac地址");
            //mac = "nomacaddress";
        }

        return mac;

    }


    /**
     * 在未连接网络的情况下获取mac地址
     * @return
     */
    public static String getOfflineMac() {
        String mac = null;
        try {
            String path = "sys/class/net/eth0/address";
            FileInputStream fis_name = new FileInputStream(path);
            byte[] buffer_name = new byte[8192];
            int byteCount_name = fis_name.read(buffer_name);
            if (byteCount_name > 0) {
                mac = new String(buffer_name, 0, byteCount_name, "utf-8");
            }


            if (mac == null) {
                fis_name.close();
                return "";
            }
            fis_name.close();
        } catch (Exception io) {
            String path = "sys/class/net/wlan0/address";
            FileInputStream fis_name;
            try {
                fis_name = new FileInputStream(path);
                byte[] buffer_name = new byte[8192];
                int byteCount_name = fis_name.read(buffer_name);
                if (byteCount_name > 0) {
                    mac = new String(buffer_name, 0, byteCount_name, "utf-8");
                }
                fis_name.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }


        if (mac == null) {
            return null;
        } else {
            return mac.trim();
        }

    }


    public static String getLocalMacAddressFromBusybox(){
        String result = "";
        String Mac = "";
        result = callCmd("busybox ifconfig","HWaddr");

        //如果返回的result == null，则说明网络不可取
        if(result==null){
            return "网络出错，请检查网络";
        }

        //对该行数据进行解析
        //例如：eth0      Link encap:Ethernet  HWaddr 00:16:E8:3E:DF:67
        if(result.length()>0 && result.contains("HWaddr")==true){
            Mac = result.substring(result.indexOf("HWaddr")+6, result.length()-1);

             /*if(Mac.length()>1){
                 Mac = Mac.replaceAll(" ", "");
                 result = "";
                 String[] tmp = Mac.split(":");
                 for(int i = 0;i<tmp.length;++i){
                     result +=tmp[i];
                 }
             }*/
            result = Mac;
        }
        return result;
    }

    private static String callCmd(String cmd, String filter) {
        String result = "";
        String line = "";
        try {
            Process proc = Runtime.getRuntime().exec(cmd);
            InputStreamReader is = new InputStreamReader(proc.getInputStream());
            BufferedReader br = new BufferedReader(is);

            //执行命令cmd，只取结果中含有filter的这一行
            while ((line = br.readLine ()) != null && line.contains(filter)== false) {
                //result += line;
                Log.i("test","line: "+line);
            }

            result = line;
            Log.i("test","result: "+result);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public static InetAddress getLocalInetAddress() {
        InetAddress ip = null;
        try {
            Enumeration<NetworkInterface> en_netInterface = NetworkInterface.getNetworkInterfaces();
            while (en_netInterface.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) en_netInterface.nextElement();
                Enumeration<InetAddress> en_ip = ni.getInetAddresses();
                while (en_ip.hasMoreElements()) {
                    ip = en_ip.nextElement();
                    if (!ip.isLoopbackAddress() && ip.getHostAddress().indexOf(":") == -1)
                        break;
                    else
                        ip = null;
                }

                if (ip != null) {
                    break;
                }
            }
        } catch (SocketException e) {
            e.printStackTrace();
        }
        return ip;
    }

    private static String getLocalMac() /* throws UnknownHostException */{
        String strMacAddr = null;
        try {
            InetAddress ip = getLocalInetAddress();

            byte[] b = NetworkInterface.getByInetAddress(ip).getHardwareAddress();
            StringBuffer buffer = new StringBuffer();
            for (int i = 0; i < b.length; i++) {
                if (i != 0) {
                    buffer.append('-');
                }

                String str = Integer.toHexString(b[i] & 0xFF);
                buffer.append(str.length() == 1 ? 0 + str : str);
            }
            strMacAddr = buffer.toString().toUpperCase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return strMacAddr;
    }
}
