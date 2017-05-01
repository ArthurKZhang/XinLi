package com.xinli.xinli.net;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Map;

/**
 * Created by zhangyu on 15/02/2017.
 */
public class FileUtil {

//    public static Bitmap getPic(String requestUrl) {
//        InputStream inputStream = null;
//        HttpURLConnection urlConnection = null;
//
//        try {
//            // open connection
//            URL url = new URL(requestUrl);
//            urlConnection = (HttpURLConnection) url.openConnection();
//            /* for Get request */
//            urlConnection.setRequestMethod("GET");
//            int fileLength = urlConnection.getContentLength();
//            int statusCode = urlConnection.getResponseCode();
//            if (statusCode == 200) {
//                inputStream = urlConnection.getInputStream();
//                byte data[] = new byte[4096];
////                long total = 0;
//                int count;
//                ByteArrayOutputStream output = new ByteArrayOutputStream();
//                while ((count = inputStream.read(data)) != -1) {
////                    total += count;
//                    // publishing the progress.... 进度条
////                    if (fileLength > 0 && handler != null) {
////                        handler.sendEmptyMessage(((int) (total * 100 / fileLength)) - 1);
////                    }
//                    output.write(data, 0, count);
//                    System.out.println("SYStestzhangyu:"+"getPic():"+output.toByteArray()+" size:"+output.toByteArray().length);
//                }
//
//                ByteArrayInputStream bufferInput = new ByteArrayInputStream(output.toByteArray());
//
//                Bitmap bitmap = BitmapFactory.decodeStream(bufferInput);
//                if (bitmap==null)
//                    System.out.println("SYS--inner-->bitmap is null");
//                inputStream.close();
//                bufferInput.close();
//                output.close();
//                return bitmap;
//            }else {
//                System.out.println("SYStestzhangyu:"+"statusCode"+statusCode);
//                return null;
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            System.out.println("SYStestzhangyu:"+"FileUtil.getPic()-"+e.getClass().getName()+" occurred");
//            return null;
//        }
//    }


    /**
     * 直接通过HTTP协议提交数据到服务器,实现如下面表单提交功能:
     * <FORM METHOD=POST ACTION="http://192.168.1.101:8083/upload/servlet/UploadServlet" enctype="multipart/form-data">
     * <INPUT TYPE="text" NAME="name">
     * <INPUT TYPE="text" NAME="id">
     * <input type="file" name="imagefile"/>
     * <input type="file" name="zip"/>
     * </FORM>????
     *
     * @param path   上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.iteye.cn或http://192.168.1.101:8083这样的路径测试)
     * @param params 请求参数 key为参数名,value为参数值
     * @param files  上传文件
     */
    public static String post(String path, Map<String, String> params, FormFile[] files) throws Exception {
        final String BOUNDARY = "---------------------------7da2137580612"; //数据分隔线
        final String endline = "--" + BOUNDARY + "--\r\n";//数据结束标志

        int fileDataLength = 0;
        for (FormFile uploadFile : files) {//得到文件类型数据的总长度
            StringBuilder fileExplain = new StringBuilder();
            fileExplain.append("--");
            fileExplain.append(BOUNDARY);
            fileExplain.append("\r\n");
            fileExplain.append("Content-Disposition: form-data;name=\"" + uploadFile.getParameterName() + "\";filename=\"" + uploadFile.getFilname() + "\"\r\n");
            fileExplain.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
            fileExplain.append("\r\n");
            fileDataLength += fileExplain.length();
            if (uploadFile.getInStream() != null) {
                fileDataLength += uploadFile.getFile().length();
            } else {
                fileDataLength += uploadFile.getData().length;
            }
        }
        StringBuilder textEntity = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {//构造文本类型参数的实体数据
            textEntity.append("--");
            textEntity.append(BOUNDARY);
            textEntity.append("\r\n");
            textEntity.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"\r\n\r\n");
            textEntity.append(entry.getValue());
            textEntity.append("\r\n");
        }
        //计算传输给服务器的实体数据总长度
        int dataLength = textEntity.toString().getBytes().length + fileDataLength + endline.getBytes().length;

