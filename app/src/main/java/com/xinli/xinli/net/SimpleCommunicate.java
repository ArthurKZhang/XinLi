package com.xinli.xinli.net;

import android.util.Log;

import org.apache.commons.io.Charsets;
import org.apache.commons.io.IOUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by zhangyu on 21/02/2017.
 */
public class SimpleCommunicate {

    /**
     * 使用POST请求向服务提交数据，返回json格式的字符串
     *
     * @param posrUrl
     * @param message
     * @return
     */
    public static String post(String posrUrl, String message) {
        //请求后服务器传回来的结果
        String result = null;
        try {
//            message = "message=" + URLEncoder.encode(message, "UTF-8");
            URL url = new URL(posrUrl);
            // 根据URL对象打开链接
            HttpURLConnection urlConn = (HttpURLConnection) url.openConnection();
            // 设置连接主机超时时间
            urlConn.setConnectTimeout(10 * 1000);
            //设置从主机读取数据超时
            urlConn.setReadTimeout(10 * 1000);
            // 设置是否使用缓存  默认是true
//            urlConn.setUseCaches(true);
            // 设置为Post请求
            urlConn.setRequestMethod("POST");
            // 设置编码格式
            urlConn.setRequestProperty("Charset", "UTF-8");
            //urlConn设置请求头信息
            urlConn.setRequestProperty("Content-Length",
                    String.valueOf(message.getBytes("UTF-8").length));

            urlConn.setRequestProperty("User-Agent",
                            "Mozilla/5.0 (Windows NT 6.3; WOW64; rv:27.0) Gecko/20100101 Firefox/27.0");

            //设置请求中的媒体类型信息。
            urlConn.setRequestProperty("Content-Type", "application/json");
            //设置客户端与服务连接类型
            urlConn.addRequestProperty("Connection", "Keep-Alive");
            // 发送POST请求必须设置允许输出
            urlConn.setDoOutput(true);
            // 发送POST请求必须设置允许输入
            urlConn.setDoInput(true);

            //获取输出流
            OutputStream os = urlConn.getOutputStream();
            os.write(message.getBytes());
            os.flush();
            // 开始连接
//            urlConn.connect();
            // 判断请求是否成功
            if (urlConn.getResponseCode() == 200) {
                // 获取返回的数据
                result = IOUtils.toString(urlConn.getInputStream(),"UTF-8");
                int len = result.getBytes("UTF-8").length;
                Log.e("NETWORK", "POST方式请求成功，length--->" + len);
//                result = streamToString();
                Log.e("NETWORK", "POST方式请求成功，result--->" + result);
//                System.out.println("POST方式请求成功，result--->" + result);
            } else {
                Log.e("NETWORK", "POST方式请求失败，result--->" + result+"CODE:"+urlConn.getResponseCode());
                result = null;
//                System.out.println("POST方式请求失败");
            }
            // 关闭连接
            urlConn.disconnect();
            return result;
        } catch (SocketTimeoutException exception){
            return null;

        } catch (Exception e) {
            // TODO: handle exception
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 将输入流转换成字符串
     *
     * @param is 从网络获取的输入流
     * @return
//     */
//    int len = 0;
//    int off = 0;
//    while ((len = is.read(buffer)) != -1) {
//        baos.write(buffer, off, len);
//        off += len;
//    }
//    baos.close();
//    is.close();
//    byte[] byteArray = baos.toByteArray();
//    return new String(byteArray);
    private static String streamToString(InputStream is) {
        try {
            String result = IOUtils.toString(is, "UTF-8");
            is.close();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }

//        try {
//            byte[] buffer = new byte[1024];
//            StringBuffer sb = new StringBuffer();
//            int len = 0;
//            while ((len = is.read(buffer)) != -1) {
//                sb.append(new String(buffer,0,len));
//            }
//            is.close();
//            return sb.toString();
//        } catch (Exception e) {
////            Log.e(TAG, e.toString());
//            e.printStackTrace();
//            return null;
//        }
//        try {
//            ByteArrayOutputStream baos = new ByteArrayOutputStream();
//            byte[] buffer = new byte[4096];
//            int len = 0;
//            int off = 0;
//            while ((len = is.read(buffer)) != -1) {
//                baos.write(buffer, off, len);
//                off += len;
//            }
//            baos.close();
//            is.close();
//            byte[] byteArray = baos.toByteArray();
//            return new String(byteArray);
//        } catch (Exception e) {
////            Log.e(TAG, e.toString());
//            e.printStackTrace();
//            return null;
//        }
    }
}