        URL url = new URL(path);
        int port = url.getPort() == -1 ? 80 : url.getPort();
        Socket socket = new Socket(InetAddress.getByName(url.getHost()), port);
        OutputStream outStream = socket.getOutputStream();
        //下面完成HTTP请求头的发送
        String requestmethod = "POST " + url.getPath() + " HTTP/1.1\r\n";
        outStream.write(requestmethod.getBytes());
        String accept = "Accept: image/gif, image/jpeg, image/pjpeg, image/pjpeg, application/x-shockwave-flash, application/xaml+xml, application/vnd.ms-xpsdocument, application/x-ms-xbap, application/x-ms-application, application/vnd.ms-excel, application/vnd.ms-powerpoint, application/msword, */*\r\n";
        outStream.write(accept.getBytes());
        String language = "Accept-Language: zh-CN\r\n";
        outStream.write(language.getBytes());
        String contenttype = "Content-Type: multipart/form-data; boundary=" + BOUNDARY + "\r\n";
        outStream.write(contenttype.getBytes());
        String contentlength = "Content-Length: " + dataLength + "\r\n";
        outStream.write(contentlength.getBytes());
        String alive = "Connection: Keep-Alive\r\n";
        outStream.write(alive.getBytes());
        String host = "Host: " + url.getHost() + ":" + port + "\r\n";
        outStream.write(host.getBytes());
        //写完HTTP请求头后根据HTTP协议再写一个回车换行
        outStream.write("\r\n".getBytes());
        //把所有文本类型的实体数据发送出来
        outStream.write(textEntity.toString().getBytes());
        //把所有文件类型的实体数据发送出来
        for (FormFile uploadFile : files) {
            StringBuilder fileEntity = new StringBuilder();
            fileEntity.append("--");
            fileEntity.append(BOUNDARY);
            fileEntity.append("\r\n");
            fileEntity.append("Content-Disposition: form-data;name=\"" + uploadFile.getParameterName() + "\";filename=\"" + uploadFile.getFilname() + "\"\r\n");
            fileEntity.append("Content-Type: " + uploadFile.getContentType() + "\r\n\r\n");
            outStream.write(fileEntity.toString().getBytes());
            if (uploadFile.getInStream() != null) {
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = uploadFile.getInStream().read(buffer, 0, 1024)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                uploadFile.getInStream().close();
            } else {
                outStream.write(uploadFile.getData(), 0, uploadFile.getData().length);
            }
            outStream.write("\r\n".getBytes());
        }
        //下面发送数据结束标志，表示数据已经结束
        outStream.write(endline.getBytes());

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        /**
         * 测试：
         * 用BufferReader的read()方法来读取
         */
        StringBuffer stringBuffer = new StringBuffer();
        char cbuf[] = new char[100];
        int num = 0;
        while ((num = reader.read(cbuf, 0, 100)) != -1) {
            stringBuffer.append(cbuf);
        }
        String result = stringBuffer.toString();
        Log.i("DLtestzhangyu", "FileUtil.test read()-->" + result);


        /**
         * 测试：
         * 用readLine()方法来读，这个方法会阻塞，所以尽量避免用。
         */
//        String mes;
//        boolean isFirstLIne = true;
//        while ((mes = reader.readLine()) != null) {
//            if (!mes.contains("200") && isFirstLIne) {
//                //读取web服务器返回的数据，判断请求码是否为200，如果不是200，代表请求失败
//                isFirstLIne = false;
//                return false;
//            }
//            System.out.println("SYStestzhangyu:"+"FileUtil.test-->" + mes);
//        }


        outStream.flush();
        outStream.close();
        reader.close();
        socket.close();
        if (result.contains("200")) return result;

        return null;
    }

    /**
     * 提交数据到服务器
     *
     * @param path   上传路径(注：避免使用localhost或127.0.0.1这样的路径测试，因为它会指向手机模拟器，你可以使用http://www.itcast.cn或http://192.168.1.10:8080这样的路径测试)
     * @param params 请求参数 key为参数名,value为参数值
     * @param file   上传文件
     */
    public static String post(String path, Map<String, String> params, FormFile file) throws Exception {
        return post(path, params, new FormFile[]{file});
    }
}
